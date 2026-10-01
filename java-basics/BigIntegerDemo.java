import java.math.BigInteger;

public class BigIntegerDemo {

    public static void main(String[] args) {
        long asLong = 1;
        BigInteger exact = BigInteger.ONE;
        for (int n = 1; n <= 25; n++) {
            asLong = asLong * n;
            exact = exact.multiply(BigInteger.valueOf(n));
        }
        System.out.println("25! as long:       " + asLong);
        System.out.println("25! as BigInteger: " + exact);
    }
}
