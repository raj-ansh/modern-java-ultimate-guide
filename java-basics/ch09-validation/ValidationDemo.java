import java.math.BigDecimal;

public class ValidationDemo {

    public static void main(String[] args) {
        Book novel = new Book("  The Quiet Library ",
                new BigDecimal("12.99"));
        System.out.println("Created: [" + novel.getTitle() + "]");

        Book broken = new Book("Free Pamphlet",
                new BigDecimal("-5.00"));
        System.out.println("Never printed: " + broken.getTitle());
    }
}
