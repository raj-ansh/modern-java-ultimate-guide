import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DeliveryDemo {

    public static void main(String[] args) {
        LocalDate ordered = LocalDate.of(2026, 9, 28);
        DeliveryWindow window =
                DeliveryWindow.startingOn(ordered, 3, 5);

        System.out.println(window);
        System.out.println("Length in days: " + window.days());
        System.out.println("Oct 2 inside?  "
                + window.contains(LocalDate.of(2026, 10, 2)));
        System.out.println("Oct 9 inside?  "
                + window.contains(LocalDate.of(2026, 10, 9)));
    }
}

record DeliveryWindow(LocalDate earliest, LocalDate latest) {

    DeliveryWindow {
        if (latest.isBefore(earliest)) {
            throw new IllegalArgumentException("latest " + latest
                    + " is before earliest " + earliest);
        }
    }

    static DeliveryWindow startingOn(LocalDate orderDate,
                                     int minDays, int maxDays) {
        return new DeliveryWindow(orderDate.plusDays(minDays),
                orderDate.plusDays(maxDays));
    }

    long days() {
        return ChronoUnit.DAYS.between(earliest, latest) + 1;
    }

    boolean contains(LocalDate date) {
        return !date.isBefore(earliest) && !date.isAfter(latest);
    }
}
