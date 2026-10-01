public class BookNestWelcome {

    public static void main(String[] args) {
        System.out.println("+------------------------------------+");
        System.out.println("|        Welcome to BookNest         |");
        System.out.println("|   Your neighbourhood online store  |");
        System.out.println("+------------------------------------+");

        if (args.length > 0) {
            System.out.println("Hello, " + args[0]
                    + "! Happy reading.");
        } else {
            System.out.println("Hello, Guest! Happy reading.");
        }

        System.out.println("Books in stock today: " + 128);
        System.out.println("Today's deal: 20% off all Java books");
    }
}
