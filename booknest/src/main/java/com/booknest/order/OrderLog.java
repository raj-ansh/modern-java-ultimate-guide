package com.booknest.order;

import com.booknest.common.Csv;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * Keeps a permanent record of completed orders: one CSV line per
 * order, appended to a file that outlives the program. The file is
 * created, with a header line, when the first order is written.
 * Thread-safe: one append at a time, so lines never interleave.
 */
public final class OrderLog {

    private static final String HEADER =
            "id,placed_at,level,items,total";

    private final Path file;

    public OrderLog(Path file) {
        this.file = Objects.requireNonNull(file,
                "file must not be null");
    }

    /** Returns the file name, for messages. */
    public String fileName() {
        return file.getFileName().toString();
    }

    /** Adds one line for the order to the end of the file. */
    public synchronized void append(Order order) throws IOException {
        if (Files.notExists(file)) {
            Files.writeString(file, HEADER + "\n");
        }
        String line = Csv.formatLine(order.id(),
                order.placedAt().toString(), order.level().name(),
                String.valueOf(order.items()),
                order.total().toPlainString());
        Files.writeString(file, line + "\n",
                StandardOpenOption.APPEND);
    }
}
