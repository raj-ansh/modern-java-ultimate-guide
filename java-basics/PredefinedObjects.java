import java.time.LocalDate;

public class PredefinedObjects {

    public static void main(String[] args) {
        LocalDate orderDate = LocalDate.of(2026, 9, 28);
        LocalDate deliveryDate = orderDate.plusDays(5);
        LocalDate promised = LocalDate.of(2026, 10, 2);

        System.out.println("Ordered:   " + orderDate);
        System.out.println("Delivery:  " + deliveryDate);
        System.out.println("Weekday:   "
                + deliveryDate.getDayOfWeek());
        System.out.println("Late?      "
                + deliveryDate.isAfter(promised));
        System.out.println("Order date is unchanged: " + orderDate);

        StringBuilder note = new StringBuilder("Gift");
        note.append(" wrap");
        System.out.println("The note object changed: " + note);
    }
}
