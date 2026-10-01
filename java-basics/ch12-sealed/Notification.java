public sealed interface Notification permits Email, Sms, Push {
}

record Email(String address, String subject) implements Notification {
}

record Sms(String phone, String text) implements Notification {
}

record Push(String deviceId) implements Notification {
}
