import java.util.Arrays;

public class SplitPitfall {

    public static void main(String[] args) {
        String tags = "fiction|mystery|new";

        String[] wrong = tags.split("|");
        System.out.println("Wrong: " + Arrays.toString(wrong));

        String[] right = tags.split("\\|");
        System.out.println("Right: " + Arrays.toString(right));
    }
}
