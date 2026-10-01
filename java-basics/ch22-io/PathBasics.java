import java.nio.file.Path;

public class PathBasics {

    /** Shows a path with "/" on every operating system. */
    static String show(Path path) {
        return path.toString()
                .replace(path.getFileSystem().getSeparator(), "/");
    }

    public static void main(String[] args) {
        Path catalog = Path.of("data", "catalog.csv");
        System.out.println("Path:        " + show(catalog));
        System.out.println("File name:   " + catalog.getFileName());
        System.out.println("Parent:      "
                + show(catalog.getParent()));
        System.out.println("Name count:  " + catalog.getNameCount());
        System.out.println("Absolute?    " + catalog.isAbsolute());
        System.out.println("Ends .csv?   "
                + catalog.getFileName().toString().endsWith(".csv"));

        // Building paths from other paths
        Path data = Path.of("data");
        Path orders = data.resolve("orders.csv");
        System.out.println("Resolved:    " + show(orders));
        System.out.println("Sibling:     "
                + show(orders.resolveSibling("orders-old.csv")));

        // Cleaning up and comparing
        Path messy =
                Path.of("data", "..", "data", ".", "catalog.csv");
        System.out.println("Messy:       " + show(messy));
        System.out.println("Normalized:  " + show(messy.normalize()));
        System.out.println("Same file?   "
                + messy.normalize().equals(catalog));
        Path reports = Path.of("reports", "2026", "march.txt");
        System.out.println("From data to report: "
                + show(data.relativize(reports)));
    }
}
