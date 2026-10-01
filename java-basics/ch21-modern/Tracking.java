import java.time.LocalDate;
import java.util.List;

public class Tracking {

    // ---- The data: records, closed by a sealed interface ----------

    record Carrier(String name, String trackingPrefix) {}

    sealed interface Status {}
    record Ordered(LocalDate on) implements Status {}
    record Dispatched(LocalDate on, Carrier carrier, String number)
            implements Status {}
    record Delivered(LocalDate on, String signedBy)
            implements Status {}
    record Returned(LocalDate on, String reason) implements Status {}

    // ---- The operations: functions over the data ------------------

    static String describe(Status status) {
        return switch (status) {
            case Ordered(var on) -> "Ordered on " + on;
            case Dispatched(var on, Carrier(var name, var prefix),
                            var number) ->
                    "Sent " + on + " with " + name + ", tracking "
                            + prefix + number;
            case Delivered(var on, var signedBy)
                    when signedBy.isBlank() ->
                    "Left at the door on " + on;
            case Delivered(var on, var signedBy) ->
                    "Delivered " + on + ", signed by " + signedBy;
            case Returned(_, var reason) -> "Returned: " + reason;
        };
    }

    /** Only dispatched parcels can be tracked with a carrier. */
    static boolean trackable(Status status) {
        return status instanceof Dispatched(_, Carrier _, var number)
                && !number.isBlank();
    }

    public static void main(String[] args) {
        Carrier bookPost = new Carrier("BookPost", "BP-");
        List<Status> history = List.of(
                new Ordered(LocalDate.of(2026, 3, 6)),
                new Dispatched(LocalDate.of(2026, 3, 9), bookPost,
                        "88213"),
                new Delivered(LocalDate.of(2026, 3, 12), ""),
                new Delivered(LocalDate.of(2026, 3, 12), "A. Rao"),
                new Returned(LocalDate.of(2026, 3, 20), "damaged"));

        for (Status status : history) {
            System.out.printf("%-5s %s%n",
                    trackable(status) ? "[T]" : "", describe(status));
        }
    }
}
