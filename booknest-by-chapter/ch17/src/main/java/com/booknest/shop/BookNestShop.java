package com.booknest.shop;

import com.booknest.cart.Cart;
import com.booknest.cart.CartLine;
import com.booknest.catalog.Accessory;
import com.booknest.catalog.Book;
import com.booknest.catalog.Catalog;
import com.booknest.catalog.EBook;
import com.booknest.catalog.Product;
import com.booknest.common.InMemoryRepository;
import com.booknest.common.Repository;
import com.booknest.inventory.Inventory;
import com.booknest.inventory.OutOfStockException;
import com.booknest.order.Order;
import com.booknest.payment.Card;
import com.booknest.payment.CashOnDelivery;
import com.booknest.payment.PaymentMethod;
import com.booknest.pricing.MembershipLevel;
import com.booknest.pricing.Pricing;
import com.booknest.pricing.ShippingPolicy;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * The BookNest console shop: menu, input, and output.
 */
public class BookNestShop {

    private static final Catalog CATALOG = new Catalog(
            new Book("The Quiet Library", "Asha Rao", "12.99"),
            new Book("Java for Everyone", "Dev Malhotra", "29.50"),
            new EBook("Java for Everyone", "Dev Malhotra", "19.99"),
            new Accessory("Bookmark set", "3.35"),
            new Accessory("Reading lamp", "24.00"));

    private static final String CLOSE = "q";
    private static final String SEARCH = "8";
    private static final String VIEW_CART = "9";
    private static final String CHECKOUT = "0";
    private static final NumberFormat MONEY =
            NumberFormat.getCurrencyInstance(Locale.US);
    private static final String SUMMARY_LINE = "%-22s %10s%n";
    private static final String PRODUCT_LINE = "%d) %-28s %8s%n";

    /** Receipt order: biggest line total first, then by label. */
    private static final Comparator<CartLine> MOST_EXPENSIVE_FIRST =
            Comparator.comparing(CartLine::total).reversed()
                    .thenComparing(line ->
                            line.product().menuLabel());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Stock per product (the e-book is digital: unlimited)
        Inventory inventory =
                new Inventory(CATALOG, 6, 4, 999, 20, 2);
        ShippingPolicy shipping = chooseShipping(args);
        Repository<Order, String> orders = new InMemoryRepository<>();

