import java.math.BigDecimal;

public class ForLoops {

    public static void main(String[] args) {
        BigDecimal price = new BigDecimal("12.99");

        System.out.println("Qty   Total");
        for (int qty = 1; qty <= 5; qty++) {
            BigDecimal total =
                    price.multiply(BigDecimal.valueOf(qty));
            System.out.printf("%3d  %6s%n", qty, total);
        }

        for (int seconds = 3; seconds >= 1; seconds--) {
            System.out.println("Sale starts in " + seconds + "...");
        }
        System.out.println("The sale is live!");

        System.out.print("Even page numbers:");
        for (int page = 2; page <= 10; page += 2) {
            System.out.print(" " + page);
        }
        System.out.println();
    }
}
