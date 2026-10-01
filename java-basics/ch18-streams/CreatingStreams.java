import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CreatingStreams {

    public static void main(String[] args) {
        // From values, an array, and a collection
        System.out.println(Stream.of("Dune", "Emma").toList());
        String[] shelves = {"A1", "A2", "B1"};
        System.out.println(Arrays.stream(shelves).toList());
        System.out.println(Book.SAMPLE.stream().count() + " books");

        // From a map: stream its entries (or keys, or values)
        Map<String, Integer> stock = Map.of("Dune", 4);
        stock.entrySet().stream()
                .forEach(e -> System.out.println(
                        e.getKey() + " -> " + e.getValue()));

        // Ranges of ints: a stream-based counting loop
        System.out.println(
                IntStream.rangeClosed(1, 5).boxed().toList());

        // Generated: each element computed from the previous one
        System.out.println(Stream.iterate(1, n -> n * 2)
                .limit(8)
                .toList());
        System.out.println(Stream.iterate(1, n -> n < 100, n -> n * 3)
                .toList());
    }
}
