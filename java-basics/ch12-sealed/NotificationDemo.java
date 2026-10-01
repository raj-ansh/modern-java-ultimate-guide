public class NotificationDemo {

    public static void main(String[] args) {
        Notification[] outbox = {
                new Email("priya@example.com", "Your order shipped"),
                new Sms("+1-555-0100", "Order BN-1042 shipped"),
                new Sms("+1-555-0101", "x".repeat(200)),
                new Push("device-7")
        };

        for (Notification notification : outbox) {
            System.out.println(describe(notification));
        }
    }

    static String describe(Notification notification) {
        return switch (notification) {
            case Email(String address, String subject) ->
                    "Email to " + address + ": " + subject;
            case Sms(String phone, String text)
                    when text.length() > 160 ->
                    "SMS to " + phone + " (too long, will be split)";
            case Sms(String phone, String text) ->
                    "SMS to " + phone + ": " + text;
            case Push push ->
                    "Push to " + push.deviceId();
        };
    }
}
