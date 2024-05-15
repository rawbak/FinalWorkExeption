/*
 * Промежуточная аттестационная работа.
 *
 * Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке,
 * разделенные пробелом:
 *
 *      Фамилия Имя Отчество дата рождения номер телефона пол
 *
 * Форматы данных:
 *      фамилия, имя, отчество - строки
 *      датарождения - строка формата dd.mm.yyyy
 *      номертелефона - целое беззнаковое число без форматирования
 *      пол - символ латиницей f или m
 *
 * Приложение должно проверить введенные данные по количеству.
 * Если количество не совпадает с требуемым, вернуть код ошибки, обработать его и показать пользователю сообщение,
 * что он ввел меньше и больше данных, чем требуется.
 * Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры.
 * Если форматы данных не совпадают, нужно бросить исключение, соответствующее типу проблемы.
 * Можно использовать встроенные типы java и создать свои. Исключение должно быть корректно обработано, пользователю
 * выведено сообщение с информацией, что именно неверно.
 * Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, в него в одну строку
 * должны записаться полученные данные, вида:
 *
 * <Фамилия><Имя><Отчество><датарождения><номертелефона><пол>
 *
 * Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
 * Не забудьте закрыть соединение с файлом.
 * При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано,
 * пользователь должен увидеть стектрейс ошибки.
 */


import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        //ввод и проверка строки ФИО
        System.out.println("Введите Фамилию Имя Отчество: ");
        String firstLastNameString = scanner.nextLine ();
        String[] arrayFirstLastName = firstLastNameString.split(" ");
        while (true) {
            try {
                countEnteredData(arrayFirstLastName);
                try {
                    stringCheckLettersAndSpaces(firstLastNameString);
                    break;
                } catch (IllegalInputException e) {
                    throw new IllegalInputException();
                }
            } catch (CountInputElementException e) {
                throw new CountInputElementException();
            }
        }

        //ввод и проверка даты
        System.out.println("Введите дату рождения в формате дд.мм.гггг ");
        String dateBorn = scanner.nextLine ();
        String[] arrayDateBorn = dateBorn.split("\\.");
        while (true) {
            try {
                countEnteredData(arrayDateBorn);
                try {
                    stringCheckNumsAndPoints(dateBorn);
                    break;
                } catch (IllegalNumInputException e) {
                    throw new IllegalNumInputException();
                }
            } catch (CountInputElementException e) {
                throw new CountInputElementException();
            }
        }
        //ввод и проверка номера телефона
        System.out.println("введите номер телефона только цифрами 8877788877");
        String stringPhoneNumber = scanner.nextLine ();
        while (true) {
            try {
                stringNumberPhone(stringPhoneNumber);
                break;
            } catch (IllegalNumPhoneNumberException e) {
                throw new IllegalNumPhoneNumberException();
            }
        }
        //ввод пола и проверка гендера
        System.out.println("введите пол в формате f или m ");
        char charGender = scanner.next().charAt(0);
//        char charGender = 'f';
        while (true) {
            try {
                isGender(charGender);
                break;
            } catch (IllegalCharGenderException e) {
                throw new IllegalCharGenderException();
            }
        }
        //запись в файл строки с правильными данными
        writeToFile(arrayFirstLastName[0], arrayAllData(firstLastNameString, dateBorn, stringPhoneNumber, charGender));
    }
    //запись в файл, автоматическое закрытие после выполнения блока кода try-with-resources
    public static void writeToFile(String fileName, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("\n" + content);
        }
    }
    //метод объединения всех данных в строку массива
    public static String arrayAllData(String str1, String str2, String str3, char ch) {
        String[] array = {str1, str2, str3, String.valueOf(ch)};
        return Arrays.toString(array);
    }
    //Проверка ввода пола
    public static boolean isGender(char s) {
        if (s == 'f' || s == 'm') {
            return true;
        }
        throw new IllegalCharGenderException();
    }
    //Проверка что введены 3 части массива строк - ФИО
    public static void countEnteredData(String[] array) {
        for (String s : array) {
            if (array.length != 3) {
                throw new CountInputElementException();
            }
        }
    }
    //проверка ввода номера телефона - только цифры
    public static void stringNumberPhone(String s) {
        Pattern p = Pattern.compile("^[1-9]+[0-9]*$");
        Matcher m = p.matcher(s);
        boolean b = m.matches();
        if (!b) {
            throw new IllegalNumPhoneNumberException();
        }
    }
    //Проверка что в строке есть только буквы и пробелы
    public static void stringCheckLettersAndSpaces(String s) {
        Pattern p = Pattern.compile("^[ А-Яа-я]+$");
        Matcher m = p.matcher(s);
        boolean b = m.matches();
        if (!b) {
            throw new IllegalInputException();
        }
    }
    //Проверка что есть только цифры и точки
    public static void stringCheckNumsAndPoints(String s) {
        Pattern p = Pattern.compile("^[0-9.]+$");
        Matcher m = p.matcher(s);
        boolean b = m.matches();
        if (!b) {
            throw new IllegalNumInputException();
        }
    }
}
class CountInputElementException extends ArrayIndexOutOfBoundsException {
    @Override
    public String getMessage() {
        return "Ошибка ввода данных, внимательнее ";
    }
}
class IllegalInputException extends IllegalArgumentException {
    @Override
    public String getMessage() {
        return "Введите только буквы без символов";
    }
}
class IllegalNumInputException extends IllegalArgumentException {
    @Override
    public String getMessage() {
        return "Дата введена неверно, формат ввода дд.мм.гггг ";
    }
}
class IllegalNumPhoneNumberException extends IllegalArgumentException {
    @Override
    public String getMessage() {
        return "Можно ввести только цифры ";
    }
}
class IllegalCharGenderException extends IllegalArgumentException {
    @Override
    public String getMessage() {
        return "Пол вводится символами f или m, проверьте ввод ";
    }
}