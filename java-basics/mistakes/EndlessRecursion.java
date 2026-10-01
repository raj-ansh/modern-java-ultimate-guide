public class EndlessRecursion {

    public static void main(String[] args) {
        countdown(3);
    }

    static void countdown(int seconds) {
        System.out.println(seconds + "...");
        countdown(seconds - 1);
    }
}
