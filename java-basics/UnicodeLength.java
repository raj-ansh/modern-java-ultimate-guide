public class UnicodeLength {

    public static void main(String[] args) {
        String plain = "Java";
        String withEmoji = "Java 😀";

        System.out.println("plain length:          "
                + plain.length());
        System.out.println("withEmoji length:      "
                + withEmoji.length());
        System.out.println("withEmoji code points: "
                + withEmoji.codePointCount(0, withEmoji.length()));
    }
}
