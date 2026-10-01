import java.math.BigDecimal;

public class EBook extends Book {

    private final String format;

    public EBook(String title, String author, BigDecimal price,
                 String format) {
        super(title, author, price);
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public String describe() {
        return super.describe() + " [" + format + " download]";
    }
}
