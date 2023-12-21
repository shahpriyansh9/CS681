package hw14.STOCK_APP;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class StockQuoteObservable extends Observable<StockEvent> {
    private final Map<String, Double> tickerMap = new HashMap<>();
    private final Lock tickerMapLock = new ReentrantLock();

    public void changeQuote(String ticker, double quote) {
        tickerMapLock.lock();
        try {
            tickerMap.put(ticker, quote);
        } finally {
            tickerMapLock.unlock();
        }
        notifyObservers(new StockEvent(ticker, quote));
    }

    public Map<String, Double> getTickerMap() {
        tickerMapLock.lock();
        try {
            return new HashMap<>(tickerMap); 
        } finally {
            tickerMapLock.unlock();
        }
    }


    
}
