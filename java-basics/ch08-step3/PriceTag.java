import java.math.BigDecimal;

public class PriceTag {

    public static void main(String[] args) {
        Book novel = new Book("The Quiet Library",
                new BigDecimal("12.99"));

        System.out.println(novel.getTitle() + ": "
                + novel.getPrice());
        System.out.println("Two copies: " + novel.priceFor(2));
    }
}
