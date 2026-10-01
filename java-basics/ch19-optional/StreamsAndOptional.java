import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class StreamsAndOptional {

    public static void main(String[] args) {
        List<String> wanted =
                List.of("Dune", "Circe", "Emma", "Ulysses");

        // Keep only the prices that exist
        List<BigDecimal> prices = wanted.stream()
                .map(Library::priceOf)       // Stream<Optional<...>>
                .flatMap(Optional::stream)   // empty ones vanish
                .toList();
        System.out.println("Known prices: " + prices);

        Optional<String> firstLong = wanted.stream()
                .filter(title -> title.length() > 6)
                .findFirst();
        System.out.println("First long title: " + firstLong);

        // Primitive optionals, from primitive streams
        OptionalInt biggest = IntStream.of(612, 474, 249).max();
        OptionalDouble average = IntStream.empty().average();
        System.out.println("Max pages: " + biggest.getAsInt());
        System.out.println("Average of nothing: " + average);
        System.out.println("  or 0: " + average.orElse(0));
    }
}
