import java.math.BigDecimal;

public class BadCast {

    public static void main(String[] args) {
        Product lamp = new Product("Reading lamp",
                new BigDecimal("24.00"));
        Book book = (Book) lamp;
        System.out.println("Never printed: " + book.getAuthor());
    }
}
