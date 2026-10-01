import java.math.BigDecimal;

public class InheritanceDemo {

    public static void main(String[] args) {
        Product lamp = new Product("Reading lamp",
                new BigDecimal("24.00"));
        Book novel = new Book("The Quiet Library", "Asha Rao",
                new BigDecimal("12.99"));
        EBook guide = new EBook("Java for Everyone", "Dev Malhotra",
                new BigDecimal("19.99"), "EPUB");

        System.out.println(lamp.describe());
        System.out.println(novel.describe());
        System.out.println(guide.describe());

        System.out.println("Inherited method:  " + guide.priceFor(2));
        System.out.println("Inherited getter:  " + guide.getAuthor());
        System.out.println("Its own getter:    " + guide.getFormat());
    }
}
