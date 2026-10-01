package com.booknest.catalog;

import com.booknest.common.Csv;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Loads products and their stock levels from a CSV file:
 * <pre>
 * kind,title,author,price,stock
 * book,The Quiet Library,Asha Rao,12.99,6
 * accessory,Bookmark set,,3.35,20
 * </pre>
 * A line with a mistake is skipped and reported, so one typo doesn't
 * stop the shop from opening.
 */
public final class CatalogFile {

    private static final List<String> HEADER =
            List.of("kind", "title", "author", "price", "stock");

    /** One product and how many are in stock. */
    public record Entry(Product product, int stock) {}

    /** The products that loaded, and a message per skipped line. */
    public record Result(List<Entry> entries, List<String> problems) {

        public Result {
            entries = List.copyOf(entries);
            problems = List.copyOf(problems);
        }

        public Catalog catalog() {
            return new Catalog(entries.stream()
                    .map(Entry::product)
                    .toArray(Product[]::new));
        }

        public int[] stockLevels() {
            return entries.stream().mapToInt(Entry::stock).toArray();
        }
    }

    private CatalogFile() {
        // static loading only
    }

    /**
     * Reads the file.
     *
     * @throws IOException if the file can't be read, or doesn't start
     *         with the expected header line
     */
    public static Result load(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file);
        if (lines.isEmpty()
                || !Csv.parseLine(lines.getFirst()).equals(HEADER)) {
            throw new IOException(file.getFileName()
                    + " must start with " + String.join(",", HEADER));
        }
        List<Entry> entries = new ArrayList<>();
        List<String> problems = new ArrayList<>();
        Set<Product> seen = new HashSet<>();
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).isBlank()) {
                continue;
            }
            int lineNumber = i + 1;             // people count from 1
            try {
                Entry entry = parse(Csv.parseLine(lines.get(i)));
                if (!seen.add(entry.product())) {
                    throw new IllegalArgumentException(
                            "duplicate product: "
                                    + entry.product().menuLabel());
                }
                entries.add(entry);
            } catch (IllegalArgumentException e) {
                problems.add("line " + lineNumber + ": "
                        + e.getMessage());
            }
        }
        return new Result(entries, problems);
    }

    private static Entry parse(List<String> fields) {
        if (fields.size() != HEADER.size()) {
            throw new IllegalArgumentException("expected "
                    + HEADER.size() + " fields, found "
                    + fields.size());
        }
        String kind = fields.get(0).strip();
        String title = fields.get(1);
        String author = fields.get(2);
        String price = fields.get(3).strip();
        if (!price.matches("\\d+\\.\\d{2}")) {
            throw new IllegalArgumentException(
                    "price is not an amount: " + price);
        }
        Product product = switch (kind) {
            case "book" -> new Book(title, author, price);
            case "ebook" -> new EBook(title, author, price);
            case "accessory" -> new Accessory(title, price);
            default -> throw new IllegalArgumentException(
                    "unknown kind: " + kind);
        };
        return new Entry(product, parseStock(fields.get(4).strip()));
    }

    private static int parseStock(String text) {
        try {
            int stock = Integer.parseInt(text);
            if (stock >= 0) {
                return stock;
            }
        } catch (NumberFormatException _) {
            // not a number: fall through to the message below
        }
        throw new IllegalArgumentException(
                "stock must be a whole number, 0 or more: " + text);
    }
}
