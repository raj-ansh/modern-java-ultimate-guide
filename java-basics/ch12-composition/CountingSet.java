import java.util.Collection;
import java.util.HashSet;

public class CountingSet extends HashSet<String> {

    private int addAttempts = 0;

    @Override
    public boolean add(String element) {
        addAttempts++;
        return super.add(element);
    }

    @Override
    public boolean addAll(Collection<? extends String> elements) {
        addAttempts += elements.size();
        return super.addAll(elements);
    }

    public int getAddAttempts() {
        return addAttempts;
    }
}
