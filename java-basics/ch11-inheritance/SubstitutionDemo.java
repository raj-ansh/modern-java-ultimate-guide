import java.math.BigDecimal;

public class SubstitutionDemo {

    public static void main(String[] args) {
        Product[] shelf = {
                new Product("Reading lamp", new BigDecimal("24.00")),
                new Book("The Quiet Library", "Asha Rao",
                        new BigDecimal("12.99")),
                new EBook("Java for Everyone", "Dev Malhotra",
                        new BigDecimal("19.99"), "EPUB")
        };

        for (Product product : shelf) {
            System.out.println(product.describe());
        }

        for (Product product : shelf) {
            if (product instanceof EBook ebook) {
                System.out.println("Download format: "
                        + ebook.getFormat());
            } else if (product instanceof Book book) {
                System.out.println("Printed book by "
                        + book.getAuthor());
            } else {
                System.out.println("Not a book: "
                        + product.getTitle());
            }
        }
    }
}
