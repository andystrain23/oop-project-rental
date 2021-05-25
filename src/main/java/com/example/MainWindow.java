package com.example;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class MainWindow extends Window {


    public MainWindow(int height, int width, String title) {
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
        buildUserManage(panes.get(2), user);

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

    private void buildHome(GridPane gridPane) {
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

    private void setInactive(ArrayList<JFXButton> buttons) {
        // set all buttons as inactive
        buttons.forEach(button -> button.setId("inactive"));
    }

    private void setPaneInvisible(ArrayList<GridPane> gridPanes) {
        // set all GridPanes as invisible
        gridPanes.forEach(gridPane -> gridPane.setVisible(false));
    }
}
