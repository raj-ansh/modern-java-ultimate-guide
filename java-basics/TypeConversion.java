public class TypeConversion {

    public static void main(String[] args) {
        int pages = 320;
        double pagesAsDouble = pages;      // widening: automatic
        System.out.println("Widened: " + pagesAsDouble);

        double price = 9.99;
        int truncated = (int) price;       // narrowing: needs a cast
        long rounded = Math.round(price);
        System.out.println("Cast:    " + truncated);
        System.out.println("Rounded: " + rounded);

        long bigNumber = 3_000_000_000L;
        int broken = (int) bigNumber;      // too big for an int
        System.out.println("Too big: " + broken);

        byte level = 10;
        level += 5;                        // compound: casts for you
        System.out.println("Level:   " + level);
    }
}
