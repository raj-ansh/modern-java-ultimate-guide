public class DefaultResolved {

    public static void main(String[] args) {
        System.out.println(new Hamper().label());
    }
}

interface Fragile {
    default String label() {
        return "Handle with care";
    }
}

interface Perishable {
    default String label() {
        return "Deliver quickly";
    }
}

class Hamper implements Fragile, Perishable {

    @Override
    public String label() {
        return Fragile.super.label() + ". " + Perishable.super.label()
                + ".";
    }
}
