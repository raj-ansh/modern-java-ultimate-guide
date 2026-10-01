public class ResourcesDemo {

    public static void main(String[] args) {
        try (Connection db = new Connection("inventory-db", false);
             Connection api = new Connection("payments-api", true)) {
            System.out.println("Working with both connections...");
            throw new IllegalStateException("payment rejected");
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("  also: "
                        + suppressed.getMessage());
            }
        }
    }
}

class Connection implements AutoCloseable {

    private final String name;
    private final boolean failOnClose;

    Connection(String name, boolean failOnClose) {
        this.name = name;
        this.failOnClose = failOnClose;
        System.out.println("Opened " + name);
    }

    @Override
    public void close() {
        System.out.println("Closing " + name);
        if (failOnClose) {
            throw new IllegalStateException(
                    name + " failed to close");
        }
    }
}
