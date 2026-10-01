import java.sql.Connection;

public class ClassLoading {

    static class TaxTable {
        static final double RATE;

        static {                          // runs once, on first use
            System.out.println("  (TaxTable initialized)");
            RATE = 0.0825;
        }
    }

    public static void main(String[] args) {
        System.out.println("Loaders:");
        System.out.println("  String:      " + nameOf(String.class));
        System.out.println("  Connection:  "
                + nameOf(Connection.class));
        System.out.println("  ClassLoading: "
                + nameOf(ClassLoading.class));

        System.out.println("Before first use of TaxTable");
        System.out.println("Rate: " + TaxTable.RATE);
        System.out.println("Rate again: " + TaxTable.RATE);
    }

    static String nameOf(Class<?> type) {
        ClassLoader loader = type.getClassLoader();
        if (loader == null) {
            return "bootstrap";
        }
        // "java File.java" loads your code with an unnamed loader
        return loader.getName() != null ? loader.getName()
                : "unnamed " + loader.getClass().getSimpleName();
    }
}
