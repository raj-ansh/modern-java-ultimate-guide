public class FormattingOutput {

    public static void main(String[] args) {
        String title = "The Quiet Library";
        int copies = 1250;
        double rating = 4.6789;

        System.out.printf("Title:  %s%n", title);
        System.out.printf("Copies: %d%n", copies);
        System.out.printf("Copies: %,d%n", copies);
        System.out.printf("Rating: %.2f%n", rating);
        System.out.printf("Rating: %.1f out of 5%n", rating);

        System.out.printf("[%10s]%n", "right");
        System.out.printf("[%-10s]%n", "left");
        System.out.printf("[%05d]%n", 42);
        System.out.printf("Discount: %d%%%n", 15);

        String line = String.format("%-20s|%6d", title, copies);
        System.out.println(line);
        System.out.println(
                "%s has %d copies".formatted(title, copies));
    }
}
