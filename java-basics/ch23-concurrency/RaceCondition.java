import java.util.ArrayList;
import java.util.List;

public class RaceCondition {

    static int sold = 0;                    // shared by every thread

    public static void main(String[] args)
            throws InterruptedException {
        List<Thread> tills = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tills.add(Thread.ofPlatform().start(() -> {
                for (int n = 0; n < 100_000; n++) {
                    sold++;             // read, add one, write back
                }
            }));
        }
        for (Thread till : tills) {
            till.join();
        }
        System.out.println("Expected: 400000");
        System.out.println("Actual:   " + sold);
    }
}
