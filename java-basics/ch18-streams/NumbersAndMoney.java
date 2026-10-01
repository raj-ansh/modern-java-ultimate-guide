import java.math.BigDecimal;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.IntStream;

public class NumbersAndMoney {

    public static void main(String[] args) {
        List<Book> books = Book.SAMPLE;

        int totalPages = books.stream()
                .mapToInt(Book::pages)   // now an IntStream
                .sum();
        System.out.println("Total pages: " + totalPages);

        double average = books.stream()
                .mapToInt(Book::pages)
                .average()               // an OptionalDouble
                .orElse(0);
        System.out.printf("Average pages: %.1f%n", average);

        IntSummaryStatistics stats = books.stream()
                .mapToInt(Book::pages)
                .summaryStatistics();
        System.out.println("Shortest " + stats.getMin()
                + ", longest " + stats.getMax()
                + ", books " + stats.getCount());

        // BigDecimal has no special stream: reduce with add
        BigDecimal shelfValue = books.stream()
                .map(Book::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Value of one of each: $" + shelfValue);

        // Squares of 1..5, added up
        int sumOfSquares = IntStream.rangeClosed(1, 5)
                .map(n -> n * n)
                .sum();
        System.out.println("Sum of squares 1..5: " + sumOfSquares);
    }
}
