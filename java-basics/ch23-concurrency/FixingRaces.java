import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

public class FixingRaces {

    // Fix 1: synchronized - one thread at a time inside the method
    static class SynchronizedCounter {
        private int count;

        synchronized void increment() {
            count++;
        }

        synchronized int get() {
            return count;
        }
    }

    // Fix 2: an explicit lock, always released in finally
    static class LockedCounter {
        private final ReentrantLock lock = new ReentrantLock();
        private int count;

        void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }

        int get() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args)
            throws InterruptedException {
        SynchronizedCounter synced = new SynchronizedCounter();
        LockedCounter locked = new LockedCounter();
        AtomicInteger atomic = new AtomicInteger();    // Fix 3
        LongAdder adder = new LongAdder();              // Fix 4

        runOnFourThreads(() -> {
            synced.increment();
            locked.increment();
            atomic.incrementAndGet();
            adder.increment();
        });

        System.out.println("synchronized:  " + synced.get());
        System.out.println("ReentrantLock: " + locked.get());
        System.out.println("AtomicInteger: " + atomic.get());
        System.out.println("LongAdder:     " + adder.sum());
    }

    /** Runs the action 100,000 times on each of four threads. */
    static void runOnFourThreads(Runnable action)
            throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            threads.add(Thread.ofPlatform().start(() -> {
                for (int n = 0; n < 100_000; n++) {
                    action.run();
                }
            }));
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
