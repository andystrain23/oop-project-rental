package com.example;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MainWindow extends Window {


    MainWindow(int height, int width, String title) {
        this.setHeight(height);
        this.setWidth(width);
        this.setTitle(title);
    }

    @Override
    public void buildWindow(Stage primaryStage, User user, String[] dbDetails) {
        //create border pane for layout
        BorderPane border = new BorderPane();

        //create main scene
        Scene scene = new Scene(border, this.getWidth(), this.getHeight());
        primaryStage.setScene(scene);

        //set stylesheet for main window
        try {
            scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.out.println(e);
        }
        primaryStage.setTitle("UoWRentals");

        //left pane
        VBox leftVBox = new VBox();
        leftVBox.setMinHeight(100);
        leftVBox.setSpacing(8);
        leftVBox.setMinWidth(200);
        leftVBox.setId("sidebar");

        Label sidebarTitle = new Label(this.getTitle());
        sidebarTitle.setId("sidebar-title");
        sidebarTitle.setPadding(new Insets(15));
        leftVBox.getChildren().add(sidebarTitle);

        ArrayList<String> buttonNames = new ArrayList<>(Arrays.asList("Home", "Rentals", "Manage Users"));

        ArrayList<JFXButton> buttons = new ArrayList<>();
        // add new buttons for all button names (for easy extension)
        buttonNames.forEach(buttonName -> buttons.add(new JFXButton(buttonName)));
        buttons.get(0).setId("active");

        buttons.forEach(button -> leftVBox.getChildren().add(button));

        border.setLeft(leftVBox);

        //centre pane
        //do the centre pane here
        StackPane mainStackPane = new StackPane();
        //create arraylist of grid panes
        ArrayList<GridPane> panes = new ArrayList<>();
        //add 3 grid panes
        //1 = home
        //2 = manage rentals
        //3 = manage users
        for (int i=0; i< buttons.size(); i++) {
            panes.add(new GridPane());
        }
        // for each of the panes, set invisible (so only active shows),
        // set a style class and add to the main stack pane
        panes.forEach(pane -> {
            pane.setVisible(false);
            pane.getStyleClass().add("grid-pane");
            mainStackPane.getChildren().add(pane);
        });
        //TODO: remove when login logic added, move there
        panes.get(0).setVisible(true);

        buildHome(panes.get(0));
        buildRentalsManage(panes.get(1));

        border.setCenter(mainStackPane);

        for (int i = 0; i < buttons.size(); i++) {
            // to finalise variable for lambda expression
            int index = i;
            // add event handler to all buttons that sets the button active
            // and sets all panes inactive but the clicked pane
            buttons.get(index).setOnAction(event -> {
                // set all buttons inactive
                setInactive(buttons);

                //set currently clicked button to active
                buttons.get(index).setId("active");

                //set all panes invisible
                setPaneInvisible(panes);

                //this ugliness resets content of the user
                //management window every time it is clicked to prevent wrong info
                if (panes.get(index) == panes.get(2)) {
                    panes.get(index).getChildren().clear();
                    buildUserManage(panes.get(index), user, dbDetails);
                }
                //set pane of clicked button visible
                panes.get(index).setVisible(true);
            });
        }

        //finally, show the main window
        primaryStage.show();
    }

    private void buildHome(GridPane gridPane) {
        gridPane.setId("home-grid");
        Label title = new Label("Welcome to the Rentals app!");
        title.setId("title");
        Label info = new Label("Click on one of the buttons to the side to get started.");

        gridPane.add(title, 0, 0);
        gridPane.add(info, 0, 1);
    }

    private void buildRentalsManage(GridPane gridPane) {
        Label title = new Label("Rentals");
        title.setId("title");

        gridPane.add(title, 0, 0);

        //TODO: do things

        // table must include
        // numplate, make, type, model, mileage, price, available, start date, end date
    }

    public void buildUserManage(GridPane gridPane, User user, String[] dbDetails) {
        Label title = new Label("Manage User");
        title.setId("title");

        gridPane.add(title, 0,0);

        Label lblSubtitle = new Label("Current User:");
        Label lblName = new Label("Name");
        Label lblPassword = new Label("Password");
        Label lblAddress = new Label("Address");
        Label lblPhone = new Label("Phone");
        Label lblEmail = new Label("Email");
        Label lblCreatedDate = new Label("Created date");

        gridPane.add(lblSubtitle, 0, 1);
        gridPane.add(lblName, 0, 2);
        gridPane.add(lblPassword, 0, 3);
        gridPane.add(lblAddress, 0, 4);
        gridPane.add(lblPhone, 0, 5);
        gridPane.add(lblEmail, 0, 6);
        gridPane.add(lblCreatedDate, 0, 7);

        JFXTextField inpName = new JFXTextField();
        inpName.setText(user.getUsername());

        JFXPasswordField inpPass = new JFXPasswordField();

        JFXTextArea inpAddress = new JFXTextArea();
        inpAddress.setText(user.getAddress());

        JFXTextField inpPhone = new JFXTextField();
        inpPhone.setText(user.getPhone());

        JFXTextField inpEmail = new JFXTextField();
        inpEmail.setText(user.getEmail());

        JFXTextField createdDate = new JFXTextField();
        Date userCreated = user.getCreationDate();
        createdDate.setText(userCreated.toString());
        createdDate.setDisable(true);

        gridPane.add(inpName, 1, 2);
        gridPane.add(inpPass, 1, 3);
        gridPane.add(inpAddress, 1, 4);
        gridPane.add(inpPhone, 1, 5);
        gridPane.add(inpEmail, 1, 6);
        gridPane.add(createdDate, 1, 7);

        HBox updateHBox = new HBox();
        updateHBox.setPadding(new Insets(10, 0, 10, 0));
        updateHBox.setAlignment(Pos.BOTTOM_RIGHT);
        JFXButton btnUpdate = new JFXButton("Update");
        updateHBox.getChildren().add(btnUpdate);

        //TODO: implement update functionality

        Label disclaimer = new Label("This software is proof of concept and does not implement proper security,\npasswords are stored in plain text. DO NOT USE REGULAR PASSWORDS.");
        disclaimer.setId("disclaimer");

        gridPane.add(updateHBox, 1,8);
        gridPane.add(disclaimer, 0, 10, 2, 1);


        if (user.isAdmin()) {
            // build admin screen
            // list view
            HBox adminHBox = new HBox();
            Separator separator = new Separator();
            separator.setOrientation(Orientation.VERTICAL);
            separator.setPadding(new Insets(0, 10, 0, 10));

            adminHBox.getChildren().add(separator);

            VBox vBox = new VBox();
            Label pending = new Label("Pending Accounts:");
            vBox.getChildren().add(pending);

            JFXListView<String> listView = new JFXListView<>();
            listView.setMinHeight(600);
            ObservableList<String> observableList = fetchPending(dbDetails);
            listView.setItems(observableList);
            vBox.getChildren().add(listView);

            JFXButton btnDelete = new JFXButton("Delete");
            JFXButton btnApprove = new JFXButton("Approve");

            HBox buttonBox = new HBox();
            buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
            buttonBox.getChildren().add(btnDelete);
            buttonBox.getChildren().add(btnApprove);

            vBox.getChildren().add(buttonBox);

            adminHBox.getChildren().add(vBox);

            btnDelete.setOnAction(e -> {
                int selectedIndex = listView.getSelectionModel().getSelectedIndex();
                String selectedUser = observableList.get(selectedIndex);
                String query = String.format("DELETE FROM accounts WHERE username = \"%s\";", selectedUser);

                updatePending(dbDetails, observableList, selectedIndex, query);
            });

            btnApprove.setOnAction(e -> {
                int selectedIndex = listView.getSelectionModel().getSelectedIndex();
                String selectedUser = observableList.get(selectedIndex);
                String query = String.format("UPDATE accounts SET pending = 1 WHERE username = \"%s\";", selectedUser);

                updatePending(dbDetails, observableList, selectedIndex, query);
            });

            gridPane.add(adminHBox, 3, 1, 1, 10);
        }
    }

    private void updatePending(String[] dbDetails, ObservableList<String> observableList, int selectedIndex, String query) {
        Connection connection = null;
        Statement statement;
        try {
            connection = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            statement = connection.createStatement();
            statement.executeUpdate(query);
            observableList.remove(selectedIndex);
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
    }

    private void setInactive(ArrayList<JFXButton> buttons) {
        // set all buttons as inactive
        buttons.forEach(button -> button.setId("inactive"));
    }

    private void setPaneInvisible(ArrayList<GridPane> gridPanes) {
        // set all GridPanes as invisible
        gridPanes.forEach(gridPane -> gridPane.setVisible(false));
    }

    private ObservableList<String> fetchPending(String[] dbDetails) {
        ObservableList<String> pendingAccs = FXCollections.observableArrayList();

        Connection connection = null;
        Statement statement;

        try {
            connection = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            String query = "SELECT username FROM accounts WHERE approved = 2";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                pendingAccs.add(resultSet.getString("USERNAME"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }

        return pendingAccs;
    }
}
