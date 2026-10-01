public class StackDepth {

    static int depth = 0;

    /** Calls itself forever: there is no base case. */
    static void countDown(int n) {
        depth++;
        countDown(n - 1);
    }

    /** The same idea, with a base case. */
    static long sumTo(int n) {
        return n == 0 ? 0 : n + sumTo(n - 1);
    }

    public static void main(String[] args) {
        System.out.println("sumTo(1000) = " + sumTo(1000));

        try {
            countDown(10);
        } catch (StackOverflowError e) {
            System.out.println("StackOverflowError after more than "
                    + "1,000 calls? " + (depth > 1_000));
        }
        System.out.println("The program survived and continues.");
    }
}
