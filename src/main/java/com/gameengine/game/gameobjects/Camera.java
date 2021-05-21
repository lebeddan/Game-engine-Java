package com.gameengine.game.gameobjects;

import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.game.GameManager;
/**
 * Camera class. Game object which follows the player and.
 * @author Vasily Levitskiy
 */
public class Camera {
    /**
     * Parametrs of camera starts.
     */
    private float offX, offY;

    private String targetTag;
    private GameObject target = null;
    /**
     * Parametrs of camera starts.
     */

    /**
     * A public costructor for set object tracking
     * @param tag - tag of game object
     */
    public Camera(String tag){
        this.targetTag = tag;
    }

    /**
     * A public function for update a game scene
     * @param gc - game container used for get width and height of window.
     * @param gm - game manager used for get level width and level height of map.
     * @param dt - frames
     */
    public void update(GameContainer gc, GameManager gm, float dt){
        if(target == null){
            target = gm.getObject(targetTag);
        }

        if(target == null){
            return;
        }

        float targetX = (target.getPosX() + target.getWidth() / 2) - gc.getWidth() / 2;
        float targetY = (target.getPosY() + target.getHeight() / 2) - gc.getHeight() / 2;

        offX = targetX;
        offY = targetY;

        if(offX < 0) offX = 0;
        if(offY < 0) offY = 0;

        if(offX  + gc.getWidth() > gm.getLevelW()){
            offX = gm.getLevelW() - gc.getWidth();
        }
        if(offY + gc.getHeight() > gm.getLevelH()){
            offY = gm.getLevelH() - gc.getHeight();
        }
    }

    /**
     * A public function for rendering position of camera
     * @param r
     */
    public void render(Renderer r){
        r.setCamX((int)offX);
        r.setCamY((int)offY);
    }

    public float getOffX() {
        return offX;
    }

    public void setOffX(float offX) {
        this.offX = offX;
    }

    public float getOffY() {
        return offY;
    }

    public void setOffY(float offY) {
        this.offY = offY;
    }

    public String getTargetTag() {
        return targetTag;
    }

    public void setTargetTag(String targetTag) {
        this.targetTag = targetTag;
    }

    public GameObject getTarget() {
        return target;
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }
}
