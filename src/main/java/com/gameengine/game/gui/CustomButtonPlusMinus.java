package com.gameengine.game.gui;

import com.gameengine.engine.audio.SoundClip;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CustomButtonPlusMinus extends CustomButton{
    private final String BUTTON_PRESSED_STYLE_MINUS = "-fx-background-color: transparent;" +
            " -fx-background-image: url('GUI/minusbuttondown.png');";
    private final String BUTTON_FREE_STYLE_MINUS = "-fx-background-color: transparent;" +
            " -fx-background-image: url('GUI/minusbutton.png');";
    private final String BUTTON_FREE_STYLE_PLUS = "-fx-background-color: transparent;" +
            " -fx-background-image: url('GUI/plusbutton.png');";
    private final String BUTTON_PRESSED_STYLE_PLUS = "-fx-background-color: transparent;" +
            " -fx-background-image: url('GUI/plusbuttondown.png');";

    private SoundClip select = new SoundClip("/Sounds/buttonSelect.wav", "clip");
    private SoundClip click = new SoundClip("/Sounds/buttonClick.wav", "clip");

    private String type;

    public CustomButtonPlusMinus(String text, String type) {
        super(text);
        this.type = type;
        if(type.equals("+")){
            setStyle(BUTTON_FREE_STYLE_PLUS);
        } else {
            setStyle(BUTTON_FREE_STYLE_MINUS);
        }
        setPrefWidth(140);
        setPrefHeight(76);
        initButtonListeners();
        click.setVolume(0.3);
        select.setVolume(0.3);
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

    private void setButtonPressedStyle(){
//        setGraphic(new ImageView(BUTTON_PRESSED_STYLE));
        if(type.equals("+")){
            setStyle(BUTTON_PRESSED_STYLE_PLUS);
        } else {
            setStyle(BUTTON_PRESSED_STYLE_MINUS);
        }
        setPrefHeight(70);
        setLayoutY(getLayoutY() + 4);
    }

    private void setButtonReleasedStyle(){
//        setGraphic(new ImageView(BUTTON_FREE_STYLE));
        if(type.equals("+")) {
            setStyle(BUTTON_FREE_STYLE_PLUS);
        } else {
            setStyle(BUTTON_FREE_STYLE_MINUS);
        }
        setPrefHeight(76);
        setLayoutY(getLayoutY() - 4);
    }
}
