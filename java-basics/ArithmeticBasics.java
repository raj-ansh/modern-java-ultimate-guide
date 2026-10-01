public class ArithmeticBasics {

    public static void main(String[] args) {
        int books = 17;
        int perShelf = 5;

        System.out.println("Full shelves:    " + books / perShelf);
        System.out.println("Books left over: " + books % perShelf);
        System.out.println("Exact ratio:     "
                + (double) books / perShelf);

        System.out.println("-17 / 5  = " + (-17 / 5));
        System.out.println("-17 % 5  = " + (-17 % 5));
        System.out.println("floorMod = " + Math.floorMod(-17, 5));

        System.out.println("1.0 / 0 = " + (1.0 / 0));
        System.out.println("0.0 / 0 = " + (0.0 / 0));
    }
}
