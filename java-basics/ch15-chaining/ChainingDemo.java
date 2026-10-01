import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChainingDemo {

    public static void main(String[] args) {
        try {
            loadCatalog("catalog-2026.csv");
        } catch (CatalogLoadException e) {
            System.out.println("Message: " + e.getMessage());
            System.out.println("Cause:   " + e.getCause());
        }
    }

    static String loadCatalog(String fileName)
            throws CatalogLoadException {
        try {
            return Files.readString(Path.of(fileName));
        } catch (IOException e) {
            throw new CatalogLoadException(
                    "Cannot load catalog from " + fileName, e);
        }
    }
}
