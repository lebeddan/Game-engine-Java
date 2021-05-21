package com.gameengine.game.gameobjects.world;

import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameManager;
import com.gameengine.game.gameobjects.GameObject;
import com.gameengine.game.gameobjects.Bullet;
import javafx.geometry.Point2D;

import java.io.IOException;
/**
 * Tree class. Game object which as a part of a game.
 * @author Vasily Levitskiy
 */
public class Tree extends GameObject {
    /**
     * Parametrs of tree starts.
     */
    private ImageTile treeSprite = new ImageTile("src/main/resources/Tile/myspritesheet.png", 32, 32, 4);
    private ImageTile hitSprite = new ImageTile("src/main/resources/Tile/hit_effect.png", 32, 32, 3);
    private ImageTile deathSprite = new ImageTile("src/main/resources/Tile/explosion.png", 64, 64, 2);
    private int hp = 3;
    private int death_frames = 30;
    private int hit_frames = 5;
    private float animX, animY = 0;
    private float hit_animX, hit_animY = 0;
    private boolean hit_anim = false;
    private Point2D centerPoint;
    /**
     * Parametrs of tree ends.
     */

    /**
     * A public constructor for creating a tree object.
     * @param tag - a tag of tree
     * @param posX - start position on X axis of tree
     * @param posY - start position on Y axis of tree
     * @throws IOException
     */
    public Tree(String tag, int posX, int posY) throws IOException {
        this.shape = "circle"; // for detection collisions
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
            r.drawImageTile(treeSprite, (int) posX, (int) posY, 1, 0, 0);
        }
        if(hit_anim){
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

    /**
     * A public function for to lower the hp of a tree
     * when a player or enemy shoots at it
     */
    public void bullet_hit(){
        hp--;
        if(hp == 0){
            deathAnimation = true;
        } else {
            hit_anim = true;
        }
    }

    /**
     * A public function for draw a hit animation
     */
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

    /**
     * A public function for draw a death animation
     */
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
