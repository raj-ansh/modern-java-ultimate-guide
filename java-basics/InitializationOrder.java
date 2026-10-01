public class InitializationOrder {

    static final String STORE = log("1. static field initializer");

    static {
        log("2. static initializer block");
    }

    private final String name = log("4. instance field initializer");

    {
        log("5. instance initializer block");
    }

    InitializationOrder() {
        log("6. constructor body");
    }

    static String log(String message) {
        System.out.println(message);
        return message;
    }

    public static void main(String[] args) {
        log("3. main starts");
        new InitializationOrder();
        log("7. first object ready");
        new InitializationOrder();
    }
}
