import java.util.Comparator;

public class ByTitle implements Comparator<ShelfBook> {

    @Override
    public int compare(ShelfBook first, ShelfBook second) {
        return first.title().compareToIgnoreCase(second.title());
    }
}
