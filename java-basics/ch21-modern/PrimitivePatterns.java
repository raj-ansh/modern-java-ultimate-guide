public class PrimitivePatterns {

    // switch on an int, with type patterns and guards (preview)
    static String describeQuantity(int quantity) {
        return switch (quantity) {
            case 0 -> "nothing";
            case int n when n < 0 -> "invalid (" + n + ")";
            case int n when n <= 10 -> "a few (" + n + ")";
            case int n -> "a bulk order of " + n;
        };
    }

    public static void main(String[] args) {
        for (int quantity : new int[] {0, -2, 3, 250}) {
            System.out.println(quantity + ": "
                    + describeQuantity(quantity));
        }

        // instanceof checks whether a value converts WITHOUT loss
        double[] prices = {12.0, 12.99};
        for (double price : prices) {
            if (price instanceof int whole) {
                System.out.println(price + " is exactly " + whole);
            } else {
                System.out.println(price + " has a fractional part");
            }
        }
    }
}
