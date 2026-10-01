public class StringEquality {

    public static void main(String[] args) {
        String a = "GOLD";
        String b = "GOLD";
        String c = new String("GOLD");
        String d = "gold";

        System.out.println("a == b:        " + (a == b));
        System.out.println("a == c:        " + (a == c));
        System.out.println("a.equals(c):   " + a.equals(c));
        System.out.println("a.equals(d):   " + a.equals(d));
        System.out.println("ignoring case: " + a.equalsIgnoreCase(d));

        System.out.println("apple vs banana: "
                + "apple".compareTo("banana"));
        System.out.println("pear vs apple:   "
                + "pear".compareTo("apple"));
        System.out.println("same:            "
                + "kiwi".compareTo("kiwi"));
    }
}
