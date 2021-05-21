package com.gameengine.game.gameobjects.world;

import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameManager;
import com.gameengine.game.gameobjects.Bullet;
import com.gameengine.game.gameobjects.GameObject;
import javafx.geometry.Point2D;

import java.io.IOException;

public class Wall extends GameObject {

    private ImageTile wallSprite = new ImageTile("/Tile/myspritesheet.png", 16, 16, 4);
    private ImageTile hitSprite = new ImageTile("/Tile/hit_effect.png", 32, 32, 3);
    private ImageTile deathSprite = new ImageTile("/Tile/explosion.png", 64, 64, 2);
    private int hp = 3;
    private int death_frames = 30;
    private int hit_frames = 5;
    private Point2D centerPoint;
    private float animX, animY = 0;
    private int oldPosX, oldPosY;
    private float hit_animX, hit_animY = 0;
    private boolean hit_anim = false;
    private int imTWidth;
    private int number;

    public Wall(String tag, int posX, int posY, int number) throws IOException {
        this.shape = "square";
        this.number = number;
        this.tag = tag;
        this.posX = posX;
        this.posY = posY;
        oldPosX = posX;
        oldPosY = posY;
        this.width = 48;
        this.height = 48;
        imTWidth = wallSprite.getWidth()/ wallSprite.getTileW();
        switch (number){
            case 26:
                this.posX += 4*4;
                this.width -= 16;
                this.height = 64;
                break;
            case 27:
                this.posX += 16;
                this.posY += 16;
                break;
            case 29:
                this.posY += 16;
                break;
            case 31:
                this.posY += 4*4;
                this.height -= 16;
                this.width = 64;
                break;
            case 37:
                this.posX += 16;
                break;
            case 39:
                break;
        }
        this.number--;

        centerPoint = Point2D.ZERO.add(width/2, height/2);
    }

    public Wall() throws IOException {
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
            r.drawImageTile(wallSprite, oldPosX, oldPosY, number % imTWidth, number / imTWidth, 0);
//        r.drawFillRect((int) posX, (int) posY,width,height,0x99ff0000);
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

    private void hit_animation(Renderer r){
        r.drawImageTile(hitSprite, (int) oldPosX, (int) oldPosY, (int)hit_animX, (int)hit_animY, 0);
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

    public void bullet_hit(){
        hp--;
        if(hp == 0){
            deathAnimation = true;
        } else {
            hit_anim = true;
        }
    }

    private void death_animation(Renderer r){
        r.drawImageTile(deathSprite, (int) oldPosX-width/2, (int) oldPosY-height/2, (int)animX, (int)animY, 0);
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
