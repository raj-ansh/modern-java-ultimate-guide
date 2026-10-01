public class ReportDemo {

    public static void main(String[] args) {
        Report[] reports = {
                new SalesReport(
                        new int[] {12, 9, 15, 11, 22, 41, 30}),
                new StockReport(
                        new String[] {"Poems", "Atlas", "Zen"},
                        new int[] {5, 1, 2})
        };

        for (Report report : reports) {
            report.print();
        }
    }
}
