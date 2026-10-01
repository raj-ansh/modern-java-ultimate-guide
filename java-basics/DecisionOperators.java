public class DecisionOperators {

    public static void main(String[] args) {
        int stock = 0;
        int ordersToday = 12;
        boolean isMember = true;
        double cartTotal = 42.50;

        boolean canShip = stock > 0;
        boolean freeDelivery = isMember || cartTotal >= 50.0;
        boolean restockSoon = ordersToday >= 10 && stock < 5;

        System.out.println("Can ship?      " + canShip);
        System.out.println("Free delivery? " + freeDelivery);
        System.out.println("Restock soon?  " + restockSoon);
        System.out.println("Out of stock?  " + !canShip);

        // && stops early: the division never runs when stock is 0
        boolean popular = stock > 0 && ordersToday / stock > 3;
        System.out.println("Popular?       " + popular);

        String label = stock > 0 ? "In stock" : "Sold out";
        System.out.println("Label:         " + label);
    }
}
