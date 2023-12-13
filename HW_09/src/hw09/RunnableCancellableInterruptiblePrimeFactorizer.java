package hw09;


public class RunnableCancellableInterruptiblePrimeFactorizer extends RunnableCancellablePrimeFactorizer {
    
    public RunnableCancellableInterruptiblePrimeFactorizer(long dividend, long from, long to) {
        super(dividend, from, to);
    }

    @Override
    public void generatePrimeFactors() {
        long divisor = from;
        while (dividend != 1 && divisor <= to) {
            if (divisor > 2 && divisor % 2 == 0) {
                divisor++;
                continue;
            }
            lock.lock();
            try {
                if (done || Thread.interrupted()) {
                    System.out.println("Factorization cancelled or interrupted for " + dividend);
                    break;
                }
                if (dividend % divisor == 0) {
                    factors.add(divisor);
                    dividend /= divisor;
                } else {
                    divisor++;
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        RunnableCancellableInterruptiblePrimeFactorizer factorizer = new RunnableCancellableInterruptiblePrimeFactorizer(123456789, 2, 500);
        Thread thread = new Thread(factorizer);
        thread.start();

        try {
            Thread.sleep(500);  // Simulate some processing time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        factorizer.setDone();
        thread.interrupt();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Prime factors (if not cancelled or interrupted): " + factorizer.getPrimeFactors());
    }
}
