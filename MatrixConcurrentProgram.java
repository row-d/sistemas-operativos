import java.util.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MatrixConcurrentProgram {
    private int[][] matrixA;
    private int[][] matrixB;
    private int[][] matrixC;
    private int n;
    private Scanner scanner;
    ExecutorService executor;
    private int numThreads;

    public MatrixConcurrentProgram() {
        scanner = new Scanner(System.in);
    }

    public void setThreadPoolSize(int newSize) {
        if (newSize > 0) {
            if (executor != null && !executor.isShutdown()) {
                executor.shutdown();
            }
            numThreads = newSize;
            executor = Executors.newFixedThreadPool(numThreads);
        }
    }

    public static void main(String[] args) {
        MatrixConcurrentProgram program = new MatrixConcurrentProgram();
        program.run();
    }

    public void run() {
        initializeSystem();
        showMenu();
    }

    private void initializeSystem() {
        do {
            System.out.print("Ingrese el tamaño de las matrices (n > 1): ");
            n = scanner.nextInt();
        } while (n <= 1);

        matrixA = new int[n][n];
        matrixB = new int[n][n];
        fillMatrixWithRandomNumbers(matrixA);
        fillMatrixWithRandomNumbers(matrixB);
    }

    private void fillMatrixWithRandomNumbers(int[][] matrix) {
        Random random = new Random();
        for (int i = 0; i < n * n; i++) {
            matrix[i / n][i % n] = random.nextInt(1, 10);
        }
    }

    private void printMatrix(int[][] matrix) {
        for (int i = 0; i < n; i++) {
            System.out.print("|");
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1) {
                    // Fondo blanco, letra negra
                    System.out.print("\033[30;47m");
                    System.out.printf("%3d ", matrix[i][j]);
                    System.out.print("\033[0m"); // Reset
                } else {
                    System.out.printf("%3d ", matrix[i][j]);
                }
            }
            System.out.println("|");
        }
    }

    private void showMenu() {
        int option;
        do {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("\nMatriz A:");
            printMatrix(matrixA);
            System.out.println("\nMatriz B:");
            printMatrix(matrixB);
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Conteo de Unos (1)");
            System.out.println("2. Identificación y Transformación de Primos");
            System.out.println("3. Coincidencias por Coordenadas");
            System.out.println("4. Multiplicación Elemento a Elemento");
            System.out.println("5. Reinicializar Sistema");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Conteo de unos en la matriz A: " + countOnes(matrixA));
                    System.out.println("Conteo de unos en la matriz B: " + countOnes(matrixB));
                    System.out.println("Presione Enter para continuar...");
                    scanner.nextLine(); // Consume the newline character
                    scanner.nextLine(); // Espera Enter
                    break;
                case 2:
                    identifyAndTransformPrimes(matrixA);
                    identifyAndTransformPrimes(matrixB);
                    System.out.println("Matrices transformadas (primos reemplazados por 1):");
                    System.out.println("Matriz A:");
                    printMatrix(matrixA);
                    System.out.println("Matriz B:");
                    printMatrix(matrixB);
                    System.out.println("Conteo de unos en la matriz A: " + countOnes(matrixA));
                    System.out.println("Conteo de unos en la matriz B: " + countOnes(matrixB));
                    System.out.println("Presione Enter para continuar...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;
                case 3:
                    int coincidencias = countCoordinateMatches(matrixA, matrixB);
                    System.out.println("Total de coincidencias: " + coincidencias);
                    System.out.println("Presione Enter para continuar...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;
                case 4:
                    matrixC = elementWiseMultiplication(matrixA, matrixB);
                    System.out.println("Resultado de la multiplicación elemento a elemento (Matriz C):");
                    printMatrix(matrixC);
                    System.out.println("Presione Enter para continuar...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;
                case 5:
                    initializeSystem();
                    matrixC = null;
                    break;
                case 0:
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (option != 0);
    }

    private int countOnes(int[][] matrix) {
        setThreadPoolSize(10);
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

    // --- FUNCIONALIDAD 2: Identificación y Transformación de Primos ---
    private void identifyAndTransformPrimes(int[][] matrix) {
        setThreadPoolSize(10);
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
        // Transformar los primos encontrados
        threadList.clear();
        for (int i = 0; i < numThreads; i++) {
            final int idx = i;
            Thread t = new Thread(() -> {
                for (int j = idx; j < primeCoords.size(); j += numThreads) {
                    int[] coord = primeCoords.get(j);
                    int row = coord[0], col = coord[1];
                    int val = matrix[row][col];
                    matrix[row][col] = val / val; // dividir por sí mismo (queda 1)
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

    private boolean isPrime(int num) {
        if (num < 2)
            return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0)
                return false;
        }
        return true;
    }

    // --- FUNCIONALIDAD 3: Coincidencias por Coordenadas ---
    private int countCoordinateMatches(int[][] a, int[][] b) {
        setThreadPoolSize(20);
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

    // --- FUNCIONALIDAD 4: Multiplicación Elemento a Elemento ---
    private int[][] elementWiseMultiplication(int[][] a, int[][] b) {
        setThreadPoolSize(10);
        int[][] result = new int[n][n];
        int rowsPerThread = Math.max(1, n / numThreads);
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            final int startRow = i * rowsPerThread;
            final int endRow = (i == numThreads - 1) ? n : (i + 1) * rowsPerThread;
            Thread t = new Thread(() -> {
                for (int row = startRow; row < endRow; row++) {
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
            } catch (InterruptedException e) {
            }
        }
        return result;
    }

}