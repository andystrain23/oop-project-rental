package com.example;

import com.jfoenix.controls.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Arrays;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //instantiate user in scope of both windows
        User user = new User();

        // BUILD MAIN WINDOW UI
        initMainUI(primaryStage, user);

        // LOGIN WINDOW
        initLoginWindow(primaryStage, user);
    }

    public void initMainUI(Stage primaryStage, User user) {
        //create border pane for layout
        BorderPane border = new BorderPane();

        //create main scene
        Scene scene = new Scene(border, 1600, 900);
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

        Label sidebarTitle = new Label("Rentals");
        sidebarTitle.setId("sidebar-title");
        sidebarTitle.setPadding(new Insets(15));
        leftVBox.getChildren().add(sidebarTitle);

        ArrayList<String> buttonNames = new ArrayList<>(Arrays.asList("Home", "Rentals", "Manage Customers", "Manage Fleet", "Manage Users"));

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
        //3 = manage fleet
        //4 = manage users
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
        buildCustomerManage(panes.get(2));
        buildFleetManage(panes.get(3));
        buildUserManage(panes.get(4), user);

        border.setCenter(mainStackPane);

        for (int i = 0; i < buttons.size(); i++) {
            // to finalise variable for lambda expression
            int index = i;
            // add event handler to all buttons that sets the button active
            // and sets all panes inactive but the clicked pane
            buttons.get(index).setOnAction(event -> {
                setInactive(buttons);
                buttons.get(index).setId("active");
                setPaneInvisible(panes);
                panes.get(index).setVisible(true);
            });
        }

        //finally, show the main window
        primaryStage.show();
    }

    public void initLoginWindow(Stage primaryStage, User user) {
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
        Scene loginWindow = new Scene(loginStackPane, 500, 550);

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
        Label lblUser = new Label("Username:");
        Label lblPass = new Label("Password:");
        JFXTextField inputUser = new JFXTextField();
        JFXPasswordField inputPass = new JFXPasswordField();

        login.setOnAction(event -> {
            //TODO: handle login
            user.login(inputUser.getText(), inputPass.getText());

            System.out.printf("Currently logged in user is %s.", user.getUsername());
            newWindow.close();
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

        //add elements to the grid
        loginLayout.add(imageView, 0, 0, 3, 1);
        loginLayout.add(title, 0, 1, 3, 1);
        loginLayout.add(lblUser, 0, 2, 2, 1);
        loginLayout.add(inputUser, 2, 2);
        loginLayout.add(lblPass, 0, 3, 2, 1);
        loginLayout.add(inputPass, 2, 3);

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
        loginLayout.add(hBoxForgot, 0, 5, 3, 1);

        newWindow.show();
    }

    public void buildHome(GridPane gridPane) {
        gridPane.setId("home-grid");
        Label title = new Label("Welcome to the Rentals app!");
        title.setId("title");
        Label info = new Label("Click on one of the buttons to the side to get started.");

        gridPane.add(title, 0, 0);
        gridPane.add(info, 0, 1);
    }

    public void buildRentalsManage(GridPane gridPane) {
        Label title = new Label("Rentals");
        title.setId("title");

        gridPane.add(title, 0, 0);
        //TODO: do things
    }

    public void buildCustomerManage(GridPane gridPane) {
        Label title = new Label("Customers");
        title.setId("title");

        gridPane.add(title, 0, 0);
        //TODO: do things

    }

    public void buildFleetManage(GridPane gridPane) {
        Label title = new Label("Manage Fleet");
        title.setId("title");

        gridPane.add(title, 0,0);
        //TODO: do things
        //TODO: add print function
    }

    public void buildUserManage(GridPane gridPane, User user) {
        Label title = new Label("Manage User");
        title.setId("title");

        gridPane.add(title, 0,0);
        //TODO: do things depending on connected user

        if (user.isAdmin()) {
            // build admin screen
        } else {
            // build non admin screen
        }
    }

    public void setInactive(ArrayList<JFXButton> buttons) {
        // set all buttons as inactive
        buttons.forEach(button -> button.setId("inactive"));
    }

    public void setPaneInvisible(ArrayList<GridPane> gridPanes) {
        // set all GridPanes as invisible
        gridPanes.forEach(gridPane -> gridPane.setVisible(false));
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
