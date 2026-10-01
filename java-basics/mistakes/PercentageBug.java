public class PercentageBug {

    public static void main(String[] args) {
        int booksRead = 45;
        int booksBought = 60;

        int wrong = booksRead / booksBought * 100;
        double right = booksRead * 100.0 / booksBought;

        System.out.println("Wrong: " + wrong + "%");
        System.out.println("Right: " + right + "%");
    }
}
