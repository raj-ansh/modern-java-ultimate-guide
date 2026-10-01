import java.util.Scanner;

public class ReadingInput {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("What is your name? ");
        String name = scanner.nextLine().strip();

        System.out.print("How many books did you read this year? ");
        int books = Integer.parseInt(scanner.nextLine().strip());

        int pages = books * 300;
        System.out.println("Well done, " + name + "! " + books
                + " books is about " + pages + " pages.");
    }
}
