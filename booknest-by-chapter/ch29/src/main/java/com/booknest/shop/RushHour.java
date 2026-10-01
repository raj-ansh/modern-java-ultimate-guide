package com.booknest.shop;

import com.booknest.cart.Cart;
import com.booknest.catalog.Catalog;
import com.booknest.catalog.CatalogFile;
import com.booknest.catalog.Product;
import com.booknest.common.InMemoryRepository;
import com.booknest.common.Repository;
import com.booknest.inventory.Inventory;
import com.booknest.inventory.OutOfStockException;
import com.booknest.order.CheckoutService;
import com.booknest.order.Order;
import com.booknest.order.OrderLog;
import com.booknest.payment.Card;
import com.booknest.pricing.MembershipLevel;
import com.booknest.pricing.Promotion.NoPromotion;
import com.booknest.pricing.ShippingPolicy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * A simulated rush hour: 70 customers check out at the same moment,
 * each on its own virtual thread, against one shared inventory,
 * repository, and order log. Proves the shop is thread-safe.
 */
public final class RushHour {

    public static void main(String[] args) throws IOException {
        Path data =
                Path.of(System.getProperty("booknest.data", "data"));
        CatalogFile.Result loaded =
                CatalogFile.load(data.resolve("catalog.csv"));
        Catalog catalog = loaded.catalog();
        Inventory inventory =
                new Inventory(catalog, loaded.stockLevels());
        Path logFile = data.resolve("rush-hour-orders.csv");
        Files.deleteIfExists(logFile);      // a fresh log each run
        CheckoutService checkout = new CheckoutService(
                new InMemoryRepository<>(), new OrderLog(logFile),
                ShippingPolicy.standard(), Clock.systemUTC());

        Product lamp = find(catalog, "Reading lamp");      // 2 left
        Product bookmarks = find(catalog, "Bookmark set"); // 20 left
        LongAdder soldOut = new LongAdder();

        try (ExecutorService tills =
                     Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 50; i++) {
                tills.submit(() ->
                        buy(lamp, inventory, checkout, soldOut));
            }
            for (int i = 0; i < 20; i++) {
                tills.submit(() ->
                        buy(bookmarks, inventory, checkout, soldOut));
            }
        }                                       // waits for all 70

        Repository<Order, String> orders = checkout.orders();
        long uniqueIds = orders.stream().map(Order::id).distinct()
                .count();
        int logLines = Files.readAllLines(logFile).size() - 1;
        System.out.println("Customers:      70");
        System.out.println("Orders placed:  " + orders.count());
        System.out.println("Unique ids:     " + uniqueIds);
        System.out.println("Sold out:       " + soldOut.sum());
        System.out.println("Lamps left:     "
                + inventory.available(lamp));
        System.out.println("Bookmarks left: "
                + inventory.available(bookmarks));
        System.out.println("Log lines:      " + logLines);
    }

    /** One customer: reserve one item, then check out alone. */
    private static void buy(Product product, Inventory inventory,
                            CheckoutService checkout,
                            LongAdder soldOut) {
        try {
            inventory.reserve(product, 1);
        } catch (OutOfStockException e) {
            soldOut.increment();
            return;
        }
        Cart cart = new Cart();     // one customer's: never shared
        cart.add(product, 1);
        checkout.checkout(cart, MembershipLevel.NONE,
                new NoPromotion(), new Card("4242"));
    }

    private static Product find(Catalog catalog, String label) {
        return catalog.matching(p -> p.menuLabel().equals(label))
                .getFirst();
    }
}
