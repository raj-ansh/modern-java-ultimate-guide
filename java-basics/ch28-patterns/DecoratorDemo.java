import java.util.ArrayList;
import java.util.List;

public class DecoratorDemo {

    interface Notifier {
        void send(String message);
    }

    /** The plain component: just prints. */
    static class ConsoleNotifier implements Notifier {
        public void send(String message) {
            System.out.println(message);
        }
    }

    /** A decorator: same interface, wraps another Notifier. */
    record Signed(Notifier inner, String shop) implements Notifier {
        public void send(String message) {
            inner.send(message + " (" + shop + ")");
        }
    }

    /** Another decorator: remembers everything it passes on. */
    record Recorded(Notifier inner, List<String> history)
            implements Notifier {
        public void send(String message) {
            history.add(message);
            inner.send(message);
        }
    }

    public static void main(String[] args) {
        List<String> history = new ArrayList<>();

        // Stack behaviour at run time, like java.io's streams
        Notifier notifier = new Recorded(
                new Signed(new ConsoleNotifier(), "BookNest"),
                history);

        notifier.send("Your order BN-1001 has shipped");
        notifier.send("Your order BN-1002 has shipped");
        System.out.println("Recorded: " + history.size()
                + " messages");
    }
}
