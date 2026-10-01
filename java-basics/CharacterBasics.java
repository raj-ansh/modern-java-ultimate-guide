public class CharacterBasics {

    public static void main(String[] args) {
        char section = 'C';
        int code = section;
        char next = (char) (section + 1);

        System.out.println("Section:      " + section);
        System.out.println("Unicode code: " + code);
        System.out.println("Next section: " + next);
        System.out.println("Is a letter?  "
                + Character.isLetter(section));
        System.out.println('A' + 'B');
        System.out.println("" + 'A' + 'B');
    }
}
