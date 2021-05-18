package com.gameengine.game.Server.packets;

import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.Server.GameClient;
import com.gameengine.game.Server.GameServer;

public class Packet03Bullet extends Packet{

    private String username;
    private float x, y, rotation;
    private ImageTile bSprite;

    public Packet03Bullet(byte[] data){
        super(03);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Float.parseFloat(dataArray[1]);
        this.y = Float.parseFloat(dataArray[2]);
        this.rotation = Float.parseFloat(dataArray[3]);
    }

    public Packet03Bullet(String username, float x, float y, float rotation){
        super(03);
        this.username = username;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("03" + this.username+","+this.x+","+this.y+","+this.rotation).getBytes();
    }

    public String getUsername(){
        return this.username;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public float getRotation(){
        return this.rotation;
    }
}
