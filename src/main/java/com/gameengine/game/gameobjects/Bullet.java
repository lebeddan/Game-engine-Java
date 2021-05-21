package com.gameengine.game.gameobjects;

import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameManager;
import javafx.geometry.Point2D;
/**
 * Bullet class. Game object which as a part of a game.
 * @author Vasily Levitskiy
 */

public class Bullet extends GameObject {
    /**
     * Parametrs of bullet starts.
     */
    private float offX, offY;
    private int tileX,tileY;

    private ImageTile bulletSprite;

    private float speed = 300;
    private float direction;
    private Point2D sent_pos;
    private float animationX, animationY = 0;
    private Point2D bulletAxis = Point2D.ZERO;
    private Point2D posToDraw = Point2D.ZERO;
    private Point2D centerPoint;
    private String username;
    /**
     * Parametrs of bullet ends.
     */

    /**
     * A public constructor for creating a bullet object.
     * @param direction - the direction where the bullet goes
     * @param offX - offset X axis of bullet
     * @param offY - offset Y axis of bullet
     * @param username - from whom does the bullet fly
     * @param bSprite - bullet sprite
     */
    public Bullet(float direction, float offX, float offY, String username, ImageTile bSprite){
        this.username = username;
        this.bulletSprite = bSprite;
        this.direction = direction;
        this.shape = "circle";
        posX = tileX * GameManager.TS + offX;
        posY = tileY * GameManager.TS + offY;
        this.sent_pos = Point2D.ZERO.add(posX, posY);
        radius = 12;
        this.width = (int) (Math.max(bulletSprite.getTileW(), bulletSprite.getTileH()));
        this.height =(int) (Math.max(bulletSprite.getTileW(), bulletSprite.getTileH()));
        bulletAxis = Point2D.ZERO.add(width/2, height/2);
        double i = (Math.cos(Math.toRadians(direction)) * width);
        double j = Math.sin(Math.toRadians(direction)) * height;
        posToDraw = Point2D.ZERO.add(i, j);
        centerPoint = posToDraw;
        this.offX = offX;
        this.offY = offY;
    }

    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {
        offX += Math.cos(Math.toRadians(direction))*speed*dt;
        offY += Math.sin(Math.toRadians(direction))*speed*dt;


        //Final position
        if(offY > GameManager.TS){
            tileY++;
            offY -= GameManager.TS;
        }

        if(offY < 0){
            tileY--;
            offY += GameManager.TS;
        }

        if(offX > GameManager.TS){
            tileX++;
            offX -= GameManager.TS;
        }

        if(offX < 0){
            tileX--;
            offX += GameManager.TS;
        }

        if(sent_pos.distance(Point2D.ZERO.add(posX, posY)) > 1300){
            this.dead = true;
        }

        posX = tileX * GameManager.TS + offX;
        posY = tileY * GameManager.TS + offY;
        System.out.println(posX + " " + posY);

        animationX += dt * 20;
        if(animationX > 5){
            animationX = 0;
            animationY += 1;
            if(animationY > 4){
                animationY = 0;
            }
        }
        gm.check_collisions(this);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawImageTile(bulletSprite, (int)(posX + posToDraw.getX() - width/2), (int)(posY + posToDraw.getY()- height/2),
                (int)animationX, (int)animationY, (int)direction, bulletAxis);
    }

    @Override
    public void hit(GameObject obj) {
        dead = true;
    }

    @Override
    public Point2D getCenter() {
        return centerPoint;
    }

    public void setRotation(float rotation) {
        this.direction = rotation;
    }
}
