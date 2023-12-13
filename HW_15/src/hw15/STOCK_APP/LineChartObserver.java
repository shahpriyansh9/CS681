package hw15.STOCK_APP;

public class LineChartObserver implements Observer<StockEvent> {
    @Override
    public void update(Observable<StockEvent> sender, StockEvent event) {
        System.out.println("Line Chart Update: " + event.ticker() + " is now " + event.quote());
    }
}
