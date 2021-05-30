package com.example;

import java.sql.Date;

public class Vehicle {
    private String numberPlate;
    private String type;
    private String make;
    private String model;
    private int mileage;
    private float price;
    private boolean available;
    private Date rent_start;
    private Date rent_end;

    Vehicle(String numberPlate, String type, String make, String model, int mileage, float price) {
        this.numberPlate = numberPlate;
        this.type = type;
        this.make = make;
        this.model = model;
        this.mileage = mileage;
        this.price = price;
        this.available = true;
    }

    Vehicle(String numberPlate, String type, String make, String model, int mileage, float price, boolean available, Date rent_start, Date rent_end) {
        this.numberPlate = numberPlate;
        this.type = type;
        this.make = make;
        this.model = model;
        this.mileage = mileage;
        this.price = price;
        this.available = false;
        this.rent_start = rent_start;
        this.rent_end = rent_end;
    }

    public void rentVehicle() {

    }
}
