package com.gameengine.game.gameobjects;

import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameManager;
import javafx.geometry.Point2D;

public class Bullet extends GameObject {
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
//        System.out.println(posToDraw.getX() + " " + posToDraw.getY() + " " + direction);
        this.offX = offX;
        this.offY = offY;
    }

    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {
//        switch (direction){
//            case 0: offY -= speed * dt; break;
//            case 1 : offX += speed * dt; break;
//            case 2: offY += speed * dt; break;
//            case 3 : offX -= speed * dt; break;
//        }

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

//        if(gm.getCollision(tileX, tileY)){
//            this.dead = true;
//        }

        if(sent_pos.distance(Point2D.ZERO.add(posX, posY)) > 1300){
            this.dead = true;
        }

        posX = tileX * GameManager.TS + offX;
        posY = tileY * GameManager.TS + offY;
//        System.out.println(posX + " " + posY);

        animationX += dt * 20;
//        System.out.println((int)animationX);
        if(animationX > 5){
//            System.out.println(dt);
            animationX = 0;
            animationY += 1;
//            System.out.println(animationX + " " + animationY);
            if(animationY > 4){
                animationY = 0;
            }
        }
//        animationY++;
//        animationY = animationY%5;
//        System.out.println(animationX);

        gm.check_collisions(this);

//        System.out.println(travel_dist);
//        travel_dist += Math.sqrt(Math.pow(posX - offX, 2) + Math.pow(posX - offX, 2)) - travel_dist;
//        travel_dist += posX - offX - travel_dist;
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
//        r.drawFillRect((int)posX-2, (int)posY-2, 6, 6, 0xffff00ff);
        r.drawImageTile(bulletSprite, (int)(posX + posToDraw.getX() - width/2), (int)(posY + posToDraw.getY()- height/2),
                (int)animationX, (int)animationY, (int)direction, bulletAxis);
//        r.drawFillCirc((int)(posX +posToDraw.getX()), (int)(posY+posToDraw.getY()), radius, 0x99ff0000);
//        r.drawFillCirc((int)(posX + centerPoint.getX()), (int)(posY+centerPoint.getY()), radius, 0x99ff0000);

    }

    @Override
    public void hit(GameObject obj) {
//        if(obj.getClass() == Tree.class){
            dead = true;
//        }
    }

    @Override
    public Point2D getCenter() {
        return centerPoint;
    }

    public void setRotation(float rotation) {
        this.direction = rotation;
    }
}
