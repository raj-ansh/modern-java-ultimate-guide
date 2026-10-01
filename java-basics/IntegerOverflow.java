public class IntegerOverflow {

    public static void main(String[] args) {
        int views = Integer.MAX_VALUE;
        System.out.println("Max int:       " + views);

        views = views + 1;
        System.out.println("One more view: " + views);

        long bigViews = Integer.MAX_VALUE;
        bigViews = bigViews + 1;
        System.out.println("As a long:     " + bigViews);

        int safeViews = Math.addExact(Integer.MAX_VALUE, 1);
        System.out.println("Never printed: " + safeViews);
    }
}
