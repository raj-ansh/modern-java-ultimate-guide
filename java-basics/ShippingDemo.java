import java.math.BigDecimal;

public class ShippingDemo {

    public static void main(String[] args) {
        String row = "%-9s %-18s 2 items: %5s  6 items: %5s%n";
        for (ShippingMethod method : ShippingMethod.values()) {
            System.out.printf(row, method, method.description(),
                    method.cost(2), method.cost(6));
        }
    }
}

enum ShippingMethod {

    STANDARD("3-5 business days") {
        @Override
        BigDecimal cost(int items) {
            return items >= 5 ? FREE : new BigDecimal("4.99");
        }
    },
    EXPRESS("next business day") {
        @Override
        BigDecimal cost(int items) {
            return new BigDecimal("9.99");
        }
    },
    PICKUP("collect in store") {
        @Override
        BigDecimal cost(int items) {
            return FREE;
        }
    };

    private static final BigDecimal FREE = new BigDecimal("0.00");

    private final String description;

    ShippingMethod(String description) {
        this.description = description;
    }

    String description() {
        return description;
    }

    abstract BigDecimal cost(int items);
}
