import module java.base;

public class ModuleImport {

    public static void main(String[] args) {
        BigDecimal price = new BigDecimal("12.99");
        LocalDate released = LocalDate.of(2026, 3, 17);
        List<String> genres = List.of("Mystery", "Fiction");

        System.out.println(price + " | " + released + " | " + genres);
    }
}
