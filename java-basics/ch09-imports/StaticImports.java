import static java.lang.Math.max;
import static java.lang.Math.min;

public class StaticImports {

    public static void main(String[] args) {
        int requested = 14;
        int quantity = max(1, min(requested, 10));
        System.out.println("Requested " + requested
                + ", allowed " + quantity);
    }
}
