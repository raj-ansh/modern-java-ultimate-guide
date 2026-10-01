import java.util.Arrays;

public class ArrayBasics {

    public static void main(String[] args) {
        String[] genres = {"Fiction", "Mystery", "Science", "Poetry"};
        int[] dailySales = new int[7];

        System.out.println("Genres:        " + genres.length);
        System.out.println("First genre:   " + genres[0]);
        System.out.println("Last genre:    "
                + genres[genres.length - 1]);

        genres[3] = "History";
        System.out.println("All genres:    "
                + Arrays.toString(genres));

        System.out.println("New sales:     "
                + Arrays.toString(dailySales));
        dailySales[0] = 12;
        dailySales[6] = 30;
        System.out.println("Updated sales: "
                + Arrays.toString(dailySales));

        double[] ratings = new double[3];
        boolean[] inStock = new boolean[3];
        String[] notes = new String[3];
        System.out.println("Defaults: "
                + Arrays.toString(ratings) + " "
                + Arrays.toString(inStock) + " "
                + Arrays.toString(notes));
    }
}
