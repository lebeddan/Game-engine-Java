package com.gameengine.game.gameobjects;


import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Input;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.audio.SoundClip;
import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameManager;
import com.gameengine.game.server.packets.Packet02Move;
import com.gameengine.game.server.packets.Packet03Bullet;
import javafx.geometry.Point2D;

import java.awt.event.KeyEvent;
import java.io.IOException;

public class Player extends GameObject{

    /**
     * Path to sprites.
     */
    private String pathTankImage = "/Player/MagicTank.png";
    private String pathGunImage = "/Player/MagicTankBarrel.png";
    private String pathToBullet = "/Tile/magicball.png";

    /**
     * Parameters for rotation and animation of sprites.
     */
    private ImageTile playerSprite;
    private ImageTile gunSprite;
    private ImageTile bulletSprite;
    private float rotation;
    private int animation;
    private Point2D zeroPos = Point2D.ZERO;
    private Point2D tankAxis;
    private Point2D gunAxis;
    private String username;
    private Input input;
    private boolean menuOpen = false;

    /**
     * Position of player.
     */
    private float offX, offY;
    private float lastMPos, lastRot;

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
    private boolean isMoving, isMRotating, isRotating;
    private Point2D centerPoint;

    private SoundClip fireSound = new SoundClip("/Sounds/zapSound.wav", "clip");
    private SoundClip deathSound = new SoundClip("/Sounds/gameOver.wav", "clip");

    /**
     * Auxiliary parameters.
     */
    private long lastShootTime; // shooting delay
    private float mousePosRot;
    private boolean playerKilled;

