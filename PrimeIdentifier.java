import java.util.*;

public class PrimeIdentifier {
    public static void identifyAndTransformPrimes(int[][] matrix, int n, int numThreads) {
        List<int[]> primeCoords = new ArrayList<>();
        int rowsPerThread = Math.max(1, n / numThreads);
        Object lock = new Object();
        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            final int startRow = i * rowsPerThread;
            final int endRow = (i == numThreads - 1) ? n : (i + 1) * rowsPerThread;
            tasks.add(() -> {
                for (int row = startRow; row < endRow; row++) {
                    for (int col = 0; col < n; col++) {
                        if (primeCoords.size() >= 3)
                            return;
                        int val = matrix[row][col];
                        if (isPrime(val)) {
                            synchronized (lock) {
                                if (primeCoords.size() < 3) {
                                    primeCoords.add(new int[] { row, col });
                                }
                            }
                        }
                    }
                }
            });
        }
        List<Thread> threadList = new ArrayList<>();
        for (Runnable task : tasks) {
            Thread t = new Thread(task);
            threadList.add(t);
            t.start();
        }
        for (Thread t : threadList) {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }

        threadList.clear();
        for (int i = 0; i < numThreads; i++) {
            final int idx = i;
            Thread t = new Thread(() -> {
                for (int j = idx; j < primeCoords.size(); j += numThreads) {
                    int[] coord = primeCoords.get(j);
                    int row = coord[0], col = coord[1];
                    int val = matrix[row][col];
                    matrix[row][col] = val / val;
                }
            });
            threadList.add(t);
            t.start();
        }
        for (Thread t : threadList) {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }
    }

    private static boolean isPrime(int num) {
        if (num < 2)
            return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0)
                return false;
        }
        return true;
    }
}
