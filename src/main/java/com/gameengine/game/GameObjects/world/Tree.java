package com.gameengine.game.GameObjects.world;

import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameManager;
import com.gameengine.game.GameObjects.GameObject;
import com.gameengine.game.GameObjects.Bullet;
import javafx.geometry.Point2D;

import java.io.IOException;

public class Tree extends GameObject {

    private ImageTile treeSprite = new ImageTile("src/Resources/Tile/grass.png", 32, 32, 4);
    private ImageTile hitSprite = new ImageTile("src/Resources/Tile/hit_anim.png", 32, 32, 4);
    private int hp = 3;
    private int death_frames = 30;
    private int hit_frames = 5;
    private Point2D centerPoint;

    public Tree(String tag, int posX, int posY) throws IOException {
        this.shape = "circle";
        this.tag = tag;
        this.posX = posX;
        this.posY = posY;
        this.width = 128;
        this.height = 128;
        this.radius = width/2;
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
            r.drawImageTile(treeSprite, (int) posX, (int) posY, 2, 0, 0);
//        r.drawFillCirc((int) (posX+centerPoint.getX()), (int) (posY+centerPoint.getY()), radius,0x99ff0000);
        }
        if(hit || hit_frames < 10){
            hit_animation(r);
        }
    }

    @Override
    public void hit(GameObject obj) {
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
            try {
                treeSprite = new ImageTile("src/Resources/Tile/DEAD.png", 64, 32, 2);
                deathAnimation = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
//            this.dead = true;
        } else {
            hit = true;
        }
    }

    private void hit_animation(Renderer r){
        r.drawImageTile(hitSprite, (int) posX, (int) posY, 0, 0, 0);
        hit_frames--;
        if (hit_frames == 0){
            hit_frames = 10;
        }
        hit = false;
    }

    private void death_animation(Renderer r){
        r.drawImageTile(treeSprite, (int) posX, (int) posY, 0, 0, 0);
        death_frames--;
        if(death_frames == 0){
            dead = true;
        }
    }
}
