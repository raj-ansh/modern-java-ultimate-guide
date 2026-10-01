import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorsDemo {

    record Invoice(String id, int items, BigDecimal unitPrice) {}

    /** A task that returns a result: a Callable, not a Runnable. */
    static Callable<BigDecimal> totalOf(Invoice invoice) {
        return () -> invoice.unitPrice()
                .multiply(BigDecimal.valueOf(invoice.items()));
    }

    public static void main(String[] args)
            throws InterruptedException, ExecutionException {
        List<Invoice> invoices = List.of(
                new Invoice("INV-1", 3, new BigDecimal("12.99")),
                new Invoice("INV-2", 1, new BigDecimal("29.50")),
                new Invoice("INV-3", 10, new BigDecimal("3.35")));

        // A pool of 2 threads; close() waits for submitted tasks
        try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
            Future<BigDecimal> first = pool.submit(
                    totalOf(invoices.getFirst()));
            System.out.println("INV-1 total: " + first.get());

            // Many tasks at once; futures come back in list order
            List<Future<BigDecimal>> all = pool.invokeAll(
                    invoices.stream().map(ExecutorsDemo::totalOf)
                            .toList());
            BigDecimal sum = BigDecimal.ZERO;
            for (int i = 0; i < all.size(); i++) {
                BigDecimal total = all.get(i).get();
                System.out.println(
                        invoices.get(i).id() + ": " + total);
                sum = sum.add(total);
            }
            System.out.println("All invoices: " + sum);

            // A task that fails: the exception arrives via get()
            Future<BigDecimal> broken = pool.submit(
                    () -> BigDecimal.ONE.divide(BigDecimal.ZERO));
            try {
                broken.get();
            } catch (ExecutionException e) {
                System.out.println("Task failed: " + e.getCause());
            }
        }
        System.out.println("Pool closed.");
    }
}
