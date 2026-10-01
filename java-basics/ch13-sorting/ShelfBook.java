import java.math.BigDecimal;

public record ShelfBook(String title, BigDecimal price) {

    @Override
    public String toString() {
        return title + " " + price;
    }
}
