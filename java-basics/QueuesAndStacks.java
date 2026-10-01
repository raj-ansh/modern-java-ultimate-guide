import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;

public class QueuesAndStacks {

    record Delivery(String orderId, int priority) {}

    public static void main(String[] args) {
        // Queue: first in, first out
        Queue<String> checkout = new ArrayDeque<>();
        checkout.offer("Asha");
        checkout.offer("Ben");
        checkout.offer("Chen");
        System.out.println("Next up: " + checkout.peek());
        System.out.println("Served:  " + checkout.poll());
        System.out.println("Waiting: " + checkout);

        // Stack: last in, first out
        Deque<String> undo = new ArrayDeque<>();
        undo.push("add Dune");
        undo.push("add Emma");
        undo.push("remove Dune");
        System.out.println("Undo:    " + undo.pop());
        System.out.println("Undo:    " + undo.pop());

        // Priority queue: smallest (by comparator) comes out first
        Queue<Delivery> deliveries = new PriorityQueue<>(
                Comparator.comparingInt(Delivery::priority));
        deliveries.offer(new Delivery("BN-1001", 3));
        deliveries.offer(new Delivery("BN-1002", 1));
        deliveries.offer(new Delivery("BN-1003", 2));
        while (!deliveries.isEmpty()) {
            System.out.println("Ship:    " + deliveries.poll());
        }
        System.out.println("Empty poll: " + deliveries.poll());
    }
}
