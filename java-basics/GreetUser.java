public class GreetUser {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java GreetUser.java <name>");
            return;
        }
        System.out.println("Hello, " + args[0]
                + "! Welcome to Java.");
    }
}
