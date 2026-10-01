import java.math.BigDecimal;

public class DivideForever {

    public static void main(String[] args) {
        BigDecimal price = new BigDecimal("10");
        System.out.println(price.divide(new BigDecimal("3")));
    }
}
