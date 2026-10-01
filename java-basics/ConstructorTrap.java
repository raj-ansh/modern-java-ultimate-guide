public class ConstructorTrap {

    public static void main(String[] args) {
        new LegacyEBook("EPUB");
        new ModernEBook("EPUB");
    }
}

class Download {

    Download() {
        System.out.println("Created: " + describe());
    }

    String describe() {
        return "a download";
    }
}

class LegacyEBook extends Download {

    private final String format;

    LegacyEBook(String format) {
        super();
        this.format = format;
    }

    @Override
    String describe() {
        return "an e-book in " + format;
    }
}

class ModernEBook extends Download {

    private final String format;

    ModernEBook(String format) {
        this.format = format;
        super();
    }

    @Override
    String describe() {
        return "an e-book in " + format;
    }
}
