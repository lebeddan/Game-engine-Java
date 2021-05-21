package com.gameengine.game.gui;

import com.gameengine.engine.audio.SoundClip;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CustomButton extends Button {
    private final String FONT_PATH = "/GUI/8bitlim.ttf";

    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent;" +
            " -fx-background-image: url('GUI/Buttondown.png');";
    private final String BUTTON_FREE_STYLE = "-fx-background-color: transparent;" +
            " -fx-background-image: url('GUI/Buttonup.png');";

    private SoundClip select = new SoundClip("/Sounds/buttonSelect.wav", "clip");
    private SoundClip click = new SoundClip("/Sounds/buttonClick.wav", "clip");

    public CustomButton(String text) {
        setFont();
        setPrefWidth(260);
        setPrefHeight(76);
        setText(text);
        setDefaultButton(true);
        setStyle(BUTTON_FREE_STYLE);
        initButtonListeners();
        click.setVolume(0.3);
        select.setVolume(0.3);
    }

    private void setFont(){
        setFont(Font.loadFont(this.getClass().getResource(FONT_PATH).toExternalForm(), 35));
        setAlignment(Pos.TOP_CENTER);
    }

    private void setButtonPressedStyle(){
//        setGraphic(new ImageView(BUTTON_PRESSED_STYLE));
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(70);
        setLayoutY(getLayoutY() + 4);
    }

    private void setButtonReleasedStyle(){
//        setGraphic(new ImageView(BUTTON_FREE_STYLE));
        setStyle(BUTTON_FREE_STYLE);
        setPrefHeight(76);
        setLayoutY(getLayoutY() - 4);
    }

    private void initButtonListeners(){
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    setButtonPressedStyle();
                    click.play();
                }
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    setButtonReleasedStyle();
                }
            }
        });

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                DropShadow drop = new DropShadow(30, Color.LIGHTBLUE);
                drop.setInput(new Glow(0.2));
                setEffect(drop);
                select.play();
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setEffect(null);
            }
        });
    }
}
