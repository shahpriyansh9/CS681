package hw17.STOCK_APP;

public class ThreeDObserver implements Observer<StockEvent> {
    @Override
    public void update(Observable<StockEvent> sender, StockEvent event) {
        System.out.println("3D View: Stock " + event.ticker() + " changed to " + event.quote());
    }
}

