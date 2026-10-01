import java.util.List;
import java.util.stream.Stream;

public class StreamPuzzles {

    public static void main(String[] args) {
        Stream<String> lazy = Stream.of("a", "b")
                .peek(s -> System.out.println("peek " + s));
        System.out.println("No terminal operation: nothing peeked");

        String first = Stream.of("kiwi", "fig", "apple", "banana")
                .filter(s -> {
                    System.out.println("  filter " + s);
                    return s.length() > 4;
                })
                .map(s -> {
                    System.out.println("  map    " + s);
                    return s.toUpperCase();
                })
                .findFirst()
                .orElse("none");
        System.out.println("findFirst -> " + first);

        Stream<String> once = List.of("x", "y").stream();
        System.out.println("count = " + once.count());
        try {
            once.count();
        } catch (IllegalStateException e) {
            System.out.println("second use -> " + e.getMessage());
        }
        lazy.close();
    }
}
