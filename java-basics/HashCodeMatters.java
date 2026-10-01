import java.util.HashSet;
import java.util.Set;

public class HashCodeMatters {

    /** No equals or hashCode: equal only to itself. */
    static class IsbnClass {
        private final String value;

        IsbnClass(String value) {
            this.value = value;
        }
    }

    /** A record gets equals and hashCode from its components. */
    record Isbn(String value) {}

    public static void main(String[] args) {
        Set<IsbnClass> classes = new HashSet<>();
        classes.add(new IsbnClass("978-0441013593"));
        classes.add(new IsbnClass("978-0441013593"));
        System.out.println("Class set size:  " + classes.size());
        System.out.println("Contains it? " + classes.contains(
                new IsbnClass("978-0441013593")));

        Set<Isbn> records = new HashSet<>();
        records.add(new Isbn("978-0441013593"));
        records.add(new Isbn("978-0441013593"));
        System.out.println("Record set size: " + records.size());
        System.out.println("Contains it? " + records.contains(
                new Isbn("978-0441013593")));
    }
}
