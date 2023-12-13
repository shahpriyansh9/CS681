package hw15.STOCK_APP;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public abstract class Observable<T> { // Generic type parameter changed to T
    private final List<Observer<T>> observers = new LinkedList<>();
    private final Lock lock = new ReentrantLock();

    public void addObserver(Observer<T> o) {
        lock.lock();
        try {
            observers.add(o);
        } finally {
            lock.unlock();
        }
    }

    public void clearObservers() {
        lock.lock();
        try {
            observers.clear();	
        } finally {
            lock.unlock();
        }
    }

    public int countObservers() {
        lock.lock();
        try {
            return observers.size();
        } finally {
            lock.unlock();
        }
    }

    public void removeObserver(Observer<StockEvent> o) {
        lock.lock();
        try {
            observers.remove(o);
        } finally {
            lock.unlock();
        }
    }

    
    public void notifyObservers(T event) {
        List<Observer<T>> observersSnapshot;
    
        // Create a snapshot of observers outside the lock
        lock.lock();
        try {
            observersSnapshot = new ArrayList<>(observers);
        } finally {
            lock.unlock();
        }
    
        // Notify observers without holding the lock
        for (Observer<T> observer : observersSnapshot) {
            observer.update(this, event);
        }
    }
}