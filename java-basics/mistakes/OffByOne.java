public class OffByOne {

    public static void main(String[] args) {
        int copies = 5;
        for (int copy = 1; copy < copies; copy++) {
            System.out.println("Printing label " + copy);
        }
    }
}
