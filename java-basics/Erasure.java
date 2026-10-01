import java.util.ArrayList;
import java.util.List;

public class Erasure {

    public static void main(String[] args) {
        List<String> titles = new ArrayList<>();
        List<Integer> pages = new ArrayList<>();

        System.out.println("Same class at runtime? "
                + (titles.getClass() == pages.getClass()));
        System.out.println("Runtime class: "
                + titles.getClass().getName());
    }
}
