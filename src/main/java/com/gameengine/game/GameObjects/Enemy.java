package com.gameengine.game.GameObjects;
import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.game.GameManager;
import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.Server.packets.Packet03Bullet;
import javafx.geometry.Point2D;

import java.io.IOException;

public class Enemy extends GameObject{
    /**
     * Paths to sprites.
     */
    private String pathEnemyImage = "/Users/SamSeppi/Desktop/TankSouls/src/Resources/Player/MagicTank.png";
    private String pathEnemyGunImage = "/Users/SamSeppi/Desktop/TankSouls/src/Resources/Player/MagicTankBarrel.png";
    private String pathEnemyBullet = "/Users/SamSeppi/Desktop/TankSouls/src/Resources/Tile/fireball.png";

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
    private int tileX, tileY; // position X, Y
    /**
     * Enemy properties.
     */
    private float drag; // the rotation of the car, in radians
    private float angularVelocity; // speed the car is spinning, in radians
    private float angularDrag; //  how fast the car stops spinning
    private float power; // how fast car can accelerate
    private int turnspeed;// how fast to turn

    /**
     * Auxiliary parameters.
     */
    private String Direction;
    private String[] Movement;
    private float speed;
    private float rotation;
    private double moveCoolDownTime;
    private double movedTime;
    private float gunPos;
    private float moveAngle;
    private long lastShootTime;
    private int ammoMax;

    /**
     * Constructs a enemy object.
     * This public constructor is used for set parameters to enemy.
     * @param posX - start position on axis - X.
     * @param posY - start position on axis - Y.
     * @throws IOException
     */
    public Enemy(int posX, int posY) throws IOException {
        enemySprite = new ImageTile(pathEnemyImage, 72, 86);
        enemygunSprite = new ImageTile(pathEnemyGunImage, 33, 61);
        bulletSprite = new ImageTile(pathEnemyBullet,32,32,2);
        tankAxis = zeroPos.add(enemySprite.getTileW()/2, (enemySprite.getTileH()/2));
        gunAxis = zeroPos.add((enemygunSprite.getTileW()/2), (enemygunSprite.getTileH()/2));
        centerPoint = tankAxis;
        this.shape = "circle";
        this.tag = "enemy";
        this.tileX = posX;
        this.tileY = posY;
        this.offX = 0;
        this.offY = 0;
        this.posX = posX*16;
        this.posY = posY*16;
        this.width = (int) (enemySprite.getTileW() + tankAxis.getX());
        this.height =(int) (enemySprite.getTileH() + tankAxis.getY());
        speed = 3;
        moveCoolDownTime=.5*1000;
        movedTime = 0;
        moveAngle = 0;
        ammoMax = 1000000;
        lastShootTime = 0;
        radius = 10*(width-15)/2;
        turnspeed = 3;
        power = 2;
        angularDrag = (float)0.9;
        drag = (float)0.15;
        Movement = new String[]{"UP", "DOWN", "LEFT", "RIGHT", "STOP"};
        Direction = Movement[(int)(Math.random()*5)];
        rotation = 0;
    }

    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {
        /**
         * Movement of enemy starts.
         */
        Point2D playerPos = gm.check_radius(this); // here checks, if player in radius of enemy.
        if(playerPos != null){
            enemy_goes_to_player(playerPos);
            enemy_shoots_player(playerPos, gm);
        }else{
            double currentTime=System.currentTimeMillis();
            if ((currentTime-movedTime)>=moveCoolDownTime) {
                Direction = Movement[(int)(Math.random()*5)];
                movedTime = currentTime;
            }

            if (Direction.equals("UP")){
                offX += (float) Math.cos(Math.toRadians(rotation + (moveAngle * Math.PI/180)-90) * ((float) tankAxis.getX()/2))*speed;
                offY += (float) Math.sin(Math.toRadians(rotation + (moveAngle * Math.PI/180)-90) * ((float) tankAxis.getY()/2))*speed;
                animation++;
            }else if (Direction.equals("DOWN")){
                offX -= (float) Math.cos(Math.toRadians(rotation + (moveAngle * Math.PI/180)-90) * ((float) tankAxis.getX()/2))*speed;
                offY -= (float) Math.sin(Math.toRadians(rotation + (moveAngle * Math.PI/180)-90) * ((float) tankAxis.getY()/2))*speed;
                animation++;
            }else if (Direction.equals("LEFT")){
                rotation = (rotation-5)%360;
                moveAngle = -1;
                gunPos -= 5;
                if (gunPos < 0){
                    gunPos = 360;
                }
            }else if(Direction.equals("RIGHT")){
                rotation = (rotation+5)%360;
                moveAngle = 1;
                gunPos += 5;
                if (gunPos > 360){
                    gunPos = 0;
                }
            }else{
                offX = 0;
                offY = 0;
            }
        }
        /**
         * Movement of enemy ends.
         */

        /**
         * Update animation and speed of enemy start.
         */
        posX += offX;
        posY += offY;

        offX *= drag;
        offY *= drag;

        gm.check_collisions(this); // detect collisions

        if (offX > 0){
            offX -= speed;
        }else if(offY > 0){
            offY -= speed;
        }

        if (offX < 0){
            offX += speed*power;
        }else if(offY < 0){
            offY += speed*power;
        }

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
    public void enemy_goes_to_player(Point2D playerPos){
        float angle = (float) playerPos.angle(playerPos.getX(), playerPos.getY());
        offX += (float) Math.cos(Math.toRadians(angle * ((float) tankAxis.getX()/2)))*speed;
        offX += (float) Math.cos(Math.toRadians(angle * ((float) tankAxis.getX()/2)))*speed;
        if (gunPos <= angle){
            gunPos += 5;
        }else{
            gunPos -=5;
        }
    }

    /**
     * A public function to start shooting player.
     * @param playerPos - a coordinates of player.
     * @param gm - a GameManager needs for fireBullet().
     */
    public void enemy_shoots_player(Point2D playerPos, GameManager gm){
        if (ammoMax > 0 && System.currentTimeMillis() - lastShootTime >= 300) {
            fireBullet(gm);
            Packet03Bullet packet = new Packet03Bullet(this.tag, posX, posY, rotation);
            packet.writeData(gm.socketClient);
            lastShootTime = System.currentTimeMillis();
            ammoMax--;
            System.out.println("Ammo: " + ammoMax);
        }
    }

    /**
     * A public function to return a detected radius of enemy.
     * @return int radius.
     */
    public int getDetectRadius(){
        return radius;
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawImageTile(enemySprite, (int) posX, (int) posY, (int) animation, 0, rotation, tankAxis);
        r.drawImageTile(enemygunSprite, (int) (posX+ (int)tankAxis.getX()-6), (int) ((int) posY+4),
                (int) 0, 0, gunPos, gunAxis);
    }

    @Override
    public void hit(GameObject obj) {
        posX -= offX * 2;
        posY -= offY * 2;
        offX = 0;
        offY = 0;
    }

    @Override
    public Point2D getCenter() {
        return centerPoint;
    }

    /**
     * A public function to add a bullet.
     */
    public void fireBullet(GameManager gm){
        gm.addObject(new Bullet(gunPos, posX + (int) tankAxis.getX(), posY + (int) tankAxis.getY(), tag, bulletSprite));
    }


}
