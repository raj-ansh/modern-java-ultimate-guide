import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimesAndDurations {

    static final LocalTime CUTOFF = LocalTime.of(14, 0);    // 2 PM

    public static void main(String[] args) {
        LocalTime opens = LocalTime.of(9, 30);
        LocalTime closes = LocalTime.of(18, 0);
        Duration openFor = Duration.between(opens, closes);
        System.out.println("Open " + opens + " to " + closes + ": "
                + openFor + " = " + openFor.toMinutes() + " minutes");
        System.out.printf("That's %d h %d min%n",
                openFor.toHours(), openFor.toMinutesPart());

        LocalTime placed = LocalTime.of(16, 42, 7);
        System.out.println("Placed at " + placed + ", after cutoff? "
                + placed.isAfter(CUTOFF));
        System.out.println("Minute precision: "
                + placed.truncatedTo(ChronoUnit.MINUTES));

        LocalDateTime order =
                LocalDateTime.of(LocalDate.of(2026, 3, 6), placed);
        System.out.println("Order: " + order);
        System.out.println("48 hours later: "
                + order.plus(Duration.ofHours(48)));
        System.out.println("Date part: " + order.toLocalDate()
                + ", time part: " + order.toLocalTime());

        // Times wrap around midnight
        System.out.println("23:30 + 1 hour = "
                + LocalTime.of(23, 30).plusHours(1));
    }
}
