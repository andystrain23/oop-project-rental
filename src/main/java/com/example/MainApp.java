package com.example;

import javafx.application.Application;
import javafx.stage.Stage;


public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // hard coding these details and the details
        // themselves are terrible practice but I'm time constrained
        String[] dbDetails = {"jdbc:mysql://localhost:3306/rentals?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=BST&useSSL=false", "root", "password"};

        //instantiate user in scope of both windows
        User user = new User();

        // BUILD MAIN WINDOW UI
        MainWindow mainWindow = new MainWindow(900, 1600, "Rentals");
        mainWindow.buildWindow(primaryStage, user, dbDetails);

        // LOGIN WINDOW
        LoginWindow loginWindow = new LoginWindow(550, 500, "Login");
        loginWindow.buildWindow(primaryStage, user, dbDetails);
    }

    public static void main(String[] args) {
        try {
            System.out.println("loading database driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("jdbc loaded");
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new RuntimeException(classNotFoundException);
        }
        launch(args);
    }
}
