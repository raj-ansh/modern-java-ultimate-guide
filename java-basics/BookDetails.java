public class BookDetails {

    public static void main(String[] args) {
        String title = "The Quiet Library";
        int pages = 320;
        double rating = 4.6;
        boolean inStock = true;

        System.out.println("Title:    " + title);
        System.out.println("Pages:    " + pages);
        System.out.println("Rating:   " + rating);
        System.out.println("In stock: " + inStock);

        pages = 352; // the second edition is longer
        System.out.println("Pages in 2nd edition: " + pages);
    }
}
