package com.gameengine.game.Server.packets;

import com.gameengine.game.Server.GameClient;
import com.gameengine.game.Server.GameServer;

public class Packet00Login extends Packet{

    private String username;
    private float x,y;

    public Packet00Login(byte[] data){
        super(00);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Float.parseFloat(dataArray[1]);
        this.y = Float.parseFloat(dataArray[2]);
    }

    public Packet00Login(String username, float x, float y){
        super(00);
        this.x = x;
        this.y = y;
        this.username = username;
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
        return ("00" + this.username+","+this.getX()+","+this.getY()).getBytes();
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
}
