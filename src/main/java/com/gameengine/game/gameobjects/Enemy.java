package com.gameengine.game.gameobjects;
import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.audio.SoundClip;
import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameManager;
import com.gameengine.game.gameobjects.Multiplayer.PlayerMP;
import com.gameengine.game.gameobjects.world.Tree;
import com.gameengine.game.gameobjects.world.Wall;
import javafx.geometry.Point2D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Enemy object
 * Patrols around 4 points or goes to player
 */
public class Enemy extends GameObject{
    /**
     * Paths to sprites.
     */
    private String pathEnemyImage = "/Player/MagicTank.png";
    private String pathEnemyGunImage = "/Player/MagicTankBarrel.png";
    private String pathEnemyBullet = "/Tile/magicball.png";
    private ImageTile hitSprite = new ImageTile("/Tile/hit_effect.png", 32, 32, 3);
    private ImageTile deathSprite = new ImageTile("/Tile/explosion.png", 64, 64, 2);
    private SoundClip fireSound = new SoundClip("/Sounds/zapSound.mp3", "clip");
    private SoundClip hitSound = new SoundClip("/Sounds/hitSound.mp3", "clip");
    private SoundClip explosionSound = new SoundClip("/Sounds/explosionSound.mp3", "clip");

    /**
     * Parameters for rotation and animation of sprites.
     */
    private ImageTile enemySprite;
    private ImageTile enemygunSprite;
    private ImageTile bulletSprite;
    private int animation;
    private Point2D zeroPos = Point2D.ZERO;
    private Point2D tankAxis;
    private Point2D gunAxis;
    private Point2D centerPoint;


    /**
     * Position of enemy.
     */
    private float offX, offY;
    /**
     * Enemy properties.
     */

    /**
     * Auxiliary parameters.
     */
    private float speed;
    private float rotation;
    private long lastShootTime;
    private int ammoMax;
    private int radiusDetection;
    private float angle_now;
    private int hp = 6;
    private float animX, animY = 0;
    private float hit_animX, hit_animY = 0;
    private boolean hit_anim = false;

    private int curPoint = 0;
    List<Point2D> patrolPoints;

    private boolean followingPlayer = false;

    /**
     * Constructs a enemy object.
     * This public constructor is used for set parameters to enemy.
     * @param posX - start position on axis - X.
     * @param posY - start position on axis - Y.
     * @throws IOException
     */
    public Enemy(int posX, int posY, GameManager gm) throws IOException {
        fireSound.setVolume(0.4);
        hitSound.setVolume(0.4);

        enemySprite = new ImageTile(pathEnemyImage, 32, 32, 3);
        enemygunSprite = new ImageTile(pathEnemyGunImage, 32, 32, 3);
        bulletSprite = new ImageTile(pathEnemyBullet, 32,32, 2);

        this.shape = "circle";
        this.tag = "enemy";
        this.rotation = 0;
        this.offX = 0;
        this.offY = 0;
        this.posX = posX*32;
        this.posY = posY*32;

        this.width = enemySprite.getTileW();
        this.height = enemySprite.getTileH();

        tankAxis = zeroPos.add(enemySprite.getTileW()/2, 22*3);
        gunAxis = zeroPos.add((enemygunSprite.getTileW()/2), 24*3);
        centerPoint = tankAxis;
        speed = 5;
        ammoMax = 1000000;
        lastShootTime = 0;
        radius = (width-15)/2;
        radiusDetection = 7*(width-15)/2;
        rotation = 0;
        angle_now = 0;

        patrolPoints = new ArrayList<>();
        patrolPoints.add(Point2D.ZERO.add(this.posX, this.posY));
        patrolPoints.add(Point2D.ZERO.add(this.posX+400, this.posY));
        patrolPoints.add(Point2D.ZERO.add(this.posX+400, this.posY+400));
        patrolPoints.add(Point2D.ZERO.add(this.posX, this.posY+400));
    }

    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {

        /**
         * Movement of enemy starts.
         */
        // Go to point if not following player
        Point2D point2go = patrolPoints.get(curPoint);
        if(!followingPlayer) {
            if (Point2D.ZERO.add(tankAxis.getX() + posX, tankAxis.getY() + posY).distance(point2go) > 10) {
                enemy_goes_to_point(point2go, dt);
                angle_now = rotation;
            } else {
                curPoint++;
                if (curPoint > 3) {
                    curPoint = 0;
                }
            }
        }
        /**
         * Movement of enemy ends.
         */

        /**
         * Update animation and speed of enemy start.
         */
        Point2D enemy = gm.check_radius(this);
        if(enemy != null){
            enemy_goes_to_point(enemy, dt);
            enemy_shoots_player(enemy, gm);
            followingPlayer = true;
        } else {
            followingPlayer = false;
        }
        gm.check_collisions(this); // detect collisions

        posX += offX;
        posY += offY;

        offX = 0;
        offY = 0;

        if(animation > 2){
            animation = 0;
        }
        /**
         * Update animation and speed of enemy ends.
         */
    }

