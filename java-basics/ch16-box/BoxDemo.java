import java.math.BigDecimal;

public class BoxDemo {

    public static void main(String[] args) {
        Box<String> titleBox = new Box<>();
        titleBox.put("The Quiet Library");
        String title = titleBox.get();          // no cast needed
        System.out.println("Title: " + title.toUpperCase());

        Box<BigDecimal> priceBox = new Box<>();
        System.out.println("Price box empty? " + priceBox.isEmpty());
        priceBox.put(new BigDecimal("12.99"));
        BigDecimal doubled = priceBox.get().multiply(BigDecimal.TWO);
        System.out.println("Two copies: " + doubled);
    }
}
