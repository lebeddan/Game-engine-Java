package com.gameengine.game;

import com.gameengine.engine.AbstractGame;
import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.audio.SoundClip;
import com.gameengine.game.gui.GameStage;
import com.gameengine.game.gui.ViewManager;
import com.gameengine.game.gameobjects.Camera;
import com.gameengine.game.gameobjects.Enemy;
import com.gameengine.game.gameobjects.GameObject;
import com.gameengine.game.gameobjects.Multiplayer.PlayerMP;
import com.gameengine.game.gameobjects.Player;
import com.gameengine.game.mapobjects.Chunk;
import com.gameengine.game.mapobjects.Map;
import com.gameengine.game.mapobjects.TiledMap;
import com.gameengine.game.server.GameClient;
import com.gameengine.game.server.GameServer;
import com.gameengine.game.server.packets.Packet00Login;
import com.gameengine.game.server.packets.Packet01Disconnect;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameManager extends AbstractGame {

    public static final int TS = 64;
    private static Canvas mainCanvas;

    private boolean[] collision;
    private int levelW, levelH, levelWChunks, chunk_width, chunk_height;

    private Camera camera;
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();
    private String username;
    private Player player;

    public GameServer socketServer;
    public GameClient socketClient;

    private Map map;
    private List<Chunk> loaded_chunks;
    private int cur_chunk;
    private GameContainer gc;

    private Stage stage;
    private GameStage gameStage;
    private ViewManager viewManager;

    private SoundClip bgMusic = new SoundClip("/Sounds/gameBGmusic.mp3", "bg");

    public GameManager() throws IOException {
//        startGame();
    }

    public void startGame(Stage stage, String username, boolean server, String IP) throws IOException {
        gc = new GameContainer(this);

        bgMusic.loop();
        bgMusic.setVolume(0.5);
        bgMusic.play();

        //TODO: FIX STUFF WITH CAMERA
        gc.setWidth(1280);
        gc.setHeight(720);
        gc.setScale(1);
        gc.start();
        gameStage = new GameStage(gc, this);
//        Scene gameScene = gc.getWindow().getMainScene();
//        gameStage.setScene(gameScene);
//        Stage gameStage = new Stage();
//        gameStage.setScene(gameScene);
//        stage.hide();
//        this.stage.setScene(gc.getWindow().getMainScene());

//        System.out.println("Do you want to start a server?");
//        Scanner scanner = new Scanner(System.in);
//        String msg = scanner.nextLine();
        if(server){
            socketServer = new GameServer(this, IP);
            socketServer.start();
        }

        socketClient = new GameClient(IP, this);
        socketClient.start();

        loadLevel("/Maps/maptilesettest.json");

//        System.out.println("Your username: ");
//        username = scanner.nextLine();
        this.username = username;
        player = new PlayerMP(1, 8, username, gc.getInput(), null, -1);
        Packet00Login loginPacket = new Packet00Login(username, player.getPosX(), player.getPosY());
        getObjects().add(player);
        if(socketServer != null){
            socketServer.addConnection((PlayerMP)player, loginPacket);
        }
        loginPacket.writeData(socketClient);

        camera = new Camera(username);
//        stage.hide();
//        gameStage.show();
        getObjects().add(new Enemy(12, 10, this));
//        gameStage.show();
    }

    @Override
    public void stop(){
        System.out.println("Hi");
        if(socketClient != null) {
            Packet01Disconnect packet = new Packet01Disconnect(player.getUsername());
            packet.writeData(socketClient);
        }
        System.exit(0);
    }

    public void showPMenu(){
        if(gameStage.isHidden()){
            gameStage.showPauseMenu();
        } else {
            gameStage.hidePauseMenu();
        }
    }

    public static void main(){
        launch();
    }

    @FXML
    public void exitApplication(ActionEvent e){
        Platform.exit();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Tank Souls");
//        gc.getRenderer().setAmbientColor(-1);
        viewManager = new ViewManager(this, stage);
        stage = viewManager.getMainStage();
//        stage.setScene(gc.getWindow().getMainScene());
//        startGame(stage, "vas", true);
        this.stage = stage;
        this.stage.show();
    }

    @Override
    public void init(GameContainer gc){
        System.out.println("Starting");
        gc.getRenderer().setAmbientColor(-1);
    }

    public void loadLevel(String path) throws IOException {
        TiledMap tm = new TiledMap();
        map = tm.create_map(path, this);
        loaded_chunks = new ArrayList<Chunk>();
        loaded_chunks.add(map.get_chunk(0));

//        loaded_chunks.get(0).getObjects().add(new Enemy(20, 20));

        cur_chunk = -1;
        levelW = map.getLevel_width();
        levelH = map.getLevel_height();
        levelWChunks = map.getChunk_width();
        chunk_width = map.get_cWidth()*TS;
        chunk_height = map.get_cHeight()*TS;
        System.out.println("Chunks: "+levelWChunks);
    }

    @Override
    public void update(GameContainer gc, float deltaTime){
        gc.getRenderer().setAmbientColor(-1);
        int cur_pos_x = 0;
        int cur_pos_y = 0;
        try {
            cur_pos_x = (int) Math.floor(getObject(username).getPosX() / chunk_width);
            cur_pos_y = (int) Math.floor(getObject(username).getPosY() / chunk_height);
        } catch (NullPointerException e){
            cur_pos_x = 0;
            cur_pos_y = 0;
        }
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
        for(int i = 0; i < getObjects().size(); i++){
            getObjects().get(i).update(gc, this, deltaTime);
            if(getObjects().get(i).isDead()){
                getObjects().remove(i);
                i--;
            }
        }
        for(Chunk chunk : loaded_chunks){
            chunk.getObjects().removeIf(GameObject::isDead);
            for(GameObject obj : chunk.getObjects()){
                if(obj instanceof Enemy){
                    obj.update(gc, this, deltaTime);
                }
            }
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

//        printgetObjects()ize(map.get_chunk(cur_chunk));
//        System.out.println(getObjects()izeCalculator);
//        r.drawChunk(map.get_chunk(cur_chunk), map.get_chunk(cur_chunk).getPosX(), map.get_chunk(cur_chunk).getPosY());
//        System.out.println("Chunks loaded: " + loaded_chunks.toArray().length);
        for(int i = 0; i < loaded_chunks.toArray().length; i++){
            Chunk chnk = loaded_chunks.get(i);
//            System.out.println(loaded_chunks.toArray().length);
            r.drawChunk(chnk, chnk.getPosX(), chnk.getPosY());
        }


        for(GameObject obj : new ArrayList<>(objects)){
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

    public synchronized List<GameObject> getObjects(){
        return this.objects;
    }

    public boolean getCollision(int x, int y){
        if(x < 0 || x >= levelW || y < 0 || y >= levelH){
            return true;
        }
        return collision[x + y * levelW];
    }

    public void addObject(GameObject obj){
        getObjects().add(obj);
    }

    public GameObject getObject(String tag){
        for(int i = 0; i < getObjects().size(); i++){
            if(getObjects().get(i).getTag().equals(tag)){
                return getObjects().get(i);
            }
        }

        return null;
    }

    private int getObjectIndex(String username){
        int index = -1;
        for(GameObject obj : getObjects()){
            if(obj instanceof PlayerMP && ((PlayerMP)obj).getUsername().equals(username)){
                break;
            }
            index++;
        }
        return index;
    }

    public void check_collisions(GameObject mainObj){
        String mainType = mainObj.getShape();
        for(Chunk chunk : loaded_chunks) {
            ArrayList<GameObject> arr = new ArrayList<>(chunk.getObjects());
            for(GameObject ob : objects){
                if((ob instanceof PlayerMP && mainObj != ob) || ob instanceof Enemy){
                    arr.add(ob);
                }
            }
            for (GameObject obj : arr){
                if(obj.getShape().equals("square")) {
//                    Point2D center = Point2D.ZERO.add(mainObj.getPosX()+ mainObj.getRadius(), mainObj.getPosY()+ mainObj.getRadius());
                    Point2D center = Point2D.ZERO.add(mainObj.getCenter().getX() + mainObj.getPosX(), mainObj.getCenter().getY() + mainObj.getPosY());
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

                    if(closest.distance(center) < mainObj.getRadius()){
                        obj.hit(mainObj, this);
                        mainObj.hit(obj, this);
                    }



                } else if (obj.getShape().equals("circle")){
                    Point2D mainCent = Point2D.ZERO.add(mainObj.getPosX()+mainObj.getCenter().getX(), mainObj.getPosY()+mainObj.getCenter().getY());
                    Point2D objCent = Point2D.ZERO.add(obj.getPosX()+obj.getCenter().getX(), obj.getPosY()+obj.getCenter().getY());
                    if(mainCent.distance(objCent) < (mainObj.getRadius()+obj.getRadius())){
                        obj.hit(mainObj, this);
                        mainObj.hit(obj, this);
                    }
                } else if (obj.getShape().equals("someShape")){
                    boolean collisionX = (mainObj.getPosX() + mainObj.getWidth() >= obj.getPosX()) && (obj.getPosX() + obj.getWidth() >= mainObj.getPosX());
                    boolean collisionY = (mainObj.getPosY() + mainObj.getHeight() >= obj.getPosY()) && (obj.getPosY() + obj.getHeight() >= mainObj.getPosY());

                    if (collisionX && collisionY) {
                        System.out.println("Nice");
//                        mainObj.dead = true;
                        mainObj.hit(obj, this);
                        obj.hit(mainObj, this);
                    }
                }
            }
        }
    }

    public Point2D check_radius(Enemy mainObj){
        for(Chunk chunk : loaded_chunks) {
            ArrayList<GameObject> arr = new ArrayList<>();
            for(GameObject ob : objects){
                if(ob instanceof PlayerMP){
                    arr.add(ob);
                }
            }
            for (GameObject obj : arr){
                if (obj.getShape().equals("circle")){
                    Point2D mainCenter = Point2D.ZERO.add(mainObj.getPosX()+mainObj.getCenter().getX(), mainObj.getPosY()+mainObj.getCenter().getY());
                    Point2D objCenter = Point2D.ZERO.add(obj.getPosX()+obj.getCenter().getX(), obj.getPosY()+obj.getCenter().getY());
                    if(mainCenter.distance(objCenter) < (mainObj.getDetectRadius()+obj.getRadius())){
                        return Point2D.ZERO.add(objCenter);
                    }
                } else if (obj.getShape().equals("someShape")){
                    boolean collisionX = (mainObj.getPosX() + mainObj.getWidth() >= obj.getPosX()) && (obj.getPosX() + obj.getWidth() >= mainObj.getPosX());
                    boolean collisionY = (mainObj.getPosY() + mainObj.getHeight() >= obj.getPosY()) && (obj.getPosY() + obj.getHeight() >= mainObj.getPosY());

                    if (collisionX && collisionY) {
                        System.out.println("Nice");
//                        mainObj.dead = true;
                        mainObj.hit(obj, this);
                        obj.hit(mainObj, this);
                    }
                }
            }
        }
        return null;
    }

    public void removePlayerMP(String username){
        int index = 0;
        for(GameObject obj : getObjects()){
            if(obj instanceof PlayerMP && ((PlayerMP) obj).getUsername().equals(username)){
                break;
            }
            index++;
        }
        getObjects().remove(index);
    }


    public int getLevelW() {
        return levelW;
    }

    public int getLevelH() {
        return levelH;
    }

    public String getUsername(){
        return username;
    }

    public int clamp(int value, int min, int max){
        return  Math.max(min, Math.min(max, value));
    }
}
