import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Anagrams {

    /** The letters in sorted order: equal for all anagrams. */
    static String key(String word) {
        char[] letters = word.toLowerCase().toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }

    static boolean isAnagram(String a, String b) {
        return a.length() == b.length() && key(a).equals(key(b));
    }

    static Collection<List<String>> group(List<String> words) {
        // LinkedHashMap keeps groups in order of first appearance
        Map<String, List<String>> groups = new LinkedHashMap<>();
        for (String word : words) {
            groups.computeIfAbsent(key(word), k -> new ArrayList<>())
                  .add(word);
        }
        return groups.values();
    }

    public static void main(String[] args) {
        System.out.println("listen / silent: "
                + isAnagram("listen", "silent"));
        System.out.println("Listen / Silent: "
                + isAnagram("Listen", "Silent"));
        System.out.println("java / lava:     "
                + isAnagram("java", "lava"));

        List<String> words = List.of("stop", "java", "pots",
                "tops", "avaj", "code");
        System.out.println(group(words));
    }
}
