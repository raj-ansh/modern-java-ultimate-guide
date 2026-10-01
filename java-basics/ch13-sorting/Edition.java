public record Edition(String title, int year)
        implements Comparable<Edition> {

    @Override
    public int compareTo(Edition other) {
        int byYear = Integer.compare(year, other.year);
        if (byYear != 0) {
            return byYear;
        }
        return title.compareTo(other.title);
    }
}
