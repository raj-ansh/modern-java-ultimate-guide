import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Formatting {

    public static void main(String[] args) {
        LocalDateTime order = LocalDateTime.of(2026, 3, 6, 16, 30);

        // toString is ISO 8601: ideal for files, logs and APIs
        System.out.println("ISO:     " + order);

        DateTimeFormatter receipt = DateTimeFormatter.ofPattern(
                "EEE d MMM uuuu, h:mm a", Locale.US);
        System.out.println("Receipt: " + order.format(receipt));

        DateTimeFormatter longDate =
                DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
        LocalDate day = order.toLocalDate();
        System.out.println("US:      " + day.format(
                longDate.withLocale(Locale.US)));
        System.out.println("UK:      " + day.format(
                longDate.withLocale(Locale.UK)));
        System.out.println("France:  " + day.format(
                longDate.withLocale(Locale.FRANCE)));

        // Parsing: the pattern must match the text exactly
        DateTimeFormatter dayFirst = DateTimeFormatter.ofPattern(
                "dd/MM/uuuu");
        System.out.println("Parsed:  " + LocalDate.parse("06/03/2026",
                dayFirst));
        try {
            LocalDate.parse("2026/03/06", dayFirst);
        } catch (DateTimeParseException e) {
            System.out.println("Bad input: " + e.getMessage());
        }

        // The classic bug: YYYY is the WEEK-based year
        LocalDate newYearsEve = LocalDate.of(2026, 12, 31);
        DateTimeFormatter right =
                DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.US);
        DateTimeFormatter wrong =
                DateTimeFormatter.ofPattern("YYYY-MM-dd", Locale.US);
        System.out.println("uuuu: " + newYearsEve.format(right));
        System.out.println("YYYY: " + newYearsEve.format(wrong));
    }
}
