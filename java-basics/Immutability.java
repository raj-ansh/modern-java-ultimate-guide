public class Immutability {

    public static void main(String[] args) {
        String genre = "fantasy";

        genre.toUpperCase();
        System.out.println("After toUpperCase(): " + genre);

        genre = genre.toUpperCase();
        System.out.println("After reassigning:   " + genre);
    }
}
