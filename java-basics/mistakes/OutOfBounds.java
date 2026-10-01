public class OutOfBounds {

    public static void main(String[] args) {
        int[] dailySales = new int[7];
        dailySales[7] = 25;
        System.out.println("Never printed");
    }
}
