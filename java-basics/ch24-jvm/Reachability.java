import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Reachability {

    record Receipt(String orderId, byte[] pdf) {}

    // A static field is a GC root: everything it reaches stays alive
    static final List<Receipt> CACHE = new ArrayList<>();

    public static void main(String[] args) {
        Receipt kept = new Receipt("BN-1001", new byte[1_000_000]);
        CACHE.add(kept);
        Receipt dropped = new Receipt("BN-1002", new byte[1_000_000]);

        // Weak references don't keep objects alive; they let us watch
        WeakReference<Receipt> watchKept = new WeakReference<>(kept);
        WeakReference<Receipt> watchDropped =
                new WeakReference<>(dropped);

        kept = null;          // the cache still refers to BN-1001
        dropped = null;       // nothing refers to BN-1002 any more

        System.gc();          // a request, which this JVM honours
        System.out.println("BN-1001 still in memory? "
                + (watchKept.get() != null));
        System.out.println("BN-1002 still in memory? "
                + (watchDropped.get() != null));
    }
}
