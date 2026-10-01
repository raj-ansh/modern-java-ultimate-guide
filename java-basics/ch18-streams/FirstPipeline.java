import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirstPipeline {

    public static void main(String[] args) {
        // With a loop: say HOW to do it, step by step
        List<String> loopTitles = new ArrayList<>();
        for (Book book : Book.SAMPLE) {
            if (book.genre().equals("sci-fi")) {
                loopTitles.add(book.title().toUpperCase());
            }
        }
        Collections.sort(loopTitles);
        System.out.println("Loop:   " + loopTitles);

        // With a stream: say WHAT you want
        List<String> streamTitles = Book.SAMPLE.stream()
                .filter(book -> book.genre().equals("sci-fi"))
                .map(book -> book.title().toUpperCase())
                .sorted()
                .toList();
        System.out.println("Stream: " + streamTitles);
    }
}
