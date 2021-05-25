package com.example;

import javafx.stage.Stage;

public abstract class Window {
    private int height;
    private int width;
    private String title;

    public abstract void buildWindow(Stage primaryStage, User user, String[] dbDetails);

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getTitle() {
        return title;
    }
}
