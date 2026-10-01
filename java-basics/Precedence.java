public class Precedence {

    public static void main(String[] args) {
        System.out.println(2 + 3 * 4);
        System.out.println((2 + 3) * 4);
        System.out.println(1 + 2 + " books");
        System.out.println("books: " + 1 + 2);
        System.out.println("books: " + (1 + 2));
    }
}