        System.out.println("Welcome to BookNest!");
        while (serveCustomer(scanner, inventory, shipping, orders)) {
            System.out.println();
            System.out.println("Next customer, please!");
        }
        printOrderHistory(orders);
    }

    /**
     * Serves one customer. Returns false when the shop should close.
     */
    private static boolean serveCustomer(
            Scanner scanner, Inventory inventory,
            ShippingPolicy shipping,
            Repository<Order, String> orders) {
        Cart cart = new Cart();
        String choice = askForChoice(scanner);

        while (!choice.equals(CHECKOUT) && !choice.equals(CLOSE)) {
            int product = toProductIndex(choice);
            if (choice.equals(VIEW_CART)) {
                printCart(cart);
            } else if (choice.equals(SEARCH)) {
                search(scanner);
            } else if (product >= 0) {
                addToCart(cart, inventory, CATALOG.get(product),
                        readQuantity(scanner));
            } else {
                System.out.println("Unknown option: " + choice);
            }
            choice = askForChoice(scanner);
        }

        if (cart.isEmpty()) {
            if (!choice.equals(CLOSE)) {
                System.out.println(
                        "Your cart is empty. See you soon!");
            }
            return !choice.equals(CLOSE);
        }

        MembershipLevel level = readMembership(scanner);
        PaymentMethod payment =
                readPayment(scanner, cart.shippableItems() > 0);
        printCart(cart);
        BigDecimal total =
                printSummary(cart, level, payment, shipping);
        orders.save(new Order(nextOrderId(orders), level,
                cart.lines(), total));
        return !choice.equals(CLOSE);
    }

    private static String nextOrderId(
            Repository<Order, String> orders) {
        return "BN-" + (1001 + orders.count());
    }

    /** Runs with "free-shipping" as an argument during promotions. */
    private static ShippingPolicy chooseShipping(String[] args) {
        if (args.length > 0 && args[0].equals("free-shipping")) {
            return ShippingPolicy.freeShippingPromotion();
        }
        return ShippingPolicy.standard();
    }

    // ---- Menu -----------------------------------------------------

    private static String askForChoice(Scanner scanner) {
        printMenu();
        System.out.print("Choose an option: ");
        return scanner.nextLine().strip();
    }

    private static void printMenu() {
        System.out.println();
        for (int i = 0; i < CATALOG.size(); i++) {
            printProduct(i);
        }
        System.out.println(SEARCH + ") Search");
        System.out.println(VIEW_CART + ") View cart");
        System.out.println(CHECKOUT + ") Checkout");
        System.out.println(CLOSE + ") Checkout and close the shop");
    }

    private static void printProduct(int index) {
        Product product = CATALOG.get(index);
        System.out.printf(PRODUCT_LINE, index + 1,
                product.menuLabel(), money(product.getPrice()));
    }

    private static void search(Scanner scanner) {
        System.out.print("Search for: ");
        String text = scanner.nextLine().strip();
        List<Product> matches =
                CATALOG.matching(ProductSearch.parse(text));
        if (matches.isEmpty()) {
            System.out.println("Nothing matches \"" + text + "\".");
            return;
        }
        System.out.println("Found " + matches.size() + ":");
        for (Product product : matches) {
            printProduct(CATALOG.indexOf(product));
        }
    }

    /** Returns the catalog index for a menu choice, or -1. */
    private static int toProductIndex(String choice) {
        if (!choice.matches("\\d")) {
            return -1;
        }
        int number = Integer.parseInt(choice);
        if (number < 1 || number > CATALOG.size()) {
            return -1;
        }
        return number - 1;
    }

    // ---- Input ----------------------------------------------------

    private static int readQuantity(Scanner scanner) {
        while (true) {
            System.out.print("Quantity (1-10): ");
            String text = scanner.nextLine().strip();
            try {
                int quantity = Integer.parseInt(text);
                if (quantity >= 1 && quantity <= 10) {
                    return quantity;
                }
            } catch (NumberFormatException _) {
                // not a whole number: fall through to the message
            }
            System.out.println(
                    "Please enter a whole number from 1 to 10.");
        }
    }

    /** Reserves stock first; only then adds the item to the cart. */
    private static void addToCart(Cart cart, Inventory inventory,
                                  Product product, int quantity) {
        try {
            inventory.reserve(product, quantity);
            cart.add(product, quantity);
            System.out.println("Added " + quantity + " x "
                    + product.menuLabel() + ".");
        } catch (OutOfStockException e) {
            System.out.println("Sorry, " + e.getMessage() + ".");
        }
    }

    private static MembershipLevel readMembership(Scanner scanner) {
        while (true) {
            System.out.print(
                    "Membership (none/bronze/silver/gold): ");
            String text = scanner.nextLine().strip();
            for (MembershipLevel level : MembershipLevel.values()) {
                if (level.name().equalsIgnoreCase(text)) {
                    return level;
                }
            }
            System.out.println("Unknown membership: " + text);
        }
    }

    private static PaymentMethod readPayment(
            Scanner scanner, boolean canPayOnDelivery) {
        while (true) {
            System.out.print("Payment (card/cash): ");
            String text = scanner.nextLine().strip();
            if (text.equalsIgnoreCase("card")) {
                return new Card(readLastFourDigits(scanner));
            }
            if (text.equalsIgnoreCase("cash") && canPayOnDelivery) {
                return new CashOnDelivery();
            }
            if (text.equalsIgnoreCase("cash")) {
                System.out.println("Nothing will be delivered, "
                        + "so please pay by card.");
            } else {
                System.out.println("Unknown payment method: " + text);
            }
        }
    }

    private static String readLastFourDigits(Scanner scanner) {
        while (true) {
            System.out.print("Last four card digits: ");
            String digits = scanner.nextLine().strip();
            if (digits.matches("\\d{4}")) {
                return digits;
            }
            System.out.println("Please enter exactly 4 digits.");
        }
    }

    // ---- Output ---------------------------------------------------

    private static void printCart(Cart cart) {
        System.out.println();
        System.out.println("Your cart:");
        if (cart.isEmpty()) {
            System.out.println("  (empty)");
            return;
        }
        // cart.lines() can't be changed, so sort a copy
        List<CartLine> lines = new ArrayList<>(cart.lines());
        lines.sort(MOST_EXPENSIVE_FIRST);
        for (CartLine line : lines) {
            System.out.printf("  %2d x %-28s %9s%n", line.quantity(),
                    line.product().menuLabel(), money(line.total()));
        }
    }

    /** Prints the summary and returns the amount paid. */
    private static BigDecimal printSummary(Cart cart,
                                           MembershipLevel level,
                                           PaymentMethod payment,
                                           ShippingPolicy policy) {
        BigDecimal subtotal = cart.subtotal();
        BigDecimal discount = Pricing.memberDiscount(subtotal, level);
        BigDecimal taxable = subtotal.subtract(discount);
        BigDecimal tax = Pricing.tax(taxable);
        BigDecimal shipping = policy.cost(cart.shippableItems());
        BigDecimal fee = Pricing.paymentFee(payment);
        BigDecimal total = taxable.add(tax).add(shipping).add(fee);

        System.out.println();
        printSummaryLine("Subtotal", money(subtotal));
        printSummaryLine("Discount (" + level + " "
                + level.discountPercent() + "%)",
                money(discount.negate()));
        printSummaryLine("Tax (8.25%)", money(tax));
        printSummaryLine("Shipping (" + policy.name() + ")",
                shipping.signum() == 0 ? "FREE" : money(shipping));
        if (fee.signum() > 0) {
            printSummaryLine("Payment fee", money(fee));
        }
        printSummaryLine("TOTAL", money(total));
        System.out.println("Paid by " + describe(payment) + ".");
        System.out.println("Thank you for shopping at BookNest!");
        return total;
    }

    private static void printOrderHistory(
            Repository<Order, String> orders) {
        System.out.println();
        System.out.println("Orders today: " + orders.count());
        orders.forEach(order -> System.out.printf(
                "  %-8s %-7s %2d items %9s%n", order.id(),
                order.level(), order.items(), money(order.total())));
        int memberOrders = orders.countMatching(
                order -> order.level() != MembershipLevel.NONE);
        System.out.println("Member orders: " + memberOrders);
        printItemsSold(orders);
    }

    /** Totals each product across all orders, A-Z by label. */
    private static void printItemsSold(
            Repository<Order, String> orders) {
        Map<String, Integer> sold = new TreeMap<>();
        orders.forEach(order -> {
            for (CartLine line : order.lines()) {
                sold.merge(line.product().menuLabel(),
                        line.quantity(), Integer::sum);
            }
        });
        System.out.println("Items sold:");
        for (Map.Entry<String, Integer> entry : sold.entrySet()) {
            System.out.printf("  %-28s %3d%n",
                    entry.getKey(), entry.getValue());
        }
    }

    private static String describe(PaymentMethod payment) {
        return switch (payment) {
            case Card(String digits) -> "card ending " + digits;
            case CashOnDelivery _ -> "cash on delivery";
        };
    }

    private static void printSummaryLine(String label, String value) {
        System.out.printf(SUMMARY_LINE, label, value);
    }

    private static String money(BigDecimal amount) {
        return MONEY.format(amount);
    }
}
