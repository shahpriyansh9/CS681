package hw12;

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
			System.out.println("Lock obtained");
			System.out.println(Thread.currentThread().threadId() + 
					" (d): current balance: " + balance);
			while(balance >= 300){
				System.out.println(Thread.currentThread().threadId() + 
						" (d): await(): Balance exceeds the upper limit.");
				belowUpperLimitFundsCondition.await();
			}
			balance += amount;
			System.out.println(Thread.currentThread().threadId() + 
					" (d): new balance: " + balance);
			sufficientFundsCondition.signalAll();
		}
		catch (InterruptedException exception){
			exception.printStackTrace();
		}
		finally{
			lock.unlock();
			System.out.println("Lock released");
		}
	}
	
	public void withdraw(double amount){
		lock.lock();
		try{
			System.out.println("Lock obtained");
			System.out.println(Thread.currentThread().threadId() + 
					" (w): current balance: " + balance);
			while(balance <= 0){
				System.out.println(Thread.currentThread().threadId() + 
						" (w): await(): Insufficient funds");
				sufficientFundsCondition.await();
			}
			balance -= amount;
			System.out.println(Thread.currentThread().threadId() + 
					" (w): new balance: " + balance);
			belowUpperLimitFundsCondition.signalAll();
		}
		catch (InterruptedException exception){
			exception.printStackTrace();
		}
		finally{
			lock.unlock();
			System.out.println("Lock released");
		}
	}

	public double getBalance() { return this.balance; }

	public static void main(String[] args) throws InterruptedException {
		ThreadSafeBankAccount2 bankAccount = new ThreadSafeBankAccount2();
		double depositAmount = 100;
		double withdrawAmount = 100;
	
		DepositRunnable[] depositRunnables = new DepositRunnable[5];
		WithdrawRunnable[] withdrawRunnables = new WithdrawRunnable[5];
		Thread[] threads = new Thread[10];
	
		for (int i = 0; i < 5; i++) {
			depositRunnables[i] = new DepositRunnable(bankAccount, depositAmount);
			withdrawRunnables[i] = new WithdrawRunnable(bankAccount, withdrawAmount);
			threads[i * 2] = new Thread(depositRunnables[i]);
			threads[i * 2 + 1] = new Thread(withdrawRunnables[i]);
			threads[i * 2].start();
			threads[i * 2 + 1].start();
		}
	
		Thread.sleep(5000);
	
		for (int i = 0; i < 5; i++) {
			depositRunnables[i].stopRunning();
			withdrawRunnables[i].stopRunning();
		}
	}
	
}
