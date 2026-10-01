public class DefaultObjectMethods {

    public static void main(String[] args) {
        Customer priya = new Customer("priya@example.com", "Priya");
        Customer sameEmail =
                new Customer("priya@example.com", "Priya");

        String text = priya.toString();
        System.out.println("toString starts with 'Customer@'? "
                + text.startsWith("Customer@"));
        System.out.println("equals: " + priya.equals(sameEmail));
        System.out.println("Class:  " + priya.getClass().getName());
    }
}
