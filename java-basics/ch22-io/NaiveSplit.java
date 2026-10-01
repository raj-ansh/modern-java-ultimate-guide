public class NaiveSplit {

    public static void main(String[] args) {
        // A quoted title that contains a comma
        String line =
                "book,\"Tea, Toast and Tales\",Meera Iyer,9.75,3";
        String[] fields = line.split(",");
        System.out.println(fields.length + " fields, expected 5:");
        for (String field : fields) {
            System.out.println("  [" + field + "]");
        }

        // An empty last field silently disappears...
        String noStock = "accessory,Bookmark set,,3.35,";
        System.out.println("split(\",\"):     "
                + noStock.split(",").length + " fields");
        // ...unless you pass a negative limit
        System.out.println("split(\",\", -1): "
                + noStock.split(",", -1).length + " fields");
    }
}
