import java.util.Random;

public class MatrixUtils {
    public static void fillMatrixWithRandomNumbers(int[][] matrix, int n) {
        Random random = new Random();
        for (int i = 0; i < n * n; i++) {
            matrix[i / n][i % n] = random.nextInt(1, 10);
        }
    }

    public static void printMatrix(int[][] matrix, int n) {
        for (int i = 0; i < n; i++) {
            System.out.print("|");
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1) {
                    System.out.print("\033[30;47m");
                    System.out.printf("%3d ", matrix[i][j]);
                    System.out.print("\033[0m");
                } else {
                    System.out.printf("%3d ", matrix[i][j]);
                }
            }
            System.out.println("|");
        }
    }
}
