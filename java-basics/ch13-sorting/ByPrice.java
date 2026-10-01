import java.util.Comparator;

public class ByPrice implements Comparator<ShelfBook> {

    @Override
    public int compare(ShelfBook first, ShelfBook second) {
        return first.price().compareTo(second.price());
    }
}
