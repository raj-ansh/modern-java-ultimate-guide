import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Gatherers;

public class GatherersDemo {

    public static void main(String[] args) {
        List<String> titles = Book.SAMPLE.stream()
                .map(Book::title)
                .toList();

        // Shelves of three books each (the last may be shorter)
        List<List<String>> shelves = titles.stream()
                .gather(Gatherers.windowFixed(3))
                .toList();
        shelves.forEach(shelf ->
                System.out.println("Shelf: " + shelf));

        // Neighbouring pairs: how the page count changes book to book
        List<Integer> changes = Book.SAMPLE.stream()
                .map(Book::pages)
                .gather(Gatherers.windowSliding(2))
                .map(pair -> pair.get(1) - pair.get(0))
                .toList();
        System.out.println("Page changes: " + changes);

        // Running total: every intermediate sum, not just the last
        List<BigDecimal> runningTotal = Book.SAMPLE.stream()
                .limit(4)
                .map(Book::price)
                .gather(Gatherers.scan(() -> BigDecimal.ZERO,
                        BigDecimal::add))
                .toList();
        System.out.println("Running total: " + runningTotal);
    }
}
