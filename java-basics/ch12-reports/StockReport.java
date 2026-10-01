public class StockReport extends Report {

    private final String[] titles;
    private final int[] stock;

    public StockReport(String[] titles, int[] stock) {
        super("Low stock");
        this.titles = titles.clone();
        this.stock = stock.clone();
    }

    @Override
    protected void printBody() {
        for (int i = 0; i < titles.length; i++) {
            if (stock[i] < 3) {
                System.out.println("Reorder: " + titles[i]
                        + " (" + stock[i] + " left)");
            }
        }
    }

    @Override
    protected String footer() {
        return "send to purchasing";
    }
}
