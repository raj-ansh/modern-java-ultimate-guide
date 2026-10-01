public class SplitAndJoin {

    public static void main(String[] args) {
        String csvLine = "BN-001,The Quiet Library,Asha Rao,12.99";

        String[] fields = csvLine.split(",");
        System.out.println("Fields: " + fields.length);
        System.out.println("Code:   " + fields[0]);
        System.out.println("Title:  " + fields[1]);
        System.out.println("Author: " + fields[2]);
        System.out.println("Price:  " + fields[3]);

        String tags = String.join(" | ", "fiction", "mystery", "new");
        System.out.println("Tags:   " + tags);

        System.out.println("=".repeat(30));
    }
}
