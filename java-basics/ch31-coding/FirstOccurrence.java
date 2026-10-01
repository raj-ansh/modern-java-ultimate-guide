public class FirstOccurrence {

    /** Index of the first target in a sorted array, or -1. */
    static int firstIndexOf(int[] sorted, int target) {
        int low = 0;
        int high = sorted.length - 1;
        int found = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;   // can't overflow
            if (sorted[mid] < target) {
                low = mid + 1;
            } else if (sorted[mid] > target) {
                high = mid - 1;
            } else {
                found = mid;                    // keep looking left
                high = mid - 1;
            }
        }
        return found;
    }

    public static void main(String[] args) {
        int[] prices = {1, 3, 3, 3, 5, 8};
        for (int target : new int[] {3, 1, 8, 4, 0, 9}) {
            System.out.println("first " + target + " at "
                    + firstIndexOf(prices, target));
        }
        System.out.println("empty array: "
                + firstIndexOf(new int[0], 3));
    }
}
