import java.util.List;

public class CompositionDemo {

    public static void main(String[] args) {
        List<String> newTags = List.of("fiction", "mystery", "new");

        CountingSet inherited = new CountingSet();
        inherited.addAll(newTags);
        System.out.println("Inheritance: " + inherited.size()
                + " tags, " + inherited.getAddAttempts()
                + " attempts");

        CountingTags composed = new CountingTags();
        composed.addAll(newTags);
        System.out.println("Composition: " + composed.size()
                + " tags, " + composed.getAddAttempts()
                + " attempts");
    }
}
