package hw12;

public class WithdrawRunnable implements Runnable {
    private ThreadSafeBankAccount2 account;
    private double amount;
    private volatile boolean running = true;

    public WithdrawRunnable(ThreadSafeBankAccount2 account, double amount) {
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
                System.out.println("Attempting to withdraw: " + amount);
                account.withdraw(amount);
                System.out.println("Withdrawal completed.");
                Thread.sleep(500); 
            }
        } catch (InterruptedException e) {
            System.out.println("Withdraw thread interrupted.");
        }
        System.out.println("Withdraw thread terminating.");
    }
}
