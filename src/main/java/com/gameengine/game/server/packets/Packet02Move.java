package com.gameengine.game.server.packets;

import com.gameengine.game.server.GameClient;
import com.gameengine.game.server.GameServer;

public class Packet02Move extends Packet{

    private String username;
    private float x, y, rotation, mouseRot;

    public Packet02Move(byte[] data){
        super(02);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Float.parseFloat(dataArray[1]);
        this.y = Float.parseFloat(dataArray[2]);
        this.rotation = Float.parseFloat(dataArray[3]);
        this.mouseRot = Float.parseFloat(dataArray[4]);
    }

    public Packet02Move(String username, float x, float y, float rotation, float mouseRot){
        super(02);
        this.username = username;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.mouseRot = mouseRot;
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
        return ("02" + this.username+","+this.x+","+this.y+","+this.rotation+","+this.mouseRot).getBytes();
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

    public float getMouseRot(){
        return this.mouseRot;
    }
}
