package com.gameengine.test;

import com.gameengine.engine.AbstractGame;
import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameContainerTest {
    @Test
    void WidthTest() {
        AbstractGame gm = new AbstractGame() {
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
        };
        GameContainer gc = new GameContainer(gm);
        Assertions.assertEquals(320, gc.getWidth());
    }

    @Test
    void HeightTest() {
        AbstractGame gm = new AbstractGame() {
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
        };
        GameContainer gc = new GameContainer(gm);
        Assertions.assertEquals(240, gc.getHeight());
    }

    @Test
    void TileTest() {
        AbstractGame gm = new AbstractGame() {
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
        };
        GameContainer gc = new GameContainer(gm);
        Assertions.assertEquals("TestEngine", gc.getTitle());
    }
}