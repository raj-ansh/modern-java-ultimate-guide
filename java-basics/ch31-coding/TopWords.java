import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TopWords {

    /** The k most frequent words; ties in alphabetical order. */
    static List<String> topWords(String text, int k) {
        Map<String, Long> counts = Arrays
                .stream(text.toLowerCase().split("\\W+"))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(Function.identity(),
                        Collectors.counting()));
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue()
                        .reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(k)
                .map(e -> e.getKey() + "=" + e.getValue())
                .toList();
    }

    public static void main(String[] args) {
        String text = """
                The cat and the hat. And the bat!
                A cat, a bat, a hat.""";
        System.out.println("top 3: " + topWords(text, 3));
        System.out.println("top 5: " + topWords(text, 5));
    }
}
