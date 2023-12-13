package hw09;


import java.util.concurrent.locks.ReentrantLock;

public class RunnableCancellablePrimeFactorizer extends RunnablePrimeFactorizer {
    protected boolean done = false;
    protected final ReentrantLock lock = new ReentrantLock();
    public RunnableCancellablePrimeFactorizer(long dividend, long from, long to) {
        super(dividend, from, to);
    }

    public void setDone() {
        lock.lock();
        try {
            done = true;
        } finally {
            lock.unlock();
        }
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
                if (done) {
                    System.out.println("Factorization cancelled for " + dividend);
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
        RunnableCancellablePrimeFactorizer factorizer = new RunnableCancellablePrimeFactorizer(123456789, 2, 500);
        Thread thread = new Thread(factorizer);
        thread.start();

        try {
            Thread.sleep(500);  // Simulate some processing time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        factorizer.setDone();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Prime factors (if not cancelled): " + factorizer.getPrimeFactors());
    }
}
