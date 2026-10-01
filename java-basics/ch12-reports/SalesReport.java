public class SalesReport extends Report {

    private final int[] dailySales;

    public SalesReport(int[] dailySales) {
        super("Weekly sales");
        this.dailySales = dailySales.clone();
    }

    @Override
    protected void printBody() {
        int total = 0;
        for (int sale : dailySales) {
            total += sale;
        }
        System.out.println("Books sold: " + total);
    }
}
