import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class BehaviourAsValues {

    static boolean debugEnabled = false;
    static int reportsBuilt = 0;

    public static void main(String[] args) {
        UnaryOperator<BigDecimal> tenOff = percentOff(10);
        UnaryOperator<BigDecimal> quarterOff = percentOff(25);
        BigDecimal price = new BigDecimal("40.00");
        System.out.println("10% off: " + tenOff.apply(price));
        System.out.println("25% off: " + quarterOff.apply(price));
        System.out.println("Both:    "
                + tenOff.andThen(quarterOff).apply(price));

        debug(() -> expensiveReport());
        System.out.println("Reports built while debug is off: "
                + reportsBuilt);
        debugEnabled = true;
        debug(() -> expensiveReport());
        System.out.println("Reports built while debug is on:  "
                + reportsBuilt);
    }

    /** A method that RETURNS behaviour. */
    static UnaryOperator<BigDecimal> percentOff(int percent) {
        BigDecimal factor = BigDecimal.valueOf(100 - percent)
                .movePointLeft(2);
        return price -> price.multiply(factor)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /** The message is only built if it's actually needed. */
    static void debug(Supplier<String> message) {
        if (debugEnabled) {
            System.out.println("DEBUG " + message.get());
        }
    }

    static String expensiveReport() {
        reportsBuilt++;
        return "stock report with 10,000 lines";
    }
}
