import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FirstUnique {

    /** The first character that appears exactly once, if any. */
    static Optional<Character> firstUnique(String text) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : text.toCharArray()) {
            counts.merge(c, 1, Integer::sum);
        }
        for (char c : text.toCharArray()) {       // second pass,
            if (counts.get(c) == 1) {              // in text order
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public static void main(String[] args) {
        for (String text : new String[] {"swiss", "BookNest",
                                         "aabb", ""}) {
            System.out.println("\"" + text + "\" -> "
                    + firstUnique(text)
                          .map(String::valueOf)
                          .orElse("none"));
        }
    }
}
