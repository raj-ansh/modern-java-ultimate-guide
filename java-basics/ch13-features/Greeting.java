public interface Greeting {

    /** Abstract: every implementation must provide a name. */
    String customerName();

    /** Default: shared behaviour; implementations may override. */
    default String welcome() {
        return decorate("Welcome back, " + customerName() + "!");
    }

    /** Static: a helper that belongs to the interface itself. */
    static Greeting forGuest() {
        return new GuestGreeting();
    }

    /** Private: an internal helper for the default methods. */
    private String decorate(String text) {
        return "*** " + text + " ***";
    }
}

class GuestGreeting implements Greeting {

    @Override
    public String customerName() {
        return "guest";
    }

    @Override
    public String welcome() {
        return "Welcome to BookNest! Sign up to earn points.";
    }
}

class MemberGreeting implements Greeting {

    private final String name;

    MemberGreeting(String name) {
        this.name = name;
    }

    @Override
    public String customerName() {
        return name;
    }
}
