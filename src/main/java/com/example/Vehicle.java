package com.example;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class Vehicle {
    private SimpleStringProperty numberPlate;
    private SimpleStringProperty type;
    private SimpleStringProperty make;
    private SimpleStringProperty model;
    private SimpleIntegerProperty mileage;
    private SimpleFloatProperty price;
    private SimpleStringProperty available;
    private Date rent_start;
    private Date rent_end;

    Vehicle(String numberPlate, int type, int make, String model, int mileage, float price) {
        this.numberPlate = new SimpleStringProperty(numberPlate);
        switch (type - 1) {
            case 0:
                this.type = new SimpleStringProperty("bike");
                break;
            case 1:
                this.type = new SimpleStringProperty("car");
                break;
            case 2:
                this.type = new SimpleStringProperty("van");
                break;
            default:
                this.type = new SimpleStringProperty("truck");
        }

        switch (make - 1) {
            case 0:
                this.make = new SimpleStringProperty("VW");
                break;
            case 1:
                this.make = new SimpleStringProperty("Vauxhall");
                break;
            case 2:
                this.make = new SimpleStringProperty("Mercedes");
                break;
            case 3:
                this.make = new SimpleStringProperty("Yamaha");
                break;
            case 4:
                this.make = new SimpleStringProperty("Skoda");
                break;
            case 5:
                this.make = new SimpleStringProperty("Volvo");
                break;
            case 6:
                this.make = new SimpleStringProperty("Ducati");
                break;
            default:
                break;

        }
        this.model = new SimpleStringProperty(model);
        this.mileage = new SimpleIntegerProperty(mileage);
        this.price = new SimpleFloatProperty(price);
        this.available = new SimpleStringProperty("No");
    }

    Vehicle(String numberPlate, int type, int make, String model, int mileage, float price, boolean available, Date rent_start, Date rent_end) {
        this.numberPlate = new SimpleStringProperty(numberPlate);
        switch (type) {
            case 0:
                this.type = new SimpleStringProperty("bike");
                break;
            case 1:
                this.type = new SimpleStringProperty("car");
                break;
            case 2:
                this.type = new SimpleStringProperty("van");
            default:
                this.type = new SimpleStringProperty("truck");
        }

        switch (make) {
            case 0:
                this.type = new SimpleStringProperty("VW");
                break;
            case 1:
                this.type = new SimpleStringProperty("Vauxhall");
                break;
            case 2:
                this.type = new SimpleStringProperty("Mercedes");
                break;
            case 3:
                this.type = new SimpleStringProperty("Yamaha");
                break;
            case 4:
                this.type = new SimpleStringProperty("Skoda");
                break;
            case 5:
                this.type = new SimpleStringProperty("Volvo");
                break;
            case 6:
                this.type = new SimpleStringProperty("Ducati");
                break;
            default:
                break;

        }
        this.model = new SimpleStringProperty(model);
        this.mileage = new SimpleIntegerProperty(mileage);
        this.price = new SimpleFloatProperty(price);
        this.available = new SimpleStringProperty("Yes");
        this.rent_start = rent_start;
        this.rent_end = rent_end;
    }

    public String getNumberPlate() {
        return numberPlate.get();
    }

    public SimpleStringProperty numberPlateProperty() {
        return numberPlate;
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public String getMake() {
        return make.get();
    }

    public SimpleStringProperty makeProperty() {
        return make;
    }

    public String getModel() {
        return model.get();
    }

    public SimpleStringProperty modelProperty() {
        return model;
    }

    public int getMileage() {
        return mileage.get();
    }

    public SimpleIntegerProperty mileageProperty() {
        return mileage;
    }

    public float getPrice() {
        return price.get();
    }

    public SimpleFloatProperty priceProperty() {
        return price;
    }

    public String getAvailable() {
        return available.get();
    }

    public SimpleStringProperty availableProperty() {
        return available;
    }

    public Date getRent_start() {
        return rent_start;
    }

    public Date getRent_end() {
        return rent_end;
    }
}
