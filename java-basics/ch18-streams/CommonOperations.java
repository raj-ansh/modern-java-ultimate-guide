import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CommonOperations {

    public static void main(String[] args) {
        List<Book> books = Book.SAMPLE;

        List<String> authors = books.stream()
                .map(Book::author)
                .distinct()
                .sorted()
                .toList();
        System.out.println(authors.size() + " authors, first: "
                + authors.getFirst());

        List<String> longestTwo = books.stream()
                .sorted(Comparator.comparingInt(Book::pages)
                        .reversed())
                .limit(2)
                .map(Book::title)
                .toList();
        System.out.println("Longest two: " + longestTwo);

        // Page 2 of an A-Z listing, three titles per page
        List<String> page2 = books.stream()
                .map(Book::title)
                .sorted()
                .skip(3)
                .limit(3)
                .toList();
        System.out.println("Page 2: " + page2);

        // flatMap: each title becomes a stream of words
        List<String> words = books.stream()
                .flatMap(book ->
                        Arrays.stream(book.title().split(" ")))
                .filter(word -> word.length() > 5)
                .toList();
        System.out.println("Long words: " + words);

        System.out.println("Any over 800 pages? "
                + books.stream().anyMatch(b -> b.pages() > 800));
        System.out.println("All under $20?      "
                + books.stream().allMatch(
                        b -> b.price().doubleValue() < 20));
        System.out.println("None by Tolstoy?    "
                + books.stream().noneMatch(
                        b -> b.author().contains("Tolstoy")));

        Book shortest = books.stream()
                .min(Comparator.comparingInt(Book::pages))
                .orElseThrow();
        System.out.println("Shortest: " + shortest.title());
        System.out.println("Sci-fi count: " + books.stream()
                .filter(b -> b.genre().equals("sci-fi"))
                .count());
    }
}
