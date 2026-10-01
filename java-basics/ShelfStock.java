import java.util.Arrays;

public class ShelfStock {

    public static void main(String[] args) {
        // rows A, B, C; four shelves in each row
        int[][] stock = {
                {12, 0, 7, 3},
                {5, 9, 0, 14},
                {8, 8, 2, 1}
        };

        System.out.println("Rows:    " + stock.length);
        System.out.println("Shelves: " + stock[0].length);
        System.out.println("B4 has   " + stock[1][3] + " books");
        System.out.println(Arrays.deepToString(stock));

        for (int row = 0; row < stock.length; row++) {
            char rowName = (char) ('A' + row);
            int rowTotal = 0;
            for (int shelf = 0; shelf < stock[row].length; shelf++) {
                rowTotal += stock[row][shelf];
                if (stock[row][shelf] == 0) {
                    System.out.println("Empty shelf: " + rowName
                            + (shelf + 1));
                }
            }
            System.out.println("Row " + rowName + " total: "
                    + rowTotal);
        }
    }
}
