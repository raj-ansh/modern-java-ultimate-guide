public class CapabilitiesDemo {

    public static void main(String[] args) {
        PrintedBook novel =
                new PrintedBook("The Quiet Library", 0.45);
        GiftHamper hamper = new GiftHamper("Priya", 2.3);
        GiftVoucher voucher = new GiftVoucher("Arjun", 25);

        Shippable[] parcels = {novel, hamper};
        double totalKg = 0;
        for (Shippable parcel : parcels) {
            totalKg += parcel.weightKg();
        }
        System.out.println("Parcel weight: " + totalKg + " kg");

        Giftable[] gifts = {hamper, voucher};
        for (Giftable gift : gifts) {
            System.out.println(gift.giftMessage());
        }
    }
}

class PrintedBook implements Shippable {

    private final String title;
    private final double weightKg;

    PrintedBook(String title, double weightKg) {
        this.title = title;
        this.weightKg = weightKg;
    }

    @Override
    public double weightKg() {
        return weightKg;
    }
}

class GiftHamper implements Shippable, Giftable {

    private final String recipient;
    private final double weightKg;

    GiftHamper(String recipient, double weightKg) {
        this.recipient = recipient;
        this.weightKg = weightKg;
    }

    @Override
    public double weightKg() {
        return weightKg;
    }

    @Override
    public String giftMessage() {
        return "A hamper of books for " + recipient + "!";
    }
}

class GiftVoucher implements Giftable {

    private final String recipient;
    private final int amount;

    GiftVoucher(String recipient, int amount) {
        this.recipient = recipient;
        this.amount = amount;
    }

    @Override
    public String giftMessage() {
        return "A $" + amount + " voucher for " + recipient + "!";
    }
}
