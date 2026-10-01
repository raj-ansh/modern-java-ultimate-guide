import java.util.Optional;

public class OrElseVsOrElseGet {

    static String lookUpDefault() {
        System.out.println("  (slow default lookup ran)");
        return "Anonymous";
    }

    public static void main(String[] args) {
        Optional<String> found = Library.authorOf("Emma");

        System.out.println("orElse:");
        String a = found.orElse(lookUpDefault());     // always runs
        System.out.println("  -> " + a);

        System.out.println("orElseGet:");
        String b = found.orElseGet(() -> lookUpDefault());
        // lookUpDefault() runs only when the Optional is empty
        System.out.println("  -> " + b);
    }
}
