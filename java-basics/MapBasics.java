import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapBasics {

    public static void main(String[] args) {
        Map<String, Integer> stock = new HashMap<>();
        stock.put("Dune", 4);
        stock.put("Emma", 2);
        Integer old = stock.put("Dune", 7);   // replaces; returns old
        System.out.println("Dune was " + old + ", now "
                + stock.get("Dune"));
        System.out.println("Circe: " + stock.get("Circe"));
        System.out.println("Circe or 0: "
                + stock.getOrDefault("Circe", 0));
        System.out.println("Has Emma? " + stock.containsKey("Emma"));

        // Counting: merge adds 1, or starts at 1 if the key is new
        String[] sales = {"Dune", "Emma", "Dune", "Circe", "Dune"};
        Map<String, Integer> sold = new TreeMap<>();  // sorted keys
        for (String title : sales) {
            sold.merge(title, 1, Integer::sum);
        }
        System.out.println("Sold: " + sold);

        for (Map.Entry<String, Integer> entry : sold.entrySet()) {
            System.out.println("  " + entry.getKey() + " x"
                    + entry.getValue());
        }

        // Grouping: computeIfAbsent creates each list on first use
        String[][] books = {{"Austen", "Emma"}, {"Herbert", "Dune"},
                {"Austen", "Persuasion"}};
        Map<String, List<String>> byAuthor = new TreeMap<>();
        for (String[] book : books) {
            byAuthor.computeIfAbsent(book[0], _ -> new ArrayList<>())
                    .add(book[1]);
        }
        System.out.println("By author: " + byAuthor);
    }
}
