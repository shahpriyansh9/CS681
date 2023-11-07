package hw03;

public class CarPriceResultHolder {
    private int numCarExamined;
    private double totalPrice;

    public CarPriceResultHolder() {
        this.numCarExamined = 0;
        this.totalPrice = 0;
    }

    public void incrementCarCount() {
        this.numCarExamined++;
    }

    public void addPrice(double price) {
        this.totalPrice += price;
    }

    public double getAverage() {
        return numCarExamined > 0 ? totalPrice / numCarExamined : 0;
    }

    public int getNumCarExamined() {
        return numCarExamined;
    }

    public void addTotalPrice(double value) {
        this.totalPrice += value;
    }
}
