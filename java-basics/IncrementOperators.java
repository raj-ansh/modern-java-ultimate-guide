public class IncrementOperators {

    public static void main(String[] args) {
        int stock = 10;
        stock -= 3;  // sold 3
        stock += 5;  // received 5
        stock *= 2;  // doubled for a sale event
        System.out.println("Stock: " + stock);

        int a = 5;
        int b = a++; // use a, then increment
        System.out.println("a = " + a + ", b = " + b);

        int c = 5;
        int d = ++c; // increment, then use c
        System.out.println("c = " + c + ", d = " + d);
    }
}
