package com.gameengine.game.gui;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CustomTextField extends TextField {
    private final String FONT_PATH = "/GUI/8bitlim.ttf";
    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent;" +
            " -fx-background-image: url('GUI/textField.png'); -fx-prompt-text-fill: #B0453E";

    public CustomTextField() {
        setFont();
        setPrefWidth(260);
        setPrefHeight(70);
        setStyle(BUTTON_PRESSED_STYLE);
    }

    private void setFont(){
            setFont(Font.loadFont(this.getClass().getResource(FONT_PATH).toExternalForm(), 35));
        setAlignment(Pos.TOP_CENTER);
    }
}
