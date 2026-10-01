import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CountingTags {

    private final Set<String> tags = new HashSet<>();
    private int addAttempts = 0;

    public boolean add(String tag) {
        addAttempts++;
        return tags.add(tag);
    }

    public void addAll(Collection<String> newTags) {
        for (String tag : newTags) {
            add(tag);
        }
    }

    public boolean contains(String tag) {
        return tags.contains(tag);
    }

    public int size() {
        return tags.size();
    }

    public int getAddAttempts() {
        return addAttempts;
    }
}
