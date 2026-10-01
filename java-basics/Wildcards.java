import java.util.ArrayList;
import java.util.List;

public class Wildcards {

    /** Reads numbers of any kind: a producer, so "? extends". */
    static double total(List<? extends Number> numbers) {
        double sum = 0;
        for (Number number : numbers) {
            sum += number.doubleValue();
        }
        return sum;
    }

    /** Writes Integers into a list: a consumer, so "? super". */
    static void addStockLevels(List<? super Integer> target) {
        target.add(6);
        target.add(4);
        target.add(20);
    }

    public static void main(String[] args) {
        List<Integer> copies = List.of(3, 5, 2);
        List<Double> weights = List.of(0.45, 2.3);
        System.out.println("Total copies: " + total(copies));
        System.out.println("Total weight: " + total(weights));

        List<Integer> integers = new ArrayList<>();
        List<Number> numbers = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        addStockLevels(integers);
        addStockLevels(numbers);
        addStockLevels(objects);
        System.out.println(integers + " " + numbers + " " + objects);
    }
}
