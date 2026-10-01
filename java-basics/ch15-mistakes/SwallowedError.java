import java.math.BigDecimal;

public class SwallowedError {

    public static void main(String[] args) {
        String[] prices = {"12.99", "7,49", "3.35"};
        BigDecimal total = BigDecimal.ZERO;

        for (String price : prices) {
            try {
                total = total.add(new BigDecimal(price));
            } catch (NumberFormatException e) {
                // "it can't happen" -- so nothing is done here
            }
        }
        System.out.println("Order total: " + total);
    }
}
