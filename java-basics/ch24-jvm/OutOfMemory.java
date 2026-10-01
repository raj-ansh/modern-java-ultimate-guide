import java.util.ArrayList;
import java.util.List;

public class OutOfMemory {

    public static void main(String[] args) {
        long maxMb = Runtime.getRuntime().maxMemory() / 1_048_576;
        System.out.println("Max heap is under 100 MB? "
                + (maxMb < 100));

        List<byte[]> blocks = new ArrayList<>();
        try {
            while (true) {
                blocks.add(new byte[1_048_576]);    // 1 MB each
            }
        } catch (OutOfMemoryError e) {
            int allocated = blocks.size();
            blocks.clear();                  // release it all first
            System.out.println("OutOfMemoryError: " + e.getMessage());
            System.out.println("Got fewer blocks than the maximum? "
                    + (allocated < maxMb));
        }
    }
}
