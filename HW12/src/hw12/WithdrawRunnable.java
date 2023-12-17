package hw12;

public class WithdrawRunnable implements Runnable {
    private final ThreadSafeBankAccount2 account;
    private final double amount;
    private volatile boolean active = true;

    public WithdrawRunnable(ThreadSafeBankAccount2 account, double amount) {
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
                account.withdraw(amount);
                Thread.sleep(100); // simulate time taken for withdrawal
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
