import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CollectorsDemo {

    public static void main(String[] args) {
        List<Book> books = Book.SAMPLE;

        String catalogLine = books.stream()
                .map(Book::title)
                .limit(4)
                .collect(Collectors.joining(", ", "[", " ...]"));
        System.out.println(catalogLine);

        Map<String, Long> perGenre = books.stream()
                .collect(Collectors.groupingBy(
                        Book::genre, TreeMap::new,
                        Collectors.counting()));
        System.out.println("Books per genre: " + perGenre);

        Map<String, List<String>> titlesByAuthor = books.stream()
                .filter(book -> book.genre().equals("classic"))
                .collect(Collectors.groupingBy(
                        Book::author,
                        TreeMap::new,
                        Collectors.mapping(Book::title,
                                Collectors.toList())));
        System.out.println("Classics: " + titlesByAuthor);

        Map<String, Integer> pagesPerGenre = books.stream()
                .collect(Collectors.groupingBy(
                        Book::genre, TreeMap::new,
                        Collectors.summingInt(Book::pages)));
        System.out.println("Pages per genre: " + pagesPerGenre);

        Map<Boolean, List<String>> longOrShort = books.stream()
                .collect(Collectors.partitioningBy(
                        book -> book.pages() > 400,
                        Collectors.mapping(Book::title,
                                Collectors.toList())));
        System.out.println("Long:  " + longOrShort.get(true));
        System.out.println("Short: " + longOrShort.get(false));

        Map<String, BigDecimal> priceByTitle = books.stream()
                .filter(book -> book.genre().equals("fantasy"))
                .collect(Collectors.toMap(Book::title, Book::price));
        System.out.println("Fantasy prices: " + priceByTitle);
    }
}
