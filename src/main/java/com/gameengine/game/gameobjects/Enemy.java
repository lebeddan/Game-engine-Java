package com.gameengine.game;
import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.gfx.ImageTile;
import javafx.geometry.Point2D;

import java.io.IOException;

public class Enemy extends GameObject{
    /**
     * Paths to sprites.
     */
    private String pathEnemyImage = "src/main/resources/Player/MagicTank.png";
    private String pathEnemyGunImage = "src/main/resources/Player/MagicTankBarrel.png";
    private String pathEnemyBullet = "/Users/SamSeppi/Desktop/IdeaProjects/TestEngine/src/main/resources/Tile/magicball.png";

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
    private int radiusDetection;
    private float angle_now;
    private float Xmin,Xmax,Ymin,Ymax;
    private float Xrand, Yrand;
    private Point2D v1, v2,v3,v4;
    private int num;
    private double den, cos;

    /**
     * Constructs a enemy object.
     * This public constructor is used for set parameters to enemy.
     * @param posX - start position on axis - X.
     * @param posY - start position on axis - Y.
     * @throws IOException
     */
    public Enemy(int posX, int posY) throws IOException {
        enemySprite = new ImageTile(pathEnemyImage, 32, 32, 3);
        enemygunSprite = new ImageTile(pathEnemyGunImage, 32, 32, 3);
        bulletSprite = new ImageTile(pathEnemyBullet, 32,32, 2);
        this.shape = "circle";
        this.tag = "enemy";
        this.tileX = posX;
        this.tileY = posY;
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
        speed = 2;
        moveCoolDownTime=.5*1000;
        movedTime = 0;
        moveAngle = 0;
        ammoMax = 1000000;
        lastShootTime = 0;
        radius = (width-15)/2;
        radiusDetection = 3*(width-15)/2;
        System.out.println("COS " +angle_now);
        turnspeed = 3;
        power = 3;
        angularDrag = (float)0.9;
        drag = (float)0.15;
        rotation = 120;
        angle_now = 120;
    }

    @Override
    public void update(GameContainer gc, GameManager gm, float dt) {
//        System.out.println("posX: " + posX + " posY: " + posY);
        /**
         * Movement of enemy starts.
         */
//
        double currentTime=System.currentTimeMillis();
        if ((currentTime-movedTime)>=moveCoolDownTime) {
            Xmin = (float) (radiusDetection - tankAxis.getX());
            Xmax = (float) (radiusDetection + tankAxis.getX());
            Ymin = (float) (radiusDetection - tankAxis.getY());
            Ymax = (float) (radiusDetection + tankAxis.getY());
            Xrand = (float)(Math.random() * ((Xmax - Xmin) + 1)) + Xmin;
            Yrand = (float)(Math.random() * ((Ymax - Ymin) + 1)) + Ymin;
            System.out.println("X " + Xrand);
            System.out.println("Y " + Yrand);
            v1 = new Point2D(Xrand, 0);
            v2 = new Point2D(0, Yrand);
            v3 = v2.subtract(v1);
            v4 = new Point2D(tankAxis.getX(), tankAxis.getY());
            angle_now = (float) v4.angle(v3);
            System.out.println("angle1 " +angle_now);
            movedTime = currentTime;

        }

        if(true){
            offX += (float) Math.cos(Math.toRadians(angle_now) * (float)tankAxis.getX())*4;
            offY += (float) Math.sin(Math.toRadians(angle_now) * (float)tankAxis.getY())*4;
            rotation = angle_now;

        }


//        offX += (float) Math.cos(Math.toRadians(angle_now) * (float)tankAxis.getX())*4;
//        offY += (float) Math.sin(Math.toRadians(angle_now) * (float)tankAxis.getY())*4;
        animation++;
        System.out.println("DT: "+dt);
        System.out.println("offX: "+offX);
        System.out.println("offY: "+offY);



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
            offX -= dt*drag;
        }else if(offY > 0){
            offY -= dt*drag;
        }

        if (offX < 0){
            offX += 2*power;
        }else if(offY < 0){
            offY += 2*power;
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
    public void enemy_goes_to_player(Point2D playerPos, float dt){
        float angle = ((float)Math.toDegrees(Math.atan2(playerPos.getY() - tankAxis.getY(), playerPos.getX() - tankAxis.getX())));
        System.out.println(angle);
//        if(rotation != angle){
//            rotation += 3;
//
//            if (playerPos.getX() == tankAxis.getX())
//                offX = 0;
//                offY = 0;
//        }
        offX -= (float) Math.cos(Math.toRadians(angle) * ((float) tankAxis.getX()/2))*dt;
        offY -= (float) Math.sin(Math.toRadians(angle) * ((float) tankAxis.getY()/2))*dt;

//        if(playerPos.getY() > tankAxis.getY()){
//            offY += (float) Math.sin(Math.toRadians() * ((float) tankAxis.getY()/2))*speed;
//            if (playerPos.getY() == tankAxis.getY())
//                offY = 0;
//        }

        if (offX > 0){
            offX -= speed;
        }else if(offY > 0){
            offY -= speed;
        }

        if (offX < 0){
            offX += power*dt;
        }else if(offY < 0){
            offY += power*dt;
        }

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
//            Packet03Bullet packet = new Packet03Bullet(this.tag, posX, posY, rotation);
//            packet.writeData(gm.socketClient);
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
        return radiusDetection;
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawImageTile(enemySprite, (int)posX, (int)posY, 0, 0, rotation, tankAxis);
        r.drawImageTile(enemygunSprite, (int) (posX+ (int)tankAxis.getX()-6), (int) ((int) posY+4),
                (int) 0, 0, gunPos, gunAxis);
        r.drawFillCirc((int) (posX+centerPoint.getX()), (int) (posY+centerPoint.getY()), radiusDetection,0x99ff0000);
//        r.drawFillRect(0,0, 100, 100, 0x80ff0000);
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
