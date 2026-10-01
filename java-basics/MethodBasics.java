public class MethodBasics {

    public static void main(String[] args) {
        printDivider();
        greet("Priya");
        greet("Arjun");
        printDivider();

        int pages = totalPages(3, 320);
        System.out.println("Three 320-page books: " + pages
                + " pages");

        int days = daysToRead(pages, 50);
        System.out.println("At 50 pages a day:    " + days + " days");
    }

    static void printDivider() {
        System.out.println("-".repeat(36));
    }

    static void greet(String name) {
        System.out.println("Hello, " + name + "! Happy reading.");
    }

    static int totalPages(int books, int pagesPerBook) {
        return books * pagesPerBook;
    }

    static int daysToRead(int pages, int pagesPerDay) {
        return (pages + pagesPerDay - 1) / pagesPerDay;
    }
}
