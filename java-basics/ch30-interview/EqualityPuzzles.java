public class EqualityPuzzles {

    public static void main(String[] args) {
        Integer a = 127, b = 127;
        Integer c = 128, d = 128;
        System.out.println("127 == 127:      " + (a == b));
        System.out.println("128 == 128:      " + (c == d));
        System.out.println("128 equals 128:  " + c.equals(d));

        String s1 = "Java";
        String s2 = "Java";
        String s3 = new String("Java");
        String part = "Ja";
        System.out.println("literal == literal:  " + (s1 == s2));
        System.out.println("literal == new:      " + (s1 == s3));
        System.out.println("literal == intern(): "
                + (s1 == s3.intern()));
        System.out.println("constant concat:     "
                + ("Ja" + "va" == s1));
        System.out.println("runtime concat:      "
                + (part + "va" == s1));
        System.out.println("equals:              " + s1.equals(s3));

        Integer missing = null;
        boolean useIt = true;
        try {
            int value = useIt ? missing : 0;
            System.out.println("value = " + value);
        } catch (NullPointerException e) {
            System.out.println("Ternary unboxing:    "
                    + e.getClass().getSimpleName());
        }
    }
}