    /**
     * Constructs a player object.
     * This public constructor is used for set parameters to player.
     * @param posX - start position on axis - X.
     * @param posY - start position on axis - Y.
     * @throws IOException
     */
    public Player(int posX, int posY, String username, Input input) throws IOException {
        fireSound.setVolume(0.4);
        deathSound.setVolume(0.4);

        this.username = username;
        this.input = input;
        playerSprite = new ImageTile(pathTankImage, 32, 32, 3);
        gunSprite = new ImageTile(pathGunImage, 32, 32, 3);
        bulletSprite = new ImageTile(pathToBullet, 32,32, 2);

        this.shape = "circle";
        this.tag = username;
        this.rotation = 0;
        this.offX = 0;
        this.offY = 0;
        this.posX = posX*32;
        this.posY = posY*32;

        this.width = playerSprite.getTileW();
        this.height = playerSprite.getTileH();

        tankAxis = zeroPos.add(playerSprite.getTileW()/2, 22*3);
        gunAxis = zeroPos.add((gunSprite.getTileW()/2), 24*3);
        centerPoint = tankAxis;
        radius = (width-15)/2;
        ammoMax = 1000000;
        lastShootTime = 0;
        turnspeed = 3;
        power = 6;
        angularDrag = (float)0.9;
        drag = (float)0.9;
        MaxSpeed = 80;
    }


    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {
        if(input != null) {
            if(input.isKeyUp(KeyEvent.VK_ESCAPE)){
                openMenu(gm);
            }

            /**
             * Left right start.
             */
            if (input.isKey(KeyEvent.VK_D)) {
                animation++;
                isMoving = true;
                rotation += 3;
                angularVelocity += turnspeed;
                if (rotation > 360) {
                    rotation = 0;
                }
            }

            if (input.isKey(KeyEvent.VK_A)) {
                animation++;
                isMoving = true;
                rotation -= 3;
                angularVelocity -= turnspeed;
                if (rotation < 0) {
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
            if (input.isKey(KeyEvent.VK_W)) {
                animation++;
                isMoving = true;
                final float cos = ((float) Math.cos(Math.toRadians(rotation - 90))) * ((float) tankAxis.getX() / 2);
                final float sin = ((float) Math.sin(Math.toRadians(rotation - 90)) * ((float) tankAxis.getY() / 2));
                offX += dt * cos;
                offY += dt * sin;

            }

            if (input.isKey(KeyEvent.VK_S)) {
                animation++;
                isMoving = true;
                final float cos = ((float) Math.cos(Math.toRadians(rotation - 90))) * ((float) tankAxis.getX() / 2);
                final float sin = ((float) Math.sin(Math.toRadians(rotation - 90)) * ((float) tankAxis.getY() / 2));
                offX -= dt * cos;
                offY -= dt * sin;

            }
            /**
             * Straight back end.
             */
        }

        /**
         * Update animation and speed of player start.
         */
        if(animation > 2){
            animation = 0;
        }

        if(input != null) {
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
            /**
             * Mouse position for rotate gun and shooting end.
             */

            /**
             * Shooting start.
             */
            if (input.isKey(KeyEvent.VK_SPACE)) {
                if (ammoMax > 0 && System.currentTimeMillis() - lastShootTime >= 300) {
                    fireBullet(gm);
                    Packet03Bullet packet = new Packet03Bullet(this.username, posX, posY, rotation);
                    packet.writeData(gm.socketClient);
                    lastShootTime = System.currentTimeMillis();
                    ammoMax--;
                }
            }
        }

        // Check if the player moved:

        if(lastMPos != mousePosRot){
            isMRotating = true;
        } else {
            isMRotating = false;
        }
        lastMPos = mousePosRot;

        if(lastRot != rotation){
            isRotating = true;
        } else {
            isRotating = false;
        }
        lastRot = rotation;

        if(offX == 0 && offY == 0){
            isMoving = false;
        }


        posX += offX;
        posY += offY;

        offX *= drag;
        offY *= drag;
        angularVelocity *= angularDrag;
        /**
         * Update animation and speed of player end.
         */

        gm.check_collisions(this);

        // Send move packet
        if(isMoving || isMRotating || isRotating){
            Packet02Move packet = new Packet02Move(this.username, posX, posY, rotation, mousePosRot);
            packet.writeData(gm.socketClient);
        }


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
         * Shooting end.
         */
    }

    private void openMenu(GameManager gm) {
        if(menuOpen) {
            gm.showPMenu();
            menuOpen = false;
        } else {
            gm.showPMenu();
            menuOpen = true;
        }
    }


    @Override
    public void render(GameContainer gc, Renderer r) {
        if(!playerKilled) {
            r.drawImageTile(playerSprite, (int) posX, (int) posY, (int) 0, 0, rotation, tankAxis);
            r.drawImageTile(gunSprite, (int) (posX + tankAxis.getX() - gunSprite.getTileW() / 2), (int) ((int) posY - 2), (int) 0, 0, mousePosRot + 90, gunAxis);
            r.drawText(username, (int) posX + width / 4, (int) posY - height / 4, 0xffffffff);
//            r.drawFillCirc((int) (posX + centerPoint.getX()), (int) (posY + centerPoint.getY()), radius, 0x99ff0000);
        }

    }

    @Override
    public void hit(GameObject obj, GameManager gm) {
        if(obj instanceof Bullet) {
            if(!playerKilled) {
                playerDead(gm);
            }
        } else {
            posX -= offX * 2;
            posY -= offY * 2;
            offX = 0;
            offY = 0;
        }
    }

    private void playerDead(GameManager gm) {
        input = null;
        deathSound.play();
        gm.showDeathMenu();
        this.playerKilled = true;
    }

    @Override
    public Point2D getCenter() {
        return centerPoint;
    }

    public String getUsername(){
        return this.username;
    }

    public void setRotation(float rotation){
        this.rotation = rotation;
    }

    public void setMouseRotation(float mouseRotation){
        this.mousePosRot = mouseRotation;
    }

    public void fireBullet(GameManager gm){
        gm.addObject(new Bullet(mousePosRot, posX + (int) tankAxis.getX(), posY + (int) tankAxis.getY(), username, bulletSprite));
        fireSound.play();
    }
}
