import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentCounting {

    public static void main(String[] args) {
        List<String> tills = List.of(
                "Dune Emma Dune Circe", "Emma Emma Dune",
                "Circe Dune", "Dune Emma Circe Circe");

        // Safe for many threads; merge() is atomic for each key
        Map<String, Integer> sold = new ConcurrentHashMap<>();
        try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
            for (String receipt : tills) {
                pool.submit(() -> {
                    for (int round = 0; round < 1_000; round++) {
                        for (String title : receipt.split(" ")) {
                            sold.merge(title, 1, Integer::sum);
                        }
                    }
                });
            }
        }
        System.out.println(new TreeMap<>(sold));    // sorted to print
    }
}
