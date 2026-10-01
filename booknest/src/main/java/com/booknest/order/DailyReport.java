package com.booknest.order;

import com.booknest.cart.CartLine;
import com.booknest.pricing.MembershipLevel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The figures for one day's trading. Only calculation: printing
 * them is someone else's job.
 *
 * @param orders       the day's orders, oldest first
 * @param memberOrders how many were placed by members
 * @param revenue      the total paid
 * @param itemsSold    quantity sold per product label, A to Z
 */
public record DailyReport(List<Order> orders, long memberOrders,
                          BigDecimal revenue,
                          Map<String, Integer> itemsSold) {

    public DailyReport {
        orders = List.copyOf(orders);
        itemsSold =
                Collections.unmodifiableMap(new TreeMap<>(itemsSold));
    }

    /** The report for the orders placed on a day, in a time zone. */
    public static DailyReport of(Stream<Order> allOrders,
                                 LocalDate day, ZoneId zone) {
        List<Order> orders = allOrders
                .filter(order -> order.placedAt().atZone(zone)
                        .toLocalDate().equals(day))
                .toList();
        long memberOrders = orders.stream()
                .filter(order ->
                        order.level() != MembershipLevel.NONE)
                .count();
        BigDecimal revenue = orders.stream()
                .map(Order::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, Integer> itemsSold = orders.stream()
                .flatMap(order -> order.lines().stream())
                .collect(Collectors.groupingBy(
                        line -> line.product().menuLabel(),
                        TreeMap::new,
                        Collectors.summingInt(CartLine::quantity)));
        return new DailyReport(orders, memberOrders, revenue,
                itemsSold);
    }
}
