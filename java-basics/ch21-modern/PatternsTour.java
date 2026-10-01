import java.math.BigDecimal;
import java.util.List;

public class PatternsTour {

    record Pair<A, B>(A first, B second) {}

    /** Describes a value from an untyped source, such as JSON. */
    static String describe(Object value) {
        return switch (value) {
            case null -> "missing";
            case String s when s.isBlank() -> "blank text";
            case String s -> "text \"" + s + "\"";
            case Integer i when i < 0 -> "negative number " + i;
            case Integer i -> "whole number " + i;
            case BigDecimal amount -> "amount " + amount;
            case Pair(String key, Integer count) ->
                    "count of " + key + ": " + count;
            case Pair(var first, var second) ->
                    "pair of " + first + " and " + second;
            case List<?> list when list.isEmpty() -> "empty list";
            case List<?> list -> "list of " + list.size();
            default -> "something else: " + value.getClass();
        };
    }

    public static void main(String[] args) {
        List<Object> inputs = List.of("Dune", " ", -3, 42,
                new BigDecimal("12.50"), new Pair<>("Dune", 4),
                new Pair<>(1, 2.5), List.of(), List.of("a", "b"),
                'x');
        for (Object input : inputs) {
            System.out.println(describe(input));
        }
        System.out.println(describe(null));

        // instanceof with a pattern: test, cast, and name in one step
        Object stock = new Pair<>("Emma", 2);
        if (stock instanceof Pair(String title, Integer left)
                && left < 3) {
            System.out.println("Low stock: " + title);
        }
    }
}
