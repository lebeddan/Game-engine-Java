package com.gameengine.game.gameobjects.world;

import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameManager;
import com.gameengine.game.gameobjects.GameObject;
import com.gameengine.game.gameobjects.Bullet;
import javafx.geometry.Point2D;

import java.io.IOException;

public class Tree extends GameObject {

    private ImageTile treeSprite = new ImageTile("/Tile/myspritesheet.png", 32, 32, 4);
    private ImageTile hitSprite = new ImageTile("/Tile/hit_effect.png", 32, 32, 3);
    private ImageTile deathSprite = new ImageTile("/Tile/explosion.png", 64, 64, 2);
    private int hp = 3;
    private float animX, animY = 0;
    private float hit_animX, hit_animY = 0;
    private boolean hit_anim = false;
    private Point2D centerPoint;

    public Tree(String tag, int posX, int posY) throws IOException {
        this.shape = "circle";
        this.tag = tag;
        this.posX = posX;
        this.posY = posY;
        this.width = 128;
        this.height = 128;
        this.radius = (width-10*3)/2;
        centerPoint = Point2D.ZERO.add(width/2, height/2);
    }

    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {

    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        if(deathAnimation){
            if(!hit) {
                death_animation(r);
            }
        } else {
//        r.drawFillRect((int) posX, (int) posY,width,height,0x99ff0000);
            r.drawImageTile(treeSprite, (int) posX, (int) posY, 1, 0, 0);
//        r.drawFillCirc((int) (posX+centerPoint.getX()), (int) (posY+centerPoint.getY()), radius,0x99ff0000);
        }
        if(hit_anim){
            hit_animation(r);
        }
    }

    @Override
    public void hit(GameObject obj, GameManager gm) {
        if(obj.getClass() == Bullet.class){
            bullet_hit();
        }
    }

    @Override
    public Point2D getCenter() {
        return centerPoint;
    }

    public void bullet_hit(){
        hp--;
        if(hp == 0){
            deathAnimation = true;
        } else {
            hit_anim = true;
        }
    }

    private void hit_animation(Renderer r){
        r.drawImageTile(hitSprite, (int) (posX)+12, (int) (posY)+14, (int)hit_animX, (int)hit_animY, 0);
        hit_animX += 0.015 * 10;
        if(hit_animX > 1){
            hit_animX = 0;
            hit_animY += 1;
            if(hit_animY > 1){
                hit_animY = 0;
                hit_anim = false;
            }
        }
    }

    private void death_animation(Renderer r){
        r.drawImageTile(deathSprite, (int) posX, (int) posY, (int)animX, (int)animY, 0);
        animX += 0.015 * 10;
        if(animX > 2){
            animX = 0;
            animY += 1;
            if(animY > 2){
                animY = 0;
                this.dead = true;
            }
        }
    }
}
