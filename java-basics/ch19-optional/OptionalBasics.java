import java.util.NoSuchElementException;
import java.util.Optional;

public class OptionalBasics {

    public static void main(String[] args) {
        Optional<String> found = Library.authorOf("Dune");
        Optional<String> missing = Library.authorOf("Circe");
        System.out.println(found + " / " + missing);

        // Asking
        System.out.println("Found? " + found.isPresent()
                + ", missing empty? " + missing.isEmpty());

        // Getting the value out, with a plan for "no value"
        System.out.println(missing.orElse("(unknown author)"));
        System.out.println(missing.orElseGet(() -> "computed"));
        System.out.println(found.orElseThrow());
        try {
            missing.orElseThrow(() -> new NoSuchElementException(
                    "no author for Circe"));
        } catch (NoSuchElementException e) {
            System.out.println("Thrown: " + e.getMessage());
        }

        // Acting on it
        found.ifPresent(author -> System.out.println("By " + author));
        missing.ifPresentOrElse(
                author -> System.out.println("By " + author),
                () -> System.out.println("Author not on file"));

        // Creating
        Optional<String> some = Optional.of("Emma");
        Optional<String> none = Optional.empty();
        Optional<String> maybe = Optional.ofNullable(null);
        System.out.println(some + " " + none + " " + maybe);
        try {
            Optional.of(null);
        } catch (NullPointerException e) {
            System.out.println("Optional.of(null) throws NPE");
        }
    }
}
