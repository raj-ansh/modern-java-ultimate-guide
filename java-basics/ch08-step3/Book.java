import java.math.BigDecimal;

public class Book {

    private final String title;
    private final BigDecimal price;

    public Book(String title, BigDecimal price) {
        this.title = title;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal priceFor(int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
