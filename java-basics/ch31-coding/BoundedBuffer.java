import java.util.ArrayDeque;
import java.util.Queue;

/** A fixed-size, thread-safe queue: put waits when full. */
public class BoundedBuffer<T> {

    private final Queue<T> items = new ArrayDeque<>();
    private final int capacity;

    public BoundedBuffer(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (items.size() == capacity) {   // while, not if:
            wait();                          // re-check after waking
        }
        items.add(item);
        notifyAll();                         // wake waiting takers
    }

    public synchronized T take() throws InterruptedException {
        while (items.isEmpty()) {
            wait();
        }
        T item = items.remove();
        notifyAll();                         // wake waiting putters
        return item;
    }

    public static void main(String[] args) throws Exception {
        BoundedBuffer<String> buffer = new BoundedBuffer<>(2);

        Thread producer = Thread.ofVirtual().start(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    buffer.put("order " + i);
                }
                buffer.put("DONE");          // a "poison pill"
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        String item;
        while (!(item = buffer.take()).equals("DONE")) {
            System.out.println("Processed " + item);
        }
        producer.join();
        System.out.println("All orders processed.");
    }
}
