public class NestedClasses {

    public static void main(String[] args) {
        Order order = new Order("BN-1042");
        Order.Line first = order.new Line("The Quiet Library", 2);
        Order.Line second = order.new Line("Bookmark set", 1);
        System.out.println(first.describe());
        System.out.println(second.describe());

        Order.Stats stats = new Order.Stats(3, "42.40");
        System.out.println(stats.summary());

        System.out.println(order.shortLabel());
    }
}

class Order {

    private final String id;

    Order(String id) {
        this.id = id;
    }

    /** Inner class: every Line belongs to one Order object. */
    class Line {
        private final String title;
        private final int quantity;

        Line(String title, int quantity) {
            this.title = title;
            this.quantity = quantity;
        }

        String describe() {
            return id + ": " + quantity + " x " + title;
        }
    }

    /** Static nested class: grouped here, independent of Order. */
    static class Stats {
        private final int items;
        private final String total;

        Stats(int items, String total) {
            this.items = items;
            this.total = total;
        }

        String summary() {
            return items + " items, total " + total;
        }
    }

    String shortLabel() {
        // Local class: only visible inside this method
        class Shortener {
            String shorten(String text) {
                return text.length() <= 4 ? text : text.substring(3);
            }
        }
        return "Label: " + new Shortener().shorten(id);
    }
}
