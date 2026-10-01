import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ObserverDemo {

    record OrderPlaced(String id, String email, BigDecimal total) {}

    /** An observer: something that wants to hear about orders. */
    @FunctionalInterface
    interface OrderListener {
        void orderPlaced(OrderPlaced event);
    }

    /** The subject: knows only the listener interface. */
    static class Checkout {
        private final List<OrderListener> listeners =
                new ArrayList<>();

        void addListener(OrderListener listener) {
            listeners.add(listener);
        }

        void place(String id, String email, String total) {
            OrderPlaced event =
                    new OrderPlaced(id, email, new BigDecimal(total));
            System.out.println("Placed " + id);
            for (OrderListener listener : listeners) {
                listener.orderPlaced(event);
            }
        }
    }

    public static void main(String[] args) {
        Checkout checkout = new Checkout();

        // New reactions are added without changing Checkout at all
        checkout.addListener(event -> System.out.println(
                "  email receipt to " + event.email()));
        checkout.addListener(event -> System.out.println(
                "  loyalty points: " + event.total().intValue()));
        checkout.addListener(event -> {
            if (event.total().compareTo(new BigDecimal("50")) > 0) {
                System.out.println("  big order: thank-you card");
            }
        });

        checkout.place("BN-1001", "asha@example.com", "28.89");
        checkout.place("BN-1002", "ben@example.com", "64.10");
    }
}
