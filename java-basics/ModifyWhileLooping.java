import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

public class ModifyWhileLooping {

    public static void main(String[] args) {
        List<String> cart = new ArrayList<>(
                List.of("Dune", "Bookmark", "Emma", "Bookmark"));

        try {
            for (String item : cart) {
                if (item.equals("Bookmark")) {
                    cart.remove(item);         // changes the list...
                }                              // ...mid-loop
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Loop failed: "
                    + e.getClass().getName());
        }

        // Fix 1: remove through the iterator itself
        Iterator<String> it = cart.iterator();
        while (it.hasNext()) {
            if (it.next().equals("Bookmark")) {
                it.remove();
            }
        }
        System.out.println("With Iterator.remove: " + cart);

        // Fix 2 (usually best): say what to remove, not how
        cart.add("Pen");
        cart.removeIf(item -> item.equals("Pen"));
        System.out.println("With removeIf:        " + cart);
    }
}
