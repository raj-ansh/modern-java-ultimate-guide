public class FirstThreads {

    public static void main(String[] args)
            throws InterruptedException {
        long[] totals = new long[3];            // one slot per thread

        Thread[] workers = new Thread[3];
        for (int i = 0; i < workers.length; i++) {
            int slot = i;                       // effectively final
            workers[i] = Thread.ofPlatform()
                    .name("worker-" + i)
                    .start(() -> {
                        long sum = 0;
                        for (int n = 1; n <= 1_000_000; n++) {
                            sum += n % (slot + 2);
                        }
                        totals[slot] = sum;     // no other thread
                    });                         // writes this slot
        }
        String me = Thread.currentThread().getName();
        System.out.println("Started " + workers.length
                + " threads from " + me);

        for (Thread worker : workers) {
            worker.join();                  // wait for it to finish
        }
        for (int i = 0; i < totals.length; i++) {
            System.out.println(workers[i].getName() + " -> "
                    + totals[i] + " (alive? " + workers[i].isAlive()
                    + ")");
        }
    }
}
