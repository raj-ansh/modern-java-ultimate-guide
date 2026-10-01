public class GuardClauses {

    public static void main(String[] args) {
        System.out.println(membershipLevel(-5));
        System.out.println(membershipLevel(250));
        System.out.println(membershipLevel(1200));
        System.out.println(membershipLevel(5400));
    }

    static String membershipLevel(int points) {
        if (points < 0) {
            return "INVALID";
        }
        if (points >= 5000) {
            return "GOLD";
        }
        if (points >= 1000) {
            return "SILVER";
        }
        return "BRONZE";
    }
}
