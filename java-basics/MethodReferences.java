import java.math.BigDecimal;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MethodReferences {

    public static void main(String[] args) {
        // 1. Static method:            ClassName::staticMethod
        Function<String, Integer> parse = Integer::parseInt;
        System.out.println(parse.apply("42") + 1);

        // 2. Method of a given object: object::method
        Consumer<String> printer = System.out::println;
        printer.accept("printed by System.out::println");

        // 3. Method of the argument:   ClassName::instanceMethod
        Function<String, String> upper = String::toUpperCase;
        System.out.println(upper.apply("booknest"));

        // 4. Constructor:              ClassName::new
        Function<String, BigDecimal> money = BigDecimal::new;
        Supplier<StringBuilder> newBuilder = StringBuilder::new;
        System.out.println(money.apply("12.99").add(BigDecimal.ONE));
        System.out.println(newBuilder.get().append("fresh builder"));
    }
}
