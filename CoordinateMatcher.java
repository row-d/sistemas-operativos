import java.util.*;

public class CoordinateMatcher {
    public static int countCoordinateMatches(int[][] a, int[][] b, int n, int numThreads) {
        final int[] coincidencias = { 0 };
        int total = n * n;
        int chunk = Math.max(1, total / numThreads);
        Object lock = new Object();
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            final int start = i * chunk;
            final int end = (i == numThreads - 1) ? total : (i + 1) * chunk;
            Thread t = new Thread(() -> {
                for (int idx = start; idx < end; idx++) {
                    int row = idx / n, col = idx % n;
                    if (a[row][col] == b[row][col]) {
                        synchronized (lock) {
                            coincidencias[0]++;
                            System.out.println("Coincidencia en [" + row + "," + col + "]: " + a[row][col]);
                        }
                    }
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
        if (coincidencias[0] == 0) {
            System.out.println("No existen valores iguales en la misma ubicación.");
        }
        return coincidencias[0];
    }
}
