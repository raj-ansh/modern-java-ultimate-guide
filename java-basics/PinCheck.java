import java.util.Scanner;

public class PinCheck {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final String correctPin = "4321";
        final int maxAttempts = 3;

        int attempts = 0;
        boolean unlocked;
        do {
            System.out.print("Enter your gift-card PIN: ");
            String pin = scanner.nextLine().strip();
            attempts++;
            unlocked = pin.equals(correctPin);
            if (!unlocked) {
                System.out.println("Wrong PIN.");
            }
        } while (!unlocked && attempts < maxAttempts);

        if (unlocked) {
            System.out.println("Unlocked after " + attempts
                    + " attempt(s).");
        } else {
            System.out.println("Too many attempts. Card locked.");
        }
    }
}
