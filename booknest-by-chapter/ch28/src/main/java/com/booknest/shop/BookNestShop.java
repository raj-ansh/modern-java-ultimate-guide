package com.booknest.shop;

import com.booknest.cart.Cart;
import com.booknest.catalog.Catalog;
import com.booknest.catalog.CatalogFile;
import com.booknest.catalog.Product;
import com.booknest.common.Database;
import com.booknest.common.Repository;
import com.booknest.inventory.Inventory;
import com.booknest.inventory.OutOfStockException;
import com.booknest.order.CheckoutService;
import com.booknest.order.DailyReport;
import com.booknest.order.JdbcOrderRepository;
import com.booknest.order.Order;
import com.booknest.order.OrderLog;
import com.booknest.payment.PaymentMethod;
import com.booknest.pricing.MembershipLevel;
import com.booknest.pricing.Promotion;
import com.booknest.pricing.Promotion.NoPromotion;
import com.booknest.pricing.ShippingPolicy;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The BookNest console shop: it runs each customer's visit, from
 * the menu to checkout. Input and output are delegated to
 * CustomerInput and ShopScreen; business rules live in the
 * catalog, inventory, and checkout classes.
 */
public class BookNestShop {

    private static final Logger log =
            LoggerFactory.getLogger(BookNestShop.class);

    private final CustomerInput input;
    private final ShopScreen screen;
    private final Catalog catalog;
    private final Inventory inventory;
    private final CheckoutService checkout;

    BookNestShop(CustomerInput input, ShopScreen screen,
                 Catalog catalog, Inventory inventory,
                 CheckoutService checkout) {
        this.input = input;
        this.screen = screen;
        this.catalog = catalog;
        this.inventory = inventory;
        this.checkout = checkout;
    }

    /** Serves customers until one of them closes the shop. */
    public void run() {
        screen.say("Welcome to BookNest!");
        while (serveCustomer()) {
            screen.say("");
            screen.say("Next customer, please!");
        }
        LocalDate today = LocalDate.now(checkout.clock());
        screen.printDailyReport(DailyReport.of(
                checkout.orders().stream(), today, Store.ZONE));
    }

    /**
     * Serves one customer. Returns false when the shop should close.
     */
    private boolean serveCustomer() {
        Cart cart = new Cart();
        MenuOption ending = shopUntilCheckout(cart);
        boolean stayOpen = ending != MenuOption.CLOSE;
        if (cart.isEmpty()) {
            if (stayOpen) {
                screen.say("Your cart is empty. See you soon!");
            }
            return stayOpen;
        }
        checkOut(cart);
        return stayOpen;
    }

    /** Handles menu choices until the customer checks out. */
    private MenuOption shopUntilCheckout(Cart cart) {
        while (true) {
            screen.printMenu();
            String choice = input.ask("Choose an option: ");
            MenuOption option =
                    MenuOption.forKey(choice).orElse(null);
            if (option != null && option.endsVisit()) {
                return option;
            }
            switch (option) {
                case VIEW_CART -> screen.printCart(cart);
                case SEARCH -> search();
                case FIND_ORDER -> findOrder();
                case null, default -> chooseProduct(choice, cart);
            }
        }
    }

    /** A product number adds that product; anything else is wrong. */
    private void chooseProduct(String choice, Cart cart) {
        productFor(choice).ifPresentOrElse(
                product -> addToCart(cart, product,
                        input.readQuantity()),
                () -> screen.say("Unknown option: " + choice));
    }

    private void checkOut(Cart cart) {
        MembershipLevel level = input.readMembership();
        Promotion promotion = input.readPromotion();
        if (!(promotion instanceof NoPromotion)) {
            screen.say("Applied " + screen.describe(promotion) + ".");
        }
        PaymentMethod payment =
                input.readPayment(cart.shippableItems() > 0);
        CheckoutService.Placed placed =
                checkout.checkout(cart, level, promotion, payment);
        screen.printCart(cart);
        screen.printReceipt(placed.receipt(), level, promotion,
                payment, checkout.shippingName());
        if (!placed.logged()) {
            screen.say("Warning: order " + placed.order().id()
                    + " was not saved to " + checkout.logFileName()
                    + ".");
        }
        screen.printConfirmation(placed.order().id(),
                placed.placedAt(), cart.shippableItems() > 0);
    }

