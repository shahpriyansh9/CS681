package hw01;

import java.util.*;
import java.util.stream.Collectors;

public class Car {
    private String make;
    private String model;
    private int year;
    private float price;
    private int DomCount;
    private int mileage;

    public Car(String make, String model, int year, float price, int mileage) 
    {
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.mileage = mileage;
    }
    
    public String getMake() 
    {
        return make;
    }
    
    public String getModel() 
    {
        return model;
    }
    
    public int getYear() 
    {
        return year;
    }
    
    public float getPrice() 
    {
        return price;
    }
    
    public int getDominationCount() 
    {
        return DomCount;
    }
    
    public int getMileage() 
    {
        return mileage;
    }
    
    public void setDominationCount(LinkedList<Car> cars) 
    {
        this.DomCount = 0;
        for (Car car : cars) 
        {
            if (car.getPrice() <= this.getPrice() && car.getMileage() <= this.getMileage() && car.getYear() >= this.getYear()) 
            {
                if (car.getPrice() < this.getPrice() || car.getMileage() < this.getMileage() || car.getYear() > this.getYear()) 
                {
                    DomCount++;
                }
            }
        }
    }

    public static void main(String[] args) {
        List<Car> cars = new LinkedList<>();
        cars.add(new Car("Toyota", "RAV4", 2019, 25000, 15000));
        cars.add(new Car("Toyota", "Corolla", 2018, 24000, 14000));
        cars.add(new Car("FORD", "Eco", 2010, 21000, 11000));
        cars.add(new Car("Hyundai", "Kona", 2015, 20000, 17000));
        cars.add(new Car("Honda", "Civic", 2013, 19000, 10000));
        cars.add(new Car("Honda", "CRV", 2017, 22000, 12000));

        cars.forEach(car -> car.setDominationCount(new LinkedList<>(cars)));

        final int mileageThreshold = 14000;
        final float priceThreshold = 24000;
        final int yearThreshold = 2018;
        final int dominationCountThreshold = 2;

        // Grouping cars based on the thresholds using Streams
        Map<Boolean, List<Car>> groupedByMileage = cars.stream()
            .collect(Collectors.partitioningBy(car -> car.getMileage() >= mileageThreshold));

        Map<Boolean, List<Car>> groupedByPrice = cars.stream()
            .collect(Collectors.partitioningBy(car -> car.getPrice() >= priceThreshold));

        Map<Boolean, List<Car>> groupedByYear = cars.stream()
            .collect(Collectors.partitioningBy(car -> car.getYear() >= yearThreshold));

        Map<Boolean, List<Car>> groupedByDominationCount = cars.stream()
            .collect(Collectors.partitioningBy(car -> car.getDominationCount() >= dominationCountThreshold));

        // Sort and print for each category
        sortAndPrint("Mileage", groupedByMileage);
        sortAndPrint("Price", groupedByPrice);
        sortAndPrint("Year", groupedByYear);
        sortAndPrint("Domination Count", groupedByDominationCount);

        // Calculate and print statistics for each category
        calculateAndPrintStatistics("Mileage", groupedByMileage);
        calculateAndPrintStatistics("Price", groupedByPrice);
        calculateAndPrintStatistics("Year", groupedByYear);
        calculateAndPrintStatistics("Domination Count", groupedByDominationCount);

        // Count and print group sizes for each category
        countAndPrintGroupSize("Mileage", groupedByMileage);
        countAndPrintGroupSize("Price", groupedByPrice);
        countAndPrintGroupSize("Year", groupedByYear);
        countAndPrintGroupSize("Domination Count", groupedByDominationCount);
    }

    private static void sortAndPrint(String sortingPolicy, Map<Boolean, List<Car>> groupedCars) {
        Comparator<Car> comparator = Comparator.comparingInt(car -> getSortingValue(car, sortingPolicy));

        groupedCars.forEach((isHigh, cars) -> {
            System.out.println("Cars sorted by " + sortingPolicy + " (" + (isHigh ? "HIGH" : "LOW") + "):");
            cars.stream()
                .sorted(comparator)
                .forEach(car -> System.out.println("Make:" + car.getMake() + " Model:" + car.getModel() + " Year:" + car.getYear() + " Price:" + car.getPrice() + " Mileage:" + car.getMileage()));
            System.out.println("==================X================");
        });
    }

    private static void calculateAndPrintStatistics(String sortingPolicy, Map<Boolean, List<Car>> groupedCars) {
        groupedCars.forEach((isHigh, cars) -> {
            if (!cars.isEmpty()) {
                IntSummaryStatistics stats = cars.stream()
                                                 .mapToInt(car -> getSortingValue(car, sortingPolicy))
                                                 .summaryStatistics();

                System.out.println(sortingPolicy + " Statistics for " + (isHigh ? "HIGH" : "LOW") + " group:");
                System.out.println("Highest: " + stats.getMax());
                System.out.println("Lowest: " + stats.getMin());
                System.out.println("Average: " + stats.getAverage());
                System.out.println("==================X================");
            }
        });
    }

    private static void countAndPrintGroupSize(String sortingPolicy, Map<Boolean, List<Car>> groupedCars) {
        groupedCars.forEach((isHigh, cars) -> 
            System.out.println("Number of " + (isHigh ? "HIGH" : "LOW") + " " + sortingPolicy + " Cars: " + cars.size())
        );
    }

    private static int getSortingValue(Car car, String sortingPolicy) {
          switch (sortingPolicy) 
        {
            case "Mileage":
                return car.getMileage();
            case "Price":
                return (int) car.getPrice();
            case "Year":
                return car.getYear();
            case "Domination Count":
                return car.getDominationCount();
            default:
                return 0;
        }
    }
}
