import java.util.HashMap;
import java.util.Map;

public class LongestUnique {

    /** The longest substring without a repeated character. */
    static String longestUnique(String text) {
        Map<Character, Integer> lastSeen = new HashMap<>();
        int windowStart = 0;
        int bestStart = 0;
        int bestLength = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Integer previous = lastSeen.put(c, i);
            if (previous != null && previous >= windowStart) {
                windowStart = previous + 1;   // slide past the repeat
            }
            if (i - windowStart + 1 > bestLength) {
                bestStart = windowStart;
                bestLength = i - windowStart + 1;
            }
        }
        return text.substring(bestStart, bestStart + bestLength);
    }

    public static void main(String[] args) {
        for (String text : new String[] {"abcabcbb", "bbbbb",
                                         "pwwkew", "abba", ""}) {
            String best = longestUnique(text);
            System.out.println("\"" + text + "\" -> \"" + best
                    + "\" (" + best.length() + ")");
        }
    }
}
