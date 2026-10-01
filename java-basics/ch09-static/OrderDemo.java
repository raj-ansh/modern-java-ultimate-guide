public class OrderDemo {

    public static void main(String[] args) {
        print(Order.forCustomer("Priya"));
        print(Order.forCustomer("Arjun"));
        print(Order.forCustomer("Meera"));
        System.out.println("Orders created: "
                + Order.ordersCreated());
    }

    static void print(Order order) {
        System.out.println(order.getId() + " for "
                + order.getCustomer());
    }
}
