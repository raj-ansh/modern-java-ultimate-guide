public class BuildingText {

    public static void main(String[] args) {
        StringBuilder report = new StringBuilder();
        report.append("Top sellers: ");

        for (int rank = 1; rank <= 5; rank++) {
            report.append('#').append(rank);
            if (rank < 5) {
                report.append(", ");
            }
        }
        System.out.println(report);

        StringBuilder isbn = new StringBuilder("9781000000011");
        isbn.insert(3, '-');
        System.out.println("With dash: " + isbn);
        System.out.println("Reversed:  " + isbn.reverse());
        System.out.println("Length:    " + isbn.length());
    }
}
