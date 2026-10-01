public class Recursion {

    public static void main(String[] args) {
        countdown(3);
        System.out.println("4! = " + factorial(4));
        System.out.println("20! = " + factorial(20));
    }

    static void countdown(int seconds) {
        if (seconds == 0) {
            System.out.println("The sale is live!");
            return;
        }
        System.out.println(seconds + "...");
        countdown(seconds - 1);
    }

    static long factorial(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }
}
