public class EmptyBlankNull {

    public static void main(String[] args) {
        String empty = "";
        String spaces = "   ";
        String name = "  Priya  ";

        System.out.println("empty.isEmpty():  " + empty.isEmpty());
        System.out.println("spaces.isEmpty(): " + spaces.isEmpty());
        System.out.println("spaces.isBlank(): " + spaces.isBlank());
        System.out.println("strip():          ["
                + name.strip() + "]");
        System.out.println("stripLeading():   ["
                + name.stripLeading() + "]");

        String level = null;
        System.out.println("Safe check:       "
                + "GOLD".equals(level));
        System.out.println("Unsafe check:     "
                + level.equals("GOLD"));
    }
}
