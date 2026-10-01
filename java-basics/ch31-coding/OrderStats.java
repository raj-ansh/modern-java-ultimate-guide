import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.summingInt;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderStats {

    record Line(String title, int quantity, BigDecimal price) {
        BigDecimal amount() {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
    }

    record Order(String id, String customer, List<Line> lines) {
        BigDecimal total() {
            return lines.stream().map(Line::amount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    public static void main(String[] args) {
        BigDecimal quiet = new BigDecimal("12.99");
        BigDecimal java = new BigDecimal("29.50");
        BigDecimal tea = new BigDecimal("9.75");
        List<Order> orders = List.of(
                new Order("BN-1", "asha", List.of(
                        new Line("The Quiet Library", 2, quiet),
                        new Line("Java for Everyone", 1, java))),
                new Order("BN-2", "dev", List.of(
                        new Line("Tea, Toast and Tales", 3, tea))),
                new Order("BN-3", "asha", List.of(
                        new Line("Tea, Toast and Tales", 1, tea))),
                new Order("BN-4", "mei", List.of(
                        new Line("The Quiet Library", 1, quiet))));

        // 1. Total revenue
        BigDecimal revenue = orders.stream().map(Order::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Revenue: " + revenue);

        // 2. Revenue per customer, highest first
        Map<String, BigDecimal> perCustomer = orders.stream()
                .collect(groupingBy(Order::customer,
                        reducing(BigDecimal.ZERO, Order::total,
                                BigDecimal::add)));
        perCustomer.entrySet().stream()
                .sorted(Map.Entry
                        .<String, BigDecimal>comparingByValue()
                        .reversed())
                .forEach(e -> System.out.println("  " + e.getKey()
                        + " " + e.getValue()));

        // 3. Best-selling title by copies
        orders.stream()
                .flatMap(order -> order.lines().stream())
                .collect(groupingBy(Line::title,
                        summingInt(Line::quantity)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(e -> System.out.println("Best seller: "
                        + e.getKey() + " (" + e.getValue()
                        + " copies)"));

        // 4. Orders over $30
        List<String> big = orders.stream()
                .filter(o -> o.total().compareTo(new BigDecimal("30"))
                        > 0)
                .map(Order::id)
                .toList();
        System.out.println("Orders over $30: " + big);
    }
}
