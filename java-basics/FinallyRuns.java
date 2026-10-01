public class FinallyRuns {

    public static void main(String[] args) {
        System.out.println("Result: " + divide(10, 2));
        System.out.println("Result: " + divide(10, 0));
    }

    static String divide(int books, int shelves) {
        try {
            return books + " / " + shelves + " = " + books / shelves;
        } catch (ArithmeticException e) {
            return "cannot divide: " + e.getMessage();
        } finally {
            System.out.println("finally runs for "
                    + books + "/" + shelves);
        }
    }
}
