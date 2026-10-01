import java.util.Scanner;

public class NextIntPitfall {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Quantity: ");
        int quantity = scanner.nextInt();

        System.out.print("Gift message: ");
        String message = scanner.nextLine();

        System.out.println("Quantity: " + quantity);
        System.out.println("Message:  [" + message + "]");
    }
}
