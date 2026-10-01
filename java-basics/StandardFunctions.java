import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class StandardFunctions {

    public static void main(String[] args) {
        Predicate<String> isLong = title -> title.length() > 10;
        Predicate<String> mentionsJava =
                title -> title.contains("Java");
        System.out.println("isLong(\"Poems\"):   "
                + isLong.test("Poems"));
        System.out.println("long AND Java:     "
                + isLong.and(mentionsJava).test("Java for Everyone"));
        System.out.println("NOT long:          "
                + isLong.negate().test("Poems"));

        Function<String, Integer> length = text -> text.length();
        Function<Integer, String> stars = n -> "*".repeat(n);
        System.out.println("length then stars: "
                + length.andThen(stars).apply("Zen"));

        Supplier<String> orderId = () -> "BN-" + (1000 + 42);
        System.out.println("supplied:          " + orderId.get());

        Consumer<String> shout = text ->
                System.out.println("consumed:          "
                        + text.toUpperCase());
        shout.accept("welcome");

        UnaryOperator<BigDecimal> addTax =
                price -> price.multiply(new BigDecimal("1.0825"))
                        .setScale(2, RoundingMode.HALF_UP);
        BinaryOperator<BigDecimal> add = (a, b) -> a.add(b);
        System.out.println("unary:             "
                + addTax.apply(new BigDecimal("10.00")));
        System.out.println("binary:            "
                + add.apply(new BigDecimal("1.50"), BigDecimal.TWO));
    }
}
