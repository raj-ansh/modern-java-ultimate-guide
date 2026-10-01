import java.util.LinkedHashMap;
import java.util.Map;

/** Keeps the most recently used entries, up to a capacity. */
public class LruCache<K, V> extends LinkedHashMap<K, V> {

    private final int capacity;

    public LruCache(int capacity) {
        super(16, 0.75f, true);        // true = access order
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;       // evict when over capacity
    }

    public static void main(String[] args) {
        LruCache<String, String> covers = new LruCache<>(3);
        covers.put("quiet", "quiet.jpg");
        covers.put("java", "java.jpg");
        covers.put("tea", "tea.jpg");
        System.out.println("after 3 puts:     " + covers.keySet());

        covers.get("quiet");                    // now most recent
        System.out.println("after get(quiet): " + covers.keySet());

        covers.put("lamp", "lamp.jpg");         // evicts "java"
        System.out.println("after put(lamp):  " + covers.keySet());
        System.out.println("java cached?      "
                + covers.containsKey("java"));
    }
}
