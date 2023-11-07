package hw03;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // A list of Car objects
        List<Car> cars = new ArrayList<>();
        cars.add(new Car("Toyota", "Camry", 2020, 25000, 15000));
        cars.add(new Car("Ford", "Figo", 2017, 23000, 25000));
        cars.add(new Car("Honda", "City", 2018, 21000, 30000));
        cars.add(new Car("Hyundai", "Kona", 2019, 27000, 35000));
        cars.add(new Car("Mazda", "RX", 2016, 24000, 20000));
        // ... add more cars ...

        // Average car price using map-reduce 
        double averagePrice = cars.stream()
        .map(car -> car.getPrice())
        .reduce(new CarPriceResultHolder(), 
                (result, price) -> {
                    result.incrementCarCount();
                    result.addPrice(price);
                    return result;
                }, 
                (finalResult, intermediateResult) -> {
                    for (int i = 0; i < intermediateResult.getNumCarExamined(); i++) {
                        finalResult.incrementCarCount();
                    }
                    finalResult.addTotalPrice(intermediateResult.getAverage() * intermediateResult.getNumCarExamined());
                    return finalResult;
                })
        .getAverage();

        System.out.println("Average car price: " + averagePrice);
    }
}
