import java.util.Map;

public class NullProblem {

    static final Map<String, String> AUTHORS = Map.of(
            "Dune", "Frank Herbert",
            "Emma", "Jane Austen");

    /** Returns the author, or null if the title is unknown. */
    static String authorOf(String title) {
        return AUTHORS.get(title);
    }

    public static void main(String[] args) {
        System.out.println(authorOf("Dune").toUpperCase());
        System.out.println(authorOf("Circe").toUpperCase());
    }
}
