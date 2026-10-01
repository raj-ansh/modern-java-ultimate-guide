public class WeeklySales {

    public static void main(String[] args) {
        String[] days =
                {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        int[] sales = {12, 9, 15, 11, 22, 41, 30};

        int total = 0;
        for (int sale : sales) {
            total += sale;
        }

        int bestDay = 0;
        for (int i = 1; i < sales.length; i++) {
            if (sales[i] > sales[bestDay]) {
                bestDay = i;
            }
        }

        double average = (double) total / sales.length;
        System.out.println("Total books sold: " + total);
        System.out.printf("Daily average:    %.1f%n", average);
        System.out.println("Best day:         " + days[bestDay]
                + " (" + sales[bestDay] + ")");

        System.out.println("Busy days:");
        for (int i = 0; i < sales.length; i++) {
            if (sales[i] > average) {
                System.out.println("  " + days[i] + " "
                        + "#".repeat(sales[i]));
            }
        }
    }
}
