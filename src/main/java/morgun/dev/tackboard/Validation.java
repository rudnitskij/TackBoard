package morgun.dev.tackboard;

import java.util.regex.Pattern;

/**
 * Contains methods of validation for incoming data
 *
 * @author Vitaliy Morgun
 */
public class Validation {
    static Pattern pattern;

    public static boolean headerIsValid(String header) {
        pattern = Pattern.compile("^[а-яА-ЯЁё0-9A-Za-z][а-яА-ЯЁё0-9A-Za-z\\s]{3,30}"); // текст заголовка от 3 до 30 знаков, только буквы, цифры и пробелы
        return pattern.matcher(header).matches();
    }

    public static boolean textIsValid(String text) {
        pattern = Pattern.compile("^[а-яА-ЯЁё0-9A-Za-z][а-яА-ЯЁё0-9A-Za-z\\s.,/()!?;:'\\u0034]{3,510}"); // текст объявления от 3 до 511 знаков, буквы, цифры и знаки препинания
        return pattern.matcher(text).matches();
    }

    public static boolean loginPasswordIsValid(String login) {
        pattern = Pattern.compile("^[A-Za-z][A-Za-z0-9]{3,29}");   //длина логина и пароля от 4 до 30 знаков, первый - буква, все буквы латинские
        return pattern.matcher(login).matches();
    }

}
