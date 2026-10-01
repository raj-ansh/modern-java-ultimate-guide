import java.math.BigDecimal;
import java.util.Objects;

public final class Book {

    private final String title;
    private final BigDecimal price;

    public Book(String title, BigDecimal price) {
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(price, "price must not be null");
        if (title.isBlank()) {
            throw new IllegalArgumentException(
                    "title must not be blank");
        }
        if (price.signum() < 0) {
            throw new IllegalArgumentException(
                    "price must not be negative: " + price);
        }
        this.title = title.strip();
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
