package hw12;

public class DepositRunnable implements Runnable {
    private ThreadSafeBankAccount2 account;
    private double amount;
    private volatile boolean running = true;

    public DepositRunnable(ThreadSafeBankAccount2 account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        try {
            while (running) {
                System.out.println("Attempting to deposit: " + amount);
                account.deposit(amount);
                System.out.println("Deposit completed.");
                Thread.sleep(500); 
            }
        } catch (InterruptedException e) {
            System.out.println("Deposit thread interrupted.");
        }
        System.out.println("Deposit thread terminating.");
    }
}
