public class CatchingBasics {

    public static void main(String[] args) {
        String[] inputs = {"3", "three", "", "12"};

        for (String input : inputs) {
            try {
                int quantity = Integer.parseInt(input);
                System.out.println("[" + input + "] -> quantity "
                        + quantity);
            } catch (NumberFormatException e) {
                System.out.println("[" + input + "] -> not a number: "
                        + e.getMessage());
            }
        }
        System.out.println("The program keeps running.");
    }
}
