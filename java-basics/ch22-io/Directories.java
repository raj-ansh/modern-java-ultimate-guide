import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;

public class Directories {

    static String show(Path path) {
        return path.toString()
                .replace(path.getFileSystem().getSeparator(), "/");
    }

    public static void main(String[] args) throws IOException {
        Path shop = Path.of("demo-shop");
        deleteTree(shop);                           // start fresh

        Path year = Files.createDirectories(
                shop.resolve("reports").resolve("2026"));
        Files.writeString(shop.resolve("catalog.csv"), "kind,title");
        Files.writeString(year.resolve("march.txt"), "ok");
        Files.writeString(year.resolve("april.txt"), "ok");

        // Copy and move
        Files.copy(shop.resolve("catalog.csv"),
                shop.resolve("catalog-backup.csv"),
                StandardCopyOption.REPLACE_EXISTING);
        Files.move(year.resolve("april.txt"),
                shop.resolve("reports").resolve("april-draft.txt"));

        // List one folder, or walk the whole tree (sorted: the
        // operating system may return entries in any order)
        try (Stream<Path> entries = Files.list(shop)) {
            System.out.println("In demo-shop: " + entries
                    .map(p -> p.getFileName().toString())
                    .sorted()
                    .toList());
        }
        try (Stream<Path> tree = Files.walk(shop)) {
            tree.sorted().forEach(p -> System.out.println(
                    (Files.isDirectory(p) ? "[dir] " : "      ")
                            + show(p)));
        }

        // Find files by name anywhere below a folder
        try (Stream<Path> found = Files.find(shop, 10,
                (path, attributes) -> attributes.isRegularFile()
                        && path.toString().endsWith(".txt"))) {
            System.out.println(".txt files: " + found.count());
        }

        deleteTree(shop);
        System.out.println("Deleted? " + Files.notExists(shop));
    }

    /** Deletes a folder and everything in it: deepest paths first. */
    static void deleteTree(Path root) throws IOException {
        if (Files.notExists(root)) {
            return;
        }
        try (Stream<Path> tree = Files.walk(root)) {
            for (Path path : tree.sorted(Comparator.reverseOrder())
                    .toList()) {
                Files.delete(path);
            }
        }
    }
}
