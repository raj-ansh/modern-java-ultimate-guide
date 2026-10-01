import java.math.BigDecimal;

public class BookDemo {

    public static void main(String[] args) {
        Book novel = new Book();
        novel.title = "The Quiet Library";
        novel.price = new BigDecimal("12.99");

        Book guide = new Book();
        guide.title = "Java for Everyone";
        // oops: we forgot to set the guide's price

        System.out.println(novel.describe());
        System.out.println(guide.describe());
    }
}
