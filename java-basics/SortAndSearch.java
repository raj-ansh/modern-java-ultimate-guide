import java.util.Arrays;

public class SortAndSearch {

    public static void main(String[] args) {
        String[] titles = {"Poems", "atlas", "Zen", "Java", "Baking"};

        System.out.println("Index of 'Java':    "
                + indexOf(titles, "Java"));
        System.out.println("Index of 'Missing': "
                + indexOf(titles, "Missing"));

        Arrays.sort(titles);
        System.out.println("Sorted:             "
                + Arrays.toString(titles));

        Arrays.sort(titles, String.CASE_INSENSITIVE_ORDER);
        System.out.println("Ignoring case:      "
                + Arrays.toString(titles));

        int[] prices = {1299, 350, 2950, 799, 2400};
        Arrays.sort(prices);
        System.out.println("Prices in cents:    "
                + Arrays.toString(prices));
        System.out.println("Binary search 2400: index "
                + Arrays.binarySearch(prices, 2400));

        int[] ratings = new int[5];
        Arrays.fill(ratings, 3);
        System.out.println("Filled:             "
                + Arrays.toString(ratings));
    }

    static int indexOf(String[] items, String wanted) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(wanted)) {
                return i;
            }
        }
        return -1;
    }
}
