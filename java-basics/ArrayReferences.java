import java.util.Arrays;

public class ArrayReferences {

    public static void main(String[] args) {
        int[] stock = {5, 3, 8};

        int[] alias = stock;
        alias[0] = 0;
        System.out.println("stock after alias change: "
                + Arrays.toString(stock));

        int[] copy = Arrays.copyOf(stock, stock.length);
        copy[1] = 99;
        System.out.println("stock after copy change:  "
                + Arrays.toString(stock));
        System.out.println("copy:                     "
                + Arrays.toString(copy));

        int[] bigger = Arrays.copyOf(stock, 5);
        System.out.println("bigger copy:              "
                + Arrays.toString(bigger));

        int[] same = {0, 3, 8};
        System.out.println("stock == same:            "
                + (stock == same));
        System.out.println("Arrays.equals:            "
                + Arrays.equals(stock, same));

        restock(stock);
        System.out.println("stock after restock():    "
                + Arrays.toString(stock));
    }

    static void restock(int[] quantities) {
        for (int i = 0; i < quantities.length; i++) {
            quantities[i] += 10;
        }
    }
}
