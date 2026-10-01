import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Unmodifiable {

    public static void main(String[] args) {
        List<String> genres = List.of("classic", "poetry");
        try {
            genres.add("horror");
        } catch (UnsupportedOperationException e) {
            System.out.println("List.of can't change: " + e);
        }
        try {
            List.of("classic", null);
        } catch (NullPointerException e) {
            System.out.println("List.of rejects null");
        }

        List<String> shelf = new ArrayList<>(List.of("Dune"));
        List<String> view = Collections.unmodifiableList(shelf);
        List<String> copy = List.copyOf(shelf);
        shelf.add("Emma");                    // change the original
        System.out.println("Original: " + shelf);
        System.out.println("View:     " + view);
        System.out.println("Copy:     " + copy);
    }
}
