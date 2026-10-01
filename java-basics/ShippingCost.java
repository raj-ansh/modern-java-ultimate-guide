import java.util.Scanner;

public class ShippingCost {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many books are in the cart? ");
        int books = Integer.parseInt(scanner.nextLine().strip());

        String shipping;
        if (books <= 0) {
            shipping = "Your cart is empty.";
        } else if (books >= 5) {
            shipping = "Shipping: FREE";
        } else if (books >= 3) {
            shipping = "Shipping: $2.99";
        } else {
            shipping = "Shipping: $4.99";
        }
        System.out.println(shipping);
    }
}
