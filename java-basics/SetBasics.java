import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SetBasics {

    public static void main(String[] args) {
        List<String> tagged = List.of("sci-fi", "classic", "romance",
                "classic", "adventure", "sci-fi");

        Set<String> hash = new HashSet<>(tagged);
        Set<String> linked = new LinkedHashSet<>(tagged);
        Set<String> tree = new TreeSet<>(tagged);
        System.out.println("HashSet:       " + hash);
        System.out.println("LinkedHashSet: " + linked);
        System.out.println("TreeSet:       " + tree);

        System.out.println("Added poetry?  " + tree.add("poetry"));
        System.out.println("Added classic? " + tree.add("classic"));

        Set<String> mine = Set.of("classic", "poetry", "sci-fi");
        Set<String> yours = Set.of("classic", "romance", "sci-fi");

        Set<String> both = new TreeSet<>(mine);
        both.retainAll(yours);                  // intersection
        Set<String> either = new TreeSet<>(mine);
        either.addAll(yours);                   // union
        Set<String> onlyMine = new TreeSet<>(mine);
        onlyMine.removeAll(yours);              // difference
        System.out.println("In both:     " + both);
        System.out.println("In either:   " + either);
        System.out.println("Only mine:   " + onlyMine);
    }
}
