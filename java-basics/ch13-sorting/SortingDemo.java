import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;

public class SortingDemo {

    public static void main(String[] args) {
        Edition[] editions = {
                new Edition("Java for Everyone", 2025),
                new Edition("The Quiet Library", 2019),
                new Edition("Atlas of Stars", 2025)
        };
        Arrays.sort(editions);
        System.out.println("Natural order (year, then title):");
        for (Edition edition : editions) {
            System.out.println("  " + edition);
        }

        ShelfBook[] shelf = {
                new ShelfBook("Zen", new BigDecimal("9.99")),
                new ShelfBook("atlas", new BigDecimal("24.00")),
                new ShelfBook("Poems", new BigDecimal("9.99")),
                new ShelfBook("Baking", new BigDecimal("15.50"))
        };

        Arrays.sort(shelf, new ByPrice());
        print("By price:          ", shelf);

        Arrays.sort(shelf, new ByPrice().reversed());
        print("By price, reversed:", shelf);

        Comparator<ShelfBook> priceThenTitle =
                new ByPrice().thenComparing(new ByTitle());
        Arrays.sort(shelf, priceThenTitle);
        print("Price, then title: ", shelf);
    }

    static void print(String label, ShelfBook[] books) {
        System.out.println(label + " " + Arrays.toString(books));
    }
}
