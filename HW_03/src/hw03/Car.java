package hw03;

import java.util.ArrayList;

public class Car 
{
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
    
    public void setDominationCount(ArrayList<Car> cars) 
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
}