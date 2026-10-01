import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class LargeFiles {

    public static void main(String[] args) throws IOException {
        Path folder = Files.createDirectories(Path.of("demo-files"));
        Path log = folder.resolve("visits.log");

        // Write 200,000 lines through a buffer, one line at a time
        try (BufferedWriter out = Files.newBufferedWriter(log)) {
            for (int i = 1; i <= 200_000; i++) {
                String page =
                        i % 7 == 0 ? "/checkout" : "/books/" + i % 50;
                out.write("visit " + i + " " + page);
                out.newLine();
            }
        }

        // Read it back line by line: one line in memory at a time
        long checkouts = 0;
        try (BufferedReader in = Files.newBufferedReader(log)) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.endsWith("/checkout")) {
                    checkouts++;
                }
            }
        }
        System.out.println("Checkouts (reader): " + checkouts);

        // The same, as a lazy stream of lines
        try (Stream<String> lines = Files.lines(log)) {
            System.out.println("Checkouts (stream): " + lines
                    .filter(line -> line.endsWith("/checkout"))
                    .count());
        }
        try (Stream<String> lines = Files.lines(log)) {
            System.out.println("First /books/7 visit: " + lines
                    .filter(line -> line.endsWith("/books/7"))
                    .findFirst()
                    .orElse("none"));
        }
    }
}
