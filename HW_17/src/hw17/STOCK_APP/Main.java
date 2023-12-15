package hw17.STOCK_APP;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int NUM_OBSERVABLES = 5;
    private static final int NUM_THREADS = 13;

    public static void main(String[] args) {
        // Create observables and observers
        StockQuoteObservable[] observables = new StockQuoteObservable[NUM_OBSERVABLES];
        for (int i = 0; i < NUM_OBSERVABLES; i++) {
            observables[i] = new StockQuoteObservable();
            observables[i].addObserver(new TableObserver()); // Add a TableObserver
    observables[i].addObserver(new ThreeDObserver()); // Add a ThreeDObserver
    observables[i].addObserver(new LineChartObserver()); // Add a TableObserver
        }

        // Executor service for managing threads
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        // Submit tasks to executor service
        Random random = new Random();
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 10; j++) {
                    int index = random.nextInt(NUM_OBSERVABLES);
                    String ticker = "TICKER" + random.nextInt(10);
                    double quote = random.nextDouble() * 100;
                    observables[index].changeQuote(ticker, quote);
                }
            });
        }

        // Shutdown executor and await termination
        executor.shutdown();
        // Additional code may be needed to properly wait for all tasks to complete
    }
}


