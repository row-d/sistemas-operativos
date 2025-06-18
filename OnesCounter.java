import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class OnesCounter {
    public static int countOnes(int[][] matrix, int n, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int rowsPerThread = Math.max(1, n / numThreads);
        AtomicInteger totalCount = new AtomicInteger(0);

        for (int i = 0; i < numThreads; i++) {
            final int startRow = i * rowsPerThread;
            final int endRow = (i == numThreads - 1) ? n : (i + 1) * rowsPerThread;
            executor.submit(() -> {
                for (int row = startRow; row < endRow; row++) {
                    for (int j = 0; j < n; j++) {
                        if (matrix[row][j] == 1) {
                            totalCount.incrementAndGet();
                        }
                    }
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        return totalCount.get();
    }
}
