import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;

public class FourWays {

    record ShelfBook(String title, BigDecimal price) {
        @Override
        public String toString() {
            return title + " " + price;
        }
    }

    // 1. A named class (Chapter 13)
    static class ByPrice implements Comparator<ShelfBook> {
        @Override
        public int compare(ShelfBook a, ShelfBook b) {
            return a.price().compareTo(b.price());
        }
    }

    public static void main(String[] args) {
        ShelfBook[] shelf = {
                new ShelfBook("Atlas", new BigDecimal("24.00")),
                new ShelfBook("Poems", new BigDecimal("9.99")),
                new ShelfBook("Zen", new BigDecimal("15.50"))
        };

        Arrays.sort(shelf, new ByPrice());
        print("1. Named class, by price:     ", shelf);

        // 2. An anonymous class: declared and created in one place
        Arrays.sort(shelf, new Comparator<ShelfBook>() {
            @Override
            public int compare(ShelfBook a, ShelfBook b) {
                return b.price().compareTo(a.price());
            }
        });
        print("2. Anonymous, price reversed: ", shelf);

        // 3. A lambda expression
        Arrays.sort(shelf, (a, b) -> a.title().compareTo(b.title()));
        print("3. Lambda, by title:          ", shelf);

        // 4. A comparator factory with a method reference
        Arrays.sort(shelf, Comparator.comparing(ShelfBook::price));
        print("4. Method ref, by price:      ", shelf);
    }

    static void print(String label, ShelfBook[] books) {
        System.out.println(label + Arrays.toString(books));
    }
}
