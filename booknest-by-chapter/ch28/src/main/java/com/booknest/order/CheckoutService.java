package com.booknest.order;

import com.booknest.cart.Cart;
import com.booknest.common.Repository;
import com.booknest.payment.PaymentMethod;
import com.booknest.pricing.MembershipLevel;
import com.booknest.pricing.Promotion;
import com.booknest.pricing.Receipt;
import com.booknest.pricing.ShippingPolicy;
import java.io.IOException;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Turns a customer's cart into a placed order: prices it, numbers
 * it, stores it, and logs it. Thread-safe: any number of tills may
 * call {@link #checkout} at the same time.
 */
public final class CheckoutService {

    private static final Logger log =
            LoggerFactory.getLogger(CheckoutService.class);

    /** The outcome of a checkout. */
    public record Placed(Order order, Receipt receipt,
                         ZonedDateTime placedAt, boolean logged) {}

    private final Repository<Order, String> orders;
    private final OrderLog orderLog;
    private final ShippingPolicy shipping;
    private final Clock clock;
    private final AtomicInteger nextNumber;

    public CheckoutService(Repository<Order, String> orders,
                           OrderLog orderLog, ShippingPolicy shipping,
                           Clock clock) {
        this.orders = Objects.requireNonNull(orders);
        this.orderLog = Objects.requireNonNull(orderLog);
        this.shipping = Objects.requireNonNull(shipping);
        this.clock = Objects.requireNonNull(clock);
        // Continue after any orders stored by earlier runs
        this.nextNumber = new AtomicInteger(1001 + orders.count());
    }

    /**
     * Places the order. The cart must belong to one customer, and
     * must not be changed while this method runs.
     */
    public Placed checkout(Cart cart, MembershipLevel level,
                           Promotion promotion,
                           PaymentMethod payment) {
        Receipt receipt =
                Receipt.of(cart, level, promotion, payment, shipping);
        ZonedDateTime placedAt = ZonedDateTime.now(clock);
        // getAndIncrement is atomic: no two orders share a number
        String id = "BN-" + nextNumber.getAndIncrement();
        Order order = new Order(id, level, cart.lines(),
                receipt.total(), placedAt.toInstant());
        orders.save(order);
        log.info("Order placed: id={}, items={}, total={}", id,
                order.items(), order.total());
        boolean logged = true;
        try {
            orderLog.append(order);
        } catch (IOException e) {
            logged = false;     // the sale stands; the caller warns
            log.warn("Order not logged: id={}, file={}", id,
                    orderLog.fileName(), e);
        }
        return new Placed(order, receipt, placedAt, logged);
    }

    public Clock clock() {
        return clock;
    }

    public String shippingName() {
        return shipping.name();
    }

    public String logFileName() {
        return orderLog.fileName();
    }

    public Repository<Order, String> orders() {
        return orders;
    }
}
