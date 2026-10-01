public class SafeShelf {

    private final String[] titles;

    public SafeShelf(String[] titles) {
        this.titles = titles.clone();
    }

    public String[] getTitles() {
        return titles.clone();
    }
}
