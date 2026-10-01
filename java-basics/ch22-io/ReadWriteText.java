import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

public class ReadWriteText {

    public static void main(String[] args) throws IOException {
        Path folder = Path.of("demo-files");
        Files.createDirectories(folder);      // fine if it exists
        Path notes = folder.resolve("notes.txt");

        // Write a whole string (creates or replaces the file)
        Files.writeString(notes, """
                Dune - loved it
                Emma - slow start
                Café reading list
                """);
        System.out.println("Exists? " + Files.exists(notes)
                + ", bytes: " + Files.size(notes));

        // Read it all at once: as one String, or as lines
        String all = Files.readString(notes);
        System.out.println("Characters: " + all.length());
        List<String> lines = Files.readAllLines(notes);
        System.out.println("Lines: " + lines.size()
                + ", first: " + lines.getFirst());

        // Append instead of replacing
        Files.writeString(notes, "Circe - next\n",
                StandardOpenOption.APPEND);

        // Stream the lines lazily; close the stream when done
        try (Stream<String> stream = Files.lines(notes)) {
            long finished = stream
                    .filter(line -> line.contains(" - "))
                    .filter(line -> !line.endsWith("next"))
                    .count();
            System.out.println("Books finished: " + finished);
        }
        System.out.println("Last line now: "
                + Files.readAllLines(notes).getLast());
    }
}
