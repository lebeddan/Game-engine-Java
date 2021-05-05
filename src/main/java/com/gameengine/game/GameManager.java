package com.gameengine.game;

import com.gameengine.engine.AbstractGame;
import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.game.GameObjects.Camera;
import com.gameengine.game.GameObjects.GameObject;
import com.gameengine.game.GameObjects.Player;
import com.gameengine.game.MapObjects.Chunk;
import com.gameengine.game.MapObjects.Map;
import com.gameengine.game.MapObjects.TiledMap;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameManager extends AbstractGame {

    public static final int TS = 128;
    private static Canvas mainCanvas;

    private boolean[] collision;
    private int levelW, levelH, levelWChunks, chunk_width, chunk_height;

    private Camera camera;
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();

    private Map map;
    private List<Chunk> loaded_chunks;
    private int cur_chunk;

    public GameManager() throws IOException {
        objects.add(new Player(5, 5));
        camera = new Camera("player");
        loadLevel("src/Resources/Maps/loadtest.json");
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("hi");
        GameContainer gc = new GameContainer(this);

        //TODO: FIX STUFF WITH CAMERA
        gc.setWidth(1280);
        gc.setHeight(720);
        gc.setScale(1);
        gc.start();
//        gc.getRenderer().setAmbientColor(-1);
        stage.setScene(gc.getWindow().getMainScene());
        stage.show();
    }

    @Override
    public void init(GameContainer gc){
        gc.getRenderer().setAmbientColor(-1);
    }

    // TODO: Make a class that loads a level
    public void loadLevel(String path) throws IOException {
        map = TiledMap.create_map(path);
        loaded_chunks = new ArrayList<Chunk>();
        loaded_chunks.add(map.get_chunk(0));
        cur_chunk = -1;
        levelW = map.getLevel_width();
        levelH = map.getLevel_height();
        levelWChunks = map.getChunk_width();
        chunk_width = map.get_cWidth()*TS;
        chunk_height = map.get_cHeight()*TS;
    }

    @Override
    public void update(GameContainer gc, float deltaTime){
        gc.getRenderer().setAmbientColor(-1);
        int cur_pos_x = (int)Math.floor(getObject("player").getPosX()/chunk_width);
        int cur_pos_y = (int)Math.floor(getObject("player").getPosY()/chunk_height);
        if(cur_chunk != cur_pos_x + cur_pos_y*levelWChunks){
            cur_chunk = cur_pos_x + cur_pos_y*levelWChunks;
            System.out.println(cur_pos_y);
            System.out.println(cur_pos_x);
            loaded_chunks.clear();

            for(int i = cur_pos_y-1; i <= cur_pos_y+1; i++) {
                for(int j = cur_pos_x-1; j <= cur_pos_x+1; j++) {
                    if(j >= 0 && j < levelWChunks && i >= 0 && i < map.getChunk_height()) {
                        if(j + i*levelWChunks < map.getChunk_height()*levelWChunks){
                            loaded_chunks.add(map.get_chunk(j + i*levelWChunks));
                        }
//                        System.out.println("Chunk to load: " + i + " " + j);
                    }
                }
            }
            System.out.println("-----------");

//            System.out.println(loaded_chunks.toArray().length + " " + cur_chunk);
        }
//        if(cur_pos != cur_chunk){
//            loaded_chunks.add(map.get_chunk(cur_chunk++));
//            System.out.println(cur_chunk);
//        }
        for(int i = 0; i < objects.size(); i++){
            objects.get(i).update(gc, this, deltaTime);
            if(objects.get(i).isDead()){
                objects.remove(i);
                i--;
            }
        }
        for(Chunk chunk : loaded_chunks){
            chunk.getObjects().removeIf(GameObject::isDead);
        }
        camera.update(gc, this, deltaTime);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        // TODO: Update the boundaries in all of the drawing methods.
        //  OPTIMIZE the upper boundary of the drawing.
        //  Currently adds to array even the parts that arent seen.
        //  The entities are loading incorrectly. Or chunks.


        camera.render(r);

//        r.drawImage(skyImage, 0, 0);
//        r.drawImage(tileImage, 0, 0);

//        for (int i = 0; i < loaded_chunks.size(); i++){
//            Chunk curCh = map.get_chunk(i);
//            r.drawChunk(curCh, (int)(i%2)*curCh.getWidth(), (int)(i/2)*curCh.getHeight());
//        }

//        printObjectSize(map.get_chunk(cur_chunk));
//        System.out.println(ObjectSizeCalculator);
//        r.drawChunk(map.get_chunk(cur_chunk), map.get_chunk(cur_chunk).getPosX(), map.get_chunk(cur_chunk).getPosY());
//        System.out.println("Chunks loaded: " + loaded_chunks.toArray().length);
        for(int i = 0; i < loaded_chunks.toArray().length; i++){
            Chunk chnk = loaded_chunks.get(i);
//            System.out.println(loaded_chunks.toArray().length);
            r.drawChunk(chnk, chnk.getPosX(), chnk.getPosY());
        }


        for(GameObject obj : objects){
            obj.render(gc, r);
        }

        for(Chunk cnk : loaded_chunks){
            for(GameObject obj : cnk.getObjects()){
                obj.render(gc, r);
            }
        }
        r.drawFillRect(0,0, 100, 100, 0x80ff0000);
    }

    public static void main(String args[]) throws IOException {
        launch(args);
    }

    public boolean getCollision(int x, int y){
        if(x < 0 || x >= levelW || y < 0 || y >= levelH){
            return true;
        }
        return collision[x + y * levelW];
    }

    public void addObject(GameObject obj){
        objects.add(obj);
    }

    public GameObject getObject(String tag){
        for(int i = 0; i < objects.size(); i++){
            if(objects.get(i).getTag().equals(tag)){
                return objects.get(i);
            }
        }

        return null;
    }

    public void check_collisions(GameObject mainObj){
        String mainType = mainObj.getShape();
        for(Chunk chunk : loaded_chunks) {
            for (GameObject obj : chunk.getObjects()){
                if(obj.getShape().equals("square")) {
                    int centerX = (int) ( mainObj.getPosX() + mainObj.getRadius());
                    int centerY = (int) (mainObj.getPosY() + mainObj.getRadius());
                    Point2D center = Point2D.ZERO.add(mainObj.getPosX()+ mainObj.getRadius(), mainObj.getPosY()+ mainObj.getRadius());
                    int sqHalfX = obj.getWidth()/2;
                    int sqHalfY = obj.getHeight()/2;

                    Point2D aabb = Point2D.ZERO.add(sqHalfX+obj.getPosX(), sqHalfY+obj.getPosY());

                    int diffX = (int) (center.getX() - aabb.getX());
                    int diffY = (int) (center.getY() - aabb.getY());

                    int diffClampX = clamp(diffX, -sqHalfX, sqHalfX);
                    int diffClampY = clamp(diffY, -sqHalfY, sqHalfY);

//                    int closeX = sqHalfX + diffClampX;
//                    int closeY = sqHalfY + diffClampY;
                    Point2D closest = Point2D.ZERO.add(aabb.getX() + diffClampX, aabb.getY() + diffClampY);

                    if(closest.distance(center) < 300) {
                        System.out.println(mainObj.getRadius());
                    }
                    if(closest.distance(center) < mainObj.getRadius()){
                        System.out.println("Nice");
                        obj.hit(mainObj);
                        mainObj.hit(obj);
                    }
                } else if (obj.getShape().equals("circle")){
                    Point2D mainCent = Point2D.ZERO.add(mainObj.getPosX()+mainObj.getCenter().getX(), mainObj.getPosY()+mainObj.getCenter().getY());
                    Point2D objCent = Point2D.ZERO.add(obj.getPosX()+obj.getCenter().getX(), obj.getPosY()+obj.getCenter().getY());
                    if(mainCent.distance(objCent) < (mainObj.getRadius()+obj.getRadius())){
                        obj.hit(mainObj);
                        mainObj.hit(obj);
                    }
                } else if (obj.getShape().equals("someShape")){
                    boolean collisionX = (mainObj.getPosX() + mainObj.getWidth() >= obj.getPosX()) && (obj.getPosX() + obj.getWidth() >= mainObj.getPosX());
                    boolean collisionY = (mainObj.getPosY() + mainObj.getHeight() >= obj.getPosY()) && (obj.getPosY() + obj.getHeight() >= mainObj.getPosY());

                    if (collisionX && collisionY) {
                        System.out.println("Nice");
//                        mainObj.dead = true;
                        mainObj.hit(obj);
                        obj.hit(mainObj);
                    }
                }
            }
        }
    }

    public int getLevelW() {
        return levelW;
    }

    public int getLevelH() {
        return levelH;
    }

    public int clamp(int value, int min, int max){
        return  Math.max(min, Math.min(max, value));
    }
}
