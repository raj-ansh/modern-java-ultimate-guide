import java.util.Scanner;

public class QuantityInput {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int quantity = 0;

        while (quantity < 1 || quantity > 10) {
            System.out.print("Quantity (1-10): ");
            String text = scanner.nextLine().strip();

            if (text.matches("\\d{1,2}")) {
                quantity = Integer.parseInt(text);
            }
            if (quantity < 1 || quantity > 10) {
                System.out.println(
                        "Please enter a whole number from 1 to 10.");
            }
        }

        System.out.println("You ordered " + quantity + " copies.");
    }
}
