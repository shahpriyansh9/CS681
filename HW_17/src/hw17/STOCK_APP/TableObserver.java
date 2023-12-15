package hw17.STOCK_APP;
public class TableObserver implements Observer<StockEvent> {
    @Override
    public void update(Observable<StockEvent> sender, StockEvent event) {
        System.out.println("TableObserver: " + event.ticker() + " quote: " + event.quote());
    }
}