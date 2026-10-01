public class ArithmeticPuzzles {

    public static void main(String[] args) {
        System.out.println("MAX_VALUE + 1  = "
                + (Integer.MAX_VALUE + 1));
        System.out.println("0.1 + 0.2      = " + (0.1 + 0.2));
        System.out.println("5 / 2          = " + (5 / 2));
        System.out.println("5 / 2.0        = " + (5 / 2.0));
        System.out.println("-7 / 2         = " + (-7 / 2));
        System.out.println("-7 % 2         = " + (-7 % 2));
        System.out.println("floorMod(-7,2) = "
                + Math.floorMod(-7, 2));
        System.out.println("'a' + 1        = " + ('a' + 1));
        System.out.println("(char)('a'+1)  = " + (char) ('a' + 1));
        System.out.println("1 + 2 + \"3\"    = " + (1 + 2 + "3"));
        System.out.println("\"1\" + 2 + 3    = " + ("1" + 2 + 3));
        System.out.println("10 / 0.0       = " + (10 / 0.0));
        System.out.println("0.0 / 0.0      = " + (0.0 / 0.0));
        System.out.println("NaN == NaN     = "
                + (Double.NaN == Double.NaN));
        System.out.println("(int) 3.99     = " + (int) 3.99);
        System.out.println("round(-2.5)    = " + Math.round(-2.5));
        System.out.println("(byte) 200     = " + (byte) 200);
        try {
            System.out.println(10 / 0);
        } catch (ArithmeticException e) {
            System.out.println("10 / 0         -> " + e.getMessage());
        }
    }
}
