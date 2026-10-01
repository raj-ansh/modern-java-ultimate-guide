import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

public class VirtualThreads {

    public static void main(String[] args) {
        LongAdder completed = new LongAdder();
        Instant start = Instant.now();

        // One new virtual thread per task: 10,000 threads
        try (ExecutorService executor =
                     Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 10_000; i++) {
                executor.submit(() -> {
                    Thread.sleep(100);    // waiting, as on a network
                    completed.increment();
                    return null;          // a Callable may throw
                });
            }
        }                                 // waits for all 10,000

        Duration took = Duration.between(start, Instant.now());
        System.out.println("Completed: " + completed.sum());
        System.out.println("Well under 10,000 x 100 ms? "
                + (took.toSeconds() < 5));

        Thread virtual = Thread.ofVirtual().unstarted(() -> {});
        System.out.println("Is virtual? " + virtual.isVirtual());
    }
}
