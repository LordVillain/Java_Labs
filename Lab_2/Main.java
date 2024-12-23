import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Ошибка: требуется указать 1 файл с матрицей");
        }

        try (Scanner scanner = new Scanner(new File(args[0]))) {
            int[][] matrix = getMatrix(scanner);
            findLocalExtrema(matrix);
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: файл не найден");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // Считываем матрицу из файла
    public static int[][] getMatrix(Scanner sc){
        if (!sc.hasNextInt()) {
            throw new IllegalArgumentException("недостаточно данных в файле");
        }
        
        int size = sc.nextInt(); // Размер матрицы
        int[][] matrix = new int[size][size];

    try {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = sc.nextInt();
            }
        }
    } catch (InputMismatchException e) {
        throw new IllegalArgumentException("ожидались только целые числа");
    } catch (NoSuchElementException e){
        throw new IllegalArgumentException("нехватает чисел для составления матрицы");
    }

        System.out.println("Матрица:");

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        // Проверка на лишние данные в файле
        if (sc.hasNext()) {
            throw new IllegalArgumentException("Ошибка: в файле больше данных, чем нужно");
        }

        return matrix;
    }

    // Вывод локальных минимумов и максимумов
    public static void findLocalExtrema(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isLocalMinimum(matrix, i, j)) {
                    System.out.println("Локальный минимум равный " + matrix[i][j] + " на позиции (" + i + ", " + j + ")");
                }
                if (isLocalMaximum(matrix, i, j)) {
                    System.out.println("Локальный максимум равный " + matrix[i][j] + " на позиции (" + i + ", " + j + ")");
                }
            }
        }
    }

    // Проверка на локальный минимум
    private static boolean isLocalMinimum(int[][] matrix, int row, int col) {
        int value = matrix[row][col]; //рассматриваемый элемент матрицы

        //массив с соседями рассматриваемого элемента матрицы
        int[] adjacentRow = {-1, 1, 0, 0, -1, -1, 1, 1};
        int[] adjacentCol = {0, 0, -1, 1, -1, 1, -1, 1};

        for (int k = 0; k < 8; k++) {
            //координаты текущего соседа
            int newRow = row + adjacentRow[k];
            int newCol = col + adjacentCol[k];

            if (isInMatrix(matrix, newRow, newCol) && matrix[newRow][newCol] <= value) {
                return false;
            }
        }

        return true;
    }

    // Проверка на локальный максимум
    private static boolean isLocalMaximum(int[][] matrix, int row, int col) {
        int value = matrix[row][col]; //рассматриваемый элемент матрицы

        //массив с соседями рассматриваемого элемента матрицы
        int[] adjacentRow = {-1, 1, 0, 0, -1, -1, 1, 1};
        int[] adjacentCol = {0, 0, -1, 1, -1, 1, -1, 1};

        for (int k = 0; k < 8; k++) {
            //координаты текущего соседа
            int newRow = row + adjacentRow[k];
            int newCol = col + adjacentCol[k];

            if (isInMatrix(matrix, newRow, newCol) && matrix[newRow][newCol] >= value) {
                return false;
            }
        }

        return true;
    }

    // Проверка на корректность индексов
    private static boolean isInMatrix(int[][] matrix, int row, int col) {
        return row >= 0 && row < matrix.length && col >= 0 && col < matrix[0].length;
    }
}

// Класс для обработки ошибок
class IAAE extends Exception {
    public IAAE(String message) {
        super(message);
    }
}

//java Main "Matrix.txt"
//javac Main.java
