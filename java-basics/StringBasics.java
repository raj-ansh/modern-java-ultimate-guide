public class StringBasics {

    public static void main(String[] args) {
        String title = "The Quiet Library";

        System.out.println("Length:        " + title.length());
        System.out.println("First char:    " + title.charAt(0));
        System.out.println("Last char:     "
                + title.charAt(title.length() - 1));
        System.out.println("Upper case:    " + title.toUpperCase());
        System.out.println("Lower case:    " + title.toLowerCase());
        System.out.println("Has 'Quiet'?   "
                + title.contains("Quiet"));
        System.out.println("Starts 'The'?  "
                + title.startsWith("The"));
        System.out.println("Index of 'Q':  " + title.indexOf('Q'));
        System.out.println("Index of 'z':  " + title.indexOf('z'));
        System.out.println("Substring:     " + title.substring(4, 9));
        System.out.println("From index 10: " + title.substring(10));
        System.out.println("Replaced:      "
                + title.replace("Quiet", "Busy"));
    }
}
