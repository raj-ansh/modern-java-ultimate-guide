import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class ClockDemo {

    /** Code that needs "today" asks a Clock instead of the system. */
    static String greeting(Clock clock) {
        LocalDate today = LocalDate.now(clock);
        return switch (today.getDayOfWeek()) {
            case SATURDAY, SUNDAY -> "Happy weekend reading!";
            default -> "Welcome back!";
        };
    }

    public static void main(String[] args) {
        ZoneId chicago = ZoneId.of("America/Chicago");

        // In production: the real clock, in the shop's time zone
        Clock real = Clock.system(chicago);
        System.out.println("Real clock works: "
                + !greeting(real).isEmpty());

        // In tests and demos: a clock stopped at a chosen moment
        Clock friday = Clock.fixed(
                Instant.parse("2026-03-06T22:30:00Z"), chicago);
        Clock sunday = Clock.fixed(
                Instant.parse("2026-03-08T18:00:00Z"), chicago);
        System.out.println("Friday: " + greeting(friday));
        System.out.println("Sunday: " + greeting(sunday));
        System.out.println("Friday clock says: "
                + LocalDate.now(friday) + " "
                + LocalTime.now(friday));
    }
}
