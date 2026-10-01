import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SequencedMap;
import java.util.SequencedSet;

public class SequencedDemo {

    public static void main(String[] args) {
        List<String> queue = new ArrayList<>(
                List.of("Dune", "Emma", "Circe"));
        System.out.println("First: " + queue.getFirst()
                + ", last: " + queue.getLast());
        queue.addFirst("Beloved");
        System.out.println("Reversed view: " + queue.reversed());

        SequencedSet<String> recent = new LinkedHashSet<>();
        recent.addLast("Dune");
        recent.addLast("Emma");
        recent.addFirst("Emma");      // moves Emma to the front
        System.out.println("Recently viewed: " + recent);

        SequencedMap<String, Integer> arrivals =
                new LinkedHashMap<>();
        arrivals.put("BN-1001", 2);
        arrivals.put("BN-1002", 5);
        arrivals.put("BN-1003", 1);
        System.out.println("Oldest order: " + arrivals.firstEntry());
        System.out.println("Removed newest: "
                + arrivals.pollLastEntry());
        System.out.println("Left: " + arrivals);
    }
}
