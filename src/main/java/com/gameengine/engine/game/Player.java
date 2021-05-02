package com.gameengine.engine.game;

import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.gfx.ImageTile;
import javafx.geometry.Point2D;

import java.awt.event.KeyEvent;
import java.io.IOException;

public class Player extends GameObject{
    /**
     * TODO: Add some powerup's.
     */

    /**
     * Path to sprites.
     */
    private String pathTankImage = "/Users/SamSeppi/Desktop/IdeaProjects/TestEngine/src/test/resources/ww2tanki.png";
    private String pathGunImage = "/Users/SamSeppi/Desktop/IdeaProjects/TestEngine/src/test/resources/tankGun.png";

    /**
     * Parameters for rotation and animation of sprites.
     */
    private ImageTile playerSprite;
    private ImageTile gunSprite;
    private float rotation;
    private int animation;
    private Point2D zeroPos = Point2D.ZERO;
    private Point2D tankAxis;
    private Point2D gunAxis;
    private Point2D muzzleAxis;

    /**
     * Position of player.
     */
    private float offX, offY;
    private int tileX, tileY; // position X, Y

    /**
     * Player properties.
     */
    private float MaxSpeed; // speed of car
    private float drag; // the rotation of the car, in radians
    private float angularVelocity; // speed the car is spinning, in radians
    private float angularDrag; //  how fast the car stops spinning
    private float power; // how fast car can accelerate
    private int turnspeed;// how fast to turn
    private int ammoMax;

    /**
     * Auxiliary parameters.
     */
    private long lastShootTime; // shooting delay
    private float mousePosRot;

