import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListBasics {

    public static void main(String[] args) {
        List<String> wishlist = new ArrayList<>();
        wishlist.add("Dune");
        wishlist.add("Emma");
        wishlist.add("Beloved");
        wishlist.add(1, "Circe");            // insert at index 1
        System.out.println(wishlist + ", size " + wishlist.size());

        System.out.println("First: " + wishlist.get(0));
        wishlist.set(3, "Middlemarch");      // replace index 3
        System.out.println("Has Emma? " + wishlist.contains("Emma"));
        System.out.println("Emma at " + wishlist.indexOf("Emma"));

        wishlist.remove("Dune");             // remove by value
        wishlist.remove(0);                  // remove by index
        System.out.println("After removes: " + wishlist);

        wishlist.addAll(List.of("Ulysses", "Atonement", "Emma"));
        Collections.sort(wishlist);          // natural (A-Z) order
        System.out.println("Sorted: " + wishlist);
        System.out.println("Emma appears "
                + Collections.frequency(wishlist, "Emma") + " times");

        for (String title : wishlist) {
            System.out.print(title.charAt(0));
        }
        System.out.println();
    }
}
