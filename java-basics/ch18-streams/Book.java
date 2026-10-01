import java.math.BigDecimal;
import java.util.List;

/** A book in the sample data used by this chapter's examples. */
public record Book(String title, String author, String genre,
                   int pages, BigDecimal price) {

    static Book of(String title, String author, String genre,
                   int pages, String price) {
        return new Book(title, author, genre, pages,
                new BigDecimal(price));
    }

    static final List<Book> SAMPLE = List.of(
        of("Dune",        "Frank Herbert",  "sci-fi",  612, "9.99"),
        of("Emma",        "Jane Austen",    "classic", 474, "7.50"),
        of("Persuasion",  "Jane Austen",    "classic", 249, "6.99"),
        of("Foundation",  "Isaac Asimov",   "sci-fi",  255, "8.99"),
        of("Stardust",    "Neil Gaiman",    "fantasy", 336, "12.99"),
        of("The Hobbit",  "J.R.R. Tolkien", "fantasy", 310, "10.99"),
        of("Neuromancer", "William Gibson", "sci-fi",  271, "8.49"),
        of("Middlemarch", "George Eliot",   "classic", 880, "11.25"));
}
