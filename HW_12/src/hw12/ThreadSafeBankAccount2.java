package hw12;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class ThreadSafeBankAccount2 implements BankAccount{
    private double balance = 0;
    private ReentrantLock lock = new ReentrantLock();
    private Condition sufficientFundsCondition = lock.newCondition();
    private Condition belowUpperLimitFundsCondition = lock.newCondition();
    
    public void deposit(double amount){
		lock.lock();
		try{
			while(balance >= 300){
				System.out.println("Waiting to deposit, balance is too high.");
				belowUpperLimitFundsCondition.await();
			}
			balance += amount;
			System.out.println("Deposited " + amount + ", new balance: " + balance);
			sufficientFundsCondition.signalAll();
		}
        catch (InterruptedException exception){
            Thread.currentThread().interrupt();
        }
        finally{
            lock.unlock();
        }
    }
    
    public void withdraw(double amount){
		lock.lock();
		try{
			while(balance < amount){
				System.out.println("Waiting to withdraw, insufficient balance.");
				sufficientFundsCondition.await();
			}
			balance -= amount;
			System.out.println("Withdrew " + amount + ", new balance: " + balance);
			belowUpperLimitFundsCondition.signalAll();
		}
        catch (InterruptedException exception){
            Thread.currentThread().interrupt();
        }
        finally{
            lock.unlock();
        }
    }

    public double getBalance() { return balance; }

    public static void main(String[] args) throws InterruptedException {
        ThreadSafeBankAccount2 bankAccount = new ThreadSafeBankAccount2();
        List<Thread> threads = new ArrayList<>();
        List<DepositRunnable> depositRunnables = new ArrayList<>();
        List<WithdrawRunnable> withdrawRunnables = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            DepositRunnable depositRunnable = new DepositRunnable(bankAccount, 100);
            WithdrawRunnable withdrawRunnable = new WithdrawRunnable(bankAccount, 50);
            depositRunnables.add(depositRunnable);
            withdrawRunnables.add(withdrawRunnable);
            threads.add(new Thread(depositRunnable));
            threads.add(new Thread(withdrawRunnable));
        }

        threads.forEach(Thread::start);

        // Simulate main thread running for some time
        Thread.sleep(5000); 

        // Signal all threads to stop
        depositRunnables.forEach(DepositRunnable::terminate);
        withdrawRunnables.forEach(WithdrawRunnable::terminate);

        // Wait for all threads to finish
        for (Thread t : threads) {
            t.join();
        }
    }
}