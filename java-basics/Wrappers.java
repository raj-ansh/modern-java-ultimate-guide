public class Wrappers {

    public static void main(String[] args) {
        Integer stock = 42;           // autoboxing: int -> Integer
        int copies = stock + 8;       // unboxing: Integer -> int
        System.out.println("copies = " + copies);

        Integer small1 = 127;
        Integer small2 = 127;
        Integer big1 = 128;
        Integer big2 = 128;
        System.out.println("127 == 127:      " + (small1 == small2));
        System.out.println("128 == 128:      " + (big1 == big2));
        System.out.println("128 equals 128:  " + big1.equals(big2));

        System.out.println("Integer.MAX_VALUE: " + Integer.MAX_VALUE);
        System.out.println("parseInt: " + Integer.parseInt("15"));
        System.out.println("compare:  " + Integer.compare(3, 9));

        Integer missing = null;
        int total = missing + 1;
        System.out.println("Never printed: " + total);
    }
}
