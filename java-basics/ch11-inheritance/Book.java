import java.math.BigDecimal;

public class Book extends Product {

    private final String author;

    public Book(String title, String author, BigDecimal price) {
        super(title, price);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String describe() {
        return super.describe() + " by " + author;
    }
}
