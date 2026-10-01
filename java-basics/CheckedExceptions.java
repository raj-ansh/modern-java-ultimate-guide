import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CheckedExceptions {

    public static void main(String[] args) {
        try {
            String catalog = readCatalog("missing-catalog.csv");
            System.out.println(catalog);
        } catch (IOException e) {
            System.out.println("Could not read the catalog: " + e);
        }
    }

    static String readCatalog(String fileName) throws IOException {
        return Files.readString(Path.of(fileName));
    }
}