    /**
     * Constructs a player object.
     * This public constructor is used for set parametrs to player.
     * @param posX - start position on axis - X.
     * @param posY - start position on axis - Y.
     * @throws IOException
     */
    public Player(int posX, int posY) throws IOException {
        // Calculating X,Y axis start
        tankAxis = zeroPos.add(playerSprite.getTileW()/2, (playerSprite.getTileH()/2));
        gunAxis = zeroPos.add(((gunSprite.getTileW())/2), (gunSprite.getTileH()+20)/2);
        // Calculating X,Y axis end
        playerSprite = new ImageTile(pathTankImage, 72, 86);
        gunSprite = new ImageTile(pathGunImage, 33, 61);
        this.tag = "player";
        this.tileX = posX;
        this.tileY = posY;
        this.rotation = 0;
        this.offX = 0;
        this.offY = 0;
        this.posX = posX*16;
        this.posY = posY*16;
        this.width = (int) (playerSprite.getTileW() + tankAxis.getX());
        this.height =(int) (playerSprite.getTileH() + tankAxis.getY());
        ammoMax = 1000000;
        lastShootTime = 0;
        turnspeed = 3;
        power = 1;
        angularDrag = (float)0.9;
        drag = (float)0.9;
        MaxSpeed = 50;

    }


    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {
        /**
         * Left right start.
         */
        if(gc.getInput().isKey(KeyEvent.VK_D)){
            animation++;
            rotation += 3;
            angularVelocity += turnspeed;
            if(rotation > 360){
                rotation = 0;
            }
        }

        if(gc.getInput().isKey(KeyEvent.VK_A)){
            animation++;
            rotation -= 3;
            angularVelocity -= turnspeed;
            if(rotation < 0){
                rotation = 360;
            }
        }
        /**
         * Left right end.
         */

        /**
         * Straight back start.
         * TODO: Add collision.
         */
        if(gc.getInput().isKey(KeyEvent.VK_W)){
            animation++;
            final float cos = ((float)Math.cos(Math.toRadians(rotation-90))) * ((float)tankAxis.getX()/2);
            final float sin = ((float)Math.sin(Math.toRadians(rotation-90)) * ((float)tankAxis.getY()/2));
            offX += dt * cos;
            offY += dt * sin;

        }

        if(gc.getInput().isKey(KeyEvent.VK_S)){
            animation++;
            final float cos = ((float)Math.cos(Math.toRadians(rotation-90))) * ((float)tankAxis.getX()/2);
            final float sin = ((float)Math.sin(Math.toRadians(rotation-90)) * ((float)tankAxis.getY()/2));
            offX -= dt * cos;
            offY -= dt * sin;

        }
        /**
         * Straight back end.
         */

        /**
         * Update animation and speed of player start.
         */
        if(animation > 2){
            animation = 0;
        }

        posX += offX;
        posY += offY;

        offX *= drag;
        offY *= drag;
        angularVelocity *= angularDrag;
        /**
         * Update animation and speed of player end.
         */

        /**
         * Acceleration and Stopping start.
         */
        if(offX > 0) {
            if(gc.getInput().isKeyDown(KeyEvent.VK_W) & gc.getInput().isKeyDown(KeyEvent.VK_D)){
                offX -= angularVelocity;
            }

            if(gc.getInput().isKeyDown(KeyEvent.VK_W) & gc.getInput().isKeyDown(KeyEvent.VK_A)){
                offX += angularVelocity;
            }

            if (gc.getInput().isKeyUp(KeyEvent.VK_W) || gc.getInput().isKeyUp(KeyEvent.VK_S))
                offX = 0;
            else
                offX -= dt * drag;
        } else if(offX < 0) {
            if (gc.getInput().isKeyUp(KeyEvent.VK_W) || gc.getInput().isKeyUp(KeyEvent.VK_S))
                offX = 0;
            else
                offX += dt*power;
        }

        if(offY > 0) {
            if(gc.getInput().isKeyDown(KeyEvent.VK_W) & gc.getInput().isKeyDown(KeyEvent.VK_D)){
                offY -= angularVelocity;
            }

            if(gc.getInput().isKeyDown(KeyEvent.VK_W) & gc.getInput().isKeyDown(KeyEvent.VK_A)){
                offY += angularVelocity;
            }

            if (gc.getInput().isKeyUp(KeyEvent.VK_W) || gc.getInput().isKeyUp(KeyEvent.VK_S))
                offY = 0;
            else
                offY -= dt * drag;
        }
        if(offY < 0) {
            if (gc.getInput().isKeyUp(KeyEvent.VK_W) || gc.getInput().isKeyUp(KeyEvent.VK_S))
                offY = 0;
            else
                offY += dt*power;
        }
        /**
         * Acceleration and Stopping end.
         */

        /**
         * Mouse position for rotate gun and shooting start.
         */
        int mousePosX = gc.getInput().getMouseX();
        int mousePosY = gc.getInput().getMouseY();

        if(posX < gc.getWidth()/2){
            mousePosX -= posX;
        } else {
            mousePosX -= gc.getWidth()/2;
        }

        if(posY < gc.getHeight()/2){
            mousePosY -= posY;
        } else {
            mousePosY -= gc.getHeight()/2;
        }

        mousePosRot = (float)Math.toDegrees(Math.atan2(mousePosY - gunAxis.getY(), mousePosX - gunAxis.getX()));
        muzzleAxis = zeroPos.add((gunAxis.getX()+ gunSprite.getTileH()/2)*Math.cos(Math.toRadians(mousePosRot)),
                (gunAxis.getY())*Math.sin(Math.toRadians(mousePosRot)));
        /**
         * Mouse position for rotate gun and shooting end.
         */

        /**
         * Shooting start.
         */
        if(gc.getInput().isKey(KeyEvent.VK_SPACE)){
            if(ammoMax > 0 && System.currentTimeMillis() - lastShootTime >= 500){
                gm.addObject(new Bullet(mousePosRot, posX +(int)muzzleAxis.getX()+55, posY+(int)muzzleAxis.getY()+60));
                lastShootTime = System.currentTimeMillis();
                ammoMax--;
                System.out.println("Ammo: " + ammoMax);
            }
        }
        /**
         * Shooting end.
         */
    }


    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawImageTile(playerSprite, (int) posX, (int) posY, (int) animation, 0, rotation, tankAxis);
        r.drawImageTile(gunSprite, (int) (posX+ (int)tankAxis.getX()-6), (int) ((int) posY+4),
                (int) 0, 0, mousePosRot+90, gunAxis);
        r.drawFillRect((int) (posX + muzzleAxis.getX()+55), (int) (posY + muzzleAxis.getY()+60),
                4, 4, 0xff00ff00);
    }
}
