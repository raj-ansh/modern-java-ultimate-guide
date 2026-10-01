public class WithoutGenerics {

    public static void main(String[] args) {
        ObjectBox box = new ObjectBox();
        box.put("The Quiet Library");       // a String goes in...

        Integer pages = (Integer) box.get(); // ...an Integer out?
        System.out.println("Pages: " + pages);
    }
}

/** A box that holds anything, the way Java worked before generics. */
class ObjectBox {

    private Object content;

    void put(Object content) {
        this.content = content;
    }

    Object get() {
        return content;
    }
}
