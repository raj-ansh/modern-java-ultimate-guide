public class SwitchExpressions {

    public static void main(String[] args) {
        String level = "SILVER";
        String day = "SUN";

        int discount = switch (level) {
            case "GOLD" -> 15;
            case "SILVER" -> 10;
            case "BRONZE" -> 5;
            default -> 0;
        };
        System.out.println(level + " discount: " + discount + "%");

        String dayType = switch (day) {
            case "SAT", "SUN" -> "Weekend";
            case "MON", "TUE", "WED", "THU", "FRI" -> "Weekday";
            default -> "Unknown day";
        };
        System.out.println(day + " is a " + dayType);

        String banner = switch (level) {
            case "GOLD" -> "Free delivery and 15% off!";
            case "SILVER", "BRONZE" -> {
                String saving = "Members save " + discount + "%";
                yield saving + " on every order.";
            }
            default -> "Join BookNest and start saving!";
        };
        System.out.println(banner);
    }
}
