package hw14.STOCK_APP;

public interface Observer<T> {
    void update(Observable<T> sender, T event);
}