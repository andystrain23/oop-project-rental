package com.example;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;
import java.time.LocalDate;

public class Vehicle {
    private SimpleStringProperty numberPlate;
    private SimpleStringProperty type;
    private SimpleStringProperty make;
    private SimpleStringProperty model;
    private SimpleIntegerProperty mileage;
    private SimpleFloatProperty price;
    private SimpleStringProperty available;
    private Date rentStart = null;
    private Date rentEnd = null;

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
        this.available = new SimpleStringProperty("Yes");
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
                break;
            default:
                this.type = new SimpleStringProperty("truck");
                break;
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
        this.rentStart = rent_start;
        this.rentEnd = rent_end;
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

    public Date getRentStart() {
        return rentStart;
    }

    public Date getRentEnd() {
        return rentEnd;
    }

    public String[] getAllProperties() {
        String startDate = "";
        String endDate = "";

        if (available.get() == "No" && rentStart.toString() != null) {
            startDate = rentStart.toString();
            endDate = rentEnd.toString();
        } else {
            startDate = "";
            endDate = "";
        }
        return new String[]{
                numberPlate.get(),
                type.get(),
                make.get(),
                model.get(),
                String.valueOf(mileage.get()),
                String.valueOf(price.get()),
                available.get(),
                startDate,
                endDate
        };
    }

    public void loanVehicle(String[] dbDetails, LocalDate start, LocalDate end) {
        this.rentStart = Date.valueOf(start);
        this.rentEnd = Date.valueOf(end);
        this.available.set("No");

        Connection connection = null;
        Statement statement;

        String query = String.format("UPDATE vehicles SET available = 2, start = \"%s\", end = \"%s\";", this.rentStart, this.rentEnd);

        try {
            connection = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqlException) {
                    System.out.println(sqlException);
                }
            }
        }
    }

    public void returnVehicle(String[] dbDetails) {
        this.rentStart = null;
        this.rentEnd = null;
        this.available.set("Yes");

        Connection connection = null;
        Statement statement;

        String query = String.format("UPDATE vehicles SET available = 1, start = null, end = null;");

        try {
            connection = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqlException) {
                    System.out.println(sqlException);
                }
            }
        }
    }
}