    private void search() {
        String text = input.ask("Search for: ");
        screen.printMatches(text,
                catalog.matching(ProductSearch.parse(text)));
    }

    private void findOrder() {
        String id = input.ask("Order number: ").toUpperCase();
        screen.say(checkout.orders().findById(id)
                .map(screen::describe)
                .orElse("No order " + id + " today."));
    }

    /** Returns the product for a menu choice such as "3", if any. */
    private Optional<Product> productFor(String choice) {
        return Optional.of(choice)
                .filter(text -> text.matches("\\d"))
                .map(Integer::parseInt)
                .flatMap(catalog::byMenuNumber);
    }

    /** Reserves stock first; only then adds the item to the cart. */
    private void addToCart(Cart cart, Product product, int quantity) {
        try {
            inventory.reserve(product, quantity);
            cart.add(product, quantity);
            screen.say("Added " + quantity + " x "
                    + product.menuLabel() + ".");
        } catch (OutOfStockException e) {
            screen.say("Sorry, " + e.getMessage() + ".");
        }
    }

    // ---- Starting up: build the objects and connect them ----------

    /**
     * Starts the shop with the files in the "data" folder, or in the
     * folder named by -Dbooknest.data=...
     */
    public static void main(String[] args) {
        Path data =
                Path.of(System.getProperty("booknest.data", "data"));
        CatalogFile.Result loaded;
        try {
            loaded = CatalogFile.load(data.resolve("catalog.csv"));
        } catch (IOException e) {
            System.out.println("BookNest can't open: " + describe(e));
            log.error("Catalog could not be loaded", e);
            System.exit(1);
            return;
        }
        for (String problem : loaded.problems()) {
            System.out.println("Skipped catalog.csv " + problem);
            log.warn("Skipped catalog.csv {}", problem);
        }
        if (loaded.entries().isEmpty()) {
            System.out.println("BookNest can't open: no products.");
            System.exit(1);
        }
        Catalog catalog = loaded.catalog();
        System.out.println("Loaded " + catalog.size() + " products.");
        log.info("BookNest opened with {} products", catalog.size());

        Repository<Order, String> orders =
                new JdbcOrderRepository(chooseDatabase(data));
        CheckoutService checkout = new CheckoutService(orders,
                new OrderLog(data.resolve("orders.csv")),
                chooseShipping(args), chooseClock());
        BookNestShop shop = new BookNestShop(
                new CustomerInput(new Scanner(System.in), System.out),
                new ShopScreen(System.out, catalog), catalog,
                new Inventory(catalog, loaded.stockLevels()),
                checkout);
        shop.run();
    }

    /**
     * The real clock in the store's zone, or, for demos and tests, a
     * clock stopped at a chosen store time:
     * java -Dbooknest.now=2026-03-06T16:30 ...
     */
    private static Clock chooseClock() {
        return Optional.ofNullable(System.getProperty("booknest.now"))
                .map(LocalDateTime::parse)
                .map(start -> start.atZone(Store.ZONE).toInstant())
                .map(instant -> Clock.fixed(instant, Store.ZONE))
                .orElseGet(() -> Clock.system(Store.ZONE));
    }

    /**
     * The database file data/booknest, or, for demos and tests, a
     * throwaway in-memory database: java -Dbooknest.db=mem ...
     */
    private static Database chooseDatabase(Path data) {
        if ("mem".equals(System.getProperty("booknest.db"))) {
            return new Database(
                    "jdbc:h2:mem:booknest;DB_CLOSE_DELAY=-1");
        }
        Path file = data.resolve("booknest").toAbsolutePath();
        return new Database("jdbc:h2:" + file);
    }

    /** Runs with "free-shipping" as an argument during promotions. */
    private static ShippingPolicy chooseShipping(String[] args) {
        if (args.length > 0 && args[0].equals("free-shipping")) {
            return ShippingPolicy.freeShippingPromotion();
        }
        return ShippingPolicy.standard();
    }

    /** Explains a file problem without printing a full path. */
    private static String describe(IOException e) {
        return switch (e) {
            case NoSuchFileException missing -> "file not found: "
                    + Path.of(missing.getFile()).getFileName();
            case AccessDeniedException denied -> "permission denied: "
                    + Path.of(denied.getFile()).getFileName();
            default -> e.getMessage();
        };
    }
}
