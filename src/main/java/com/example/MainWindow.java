package com.example;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        panes.get(0).setVisible(true);

        buildHome(panes.get(0));
        buildRentalsManage(panes.get(1), dbDetails, primaryStage);

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

    private void buildRentalsManage(GridPane gridPane, String[] dbDetails, Stage primaryStage) {
        Label title = new Label("Rentals");
        title.setId("title");

        gridPane.add(title, 0, 0);

        // table must include
        // numplate, make, type, model, mileage, price, available
        TableView<Vehicle> tableView = new TableView<>();
        tableView.setMinWidth(1000);
        tableView.setMinHeight(700);
        tableView.setPadding(new Insets(10));

        TableColumn<Vehicle, String> numPlateCol = new TableColumn<>("Numplate");
        numPlateCol.setCellValueFactory(new PropertyValueFactory<>("numberPlate"));

        TableColumn<Vehicle, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Vehicle, String> makeCol = new TableColumn<>("Make");
        makeCol.setCellValueFactory(new PropertyValueFactory<>("make"));

        TableColumn<Vehicle, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Vehicle, Integer> mileageCol = new TableColumn<>("Mileage");
        mileageCol.setCellValueFactory(new PropertyValueFactory<>("mileage"));

        TableColumn<Vehicle, Float> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Vehicle, String> availableCol = new TableColumn<>("Available");
        availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));

        tableView.getColumns().addAll(numPlateCol, typeCol, makeCol, modelCol, mileageCol, priceCol, availableCol);

        ObservableList<Vehicle> data = fetchVehicles(dbDetails);

        // make new filtered list and initially show all entries
        FilteredList<Vehicle> filteredData = new FilteredList<>(data, p -> true);

        tableView.setItems(filteredData);

        HBox hBoxView = new HBox();
        hBoxView.setAlignment(Pos.BOTTOM_LEFT);
        JFXButton btnView = new JFXButton("View");
        hBoxView.getChildren().add(btnView);

        String[] fields = {"Number Plate", "Type", "Make", "Model", "Mileage", "Price", "Available", "Start", "End"};

        btnView.setOnAction(e -> {
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();

            Vehicle selectedVehicle = data.get(selectedIndex);
            String[] vehicleProps = selectedVehicle.getAllProperties();

            Stage viewWindow = new Stage();
            viewWindow.initOwner(primaryStage);
            viewWindow.initModality(Modality.WINDOW_MODAL);

            VBox viewWindowVBox = new VBox();
            viewWindowVBox.setAlignment(Pos.CENTER);
            Scene viewWindowScene = new Scene(viewWindowVBox, 500, 500);
            viewWindow.setScene(viewWindowScene);

            Label viewTitle = new Label("View");
            viewTitle.setStyle("-fx-font-size: 20pt;");

            ImageView imageView = new ImageView();
            Image image;
            switch (selectedVehicle.getType()) {
                case "bike":
                    image = new Image("com/example/bike.jpg", 200, 200, true, false);
                    break;
                case "car":
                    image = new Image("com/example/car.jpg", 200, 200, true, false);
                    break;
                case "van":
                    image = new Image("com/example/van.png", 200, 200, true, false);
                    break;
                case "truck":
                    image = new Image("com/example/truck.jpg", 200, 200, true, false);
                    break;
                default:
                    image = new Image("com/example/logo.png", 200, 200, true, false);
                    break;
            }
            imageView.setImage(image);

            List<Label> viewLabels = new ArrayList<>();

            for (int i = 0; i < fields.length; i++) {
                viewLabels.add(new Label(fields[i] + ": " + vehicleProps[i]));
            }

            JFXButton print = new JFXButton("Print");

            print.setOnAction(event -> {
                try {
                    File file = new File("info.txt");
                    if (file.createNewFile()) {
                        writeToFile(file, vehicleProps, fields);
                    } else if (file.delete()) {
                        try {
                            file.createNewFile();
                        } catch (IOException ioException) {
                            System.out.println(ioException);
                        }
                        writeToFile(file, vehicleProps, fields);
                    }
                } catch (IOException ioException) {
                    System.out.println("An error occurred:" + ioException);
                }
            });

            viewWindowVBox.getChildren().add(viewTitle);
            viewWindowVBox.getChildren().add(imageView);
            viewLabels.forEach(viewLabel -> viewWindowVBox.getChildren().add(viewLabel));

            HBox printHBox = new HBox();
            printHBox.setAlignment(Pos.BOTTOM_CENTER);
            printHBox.setPadding(new Insets(10));
            printHBox.getChildren().add(print);
            viewWindowVBox.getChildren().add(printHBox);

            viewWindow.show();
        });



        HBox hBoxLoan = new HBox();
        hBoxLoan.setAlignment(Pos.BOTTOM_RIGHT);

        JFXTextField filterData = new JFXTextField();
        filterData.setLabelFloat(true);
        filterData.setPromptText("Filter text");
        //add listener to text box to look for change
        filterData.textProperty().addListener((observable, oldValue, newValue) -> {
            //when text box changes, set new filter criteria
            filteredData.setPredicate(vehicle -> {
                //if text box is empty, include everything
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                //convert to lower for comparison
                String lowerCase = newValue.toLowerCase();

                //compare input to number plate, type, make and model fields and return matches
                if (vehicle.getNumberPlate().toLowerCase().contains(lowerCase)) {
                    return true;
                } else if (vehicle.getType().toLowerCase().contains(lowerCase)) {
                    return true;
                } else if (vehicle.getMake().toLowerCase().contains(lowerCase)) {
                    return true;
                } else if (vehicle.getModel().toLowerCase().contains(lowerCase)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        hBoxLoan.getChildren().add(filterData);
        JFXButton btnReturn = new JFXButton("Return");
        hBoxLoan.getChildren().add(btnReturn);
        btnReturn.setOnAction(e -> {
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();

            Vehicle selectedVehicle = data.get(selectedIndex);

            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Return " + selectedVehicle.getMake() + " " + selectedVehicle.getModel()+ "? Num plate: " + selectedVehicle.getNumberPlate(),
                    ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                selectedVehicle.returnVehicle(dbDetails);
            }
        });

        JFXButton btnLoan = new JFXButton("Loan");
        hBoxLoan.getChildren().add(btnLoan);
        btnLoan.setOnAction(e -> {
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();

            Vehicle selectedVehicle = data.get(selectedIndex);

            Stage loanWindow = new Stage();
            loanWindow.initOwner(primaryStage);
            loanWindow.initModality(Modality.WINDOW_MODAL);

            GridPane loanGP = new GridPane();
            loanGP.setAlignment(Pos.CENTER);
            Scene loanWindowScene = new Scene(loanGP, 500, 500);
            loanWindow.setScene(loanWindowScene);
            try {
                loanWindowScene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
            } catch (NullPointerException nullPointerException) {
                System.out.println(nullPointerException);
            }

            Label loanTitle = new Label("Loan Vehicle");
            loanGP.add(loanTitle, 0,0,2,1);

            Label start = new Label("Start date:");
            DatePicker startPicker = new DatePicker(LocalDate.now());
            Label end = new Label("End");
            DatePicker endPicker = new DatePicker(LocalDate.now());
            JFXButton submit = new JFXButton("Submit");
            submit.setOnAction(event -> {
                LocalDate pickedStart = startPicker.getValue();
                LocalDate pickedEnd = endPicker.getValue();

                selectedVehicle.loanVehicle(dbDetails, pickedStart, pickedEnd);
                loanWindow.close();
            });

            loanGP.addColumn(0, start, end);
            loanGP.addColumn(1, startPicker, endPicker);
            loanGP.add(submit, 0, 3, 2, 1);

            loanWindow.show();
        });

        gridPane.add(hBoxView, 0, 2);
        gridPane.add(hBoxLoan, 1, 2);

        gridPane.add(tableView, 0, 1, 2, 1);
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

//        HBox updateHBox = new HBox();
//        updateHBox.setPadding(new Insets(10, 0, 10, 0));
//        updateHBox.setAlignment(Pos.BOTTOM_RIGHT);
//        JFXButton btnUpdate = new JFXButton("Update");
//        updateHBox.getChildren().add(btnUpdate);

        Label disclaimer = new Label("This software is proof of concept and does not implement proper security,\npasswords are stored in plain text. DO NOT USE REGULAR PASSWORDS.");
        disclaimer.setId("disclaimer");

//        gridPane.add(updateHBox, 1,8);
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

    private ObservableList<Vehicle> fetchVehicles(String[] dbDetails) {
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
        Connection connection = null;
        Statement statement;
        String vehicleQuery = "SELECT * FROM vehicles;";

        try {
            connection = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            statement = connection.createStatement();


            ResultSet resultSet = statement.executeQuery(vehicleQuery);

            while (resultSet.next()) {
                boolean available = resultSet.getInt("available") == 1;

                String numPlate = resultSet.getString("number_plate");
                int type = resultSet.getInt("type");
                int make = resultSet.getInt("make");
                String model = resultSet.getString("model");
                int mileage = resultSet.getInt("mileage");
                float price = resultSet.getFloat("price");

                if (available) {
                    vehicles.add(new Vehicle(numPlate, type, make, model, mileage, price));
                } else {
                    Date start = resultSet.getDate("start");
                    Date end = resultSet.getDate("end");

                    vehicles.add(new Vehicle(numPlate, type, make, model, mileage, price, false, start, end));
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
            sqlException.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqlException) {
                    System.out.println(sqlException);
                }
            }
        }
        return vehicles;
    }

    private void writeToFile(File file, String[] vehicleProps, String[] fields) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("Vehicle:\n\n");

            for (int i = 0; i < fields.length; i++) {
                String constructor = fields[i] + ": " + vehicleProps[i] + "\n";
                fileWriter.write(constructor);
            }
            fileWriter.close();
        } catch (IOException ioException) {
            System.out.println(ioException);
        }

    }
}
