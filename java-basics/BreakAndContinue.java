public class BreakAndContinue {

    public static void main(String[] args) {
        final int dailyLimit = 5;
        int processed = 0;

        for (int orderId = 1001; orderId <= 1012; orderId++) {
            if (orderId % 4 == 0) {
                System.out.println(orderId + ": cancelled, skipped");
                continue;
            }

            System.out.println(orderId + ": processed");
            processed++;

            if (processed == dailyLimit) {
                System.out.println("Daily limit reached. Stopping.");
                break;
            }
        }

        System.out.println("Orders processed today: " + processed);
    }
}
