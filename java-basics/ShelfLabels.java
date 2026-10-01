public class ShelfLabels {

    public static void main(String[] args) {
        for (char row = 'A'; row <= 'C'; row++) {
            for (int shelf = 1; shelf <= 4; shelf++) {
                System.out.print(row + "" + shelf + " ");
            }
            System.out.println();
        }
    }
}
