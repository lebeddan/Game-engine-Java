package com.gameengine.game.GameObjects;

import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameManager;
import javafx.geometry.Point2D;

import java.awt.event.KeyEvent;
import java.io.IOException;

public class Tank extends GameObject {
    private float tractionForce; // tForce = eForce * uVector
    private float engineForce;
    private float unitVector;

    private float dragForce, dragConstant; // dForce = dConst * vVector * |vVect|
    private float velocityVector;

    private float rolResForce, rolResConst; // rrForce = -rrConst * vVect

    private float longForce; // longRoce = tracForce + dragForce + roLResForce

    private float acceleration, mass; // a = F / M

    private ImageTile playerSprite = new ImageTile("src/Resources/Player/ww2tanki.png", 72, 86);
    private ImageTile gunSprite = new ImageTile("src/Resources/Player/tankGun.png", 32, 64);
    private float offX, offY;
    private int tileX,tileY;
    private float rotation;
    private float mousePosRot;

    private float horizontal, vertical;

    private Point2D velocity;
    private Point2D tankPos;
    private Point2D mousePos;
    private Point2D zeroPos = Point2D.ZERO;
    private Point2D tankAxis;
    private Point2D gunAxis;
    private Point2D muzzleAxis;
    private Point2D centerPoint;

    private int animation;


    public Tank(int posX, int posY) throws IOException {
        this.shape = "circle";
        this.tag = "player";
        this.tileX = posX;
        this.tileY = posY;
        this.rotation = 0;
        this.offX = 0;
        this.offY = 0;
        this.posX = posX*36;
        this.posY = posY*43;
//        this.width = (int) Math.sqrt(Math.pow(playerSprite.getTileW()/2, 2) + Math.pow(playerSprite.getTileH()/2,2))*2;
//        this.height =(int) Math.sqrt(Math.pow(playerSprite.getTileW()/2, 2) + Math.pow(playerSprite.getTileH()/2,2))*2;

        this.width = playerSprite.getTileW();
        this.height = playerSprite.getTileH();

        tankAxis = zeroPos.add(playerSprite.getTileW()/2, playerSprite.getTileH()/2);
        gunAxis = zeroPos.add((gunSprite.getTileW()/2), 45);
//        muzzleAxis = zeroPos.add(gunAxis.getX(), gunAxis.getY()+gunSprite.getTileH()/2);
        System.out.println(tankAxis.getX() + " " + tankAxis.getY());
        this.horizontal = 0;
        this.vertical = 0;
//        centerPoint = tankAxis;
        centerPoint = Point2D.ZERO.add(width/2, height/2);
        radius = width/2;

    }

    // Car velocity = v + dt * accel
    // Car position = p + dt * v

    //speed = sqrt(v.x*v.x + v.y*v.y); speed is velocity magnitude
    //fdrag.x = - Cdrag * v.x * speed;
    //fdrag.y = - Cdrag * v.y * speed;


    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {
        if(gc.getInput().isKey(KeyEvent.VK_D)){
                animation++;
                rotation += 2;
                if(rotation > 360){
                    rotation = 0;
                }
        }

        if(gc.getInput().isKey(KeyEvent.VK_A)){
            animation++;
                rotation -= 2;
                if(rotation < 0){
                    rotation = 360;
                }
        }

        if(gc.getInput().isKey(KeyEvent.VK_W)){
            animation++;
            if(gm.getCollision(tileX - 1, tileY) || gm.getCollision(tileX - 1, tileY + (int)Math.signum((int)offY))){
                offX += dt * 5 * (float)Math.cos(Math.toRadians(rotation-90));
                offY += dt * 5 * (float)Math.sin(Math.toRadians(rotation-90));
            } else {
                offX += dt * 5 * (float)Math.cos(Math.toRadians(rotation-90));
                offY += dt * 5 * (float)Math.sin(Math.toRadians(rotation-90));
            }
        }

        if(gc.getInput().isKey(KeyEvent.VK_S)){
            animation++;
            if(gm.getCollision(tileX - 1, tileY) || gm.getCollision(tileX - 1, tileY + (int)Math.signum((int)offY))){
                offX -= dt * 5 * (float)Math.cos(Math.toRadians(rotation-90));
                offY -= dt * 5 * (float)Math.sin(Math.toRadians(rotation-90));
            } else {
                offX -= dt * 5 * (float) Math.cos(Math.toRadians(rotation - 90));
                offY -= dt * 5 * (float) Math.sin(Math.toRadians(rotation - 90));
            }
        }

        if(animation > 2){
            animation = 0;
        }

        if(offX > 0) {
            offX -= dt*2;
        } else if(offX < 0) {
            offX += dt*2;
        }

        if(offY > 0) {
            offY -= dt*2;
        }
        if(offY < 0) {
            offY += dt*2;
        }

        int mousePosX = gc.getInput().getMouseX();
        int mousePosY = gc.getInput().getMouseY();
        
        posX += offX;
        posY += offY;
        if(posX < 0)posX = 0;
        if(posX+width > gm.getLevelW())posX = gm.getLevelW()-width;
//        System.out.println(height);
        if(posY < 0)posY = 0;
        if(posY+height > gm.getLevelH())posY = gm.getLevelH()-height;


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


//        System.out.println(rotation);
        mousePosRot = (float)Math.toDegrees(Math.atan2(mousePosY,
                mousePosX) -
                Math.atan2(0,1));
        muzzleAxis = zeroPos.add((gunAxis.getX()+ gunSprite.getTileH()/2)*Math.cos(Math.toRadians(mousePosRot)), (gunAxis.getY())*Math.sin(Math.toRadians(mousePosRot)));
        if(gc.getInput().isKeyDown(KeyEvent.VK_E)){
            gm.addObject(new Bullet(mousePosRot, posX +(int)tankAxis.getX(), posY+(int)tankAxis.getY()));
        }

        gm.check_collisions(this);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
//        r.drawFillRect((int) (posX + (tankAxis.getX())), (int) (posY + (tankAxis.getY())), width, height, 0xffff00ff);
        r.drawImageTile(playerSprite, (int) posX, (int) posY, (int) animation, 0, rotation, tankAxis);
        r.drawImageTile(gunSprite, (int) (posX+ tankAxis.getX()- gunSprite.getTileW()/2), (int) ((int) posY-2), (int) 0, 0, mousePosRot+90, gunAxis);
//        r.drawFillRect((int) (posX + muzzleAxis.getX()), (int) (posY + muzzleAxis.getY()), 4, 4, 0xffff0000);
        r.drawFillCirc((int) (posX + (tankAxis.getX())), (int) (posY + (tankAxis.getY())), (int)(width/2),0x99ff0000);

    }

    @Override
    public void hit(GameObject obj) {
        posX -= offX;
        posY -= offY;
        offX = 0;
        offY = 0;
    }

    @Override
    public Point2D getCenter() {
        return centerPoint;
    }
}
