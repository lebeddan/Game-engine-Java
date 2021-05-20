package com.gameengine.game.gameobjects;
import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameManager;
import javafx.geometry.Point2D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Enemy extends GameObject{
    /**
     * Paths to sprites.
     */
    private String pathEnemyImage = "src/main/resources/Player/MagicTank.png";
    private String pathEnemyGunImage = "src/main/resources/Player/MagicTankBarrel.png";
    private String pathEnemyBullet = "src/main/resources/Tile/magicball.png";

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
    private GameManager gm;

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
        this.gm = gm;
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
        speed = 5;
        moveCoolDownTime=.9*1000;
        movedTime = 0;
        moveAngle = 0;
        ammoMax = 1000000;
        lastShootTime = 0;
        radius = (width-15)/2;
        radiusDetection = 7*(width-15)/2;
        System.out.println("COS " +angle_now);
        turnspeed = 3;
        power = 3;
        angularDrag = (float)0.9;
        drag = (float)0.15;
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
//        System.out.println("posX: " + posX + " posY: " + posY);
        /**
         * Movement of enemy starts.
         */
        Point2D point2go = patrolPoints.get(curPoint);
        double currentTime=System.currentTimeMillis();
//        if ((currentTime-movedTime)>=moveCoolDownTime) {
        if(!followingPlayer) {
            if (Point2D.ZERO.add(tankAxis.getX() + posX, tankAxis.getY() + posY).distance(point2go) > 10) {
//                System.out.println(posX +" "+point2go.getX() +" bb "+posY+" "+point2go.getY());
                enemy_goes_to_point(point2go, dt);
                movedTime = currentTime;
                angle_now = rotation;
            } else {
                curPoint++;
                if (curPoint > 3) {
                    curPoint = 0;
                }
            }
        }
//        }
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

//        if (offX > 0){
//            offX -= drag;
//        }else if(offY > 0){
//            offY -= drag;
//        }
//
//        if (offX < 0){
//            offX += 2*power;
//        }else if(offY < 0){
//            offY += 2*power;
//        }

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
//        System.out.println("Rot: "+rotation + " "+angle);

//        if(playerPos.getY() > tankAxis.getY()){
//            offY += (float) Math.sin(Math.toRadians() * ((float) tankAxis.getY()/2))*speed;
//            if (playerPos.getY() == tankAxis.getY())
//                offY = 0;
//        }

//        if (offX > 0){
//            offX -= speed;
//        }else if(offY > 0){
//            offY -= speed;
//        }
//
//        if (offX < 0){
//            offX += power*dt;
//        }else if(offY < 0){
//            offY += power*dt;
//        }
//
//        if (gunPos <= angle){
//            gunPos += 5;
//        }else{
//            gunPos -=5;
//        }
    }

    /**
     * A public function to start shooting player.
     * @param playerPos - a coordinates of player.
     * @param gm - a GameManager needs for fireBullet().
     */
    public void enemy_shoots_player(Point2D playerPos, GameManager gm){
        if (ammoMax > 0 && System.currentTimeMillis() - lastShootTime >= 300) {
            float angle = ((float)Math.toDegrees(Math.atan2(playerPos.getY() - (posY+tankAxis.getY()), playerPos.getX() - (posX+tankAxis.getX()))));
            angle_now = angle+90;
            fireBullet(gm, angle);
//            System.out.println(angle +" "+ playerPos.getX()+" "+playerPos.getY());
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
        r.drawImageTile(enemygunSprite, (int) (posX+ tankAxis.getX()- enemygunSprite.getTileW()/2), (int) ((int) posY-2),
                (int) 0, 0, angle_now, gunAxis);
        r.drawFillCirc((int) (posX+centerPoint.getX()), (int) (posY+centerPoint.getY()), radiusDetection,0x99ff0000);
//        r.drawFillRect(0,0, 100, 100, 0x80ff0000);
    }

    @Override
    public void hit(GameObject obj) {
        Point2D mainCent = Point2D.ZERO.add(obj.getPosX()+obj.getCenter().getX(), obj.getPosY()+obj.getCenter().getY());
        enemy_shoots_player(mainCent, gm);
        System.out.println("Enenym shoots at: "+mainCent.getX() +" " + mainCent.getY());
        if(followingPlayer){
            posX -= offX*2;
            posY -= offY*2;
        }
        posX -= offX*3;
        posY -= offY*3;
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
    public void fireBullet(GameManager gm, float angle){
        gm.addObject(new Bullet(angle, posX + (int) tankAxis.getX(), posY + (int) tankAxis.getY(), tag, bulletSprite));
    }


}
