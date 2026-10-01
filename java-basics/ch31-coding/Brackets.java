import java.util.ArrayDeque;
import java.util.Deque;

public class Brackets {

    static boolean isBalanced(String text) {
        Deque<Character> open = new ArrayDeque<>();   // a stack
        for (char c : text.toCharArray()) {
            switch (c) {
                case '(', '[', '{' -> open.push(c);
                case ')', ']', '}' -> {
                    if (open.isEmpty() || open.pop() != partner(c)) {
                        return false;
                    }
                }
                default -> { }                        // other text
            }
        }
        return open.isEmpty();          // anything left is unclosed
    }

    static char partner(char close) {
        return switch (close) {
            case ')' -> '(';
            case ']' -> '[';
            default -> '{';
        };
    }

    public static void main(String[] args) {
        String[] tests = {"([]{})", "list.get(map[key])", "([)]",
                          "((", "}", ""};
        for (String test : tests) {
            System.out.printf("%-20s %s%n", "\"" + test + "\"",
                    isBalanced(test));
        }
    }
}
