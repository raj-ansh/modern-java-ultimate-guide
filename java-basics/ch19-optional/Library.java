import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/** Lookups that may find nothing, so they return Optional. */
public class Library {

    private static final Map<String, String> AUTHORS = Map.of(
            "Dune", "Frank Herbert",
            "Emma", "Jane Austen",
            "Beowulf", "Unknown");

    private static final Map<String, Integer> BORN = Map.of(
            "Frank Herbert", 1920,
            "Jane Austen", 1775);

    private static final Map<String, BigDecimal> PRICES = Map.of(
            "Dune", new BigDecimal("9.99"),
            "Emma", new BigDecimal("7.50"));

    static Optional<String> authorOf(String title) {
        return Optional.ofNullable(AUTHORS.get(title));
    }

    static Optional<Integer> yearBorn(String author) {
        return Optional.ofNullable(BORN.get(author));
    }

    static Optional<BigDecimal> priceOf(String title) {
        return Optional.ofNullable(PRICES.get(title));
    }
}
