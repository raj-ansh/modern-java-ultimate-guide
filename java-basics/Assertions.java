public class Assertions {

    public static void main(String[] args) {
        int[] stock = {5, 0, 3};
        System.out.println("Stock checked: " + totalStock(stock));

        stock[1] = -2; // a bug somewhere else sets a negative stock
        System.out.println("Stock checked: " + totalStock(stock));
    }

    static int totalStock(int[] stock) {
        int total = 0;
        for (int count : stock) {
            assert count >= 0 : "negative stock: " + count;
            total += count;
        }
        return total;
    }
}
