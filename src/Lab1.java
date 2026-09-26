import java.util.Scanner;

public class Lab1 {

    // Стани автомата
    enum State {
        START,       // початок
        S_I,         // прочитано 'i'
        S_IN,        // прочитано "in"
        S_INT,       // прочитано "int"
        S_SPACE,     // прочитано пробіл(и) після "int"
        S_ID,        // всередині імені змінної
        S_LBRACKET,  // щойно прочитано '['
        S_DIGITS,    // цифри розміру масиву всередині []
        S_RBRACKET,  // щойно прочитано ']'
        S_COMMA,     // щойно прочитано ',' (очікуємо нове ім'я)
        ACCEPT,      // прочитано ';' — рядок прийнято
        ERROR        // помилка
    }

    /** Класифікація вхідного символу: буква -> 'c', цифра -> 'd', інше без змін. */
    private static char classify(char c) {
        if (Character.isLetter(c)) return 'c';
        if (Character.isDigit(c))  return 'd';
        return c;
    }

    /** Функція переходів (стан, символ) -> новий стан. */
    private static State step(State state, char rawChar) {
        char c = classify(rawChar);
        switch (state) {
            case START:
                return (rawChar == 'i') ? State.S_I : State.ERROR;
            case S_I:
                return (rawChar == 'n') ? State.S_IN : State.ERROR;
            case S_IN:
                return (rawChar == 't') ? State.S_INT : State.ERROR;
            case S_INT:
                return (c == ' ') ? State.S_SPACE : State.ERROR;
            case S_SPACE:
                if (c == ' ') return State.S_SPACE;
                if (c == 'c') return State.S_ID;
                return State.ERROR;
            case S_ID:
                if (c == 'c' || c == 'd') return State.S_ID;
                if (c == '[') return State.S_LBRACKET;
                if (c == ',') return State.S_COMMA;
                if (c == ';') return State.ACCEPT;
                return State.ERROR;
            case S_LBRACKET:
                if (c == 'd') return State.S_DIGITS;
                return State.ERROR;
            case S_DIGITS:
                if (c == 'd') return State.S_DIGITS;
                if (c == ']') return State.S_RBRACKET;
                return State.ERROR;
            case S_RBRACKET:
                if (c == '[') return State.S_LBRACKET;
                if (c == ',') return State.S_COMMA;
                if (c == ';') return State.ACCEPT;
                return State.ERROR;
            case S_COMMA:
                if (c == ' ') return State.S_COMMA;
                if (c == 'c') return State.S_ID;
                return State.ERROR;
            default:
                return State.ERROR;
        }
    }

    public static boolean isValid(String input) {
        State state = State.START;
        for (char ch : input.toCharArray()) {
            state = step(state, ch);
            if (state == State.ERROR) return false;
        }
        return state == State.ACCEPT;
    }

    public static void main(String[] args) {
        String[] tests = {
                "int A,B,A1[10],B1[3][6],C;",     // коректний (зразок 1)
                "int AA[3][5],X,Y[10],Z,U[10][12];", // коректний (зразок 2)
                "int A;",                          // коректний, проста змінна
                "int 1A;",                         // помилка: ім'я починається з цифри
                "int A[10;",                       // помилка: немає ']'
                "float B;",                        // помилка: не "int"
                "int A,,B;",                       // помилка: подвійна кома
                "int A B;"                         // помилка: немає коми між іменами
        };

        System.out.println("=== Автоматична перевірка тестових прикладів ===");
        for (String t : tests) {
            System.out.printf("%-40s -> %s%n", t, isValid(t) ? "ПРАВИЛЬНО" : "ПОМИЛКА");
        }

        System.out.println("\n=== Інтерактивний режим (введіть 'exit' для виходу) ===");
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine() && !(line = sc.nextLine()).equalsIgnoreCase("exit")) {
            System.out.println(isValid(line) ? "ПРАВИЛЬНО" : "ПОМИЛКА");
        }
    }
}