import java.math.BigDecimal;

public class Book {

    String title;
    BigDecimal price;

    Book(String title, BigDecimal price) {
        this.title = title;
        this.price = price;
    }

    Book(String title, String price) {
        String cleanTitle = title.strip();
        this(cleanTitle, new BigDecimal(price));
    }

    BigDecimal priceFor(int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    boolean isCheaperThan(Book other) {
        return this.price.compareTo(other.price) < 0;
    }

    String describe() {
        return title + " costs " + price;
    }
}
