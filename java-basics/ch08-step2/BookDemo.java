import java.math.BigDecimal;

public class BookDemo {

    public static void main(String[] args) {
        Book novel = new Book("The Quiet Library",
                new BigDecimal("12.99"));
        Book guide = new Book("  Java for Everyone ", "29.50");

        System.out.println(novel.describe());
        System.out.println(guide.describe());
        System.out.println("Three novels cost " + novel.priceFor(3));
        System.out.println("Novel cheaper than guide? "
                + novel.isCheaperThan(guide));

        Book sameNovel = novel;
        Book twin = new Book("The Quiet Library", "12.99");
        System.out.println("novel == sameNovel: "
                + (novel == sameNovel));
        System.out.println("novel == twin:      " + (novel == twin));
    }
}
