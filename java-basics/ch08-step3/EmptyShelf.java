import java.math.BigDecimal;

public class EmptyShelf {

    public static void main(String[] args) {
        Book[] shelf = new Book[3];
        shelf[0] = new Book("The Quiet Library",
                new BigDecimal("12.99"));
        shelf[2] = new Book("Java for Everyone",
                new BigDecimal("29.50"));

        for (Book book : shelf) {
            if (book == null) {
                System.out.println("(empty slot)");
            } else {
                System.out.println(book.getTitle());
            }
        }

        System.out.println(shelf[1].getTitle());
    }
}
