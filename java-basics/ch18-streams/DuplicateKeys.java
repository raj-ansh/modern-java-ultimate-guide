import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class DuplicateKeys {

    public static void main(String[] args) {
        try {
            Map<String, String> bookByAuthor = Book.SAMPLE.stream()
                    .collect(Collectors.toMap(
                            Book::author, Book::title));
            System.out.println(bookByAuthor);
        } catch (IllegalStateException e) {
            System.out.println("toMap failed: " + e.getMessage());
        }

        // A merge function decides what happens to a repeated key
        Map<String, String> titlesByAuthor = Book.SAMPLE.stream()
                .collect(Collectors.toMap(
                        Book::author,
                        Book::title,
                        (first, second) -> first + " & " + second,
                        TreeMap::new));
        titlesByAuthor.forEach((author, titles) ->
                System.out.println(author + ": " + titles));
    }
}
