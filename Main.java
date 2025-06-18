import java.util.*;

public class Main {
    private int[][] matrixA;
    private int[][] matrixB;
    private int[][] matrixC;
    private int n;
    private Scanner scanner;
    private int numThreads;

    public Main() {
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Main program = new Main();
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
        MatrixUtils.fillMatrixWithRandomNumbers(matrixA, n);
        MatrixUtils.fillMatrixWithRandomNumbers(matrixB, n);
    }

    private void showMenu() {
        int option;
        do {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("\nMatriz A:");
            MatrixUtils.printMatrix(matrixA, n);
            System.out.println("\nMatriz B:");
            MatrixUtils.printMatrix(matrixB, n);
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
                    numThreads = 10;
                    System.out
                            .println("Conteo de unos en la matriz A: " + OnesCounter.countOnes(matrixA, n, numThreads));
                    System.out
                            .println("Conteo de unos en la matriz B: " + OnesCounter.countOnes(matrixB, n, numThreads));
                    System.out.println("Presione Enter para continuar...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;
                case 2:
                    numThreads = 10;
                    PrimeIdentifier.identifyAndTransformPrimes(matrixA, n, numThreads);
                    PrimeIdentifier.identifyAndTransformPrimes(matrixB, n, numThreads);
                    System.out.println("Matrices transformadas (primos reemplazados por 1):");
                    System.out.println("Matriz A:");
                    MatrixUtils.printMatrix(matrixA, n);
                    System.out.println("Matriz B:");
                    MatrixUtils.printMatrix(matrixB, n);
                    System.out
                            .println("Conteo de unos en la matriz A: " + OnesCounter.countOnes(matrixA, n, numThreads));
                    System.out
                            .println("Conteo de unos en la matriz B: " + OnesCounter.countOnes(matrixB, n, numThreads));
                    System.out.println("Presione Enter para continuar...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;
                case 3:
                    numThreads = 20;
                    int coincidencias = CoordinateMatcher.countCoordinateMatches(matrixA, matrixB, n, numThreads);
                    System.out.println("Total de coincidencias: " + coincidencias);
                    System.out.println("Presione Enter para continuar...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;
                case 4:
                    numThreads = 10;
                    matrixC = ElementWiseMultiplier.elementWiseMultiplication(matrixA, matrixB, n, numThreads);
                    System.out.println("Resultado de la multiplicación elemento a elemento (Matriz C):");
                    MatrixUtils.printMatrix(matrixC, n);
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
}