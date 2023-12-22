package hw17.STOCK_APP;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.HashMap;


public class StockQuoteObservable extends Observable<StockEvent> {
    private final Map<String, Double> tickerMap = new ConcurrentHashMap<>();

    public void changeQuote(String ticker, double quote) {
        tickerMap.put(ticker, quote);
        notifyObservers(new StockEvent(ticker, quote));
    }

    public Map<String, Double> getTickerMap() {
        return new HashMap<>(tickerMap); 
    }
}
