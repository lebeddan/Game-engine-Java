package com.gameengine.game;

import com.gameengine.engine.AbstractGame;
import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.gfx.Image;
import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameObjects.Camera;
import com.gameengine.game.GameObjects.Tank;
import com.gameengine.game.GameObjects.world.Tree;
import com.gameengine.game.MapObjects.Chunk;
import com.gameengine.game.MapObjects.Map;
import com.gameengine.game.MapObjects.TiledMap;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameManager extends AbstractGame {

    public static final int TS = 128;
    private static Canvas mainCanvas;

    private Image skyImage = new Image("C:\\Users\\Vasily\\IdeaProjects\\TestEngine\\src\\Resources\\sky.png");
    private Image tileImage = new Image("C:\\Users\\Vasily\\IdeaProjects\\TestEngine\\src\\Resources\\bgcolor.png");

    private boolean[] collision;
    private int levelW, levelH;

    private Camera camera;
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();

    private Map map;
    private List<Chunk> loaded_chunks;
    private int cur_chunk;

    public GameManager() throws IOException {
        objects.add(new Tank(0, 0));
        camera = new Camera("player");
        map = TiledMap.create_map("src/Resources/Maps/loadtest.json");
        loaded_chunks = new ArrayList<Chunk>();
        loaded_chunks.add(map.get_chunk(0));
        cur_chunk = -1;
        loadLevel("src/test/resources/bw.png");
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("hi");
        GameContainer gc = new GameContainer(new GameManager());

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
        Image lebelImage = new Image(path);
        levelW = map.getLevel_width();
        levelH = map.getLevel_height();
        collision = new boolean[levelW * levelH];
    }

    @Override
    public void update(GameContainer gc, float deltaTime){
        gc.getRenderer().setAmbientColor(-1);
//        System.out.println(Math.round(getObject("player").getPosX()/1280));
        int cur_pos_x = (int)Math.floor(getObject("player").getPosX()/1280);
        int cur_pos_y = (int)Math.floor(getObject("player").getPosY()/720);
        if(cur_chunk != cur_pos_x + cur_pos_y*3){
            cur_chunk = cur_pos_x + cur_pos_y*3;
            System.out.println(cur_pos_y);
            System.out.println(cur_pos_x);
            loaded_chunks.clear();

            for(int i = cur_pos_y-1; i <= cur_pos_y+1; i++) {
                for(int j = cur_pos_x-1; j <= cur_pos_x+1; j++) {
                    if(j >= 0 && j < map.getChunk_width() && i >= 0 && i < map.getChunk_height()) {
                        if(j + i*3 < 12){
                            loaded_chunks.add(map.get_chunk(j + i*3));
                        }
//                        System.out.println("Chunk to load: " + i + " " + j);
                    }
                }
            }

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
        camera.update(gc, this, deltaTime);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        camera.render(r);

//        r.drawImage(skyImage, 0, 0);
//        r.drawImage(tileImage, 0, 0);

//        for (int i = 0; i < loaded_chunks.size(); i++){
//            Chunk curCh = map.get_chunk(i);
//            r.drawChunk(curCh, (int)(i%2)*curCh.getWidth(), (int)(i/2)*curCh.getHeight());
//        }

//        r.drawChunk(map.get_chunk(cur_chunk), (cur_chunk%3)*1280, (cur_chunk/3)*720);
//        System.out.println("Chunks loaded: " + loaded_chunks.toArray().length);
        for(int i = 0; i < loaded_chunks.toArray().length; i++){
            Chunk chnk = loaded_chunks.get(i);
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

    public int getLevelW() {
        return levelW;
    }

    public int getLevelH() {
        return levelH;
    }
}
