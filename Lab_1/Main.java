
class ExceptionLeadingZeros extends Exception {
    public ExceptionLeadingZeros(String message) {
        super(message);
    }
}

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Необходимо передать одну строку в качестве аргумента");
            return;
        }
        String inputStr = args[0];

        try {
            String result = deleteLeadingZeros(inputStr);
            System.out.println("Результат: " + result);
        } catch (ExceptionLeadingZeros e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public static String deleteLeadingZeros(String inputStr) throws ExceptionLeadingZeros {
        StringBuilder result = new StringBuilder();
        StringBuilder currentNumber = new StringBuilder();

        boolean insideGroupOfNumber = false; // флаг показывающий находимся ли мы внутри группы цифр
        boolean dotBefore = false; // флаг показывающий была ли перед цифрами точка

        for (int i = 0; i < inputStr.length(); i++) {
            char c = inputStr.charAt(i);

            if (c == '.') {
                if (insideGroupOfNumber) {
                    result.append(currentNumber);
                    currentNumber.setLength(0);  // освобождаем currentNumber для нового числа
                    insideGroupOfNumber = false; // закончилась группа чисел
                }
                result.append(c);
                dotBefore = true;
            } else if (Character.isDigit(c)) {
                // начало группы цифр
                if (!insideGroupOfNumber) {
                    insideGroupOfNumber = true;
                    currentNumber.setLength(0);  // освобождаем currentNumber для новой группы цифр
                }

                // если до цифры не было точки и это первый встречающийся 0, то не добавляем его
                if (currentNumber.length() == 0 && c == '0' && !dotBefore) {
                    continue;
                }

                currentNumber.append(c);
            } else {
                // при встречи с символом не цифрой проверяем, не закончилась ли группа цифр
                if (insideGroupOfNumber) {
                    result.append(currentNumber);
                    currentNumber.setLength(0);
                    insideGroupOfNumber = false;
                }

                dotBefore = false; // сбрасываем флаг точки
                result.append(c);
            }
        }

        // если строка завершилась во время обработки числа
        if (insideGroupOfNumber) {
            result.append(currentNumber);
        }

        return result.toString();
    }
}
