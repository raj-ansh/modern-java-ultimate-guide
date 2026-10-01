import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

public class ListPuzzles {

    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>(List.of(10, 20, 30));
        numbers.remove(1);                     // removes index 1
        System.out.println("remove(1):              " + numbers);
        numbers.remove(Integer.valueOf(10));   // removes the value
        System.out.println("remove(Integer 10):     " + numbers);

        List<String> fixed = Arrays.asList("a", "b", "c");
        fixed.set(0, "z");
        System.out.println("Arrays.asList set:      " + fixed);
        try {
            fixed.add("d");
        } catch (UnsupportedOperationException e) {
            System.out.println("Arrays.asList add:      "
                    + "UnsupportedOperationException");
        }
        try {
            List.of("a", "b").set(0, "z");
        } catch (UnsupportedOperationException e) {
            System.out.println("List.of set:            "
                    + "UnsupportedOperationException");
        }
        try {
            List.of("a", null);
        } catch (NullPointerException e) {
            System.out.println("List.of with null:      "
                    + "NullPointerException");
        }

        List<String> letters =
                new ArrayList<>(List.of("a", "b", "c"));
        try {
            for (String s : letters) {
                if (s.equals("a")) {
                    letters.remove(s);
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("remove a in for-each:   "
                    + "ConcurrentModificationException");
        }

        letters = new ArrayList<>(List.of("a", "b", "c"));
        for (String s : letters) {
            System.out.println("  visiting " + s);
            if (s.equals("b")) {
                letters.remove(s);
            }
        }
        System.out.println("remove b in for-each:   " + letters);

        letters = new ArrayList<>(List.of("a", "b", "c"));
        letters.removeIf(s -> s.equals("a"));
        System.out.println("removeIf:               " + letters);
    }
}
