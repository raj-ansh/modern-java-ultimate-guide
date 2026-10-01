import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class StrategyDemo {

    /** The strategy: one way to charge for shipping. */
    @FunctionalInterface
    interface Shipping {
        BigDecimal cost(int items);
    }

    static final Shipping FLAT = items -> new BigDecimal("4.99");
    static final Shipping FREE_FROM_FIVE = items ->
            new BigDecimal(items >= 5 ? "0.00" : "4.99");
    static final Shipping PER_ITEM = items -> new BigDecimal("1.50")
            .multiply(BigDecimal.valueOf(items));

    /** The code that uses a strategy never changes... */
    static String quote(Shipping shipping, int items) {
        return items + " items: $" + shipping.cost(items);
    }

    public static void main(String[] args) {
        // ...while the strategy is chosen at run time
        Map<String, Shipping> policies = Map.of("flat", FLAT,
                "free-from-five", FREE_FROM_FIVE,
                "per-item", PER_ITEM);
        for (String name : List.of("flat", "free-from-five",
                "per-item")) {
            Shipping shipping = policies.get(name);
            System.out.println(name + ": " + quote(shipping, 2) + ", "
                    + quote(shipping, 6));
        }
    }
}
