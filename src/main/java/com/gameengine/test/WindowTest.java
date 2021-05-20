package com.gameengine.test;

import com.gameengine.engine.AbstractGame;
import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.Window;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WindowTest {
    @Test
    void EmptyPixelArray() {
        GameContainer gc = new GameContainer(new AbstractGame() {
            @Override
            public void init(GameContainer gc) {

            }

            @Override
            public void update(GameContainer gc, float deltaTime) {

            }

            @Override
            public void render(GameContainer gc, Renderer r) {

            }

            @Override
            public void start(Stage stage) throws Exception {

            }
        });
        Window window = new Window(gc);
        int size[] = new int[(int)(gc.getWidth()*gc.getHeight()*gc.getScale()*gc.getScale())];;
        Assertions.assertArrayEquals(size, window.getPixel());
    }

    @Test
    void GameCanvasNotNullTest() {
        GameContainer gc = new GameContainer(new AbstractGame() {
            @Override
            public void init(GameContainer gc) {

            }

            @Override
            public void update(GameContainer gc, float deltaTime) {

            }

            @Override
            public void render(GameContainer gc, Renderer r) {

            }

            @Override
            public void start(Stage stage) throws Exception {

            }
        });
        Window window = new Window(gc);
        Assertions.assertNotNull(window.getCanvas());
    }
}