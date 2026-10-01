import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class LocalDates {

    public static void main(String[] args) {
        LocalDate launch = LocalDate.of(2026, Month.MARCH, 6);
        System.out.println("Launch: " + launch + ", a "
                + launch.getDayOfWeek());

        // Immutable: "changing" methods return a NEW date
        launch.plusDays(1);                        // result ignored!
        System.out.println("Still:  " + launch);
        LocalDate nextDay = launch.plusDays(1);
        System.out.println("Next:   " + nextDay);

        // Calendar arithmetic knows about month lengths
        LocalDate endOfJan = LocalDate.of(2026, 1, 31);
        System.out.println("Jan 31 + 1 month: "
                + endOfJan.plusMonths(1));
        System.out.println("2028 is a leap year? "
                + LocalDate.of(2028, 1, 1).isLeapYear());

        // Adjusters answer "which date is ..." questions
        System.out.println("Next Monday:   " + launch.with(
                TemporalAdjusters.next(DayOfWeek.MONDAY)));
        System.out.println("End of month:  "
                + launch.with(TemporalAdjusters.lastDayOfMonth()));

        // Comparing and measuring
        LocalDate sale = LocalDate.of(2026, 11, 27);
        System.out.println("Sale after launch? "
                + sale.isAfter(launch));
        System.out.println("Days until sale:   "
                + ChronoUnit.DAYS.between(launch, sale));
        Period gap = Period.between(launch, sale);
        System.out.println("As a period:       " + gap.getMonths()
                + " months, " + gap.getDays() + " days ("
                + gap + ")");
    }
}
