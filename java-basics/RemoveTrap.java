import java.util.ArrayList;
import java.util.List;

public class RemoveTrap {

    public static void main(String[] args) {
        List<Integer> quantities = new ArrayList<>(List.of(5, 10, 1));

        quantities.remove(1);                  // index 1, not value 1
        System.out.println(quantities);

        quantities.remove(Integer.valueOf(1)); // the value 1
        System.out.println(quantities);
    }
}
