/**
 * The catalog could not be loaded. Wraps the low-level cause.
 */
public class CatalogLoadException extends Exception {

    public CatalogLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
