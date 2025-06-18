import java.util.*;

public class ElementWiseMultiplier {
    public static int[][] elementWiseMultiplication(int[][] a, int[][] b, int n, int numThreads) {
        int[][] result = new int[n][n];
        List<Thread> threadList = new ArrayList<>();
        int rowsPerThread = n / numThreads;
        int remainder = n % numThreads;
        int currentRow = 0;
        for (int i = 0; i < numThreads; i++) {
            final int startRow = currentRow;
            final int endRow = startRow + rowsPerThread + (i < remainder ? 1 : 0);
            currentRow = endRow;
            if (startRow >= n) break;
            Thread t = new Thread(() -> {
                for (int row = startRow; row < endRow && row < n; row++) {
                    for (int col = 0; col < n; col++) {
                        result[row][col] = a[row][col] * b[row][col];
                    }
                }
            });
            threadList.add(t);
            t.start();
        }
        for (Thread t : threadList) {
            try {
                t.join();
            } catch (InterruptedException e) {}
        }
        return result;
    }
}
