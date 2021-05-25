package com.example;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.*;

public class LoginWindow extends Window {

    public LoginWindow(int height, int width, String title) {
        this.setHeight(height);
        this.setWidth(width);
        this.setTitle(title);
    }

    @Override
    public void buildWindow(Stage primaryStage, User user, String[] dbDetails) {
        Stage newWindow = new Stage();

        //specifies login window as modal
        //so that login must occur before interacting with program
        newWindow.initModality(Modality.WINDOW_MODAL);
        //set window owner
        newWindow.initOwner(primaryStage);
        //make login window appear with no buttons
        newWindow.initStyle(StageStyle.UTILITY);


        //create scene for login window and assign stylesheet
        StackPane loginStackPane = new StackPane();
        GridPane loginLayout = new GridPane();
        loginLayout.setHgap(10);
        loginLayout.setVgap(10);
        loginLayout.setPadding(new Insets(25));
        loginLayout.setAlignment(Pos.CENTER);
        loginStackPane.getChildren().add(loginLayout);
        Scene loginWindow = new Scene(loginStackPane, this.getWidth(), this.getHeight());

        //load login.css
        loginWindow.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
        newWindow.setTitle("Login");

        //assign new scene to new window
        newWindow.setScene(loginWindow);

        //if the user closes the login window without logging in, close the entire app
        newWindow.setOnCloseRequest(event -> Platform.exit());

        //elements
        //load an image
        Image image = new Image("com/example/car.png", 200, 200, true, false);
        //and put it into an imageview for display
        ImageView imageView = new ImageView();
        imageView.setImage(image);

        Label title = new Label("Enter your details");
        title.setId("title");
        JFXButton login = new JFXButton("Login");
        login.setId("btnLogin");
        JFXButton cancel = new JFXButton("Cancel");
        cancel.setId("btnCancel");
        JFXButton forgot = new JFXButton("Forgot your password");
        forgot.setId("btnForgot");
        JFXButton register = new JFXButton("Register");
        register.setId("btnRegister");
        Label lblUser = new Label("Username:");
        Label lblPass = new Label("Password:");
        JFXTextField inputUser = new JFXTextField();
        JFXPasswordField inputPass = new JFXPasswordField();
        Label wrongDetails = new Label("Your username or password is incorrect. \nPlease try again or see your administrator.");
        wrongDetails.setId("password-notify");
        wrongDetails.setVisible(false);

        login.setOnAction(event -> {
            //TODO: handle login
            String inpUsername = inputUser.getText();
            String inpPassword = inputPass.getText();

            Connection connection = null;
            Statement statement;
            try {
                connection = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
                System.out.println("Connected!");
                // TODO: change after database change to add more user details
                String query = String.format("SELECT password, permissions, created_date FROM accounts WHERE username = '%s'", inpUsername);
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // VERY BAD
                if (resultSet.next()) {
                    String dbPass = resultSet.getString(1);
                    int dbPerm = resultSet.getInt(2);
                    Date dbCreation = resultSet.getDate(3);

                    System.out.println(dbPass);
                    System.out.println(dbPerm);

                    if (dbPass.equals(inpPassword)) {
                        user.login(inpUsername, dbPerm, dbCreation);
                        System.out.println(user.getUsername() + user.getCreationDate() + user.isAdmin());
                        newWindow.close();
                    } else {
                        wrongDetails.setVisible(true);
                    }
                }
            } catch (SQLException sqlException) {
                System.out.println(sqlException);
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException sqlException) {
                        System.out.println(sqlException);
                    }
                }
            }
        });

        //since the user doesn't want to log in they can't access the app, so close it
        cancel.setOnAction(event -> Platform.exit());

        //display a dialogue with the
        forgot.setOnAction(event -> {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Forgotten password"));
            content.setBody(new Text("Please contact your manager or the\nadministrator to have your password reset."));
            JFXDialog dialog = new JFXDialog(loginStackPane, content, JFXDialog.DialogTransition.CENTER);
            dialog.show();
        });

        register.setOnAction(event -> buildRegistration(newWindow));

        //add elements to the grid
        loginLayout.add(imageView, 0, 0, 3, 1);
        loginLayout.add(title, 0, 1, 3, 1);
        loginLayout.add(lblUser, 0, 2, 2, 1);
        loginLayout.add(inputUser, 2, 2);
        loginLayout.add(lblPass, 0, 3, 2, 1);
        loginLayout.add(inputPass, 2, 3);
        loginLayout.add(wrongDetails, 0, 6, 3, 1);

        // create HBoxes for login, cancel and forgot buttons and add to grid
        HBox hBoxLogin = new HBox(10);
        hBoxLogin.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxLogin.getChildren().add(login);
        loginLayout.add(hBoxLogin, 2, 4);

        HBox hBoxCancel = new HBox(10);
        hBoxCancel.setAlignment(Pos.BOTTOM_LEFT);
        hBoxCancel.getChildren().add(cancel);
        loginLayout.add(hBoxCancel, 0, 4);

        HBox hBoxForgot = new HBox(10);
        hBoxForgot.setAlignment(Pos.BOTTOM_LEFT);
        hBoxForgot.getChildren().add(forgot);
        loginLayout.add(hBoxForgot, 0, 5, 2, 1);

        HBox hBoxRegister = new HBox(10);
        hBoxRegister.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxRegister.getChildren().add(register);
        loginLayout.add(hBoxRegister, 2, 5);

        newWindow.show();
    }

    private void buildRegistration(Stage newWindow) {
        //build registration form
        Stage registrationWindow = new Stage();

        registrationWindow.initOwner(newWindow);
        registrationWindow.setTitle("Register");

        StackPane registrationSP = new StackPane();

        Scene registrationScene = new Scene(registrationSP);
        registrationWindow.setScene(registrationScene);

        Label hello = new Label("Hello");
        registrationSP.getChildren().add(hello);

        registrationWindow.show();
    }
}
