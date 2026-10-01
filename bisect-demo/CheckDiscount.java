public class CheckDiscount {

    public static void main(String[] args) {
        check("GOLD", 15);
        check("SILVER", 10);
        check("NONE", 0);
        System.out.println("Discounts OK");
    }

    static void check(String level, int expected) {
        int actual = Discount.percentFor(level);
        if (actual != expected) {
            System.out.println(level + ": expected " + expected
                    + " but was " + actual);
            System.exit(1);                 // non-zero: "bad"
        }
    }
}
