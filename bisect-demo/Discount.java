public class Discount {

    static int percentFor(String level) {
        return switch (level) {
            case "GOLD" -> 15;
            case "SILVER" -> 10;
            default -> 0;
        };
    }
}
