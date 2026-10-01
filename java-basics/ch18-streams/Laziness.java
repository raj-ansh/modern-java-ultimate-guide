import java.util.stream.Stream;

public class Laziness {

    public static void main(String[] args) {
        Stream<String> pipeline = Book.SAMPLE.stream()
                .filter(book -> {
                    System.out.println("  filter " + book.title());
                    return book.pages() > 400;
                })
                .map(book -> {
                    System.out.println("  map    " + book.title());
                    return book.title().toUpperCase();
                });
        System.out.println("Pipeline built. Nothing has run yet.");

        String first = pipeline.findFirst().orElseThrow();
        System.out.println("First long book: " + first);

        try {
            pipeline.count();                  // a second terminal op
        } catch (IllegalStateException e) {
            System.out.println("Reuse fails: " + e.getMessage());
        }
    }
}
