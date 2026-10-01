public class ClassicSwitchBug {

    public static void main(String[] args) {
        String level = "SILVER";
        int discount;

        switch (level) {
            case "GOLD":
                discount = 15;
            case "SILVER":
                discount = 10;
            case "BRONZE":
                discount = 5;
                break;
            default:
                discount = 0;
        }

        System.out.println(level + " discount: " + discount + "%");
    }
}
