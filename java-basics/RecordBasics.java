import java.math.BigDecimal;

public class RecordBasics {

    public static void main(String[] args) {
        Book novel = new Book("The Quiet Library",
                new BigDecimal("12.99"));
        Book copy = new Book("The Quiet Library",
                new BigDecimal("12.99"));
        Book guide = new Book("Java for Everyone",
                new BigDecimal("29.50"));

        System.out.println(novel);
        System.out.println("Title: " + novel.title());
        System.out.println("Price: " + novel.price());
        System.out.println("novel.equals(copy):  "
                + novel.equals(copy));
        System.out.println("novel == copy:       " + (novel == copy));
        System.out.println("novel.equals(guide): "
                + novel.equals(guide));
        System.out.println("Same hash code? "
                + (novel.hashCode() == copy.hashCode()));
    }
}

record Book(String title, BigDecimal price) {
}
