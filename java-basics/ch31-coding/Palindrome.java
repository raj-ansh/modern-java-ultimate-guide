public class Palindrome {

    /** Ignores case and everything except letters and digits. */
    static boolean isPalindrome(String text) {
        int left = 0;
        int right = text.length() - 1;
        while (left < right) {
            char a = text.charAt(left);
            char b = text.charAt(right);
            if (!Character.isLetterOrDigit(a)) {
                left++;
            } else if (!Character.isLetterOrDigit(b)) {
                right--;
            } else if (Character.toLowerCase(a)
                    != Character.toLowerCase(b)) {
                return false;
            } else {
                left++;
                right--;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String[] tests = {"A man, a plan, a canal: Panama",
                          "race a car", "Was it a cat I saw?",
                          "x", ""};
        for (String test : tests) {
            System.out.printf("%-6s \"%s\"%n", isPalindrome(test),
                    test);
        }
    }
}
