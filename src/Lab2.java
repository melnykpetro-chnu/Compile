import java.util.Scanner;
import java.util.Stack;

/**
 * Лабораторна робота №2
 * Стековий автомат для перевірки збалансованості різних пар дужок
 * ()[]{} у математичних виразах, що містять числа та операції.
 *
 * Приклад коректного виразу:
 *   45+(45-34)*{[23+15]*[(11-4)*(5+45)]}-44/(12-7)
 *
 * Приклад некоректного виразу (дужки переплутані місцями):
 *   90 +(25-67)*( ]32+4[ * {4-32})
 *
 * Ідея роботи: символи, що не є дужками (цифри, крапка, знаки
 * операцій, пробіли), автомат просто пропускає — вони не впливають
 * на стан стека. Кожна відкриваюча дужка заштовхується у стек.
 * Кожна закриваюча дужка повинна знімати зі стека РІВНО ту саму
 * відкриваючу дужку (перевірка типу пари), інакше — помилка.
 * Якщо після проходу всього рядка стек порожній — вираз коректний.
 */
public class Lab2 {

    /** Перевіряє символ на приналежність до відкриваючих дужок. */
    private static boolean isOpening(char c) {
        return c == '(' || c == '[' || c == '{';
    }

    /** Перевіряє символ на приналежність до закриваючих дужок. */
    private static boolean isClosing(char c) {
        return c == ')' || c == ']' || c == '}';
    }

    /** Повертає відкриваючу дужку, що відповідає закриваючій. */
    private static char matchingOpening(char closing) {
        switch (closing) {
            case ')': return '(';
            case ']': return '[';
            case '}': return '{';
            default:  return '\0';
        }
    }

    /** Допустимі символи математичного виразу (окрім дужок). */
    private static boolean isAllowedNonBracket(char c) {
        return Character.isDigit(c)
                || c == '.'
                || c == '+' || c == '-' || c == '*' || c == '/'
                || c == ' ' || c == '\t';
    }

    /**
     * Обробляє вираз стековим автоматом.
     * Повертає null, якщо вираз коректний,
     * або текстовий опис помилки з позицією символу — інакше.
     */
    public static String check(String expr) {
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (isOpening(c)) {
                stack.push(c);                       // заштовхнути відкриваючу дужку
            } else if (isClosing(c)) {
                if (stack.isEmpty()) {
                    return "помилка на позиції " + i + ": зайва закриваюча дужка '" + c + "'";
                }
                char top = stack.pop();
                char expectedOpening = matchingOpening(c);
                if (top != expectedOpening) {
                    return "помилка на позиції " + i + ": дужка '" + c
                            + "' не відповідає відкритій раніше '" + top + "'";
                }
            } else if (!isAllowedNonBracket(c)) {
                return "помилка на позиції " + i + ": неприпустимий символ '" + c + "'";
            }
            // інші символи (цифри, операції, пробіли) стан стека не змінюють
        }

        if (!stack.isEmpty()) {
            return "помилка: не закрита дужка '" + stack.peek() + "' (стек не порожній)";
        }
        return null; // немає помилок
    }

    public static boolean isValid(String expr) {
        return check(expr) == null;
    }

    public static void main(String[] args) {
        String[] tests = {
                "45+(45-34)*{[23+15]*[(11-4)*(5+45)]}-44/(12-7)", // коректний (зразок 1)
                "90 +(25-67)*( ]32+4[ * {4-32})",                 // некоректний (зразок 2)
                "(1+2)",                                          // коректний, проста дужка
                "((1+2)",                                         // помилка: не закрита '('
                "(1+2))",                                         // помилка: зайва ')'
                "[1+(2*3)}",                                      // помилка: '}' не відповідає '['
                "1+2*3",                                          // коректний, без дужок
                "1+2#3"                                           // помилка: неприпустимий символ '#'
        };

        System.out.println("=== Автоматична перевірка тестових прикладів ===");
        for (String t : tests) {
            String result = check(t);
            System.out.printf("%-50s -> %s%n", t,
                    result == null ? "КОРЕКТНО" : "НЕКОРЕКТНО (" + result + ")");
        }

        System.out.println("\n=== Інтерактивний режим (введіть 'exit' для виходу) ===");
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine() && !(line = sc.nextLine()).equalsIgnoreCase("exit")) {
            String result = check(line);
            System.out.println(result == null ? "КОРЕКТНО" : "НЕКОРЕКТНО (" + result + ")");
        }
    }
}