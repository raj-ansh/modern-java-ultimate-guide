package com.booknest.shop;

import com.booknest.cart.Cart;
import com.booknest.cart.CartLine;
import com.booknest.catalog.Accessory;
import com.booknest.catalog.Book;
import com.booknest.catalog.Catalog;
import com.booknest.catalog.EBook;
import com.booknest.catalog.Product;
import com.booknest.inventory.Inventory;
import com.booknest.inventory.OutOfStockException;
import com.booknest.payment.Card;
import com.booknest.payment.CashOnDelivery;
import com.booknest.payment.PaymentMethod;
import com.booknest.pricing.MembershipLevel;
import com.booknest.pricing.Pricing;
import com.booknest.pricing.ShippingPolicy;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Scanner;

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
        Cart cart = new Cart(CATALOG);
        // Stock per product (the e-book is digital: unlimited)
        Inventory inventory =
                new Inventory(CATALOG, 6, 4, 999, 20, 2);
        ShippingPolicy shipping = chooseShipping(args);

        System.out.println("Welcome to BookNest!");
        String choice = askForChoice(scanner);

        while (!choice.equals(CHECKOUT)) {
            int product = toProductIndex(choice);
            if (choice.equals(VIEW_CART)) {
                printCart(cart);
            } else if (choice.equals(SEARCH)) {
                search(scanner);
            } else if (product >= 0) {
                addToCart(cart, inventory, product,
                        readQuantity(scanner));
            } else {
                System.out.println("Unknown option: " + choice);
            }
            choice = askForChoice(scanner);
        }

        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. See you soon!");
            return;
        }

        MembershipLevel level = readMembership(scanner);
        PaymentMethod payment =
                readPayment(scanner, cart.shippableItems() > 0);
        printCart(cart);
        printSummary(cart, level, payment, shipping);
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
    }

    private static void printProduct(int index) {
        Product product = CATALOG.get(index);
        System.out.printf(PRODUCT_LINE, index + 1,
                product.menuLabel(), money(product.getPrice()));
    }

    private static void search(Scanner scanner) {
        System.out.print("Search for: ");
        String text = scanner.nextLine().strip();
        int[] matches =
                CATALOG.indexesMatching(ProductSearch.parse(text));
        if (matches.length == 0) {
            System.out.println("Nothing matches \"" + text + "\".");
            return;
        }
        System.out.println("Found " + matches.length + ":");
        for (int index : matches) {
            printProduct(index);
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
                                  int product, int quantity) {
        try {
            inventory.reserve(product, quantity);
            cart.add(product, quantity);
            System.out.println("Added " + quantity + " x "
                    + CATALOG.get(product).menuLabel() + ".");
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
        CartLine[] lines = cart.lines();
        Arrays.sort(lines, MOST_EXPENSIVE_FIRST);
        for (CartLine line : lines) {
            System.out.printf("  %2d x %-28s %9s%n", line.quantity(),
                    line.product().menuLabel(), money(line.total()));
        }
    }

    private static void printSummary(Cart cart, MembershipLevel level,
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
