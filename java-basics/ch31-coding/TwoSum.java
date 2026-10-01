import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoSum {

    /** Returns the indexes of two numbers that add up to target. */
    static int[] twoSum(int[] numbers, int target) {
        Map<Integer, Integer> seen = new HashMap<>(); // value->index
        for (int i = 0; i < numbers.length; i++) {
            Integer partner = seen.get(target - numbers[i]);
            if (partner != null) {
                return new int[] {partner, i};
            }
            seen.put(numbers[i], i);
        }
        return new int[0];                              // no pair
    }

    static void check(int[] numbers, int target) {
        System.out.println(Arrays.toString(numbers) + ", target "
                + target + " -> "
                + Arrays.toString(twoSum(numbers, target)));
    }

    public static void main(String[] args) {
        check(new int[] {2, 7, 11, 15}, 9);
        check(new int[] {3, 2, 4}, 6);
        check(new int[] {3, 3}, 6);
        check(new int[] {1, 2}, 10);
        check(new int[] {}, 0);
    }
}
