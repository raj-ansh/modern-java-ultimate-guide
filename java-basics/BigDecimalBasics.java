import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalBasics {

    public static void main(String[] args) {
        System.out.println("double:      " + (0.1 + 0.2));

        BigDecimal a = new BigDecimal("0.1");
        BigDecimal b = new BigDecimal("0.2");
        System.out.println("BigDecimal:  " + a.add(b));

        System.out.println("From double: " + new BigDecimal(0.1));
        System.out.println("valueOf:     " + BigDecimal.valueOf(0.1));

        BigDecimal price = new BigDecimal("19.99");
        BigDecimal third = price.divide(new BigDecimal("3"),
                2, RoundingMode.HALF_UP);
        System.out.println("19.99 / 3:   " + third);

        BigDecimal x = new BigDecimal("2.0");
        BigDecimal y = new BigDecimal("2.00");
        System.out.println("equals:      " + x.equals(y));
        System.out.println("compareTo:   " + x.compareTo(y));
    }
}
