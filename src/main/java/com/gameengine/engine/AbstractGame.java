package com.gameengine.engine;

import javafx.application.Application;

/**
 * The abstract class the connects everything together.
 * @author Vasily Levitskiy
 */
public abstract class AbstractGame extends Application {
    public abstract void init(GameContainer gc);
    public abstract void update(GameContainer gc, float deltaTime);
    public abstract void render(GameContainer gc, Renderer r);
}