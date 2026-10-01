public class PassByValue {

    public static void main(String[] args) {
        int points = 500;
        addBonus(points);
        System.out.println("Points after addBonus:     " + points);

        StringBuilder note = new StringBuilder("Order BN-1042");
        addGiftNote(note);
        System.out.println("Note after addGiftNote:    " + note);

        replaceNote(note);
        System.out.println("Note after replaceNote:    " + note);

        points = withBonus(points);
        System.out.println("Points after withBonus:    " + points);
    }

    static void addBonus(int points) {
        points = points + 100;
    }

    static void addGiftNote(StringBuilder note) {
        note.append(" (gift wrap)");
    }

    static void replaceNote(StringBuilder note) {
        note = new StringBuilder("Something else");
    }

    static int withBonus(int points) {
        return points + 100;
    }
}
