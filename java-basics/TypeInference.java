import java.math.BigDecimal;

public class TypeInference {

    public static void main(String[] args) {
        var title = "Java for Everyone";     // String
        var copies = 40;                     // int
        var price = new BigDecimal("29.50"); // BigDecimal

        System.out.println(title + ": " + copies
                + " copies at " + price);
    }
}
