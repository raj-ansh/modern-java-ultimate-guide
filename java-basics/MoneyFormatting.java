import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MoneyFormatting {

    public static void main(String[] args) {
        BigDecimal total = new BigDecimal("1234.5");

        NumberFormat usd =
                NumberFormat.getCurrencyInstance(Locale.US);
        System.out.println("US dollars: " + usd.format(total));

        NumberFormat german =
                NumberFormat.getNumberInstance(Locale.GERMANY);
        german.setMinimumFractionDigits(2);
        System.out.println("German:     " + german.format(total));

        System.out.printf("printf:     %.2f%n", total);
        System.out.println("setScale:   " + total.setScale(2));
    }
}
