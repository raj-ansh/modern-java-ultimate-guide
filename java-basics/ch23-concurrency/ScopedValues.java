public class ScopedValues {

    // Who is being served, visible to every method called below
    static final ScopedValue<String> CUSTOMER =
            ScopedValue.newInstance();

    public static void main(String[] args)
            throws InterruptedException {
        ScopedValue.where(CUSTOMER, "asha@example.com")
                .run(ScopedValues::checkout);

        // Each thread can bind its own value
        Thread other = Thread.ofVirtual().start(() ->
                ScopedValue.where(CUSTOMER, "ben@example.com")
                        .run(ScopedValues::checkout));
        other.join();

        System.out.println("Outside any scope, bound? "
                + CUSTOMER.isBound());
    }

    static void checkout() {
        applyDiscount();
        audit("paid");
    }

    static void applyDiscount() {
        audit("discount checked");
    }

    // Deep in the call chain, without a "customer" parameter
    static void audit(String event) {
        System.out.println(CUSTOMER.get() + ": " + event);
    }
}
