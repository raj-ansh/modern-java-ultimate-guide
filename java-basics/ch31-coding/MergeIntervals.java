import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeIntervals {

    record Interval(int start, int end) {
        Interval {
            if (start > end) {
                throw new IllegalArgumentException(
                        start + " > " + end);
            }
        }

        @Override
        public String toString() {
            return "[" + start + "-" + end + "]";
        }
    }

    static List<Interval> merge(List<Interval> intervals) {
        List<Interval> sorted = new ArrayList<>(intervals);
        sorted.sort(Comparator.comparingInt(Interval::start));

        List<Interval> merged = new ArrayList<>();
        for (Interval next : sorted) {
            if (!merged.isEmpty()
                    && next.start() <= merged.getLast().end()) {
                Interval last = merged.removeLast();   // overlap
                merged.add(new Interval(last.start(),
                        Math.max(last.end(), next.end())));
            } else {
                merged.add(next);
            }
        }
        return merged;
    }

    public static void main(String[] args) {
        List<Interval> bookings = List.of(
                new Interval(9, 11), new Interval(14, 16),
                new Interval(10, 12), new Interval(15, 18),
                new Interval(18, 19), new Interval(20, 21));
        System.out.println("bookings: " + bookings);
        System.out.println("merged:   " + merge(bookings));
        System.out.println("empty:    " + merge(List.of()));
    }
}
