import java.util.ArrayList;
import java.util.List;

public class PassByValue {

    static void swap(int a, int b) {
        int t = a;
        a = b;
        b = t;
    }

    static void addItem(List<String> list) {
        list.add("added");               // changes the shared object
    }

    static void replace(List<String> list) {
        list = new ArrayList<>();        // changes only the copy
        list.add("replaced");
    }

    static void shout(String s) {
        s = s.toUpperCase();             // a new String, local only
    }

    public static void main(String[] args) {
        int x = 1, y = 2;
        swap(x, y);
        System.out.println("after swap:    x=" + x + ", y=" + y);

        List<String> list = new ArrayList<>(List.of("first"));
        addItem(list);
        System.out.println("after addItem: " + list);
        replace(list);
        System.out.println("after replace: " + list);

        String name = "asha";
        shout(name);
        System.out.println("after shout:   " + name);
    }
}
