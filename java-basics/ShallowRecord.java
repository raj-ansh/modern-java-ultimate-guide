import java.util.Arrays;

public class ShallowRecord {

    public static void main(String[] args) {
        String[] titles = {"Poems", "Atlas"};
        Shelf shelf = new Shelf("A1", titles);

        titles[0] = "CHANGED";
        System.out.println("Shelf now holds: "
                + Arrays.toString(shelf.titles()));

        Shelf twin = new Shelf("A1",
                new String[] {"CHANGED", "Atlas"});
        System.out.println("Same contents, equal? "
                + shelf.equals(twin));
    }
}

record Shelf(String label, String[] titles) {
}
