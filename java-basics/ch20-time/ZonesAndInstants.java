import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZonesAndInstants {

    public static void main(String[] args) {
        ZoneId chicago = ZoneId.of("America/Chicago");
        ZonedDateTime placed = ZonedDateTime.of(
                LocalDateTime.of(2026, 3, 6, 16, 30), chicago);
        System.out.println("Placed:    " + placed);

        // The same moment, seen from other places
        System.out.println("In London: " + placed.withZoneSameInstant(
                ZoneId.of("Europe/London")));
        System.out.println("In Mumbai: " + placed.withZoneSameInstant(
                ZoneId.of("Asia/Kolkata")));

        // An Instant is a point on the UTC timeline, with no zone
        Instant instant = placed.toInstant();
        System.out.println("Instant:   " + instant);
        System.out.println("Epoch seconds: "
                + instant.getEpochSecond());

        // Clocks in Chicago jump from 2:00 to 3:00 AM on 8 March 2026
        ZonedDateTime saturdayNoon = ZonedDateTime.of(
                LocalDateTime.of(2026, 3, 7, 12, 0), chicago);
        System.out.println("Sat noon + 1 day:    "
                + saturdayNoon.plusDays(1));
        System.out.println("Sat noon + 24 hours: "
                + saturdayNoon.plus(Duration.ofHours(24)));

        // 2:30 AM that Sunday never happened in Chicago
        ZonedDateTime gap = ZonedDateTime.of(
                LocalDateTime.of(2026, 3, 8, 2, 30), chicago);
        System.out.println("2:30 AM became:      " + gap);
    }
}
