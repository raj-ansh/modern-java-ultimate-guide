import java.math.BigDecimal;

public class Book {

    String title;
    BigDecimal price;

    String describe() {
        return title + " costs " + price;
    }
}
