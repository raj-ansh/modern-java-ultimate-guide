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
import com.booknest.delivery.DeliveryEstimator;
import com.booknest.inventory.Inventory;
import com.booknest.inventory.OutOfStockException;
import com.booknest.order.Order;
import com.booknest.payment.Card;
import com.booknest.payment.CashOnDelivery;
import com.booknest.payment.PaymentMethod;
import com.booknest.pricing.MembershipLevel;
import com.booknest.pricing.Pricing;
import com.booknest.pricing.Promotion;
import com.booknest.pricing.Promotion.AmountOff;
import com.booknest.pricing.Promotion.CheapestFree;
import com.booknest.pricing.Promotion.NoPromotion;
import com.booknest.pricing.Promotion.PercentOff;
import com.booknest.pricing.Promotions;
import com.booknest.pricing.ShippingPolicy;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
    private static final String FIND_ORDER = "7";
    private static final String SEARCH = "8";
    private static final String VIEW_CART = "9";
    private static final String CHECKOUT = "0";
    private static final NumberFormat MONEY =
            NumberFormat.getCurrencyInstance(Locale.US);
    private static final String SUMMARY_LINE = "%-22s %10s%n";
    private static final String PRODUCT_LINE = "%d) %-28s %8s%n";

    /** The shop's own time zone: receipts and dates use it. */
    private static final ZoneId STORE_ZONE =
            ZoneId.of("America/Chicago");
    private static final DateTimeFormatter PLACED_AT =
            DateTimeFormatter.ofPattern("EEE d MMM uuuu, h:mm a z",
                    Locale.US);
    private static final DateTimeFormatter DELIVERY_DAY =
            DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.US);
    private static final DateTimeFormatter TIME_OF_DAY =
            DateTimeFormatter.ofPattern("h:mm a", Locale.US);
    private static final DeliveryEstimator DELIVERY =
            DeliveryEstimator.standard();

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
        Clock clock = chooseClock();

        System.out.println("Welcome to BookNest!");
        while (serveCustomer(scanner, inventory, shipping, orders,
                clock)) {
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
            Repository<Order, String> orders, Clock clock) {
        Cart cart = new Cart();
        String choice = askForChoice(scanner);

        while (!choice.equals(CHECKOUT) && !choice.equals(CLOSE)) {
            handleChoice(choice, scanner, cart, inventory, orders);
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
        Promotion promotion = readPromotion(scanner);
        PaymentMethod payment =
                readPayment(scanner, cart.shippableItems() > 0);
        printCart(cart);
        BigDecimal total = printSummary(cart, level, promotion,
                payment, shipping);
        ZonedDateTime placedAt = ZonedDateTime.now(clock);
        String id = nextOrderId(orders);
        printConfirmation(id, placedAt, cart.shippableItems() > 0);
        orders.save(new Order(id, level, cart.lines(), total,
                placedAt.toInstant()));
        return !choice.equals(CLOSE);
    }

    /** Handles one menu choice other than checkout. */
    private static void handleChoice(
            String choice, Scanner scanner, Cart cart,
            Inventory inventory, Repository<Order, String> orders) {
        if (choice.equals(VIEW_CART)) {
            printCart(cart);
        } else if (choice.equals(SEARCH)) {
            search(scanner);
        } else if (choice.equals(FIND_ORDER)) {
            findOrder(scanner, orders);
        } else {
            productFor(choice).ifPresentOrElse(
                    product -> addToCart(cart, inventory, product,
                            readQuantity(scanner)),
                    () -> System.out.println(
                            "Unknown option: " + choice));
        }
    }

    private static String nextOrderId(
            Repository<Order, String> orders) {
        return "BN-" + (1001 + orders.count());
    }

    /**
     * The real clock in the store's zone, or, for demos and tests, a
     * clock stopped at a chosen store time:
     * java -Dbooknest.now=2026-03-06T16:30 ...
     */
    private static Clock chooseClock() {
        return Optional.ofNullable(System.getProperty("booknest.now"))
                .map(LocalDateTime::parse)
                .map(start -> start.atZone(STORE_ZONE).toInstant())
                .map(instant -> Clock.fixed(instant, STORE_ZONE))
                .orElseGet(() -> Clock.system(STORE_ZONE));
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
        System.out.println(FIND_ORDER + ") Find an order");
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

    /** Returns the product for a menu choice such as "3", if any. */
    private static Optional<Product> productFor(String choice) {
        return Optional.of(choice)
                .filter(text -> text.matches("\\d"))
                .map(Integer::parseInt)
                .flatMap(CATALOG::byMenuNumber);
    }

    private static void findOrder(Scanner scanner,
                                  Repository<Order, String> orders) {
        System.out.print("Order number: ");
        String id = scanner.nextLine().strip().toUpperCase();
        String result = orders.findById(id)
                .map(BookNestShop::describeOrder)
                .orElse("No order " + id + " today.");
        System.out.println(result);
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
            String text = scanner.nextLine();
            Optional<MembershipLevel> level =
                    MembershipLevel.parse(text);
            if (level.isPresent()) {
                return level.orElseThrow();
            }
            System.out.println("Unknown membership: " + text.strip());
        }
    }

    private static Promotion readPromotion(Scanner scanner) {
        while (true) {
            System.out.print("Promo code (or none): ");
            String text = scanner.nextLine().strip();
            Optional<Promotion> found = Promotions.find(text);
            if (found.isPresent()) {
                Promotion promotion = found.orElseThrow();
                if (!(promotion instanceof NoPromotion)) {
                    System.out.println(
                            "Applied " + describe(promotion) + ".");
                }
                return promotion;
            }
            System.out.println("Unknown promo code: " + text);
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
        // sorted() leaves cart.lines() untouched: streams never
        // change their source
        List<CartLine> lines = cart.lines().stream()
                .sorted(MOST_EXPENSIVE_FIRST)
                .toList();
        for (CartLine line : lines) {
            System.out.printf("  %2d x %-28s %9s%n", line.quantity(),
                    line.product().menuLabel(), money(line.total()));
        }
    }

    /** Prints the summary and returns the amount paid. */
    private static BigDecimal printSummary(Cart cart,
                                           MembershipLevel level,
                                           Promotion promotion,
                                           PaymentMethod payment,
                                           ShippingPolicy policy) {
        BigDecimal subtotal = cart.subtotal();
        BigDecimal discount = Pricing.memberDiscount(subtotal, level);
        BigDecimal promo =
                Pricing.promotionDiscount(promotion, cart.lines());
        BigDecimal taxable = subtotal.subtract(discount)
                .subtract(promo).max(BigDecimal.ZERO);
        BigDecimal tax = Pricing.tax(taxable);
        BigDecimal shipping = policy.cost(cart.shippableItems());
        BigDecimal fee = Pricing.paymentFee(payment);
        BigDecimal total = taxable.add(tax).add(shipping).add(fee);

        System.out.println();
        printSummaryLine("Subtotal", money(subtotal));
        printSummaryLine("Discount (" + level + " "
                + level.discountPercent() + "%)",
                money(discount.negate()));
        if (!(promotion instanceof NoPromotion)) {
            printSummaryLine("Promo (" + promotion.code() + ")",
                    money(promo.negate()));
        }
        printSummaryLine("Tax (8.25%)", money(tax));
        printSummaryLine("Shipping (" + policy.name() + ")",
                shipping.signum() == 0 ? "FREE" : money(shipping));
        if (fee.signum() > 0) {
            printSummaryLine("Payment fee", money(fee));
        }
        printSummaryLine("TOTAL", money(total));
        System.out.println("Paid by " + describe(payment) + ".");
        return total;
    }

    private static void printConfirmation(String id,
                                          ZonedDateTime placedAt,
                                          boolean shipped) {
        System.out.println("Order " + id + " placed "
                + placedAt.format(PLACED_AT) + ".");
        if (shipped) {
            LocalDate arrives =
                    DELIVERY.deliveryDate(placedAt.toLocalDateTime());
            System.out.println("Estimated delivery: "
                    + arrives.format(DELIVERY_DAY) + ".");
        } else {
            System.out.println(
                    "Your e-book is ready to download now.");
        }
        System.out.println("Thank you for shopping at BookNest!");
    }

    private static void printOrderHistory(
            Repository<Order, String> orders) {
        System.out.println();
        System.out.println("Orders today: " + orders.count());
        orders.forEach(order ->
                System.out.println("  " + describeOrder(order)));
        int memberOrders = orders.countMatching(
                order -> order.level() != MembershipLevel.NONE);
        System.out.println("Member orders: " + memberOrders);
        BigDecimal revenue = orders.stream()
                .map(Order::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Revenue: " + money(revenue));
        printItemsSold(orders);
    }

    /** Totals each product across all orders, A-Z by label. */
    private static void printItemsSold(
            Repository<Order, String> orders) {
        Map<String, Integer> sold = orders.stream()
                .flatMap(order -> order.lines().stream())
                .collect(Collectors.groupingBy(
                        line -> line.product().menuLabel(),
                        TreeMap::new,
                        Collectors.summingInt(CartLine::quantity)));
        System.out.println("Items sold:");
        for (Map.Entry<String, Integer> entry : sold.entrySet()) {
            System.out.printf("  %-28s %3d%n",
                    entry.getKey(), entry.getValue());
        }
    }

    private static String describeOrder(Order order) {
        int items = order.items();
        String time = order.placedAt().atZone(STORE_ZONE)
                .format(TIME_OF_DAY);
        return String.format("%-8s %-7s %2d %-5s %9s  %s", order.id(),
                order.level(), items, items == 1 ? "item" : "items",
                money(order.total()), time);
    }

    private static String describe(Promotion promotion) {
        return switch (promotion) {
            case NoPromotion _ -> "no promotion";
            case PercentOff(var code, var percent) ->
                    code + ": " + percent + "% off";
            case AmountOff(var code, var amount, var minimum) ->
                    code + ": " + money(amount) + " off orders of "
                            + money(minimum) + " or more";
            case CheapestFree(var code, var minimumItems) ->
                    code + ": cheapest item free with "
                            + minimumItems + "+ items";
        };
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