    /**
     * A public function to set direction to player.
     * @param playerPos - a coordinates of player.
     */
    public void enemy_goes_to_point(Point2D playerPos, float dt){
        float angle = ((float)Math.toDegrees(Math.atan2(playerPos.getY() - (posY+tankAxis.getY()), playerPos.getX() - (posX+tankAxis.getX()))));
        if(Math.abs(angle+90 - rotation) >=3){
            if(rotation > angle+90){
                rotation -= 3;
            } else {
                rotation += 3;
            }
        } else {
            rotation = angle + 90;
            if (dt < 1) {
                offX += (float) Math.cos(Math.toRadians(angle)) * dt * speed * ((float) tankAxis.getX() / 2);
                offY += (float) Math.sin(Math.toRadians(angle)) * dt * speed * ((float) tankAxis.getY() / 2);
            }
        }
    }

    /**
     * A public function to start shooting player.
     * @param playerPos - a coordinates of player.
     * @param gm - a GameManager needs for fireBullet().
     */
    public void enemy_shoots_player(Point2D playerPos, GameManager gm){
        if (ammoMax > 0 && System.currentTimeMillis() - lastShootTime >= 500) {
            float angle = ((float)Math.toDegrees(Math.atan2(playerPos.getY() - (posY+tankAxis.getY()), playerPos.getX() - (posX+tankAxis.getX()))));
            angle_now = angle+90;
            fireBullet(gm, angle);
            lastShootTime = System.currentTimeMillis();
            ammoMax--;
        }
    }

    /**
     * A public function to return a detected radius of enemy.
     * @return int radius.
     */
    public int getDetectRadius(){
        return radiusDetection;
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        if(deathAnimation) {
            if (!hit) {
                death_animation(r);
            }
        } else {
            r.drawImageTile(enemySprite, (int) posX, (int) posY, 0, 0, rotation, tankAxis);
            r.drawImageTile(enemygunSprite, (int) (posX + tankAxis.getX() - enemygunSprite.getTileW() / 2), (int) ((int) posY - 2),
                    (int) 0, 0, angle_now, gunAxis);
        }
        if(hit_anim){
            hitSound.play();
            hit_animation(r);
        }
    }

    @Override
    public void hit(GameObject obj, GameManager gm) {
        if(obj.getClass() ==  Bullet.class){
            this.hp--;
            System.out.println(hp);
            if(hp == 0){
                deathAnimation = true;
            } else {
                hit_anim = true;
            }
        }
        else if(obj.getClass() ==  PlayerMP.class){
            posX -= offX*2;
            posY -= offY*2;
        }
        else if(obj instanceof Wall || obj instanceof Tree) {
            Point2D mainCent = Point2D.ZERO.add(obj.getPosX() + obj.getCenter().getX(), obj.getPosY() + obj.getCenter().getY());
            enemy_shoots_player(mainCent, gm);
            posX -= offX * 3;
            posY -= offY * 3;
            offX = 0;
            offY = 0;
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
                explosionSound.play();
                animY = 0;
                this.dead = true;
            }
        }
    }

    @Override
    public Point2D getCenter() {
        return centerPoint;
    }

    /**
     * A public function to add a bullet.
     */
    public void fireBullet(GameManager gm, float angle){
        gm.addObject(new Bullet(angle, posX + (int) tankAxis.getX(), posY + (int) tankAxis.getY(), tag, bulletSprite));
        fireSound.play();
    }


}
