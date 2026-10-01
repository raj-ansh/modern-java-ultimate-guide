import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class WhenThingsGoWrong {

    public static void main(String[] args) throws IOException {
        Path folder = Files.createDirectories(Path.of("demo-files"));

        // 1. The file isn't there
        try {
            Files.readString(folder.resolve("missing.txt"));
        } catch (NoSuchFileException e) {
            System.out.println("No such file: "
                    + Path.of(e.getFile()).getFileName());
        }

        // 2. Creating a file that already exists
        Path once = folder.resolve("once.txt");
        Files.deleteIfExists(once);
        Files.createFile(once);
        try {
            Files.createFile(once);
        } catch (FileAlreadyExistsException e) {
            System.out.println("Already exists: "
                    + Path.of(e.getFile()).getFileName());
        }

        // 3. Text saved in another encoding: é as one Latin-1 byte
        Path latin = folder.resolve("latin1.txt");
        byte[] bytes = "Café".getBytes(StandardCharsets.ISO_8859_1);
        Files.write(latin, bytes);
        try {
            Files.readString(latin);              // expects UTF-8
        } catch (MalformedInputException e) {
            System.out.println("Not UTF-8: " + e.getMessage());
        }
        String text =
                Files.readString(latin, StandardCharsets.ISO_8859_1);
        System.out.println("Read as Latin-1: " + text.length()
                + " characters, correct? " + text.equals("Café"));

        // Every one of these is an IOException
        System.out.println(IOException.class.isAssignableFrom(
                MalformedInputException.class));
    }
}
