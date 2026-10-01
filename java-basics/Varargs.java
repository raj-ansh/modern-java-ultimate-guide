import java.math.BigDecimal;

public class Varargs {

    public static void main(String[] args) {
        BigDecimal novel = new BigDecimal("12.99");
        BigDecimal notebook = new BigDecimal("7.49");
        BigDecimal bookmark = new BigDecimal("3.35");

        System.out.println("No items:    " + sum());
        System.out.println("One item:    " + sum(novel));
        System.out.println("Three items: "
                + sum(novel, notebook, bookmark));

        printTags("Asha Rao", "fiction", "mystery");
        printTags("Unknown author");
    }

    static BigDecimal sum(BigDecimal... prices) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < prices.length; i++) {
            total = total.add(prices[i]);
        }
        return total;
    }

    static void printTags(String author, String... tags) {
        System.out.println(author + " has " + tags.length
                + " tag(s): " + String.join(", ", tags));
    }
}
