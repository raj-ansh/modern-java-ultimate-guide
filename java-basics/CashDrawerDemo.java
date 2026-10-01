public class CashDrawerDemo {

    public static void main(String[] args) {
        try (CashDrawer drawer = new CashDrawer("Till 1")) {
            drawer.record("12.99");
            drawer.record("3.35");
        }
        System.out.println("After the try block");
    }
}

class CashDrawer implements AutoCloseable {

    private final String name;

    CashDrawer(String name) {
        this.name = name;
        System.out.println(name + ": opened");
    }

    void record(String amount) {
        System.out.println(name + ": recorded " + amount);
    }

    @Override
    public void close() {
        System.out.println(name + ": closed and locked");
    }
}
