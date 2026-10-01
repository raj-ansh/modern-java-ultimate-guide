public abstract class Report {

    private final String title;

    protected Report(String title) {
        this.title = title;
    }

    public final void print() {
        System.out.println("=== " + title + " ===");
        printBody();
        System.out.println("--- " + footer() + " ---");
    }

    protected abstract void printBody();

    protected String footer() {
        return "end of report";
    }
}
