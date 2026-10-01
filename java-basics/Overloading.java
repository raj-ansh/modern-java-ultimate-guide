import java.math.BigDecimal;
import java.math.RoundingMode;

public class Overloading {

    public static void main(String[] args) {
        BigDecimal price = new BigDecimal("12.5");

        System.out.println(formatPrice(price));
        System.out.println(formatPrice(price, "EUR"));

        System.out.println(label("The Quiet Library"));
        System.out.println(label("The Quiet Library", 3));

        show(7);
        show(7.0);
        show('7');
    }

    static String formatPrice(BigDecimal amount) {
        return formatPrice(amount, "USD");
    }

    static String formatPrice(BigDecimal amount, String currency) {
        return amount.setScale(2, RoundingMode.HALF_UP)
                + " " + currency;
    }

    static String label(String title) {
        return label(title, 1);
    }

    static String label(String title, int quantity) {
        return quantity + " x " + title;
    }

    static void show(int value) {
        System.out.println("int:    " + value);
    }

    static void show(double value) {
        System.out.println("double: " + value);
    }

    static void show(char value) {
        System.out.println("char:   " + value);
    }
}
