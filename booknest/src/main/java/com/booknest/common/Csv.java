package com.booknest.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reads and writes single lines of comma-separated values (CSV).
 * A field that contains a comma or a double quote is written inside
 * double quotes, with each quote doubled: "Tea, Toast and Tales" and
 * "The ""Best"" Book". Fields may not contain line breaks.
 */
public final class Csv {

    private Csv() {
        // static helpers only
    }

    /**
     * Splits one line into its fields.
     *
     * @throws IllegalArgumentException if a quoted field isn't closed
     */
    public static List<String> parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (quoted && c == '"' && i + 1 < line.length()
                    && line.charAt(i + 1) == '"') {
                field.append('"');             // "" inside quotes
                i++;
            } else if (c == '"') {
                quoted = !quoted;
            } else if (c == ',' && !quoted) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(c);
            }
        }
        if (quoted) {
            throw new IllegalArgumentException(
                    "a quote isn't closed");
        }
        fields.add(field.toString());
        return fields;
    }

    /** Joins fields into one line, quoting those that need it. */
    public static String formatLine(String... fields) {
        return Stream.of(fields)
                .map(Csv::quoteIfNeeded)
                .collect(Collectors.joining(","));
    }

    private static String quoteIfNeeded(String field) {
        if (field.contains(",") || field.contains("\"")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}
