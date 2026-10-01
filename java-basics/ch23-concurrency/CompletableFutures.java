import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class CompletableFutures {

    // Pretend each lookup calls a slow remote service
    static BigDecimal priceOf(String title) {
        pause(200);
        return new BigDecimal("12.99");
    }

    static int stockOf(String title) {
        pause(200);
        return 6;
    }

    static double ratingOf(String title) {
        pause(100);
        throw new IllegalStateException("rating service is down");
    }

    public static void main(String[] args) {
        String title = "The Quiet Library";

        // Start all three lookups at once; none blocks the others
        CompletableFuture<BigDecimal> price =
                CompletableFuture.supplyAsync(() -> priceOf(title));
        CompletableFuture<Integer> stock =
                CompletableFuture.supplyAsync(() -> stockOf(title));
        CompletableFuture<String> rating = CompletableFuture
                .supplyAsync(() -> ratingOf(title))
                .thenApply(stars -> stars + " stars")
                .exceptionally(error -> "no rating");  // a fallback

        // Combine two results when both are ready
        CompletableFuture<String> summary = price.thenCombine(stock,
                (p, s) -> title + ": $" + p + ", " + s + " in stock");

        System.out.println(summary.join());
        System.out.println("Rating: " + rating.join());

        // Wait for several futures, then use them all
        CompletableFuture.allOf(price, stock, rating).join();
        System.out.println("All lookups finished: "
                + (price.isDone() && stock.isDone()
                        && rating.isDone()));
    }

    static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
