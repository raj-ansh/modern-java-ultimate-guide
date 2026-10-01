import java.util.Arrays;

public class ShelfDemo {

    public static void main(String[] args) {
        String[] titles = {"Poems", "Atlas", "Zen"};

        LeakyShelf leaky = new LeakyShelf(titles);
        SafeShelf safe = new SafeShelf(titles);

        titles[0] = "CHANGED BY CALLER";
        leaky.getTitles()[1] = "CHANGED THROUGH GETTER";
        safe.getTitles()[1] = "CHANGED THROUGH GETTER";

        System.out.println("Leaky: "
                + Arrays.toString(leaky.getTitles()));
        System.out.println("Safe:  "
                + Arrays.toString(safe.getTitles()));
    }
}
