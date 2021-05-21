package com.gameengine.game.gameobjects;

import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.game.GameManager;
import javafx.geometry.Point2D;
/**
 * The abstract class which defines the standard properties and methods of game objects.
 * @author Lebedev Daniil
 */
public abstract class GameObject {
    protected String tag;
    protected float posX;
    protected float posY;
    protected int width, height;
    protected boolean deathAnimation = false;
    protected boolean hit = false;
    protected boolean dead = false;
    protected String shape;
    protected int radius;

    public abstract void update(GameContainer gc, GameManager gm, float dt);
    public abstract void render(GameContainer gc, Renderer r);
    public abstract void hit(GameObject obj);
    public abstract Point2D getCenter();

    public String getTag() {
        return tag;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getShape(){
        return shape;
    }

    public double getRadius(){
        return radius;
    }
}
