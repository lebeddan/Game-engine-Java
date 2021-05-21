package com.gameengine.engine;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * Input class. Gets inputs using javaFX's listeners.
 * If button pressed, adds it to array. To use the input you just need to access the array.
 * @author Vasily Levitskiy
 */
public class Input{
    /*
   Properties of input starts
    */
    private GameContainer gc;

    private final int NUM_KEYS = 256;
    private boolean[] keys = new boolean[NUM_KEYS];
    private boolean[] keysLast = new boolean[NUM_KEYS];

    private final int NUM_BUTTONS = 5;
    private boolean[] buttons = new boolean[NUM_BUTTONS];
    private boolean[] buttonsLast = new boolean[NUM_BUTTONS];

    private int mouseX, mouseY;
    private int scroll;

    private Scene mainScene;
    /*
   Properties of input ends
    */

    /**
     * A public constructor for creating input object.
     * @param gc - game container used for get window and main scene
     */
    public Input(GameContainer gc){
        this.gc = gc;
        mainScene = gc.getWindow().getMainScene();
        mouseX = 0;
        mouseY = 0;
        scroll = 0;

        mainScene.setOnMousePressed(mouseEvent -> {
            mousePressed(mouseEvent);
        });

        mainScene.setOnMouseReleased(mouseEvent -> {
            mouseReleased(mouseEvent);
        });

        mainScene.setOnKeyPressed(keyEvent -> {
            keyPressed(keyEvent);
        });

        mainScene.setOnKeyReleased(keyEvent -> {
            keyReleased(keyEvent);
        });

        mainScene.setOnMouseDragged(mouseEvent -> {
            mouseDragged(mouseEvent);
        });

        mainScene.setOnMouseMoved(mouseEvent -> {
            mouseMoved(mouseEvent);
        });
    }

    /**
     * Updates the arrays containing button presses.
     */
    public void update(){
        scroll = 0;

        for(int i = 0; i < NUM_KEYS; i++){
            keysLast[i] = keys[i];
        }

        for(int i = 0; i < NUM_BUTTONS; i++){
            buttonsLast[i] = buttons[i];
        }
    }

    public boolean isKey(int keyCode){
        return keys[keyCode];
    }

    public boolean isMouseKey(int keyCode){
        return buttons[keyCode];
    }

    public boolean isKeyUp(int keyCode){
        return !keys[keyCode] && keysLast[keyCode];
    }

    public boolean isKeyDown(int keyCode){
        return keys[keyCode] && !keysLast[keyCode];
    }

    public boolean isButton(int button){
        return buttons[button];
    }

    public boolean isButtonUp(int button){
        return !buttons[button] && buttonsLast[button];
    }

    public boolean isButtonDown(int button){
        return buttons[button] && !buttonsLast[button];
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        keys[e.getCode().getCode()] = true;
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getCode().getCode()] = false;
    }

    public void mouseClicked(MouseEvent e) {
        buttons[mouseNumber(e)] = true;
    }

    public void mousePressed(MouseEvent e) {
        buttons[mouseNumber(e)] = true;
    }

    public void mouseReleased(MouseEvent e) {
        buttons[mouseNumber(e)] = false;
    }

    /**
     * A public function for get X,Y axis of mouse,
     * when user dragged something.
     * @param e - mouse
     */
    public void mouseDragged(MouseEvent e) {
        mouseX = (int)(e.getX() / gc.getScale());
        mouseY = (int)(e.getY() / gc.getScale());
    }

    /**
     * A public function for get X,Y axis of mouse,
     * when user move mouse.
     * @param e - mouse
     */
    public void mouseMoved(MouseEvent e) {
        mouseX = (int)(e.getX() / gc.getScale());
        mouseY = (int)(e.getY() / gc.getScale());
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getScroll() {
        return scroll;
    }

    /**
     * A public function for get a key
     * when user click on the button.
     * @param e - mouse
     */
    private int mouseNumber(MouseEvent e){
        if(e.isPrimaryButtonDown()){
            return 0;
        } else if(e.isSecondaryButtonDown()){
            return 1;
        }
        return 1;
    }
}
