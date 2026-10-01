public class FinallyPuzzles {

    @SuppressWarnings("finally")
    static int overriding() {
        try {
            return 1;
        } finally {
            return 2;          // replaces the 1: never do this
        }
    }

    static int changing() {
        int x = 1;
        try {
            return x;          // the value 1 is saved here
        } finally {
            x = 99;            // too late to change the result
        }
    }

    static String order() {
        StringBuilder log = new StringBuilder();
        try {
            log.append("try ");
            throw new IllegalStateException("boom");
        } catch (IllegalStateException e) {
            log.append("catch ");
            return log.append("return").toString();
        } finally {
            log.append(" finally");
            System.out.println("log at finally: " + log);
        }
    }

    public static void main(String[] args) {
        System.out.println("overriding() = " + overriding());
        System.out.println("changing()   = " + changing());
        System.out.println("order()      = " + order());
    }
}
