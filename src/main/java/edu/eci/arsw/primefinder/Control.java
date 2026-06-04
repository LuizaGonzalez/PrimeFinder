package edu.eci.arsw.primefinder;
import java.util.Scanner;

public class Control extends Thread {

    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    private PauseControl pauseControl;

    private Control() {
        super();
        this.pauseControl = new PauseControl();
        this.pft = new PrimeFinderThread[NTHREADS];

        int i;
        for (i = 0; i < NTHREADS - 1; i++) {
            pft[i] = new PrimeFinderThread(i * NDATA, (i + 1) * NDATA, pauseControl);
        }
        pft[i] = new PrimeFinderThread(i * NDATA, MAXVALUE + 1, pauseControl);
    }

    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for (int i = 0; i < NTHREADS; i++) {
            pft[i].start();
        }

        Scanner scanner = new Scanner(System.in);

        while (areAlive()) {
            try {
                Thread.sleep(TMILISECONDS);

                if (areAlive()) {
                    pauseControl.pause();
                    Thread.sleep(50);

                    System.out.printf("  Primos encontrados: %d%n", totalPrimes());
                    System.out.println("  Presiona ENTER para continuar...");

                    scanner.nextLine();

                    pauseControl.resume();
                    System.out.println("  Reanudando...\n");
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.printf("%nBúsqueda finalizada. Total de primos: %d%n", totalPrimes());
    }

    private int totalPrimes() {
        int total = 0;
        for (PrimeFinderThread t : pft) {
            total += t.getPrimes().size();
        }
        return total;
    }

    private boolean areAlive() {
        for (PrimeFinderThread t : pft) {
            if (t.isAlive()) return true;
        }
        return false;
    }
}
