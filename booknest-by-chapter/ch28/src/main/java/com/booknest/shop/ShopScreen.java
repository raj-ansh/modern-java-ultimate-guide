package com.booknest.shop;

import com.booknest.cart.Cart;
import com.booknest.cart.CartLine;
import com.booknest.catalog.Catalog;
import com.booknest.catalog.Product;
import com.booknest.delivery.DeliveryEstimator;
import com.booknest.order.DailyReport;
import com.booknest.order.Order;
import com.booknest.payment.Card;
import com.booknest.payment.CashOnDelivery;
import com.booknest.payment.PaymentMethod;
import com.booknest.pricing.MembershipLevel;
import com.booknest.pricing.Promotion;
import com.booknest.pricing.Promotion.AmountOff;
import com.booknest.pricing.Promotion.CheapestFree;
import com.booknest.pricing.Promotion.NoPromotion;
import com.booknest.pricing.Promotion.PercentOff;
import com.booknest.pricing.Receipt;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Everything BookNest shows on the console. It writes to the
 * PrintStream it's given, so tests can capture what it shows.
 */
final class ShopScreen {

    private static final NumberFormat MONEY =
            NumberFormat.getCurrencyInstance(Locale.US);
    private static final String SUMMARY_LINE = "%-22s %10s%n";
    private static final String PRODUCT_LINE = "%d) %-28s %8s%n";
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

    private final PrintStream out;
    private final Catalog catalog;

    ShopScreen(PrintStream out, Catalog catalog) {
        this.out = out;
        this.catalog = catalog;
    }

    void say(String message) {
        out.println(message);
    }

    void printMenu() {
        out.println();
        for (int i = 0; i < catalog.size(); i++) {
            printProduct(i);
        }
        for (MenuOption option : MenuOption.values()) {
            out.println(option.menuLine());
        }
    }

    void printMatches(String text, List<Product> matches) {
        if (matches.isEmpty()) {
            out.println("Nothing matches \"" + text + "\".");
            return;
        }
        out.println("Found " + matches.size() + ":");
        for (Product product : matches) {
            printProduct(catalog.indexOf(product));
        }
    }

    void printCart(Cart cart) {
        out.println();
        out.println("Your cart:");
        if (cart.isEmpty()) {
            out.println("  (empty)");
            return;
        }
        List<CartLine> lines = cart.lines().stream()
                .sorted(MOST_EXPENSIVE_FIRST)
                .toList();
        for (CartLine line : lines) {
            out.printf("  %2d x %-28s %9s%n", line.quantity(),
                    line.product().menuLabel(), money(line.total()));
        }
    }

    void printReceipt(Receipt receipt, MembershipLevel level,
                      Promotion promotion, PaymentMethod payment,
                      String shippingName) {
        out.println();
        printSummaryLine("Subtotal", money(receipt.subtotal()));
        printSummaryLine("Discount (" + level + " "
                + level.discountPercent() + "%)",
                money(receipt.memberDiscount().negate()));
        if (!(promotion instanceof NoPromotion)) {
            printSummaryLine("Promo (" + promotion.code() + ")",
                    money(receipt.promotionDiscount().negate()));
        }
        printSummaryLine("Tax (8.25%)", money(receipt.tax()));
        BigDecimal shipping = receipt.shipping();
        printSummaryLine("Shipping (" + shippingName + ")",
                shipping.signum() == 0 ? "FREE" : money(shipping));
        if (receipt.paymentFee().signum() > 0) {
            printSummaryLine("Payment fee",
                    money(receipt.paymentFee()));
        }
        printSummaryLine("TOTAL", money(receipt.total()));
        out.println("Paid by " + describe(payment) + ".");
    }

    void printConfirmation(String id, ZonedDateTime placedAt,
                           boolean shipped) {
        out.println("Order " + id + " placed "
                + placedAt.format(PLACED_AT) + ".");
        if (shipped) {
            LocalDate arrives =
                    DELIVERY.deliveryDate(placedAt.toLocalDateTime());
            out.println("Estimated delivery: "
                    + arrives.format(DELIVERY_DAY) + ".");
        } else {
            out.println("Your e-book is ready to download now.");
        }
        out.println("Thank you for shopping at BookNest!");
    }

    void printDailyReport(DailyReport report) {
        out.println();
        out.println("Orders today: " + report.orders().size());
        for (Order order : report.orders()) {
            out.println("  " + describe(order));
        }
        out.println("Member orders: " + report.memberOrders());
        out.println("Revenue: " + money(report.revenue()));
        out.println("Items sold:");
        for (Map.Entry<String, Integer> entry
                : report.itemsSold().entrySet()) {
            out.printf("  %-28s %3d%n",
                    entry.getKey(), entry.getValue());
        }
    }

    String describe(Order order) {
        int items = order.items();
        String time = order.placedAt().atZone(Store.ZONE)
                .format(TIME_OF_DAY);
        return String.format("%-8s %-7s %2d %-5s %9s  %s", order.id(),
                order.level(), items, items == 1 ? "item" : "items",
                money(order.total()), time);
    }

    String describe(Promotion promotion) {
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

    private void printProduct(int index) {
        Product product = catalog.get(index);
        out.printf(PRODUCT_LINE, index + 1,
                product.menuLabel(), money(product.getPrice()));
    }

    private void printSummaryLine(String label, String value) {
        out.printf(SUMMARY_LINE, label, value);
    }

    private static String money(BigDecimal amount) {
        return MONEY.format(amount);
    }
}
