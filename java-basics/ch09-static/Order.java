public class Order {

    private static final String PREFIX = "BN-";
    private static int nextNumber = 1001;

    private final String id;
    private final String customer;

    private Order(String id, String customer) {
        this.id = id;
        this.customer = customer;
    }

    public static Order forCustomer(String customer) {
        String id = PREFIX + nextNumber;
        nextNumber++;
        return new Order(id, customer);
    }

    public static int ordersCreated() {
        return nextNumber - 1001;
    }

    public String getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }
}
