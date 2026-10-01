import java.math.BigDecimal;

public class ParsingNumbers {

    public static void main(String[] args) {
        String quantityText = "3";
        String priceText = "12.99";

        int quantity = Integer.parseInt(quantityText);
        BigDecimal price = new BigDecimal(priceText);
        BigDecimal total =
                price.multiply(BigDecimal.valueOf(quantity));
        System.out.println("Total: " + total);

        String pages = String.valueOf(320);
        System.out.println("As text: " + pages + ", length "
                + pages.length());

        int broken = Integer.parseInt("3 books");
        System.out.println("Never printed: " + broken);
    }
}
