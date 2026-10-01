import java.util.Optional;

public class Transforming {

    static String describeAuthor(String title) {
        return Library.authorOf(title)
                .filter(author -> !author.equals("Unknown"))
                .map(String::toUpperCase)
                .orElse("no known author");
    }

    static String authorBirthYear(String title) {
        return Library.authorOf(title)
                .flatMap(Library::yearBorn)      // may also be empty
                .map(year -> "born " + year)
                .orElse("birth year not on file");
    }

    public static void main(String[] args) {
        System.out.println("Dune:    " + describeAuthor("Dune"));
        System.out.println("Beowulf: " + describeAuthor("Beowulf"));
        System.out.println("Circe:   " + describeAuthor("Circe"));

        System.out.println("Emma:    " + authorBirthYear("Emma"));
        System.out.println("Beowulf: " + authorBirthYear("Beowulf"));

        // or(): try a second source only if the first is empty
        Optional<String> author = Library.authorOf("Circe")
                .or(() -> Optional.of("Madeline Miller"));
        System.out.println("Circe:   " + author.orElseThrow());

        // map vs flatMap
        Optional<Optional<Integer>> nested =
                Library.authorOf("Emma").map(Library::yearBorn);
        Optional<Integer> flat =
                Library.authorOf("Emma").flatMap(Library::yearBorn);
        System.out.println("map:     " + nested);
        System.out.println("flatMap: " + flat);
    }
}
