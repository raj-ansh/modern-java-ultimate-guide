import java.math.BigDecimal;
import java.time.LocalDate;

public class GenericMethods {

    /** A generic record: two values of possibly different types. */
    record Pair<A, B>(A first, B second) {

        Pair<B, A> swapped() {
            return new Pair<>(second, first);
        }
    }

    /** A generic method: works for arrays of any element type. */
    static <T> T lastOf(T[] items) {
        return items[items.length - 1];
    }

    /** Bounded: T must know how to compare itself. */
    static <T extends Comparable<T>> T largest(T[] items) {
        T best = items[0];
        for (T item : items) {
            if (item.compareTo(best) > 0) {
                best = item;
            }
        }
        return best;
    }

    public static void main(String[] args) {
        String[] titles = {"Poems", "Atlas", "Zen"};
        Integer[] pages = {320, 96, 512};
        BigDecimal[] prices = {new BigDecimal("12.99"),
                new BigDecimal("29.50"), new BigDecimal("3.35")};
        LocalDate[] releases = {LocalDate.of(2025, 9, 16),
                LocalDate.of(2026, 3, 17)};

        System.out.println("Last title:     " + lastOf(titles));
        System.out.println("Last page count: " + lastOf(pages));
        System.out.println("Largest title:  " + largest(titles));
        System.out.println("Largest pages:  " + largest(pages));
        System.out.println("Largest price:  " + largest(prices));
        System.out.println("Latest release: " + largest(releases));

        Pair<String, Integer> book = new Pair<>("Poems", 96);
        Pair<Integer, String> swapped = book.swapped();
        System.out.println(book + " -> " + swapped);
    }
}
