package hw13;

import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;
import java.util.Map;

public class OperaBookingSystemSafe {
    private Map<Integer, String> seats;
    private Map<String, String> userAccounts; // Username, Password
    private ReentrantLock lock = new ReentrantLock();
    private volatile boolean shutdownInitiated = false;

    public OperaBookingSystemSafe(int totalSeats) {
        this.seats = new HashMap<>();
        this.userAccounts = new HashMap<>();
        for (int i = 1; i <= totalSeats; i++) {
            seats.put(i, null); // null indicates seat is available
        }
    }

    public boolean createUserAccount(String username, String password) {
        lock.lock();
        try {
            if (!userAccounts.containsKey(username)) {
                userAccounts.put(username, password);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean login(String username, String password) {
        lock.lock();
        try {
            return userAccounts.getOrDefault(username, "").equals(password);
        } finally {
            lock.unlock();
        }
    }

    public boolean bookSeat(String username, int seatNumber) {
        if (shutdownInitiated) {
            System.out.println("[" + System.currentTimeMillis() + "] System is shutting down. No new bookings.");
            return false;
        }
    
        lock.lock();
        try {
            System.out.println("[" + System.currentTimeMillis() + "] " + username + " is trying to book seat " + seatNumber);
            if (seats.get(seatNumber) == null) {
                try {
                    // Simulate processing delay
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                seats.put(seatNumber, username);
                System.out.println("[" + System.currentTimeMillis() + "] " + username + " successfully booked seat " + seatNumber);
                return true;
            }
            System.out.println("[" + System.currentTimeMillis() + "] " + username + " failed to book seat " + seatNumber + " (Already booked)");
            return false;
        } finally {
            lock.unlock();
        }
    }
    

    public void initiateShutdown() {
        shutdownInitiated = true;
    }

    public static void main(String[] args) {
        OperaBookingSystemSafe bookingSystem = new OperaBookingSystemSafe(10);

        bookingSystem.createUserAccount("user1", "pass1");
        bookingSystem.createUserAccount("user2", "pass2");

        Runnable bookSeatTask = () -> {
            String threadName = Thread.currentThread().getName();
            boolean result = bookingSystem.bookSeat(threadName, 5);
            System.out.println(threadName + " booking seat 5: " + result);
        };

        Thread user1 = new Thread(bookSeatTask, "user1");
        Thread user2 = new Thread(bookSeatTask, "user2");

        user1.start();
        user2.start();
        System.out.println("====================================THREAD_SAFE=======================================");
    }
}
