package com.gameengine.engine.game;
/*
<<<<<<< HEAD
 * The abstract class which describes
 * the behavior of game objects in the game.
 * @author Lebedev Daniil
 * */
=======
* The abstract class which describes
* the behavior of game objects in the game.
* @author Lebedev Daniil
* */
>>>>>>> Danila-Master
import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;

public abstract class GameObject {
    // Properties of game object
    protected String tag;
    protected float posX, posY;
    protected int width, height;
    protected boolean dead = false;

    /*
<<<<<<< HEAD
     * Abstract function to update the game object.
     * @param gc - the game container
     * @param gm - the game manager
     * @param dt - the delta time between frames
     * */
    public abstract void update(GameContainer gc, GameManager gm, float dt);

    /*
     * Abstract function to render the game object.
     * @param gc - the game container to now for example, how biggest the screen
     * @param r - the renderer for use renderer functions
     * */
=======
    * Abstract function to update the game object.
    * @param gc - the game container
    * @param gm - the game manager
    * @param dt - the delta time between frames
    * */
    public abstract void update(GameContainer gc, GameManager gm, float dt);

    /*
    * Abstract function to render the game object.
    * @param gc - the game container to now for example, how biggest the screen
    * @param r - the renderer for use renderer functions
    * */
>>>>>>> Danila-Master
    public abstract void render(GameContainer gc, Renderer r);

    /*
     * Obtains the name of game object.
     * @returns the string which contains the name
     * */
    public String getTag() {
        return tag;
    }

    /*
     * Check if is a game object is dead.
     * @return boolean true/false.
     */
    public boolean isDead() {
        return dead;
    }

    /*
     * Set state of game object.
     * */
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    /*
     * Set name of game object.
     * */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /*
     * Obtains the position on x-axis of game object.
     * @returns the position on x-axis of game object
     * */
    public float getPosX() {
        return posX;
    }

    /*
     * Set the position on x-axis of game object.
     * */
    public void setPosX(float posX) {
        this.posX = posX;
    }

    /*
     * Obtains the position on y-axis of game object.
     * @returns the position on y-axis of game object
     * */
    public float getPosY() {
        return posY;
    }

    /*
     * Set the position on y-axis of game object.
     * */
    public void setPosY(float posY) {
        this.posY = posY;
    }

    /*
     * Obtains the width of game object.
     * @returns the width of game object
     * */
    public int getWidth() {
        return width;
    }

    /*
     * Set the width of game object.
     * */
    public void setWidth(int width) {
        this.width = width;
    }

    /*
     * Obtains the height of game object.
     * @returns the height of game object
     * */
    public int getHeight() {
        return height;
    }

    /*
     * Set the height of game object.
     * */
    public void setHeight(int height) {
        this.height = height;
    }
}
