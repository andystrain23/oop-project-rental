package com.example;

public class Window {
    private int height;
    private int width;
    private boolean modal;
    private String title;

    public Window(int height, int width, String title, boolean modality) {
        this.height = height;
        this.width = width;
        this.modal = modality;
        this.title = title;
    }
}
