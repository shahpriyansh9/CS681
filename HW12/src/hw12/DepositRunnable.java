package hw12;

public class DepositRunnable implements Runnable {
    private final ThreadSafeBankAccount2 account;
    private final double amount;
    private volatile boolean active = true;

    public DepositRunnable(ThreadSafeBankAccount2 account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    public void stopRunning() {
        active = false;
    }

    @Override
    public void run() {
        try {
            while (active) {
                account.deposit(amount);
                Thread.sleep(100); // simulate time taken for deposit
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
