package com.gameengine.game.Server;

import com.gameengine.game.GameManager;
import com.gameengine.game.GameObjects.Multiplayer.PlayerMP;
import com.gameengine.game.GameObjects.Player;
import com.gameengine.game.Server.packets.*;
import com.gameengine.game.Server.packets.Packet.PacketTypes;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class GameServer extends Thread
{
    private GameManager gm;
    private DatagramSocket socket;
    private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();

    public GameServer(GameManager gm){
        try {
            this.gm = gm;
            this.socket = new DatagramSocket(1331);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
            String message = new String(packet.getData());
            if(message.trim().equalsIgnoreCase("ping")){
                sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
            }
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type){
            default:
            case INVALID:
                break;

            case LOGIN:
                packet = new Packet00Login(data);
                handleLogin((Packet00Login) packet, address, port);
                break;

            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                System.out.println("["+address.getHostAddress()+":"+port+"] "+((Packet01Disconnect)packet).getUsername() + " has left!");
                this.removeConnection(((Packet01Disconnect)packet));
                break;

            case MOVE:
                packet = new Packet02Move(data);
                this.handleMove(((Packet02Move)packet));
                break;

            case BULLET:
                packet = new Packet03Bullet(data);
                handleBullet((Packet03Bullet) packet);
                break;
        }
    }

    public void addConnection(PlayerMP player, Packet00Login packet){
        boolean alreadyConnected = false;
        for (PlayerMP p : this.connectedPlayers){
            if(player.getUsername().equalsIgnoreCase(p.getUsername())){
                if(p.ipAddress == null){
                    p.ipAddress = player.ipAddress;
                }

                if(p.port == -1){
                    p.port = player.port;
                }
                alreadyConnected = true;
            } else {
                sendData(packet.getData(), p.ipAddress, p.port);
                // Send to other players that a player has connected.
                System.out.println("Data before:" + new String(packet.getData()));
                Packet00Login sendPacket = new Packet00Login(p.getUsername(), p.getPosX(), p.getPosY());
                sendData(sendPacket.getData(), player.ipAddress, player.port);
            }
        }
        if(!alreadyConnected){
            this.connectedPlayers.add(player);
        }
    }

    private void handleBullet(Packet03Bullet packet){
        if(getPlayerMP(packet.getUsername()) != null){
//            System.out.println((packet).getUsername() + " has shot a bullet!");
//            int index = getPlayerMPIndex(packet.getUsername());
            packet.writeData(this);
        }
    }

    private void handleLogin(Packet00Login packet, InetAddress address, int port){
        System.out.println("["+address.getHostAddress()+":"+port+"] "+(packet).getUsername() + " has connected!");
        try {
            PlayerMP player = null;
            player = new PlayerMP((int)packet.getX(), (int)packet.getY(), (packet).getUsername(), address, port);
            System.out.println(player.getUsername()+"'s location is:" + player.getPosX()+","+player.getPosY());
            this.addConnection(player, (packet));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeConnection(Packet01Disconnect packet) {
        PlayerMP player = getPlayerMP(packet.getUsername());
        this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
        packet.writeData(this);
    }

    public int getPlayerMPIndex(String username){
        int index = 0;
        for(PlayerMP p : connectedPlayers){
            if(p.getUsername().equals(username)){
                break;
            }
            index++;
        }
        return index;
    }

    public PlayerMP getPlayerMP(String username){
        for(PlayerMP p : connectedPlayers){
            if(p.getUsername().equals(username)){
                return p;
            }
        }
        return null;
    }

    private void handleMove(Packet02Move packet) {
        if(getPlayerMP(packet.getUsername()) != null){
            int index = getPlayerMPIndex(packet.getUsername());
            PlayerMP player = this.connectedPlayers.get(index);
            player.setPosX(packet.getX());
            player.setPosY(packet.getY());
            player.setRotation(packet.getRotation());
            player.setMouseRotation(packet.getMouseRot());
            packet.writeData(this);
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port){
        DatagramPacket packet = new DatagramPacket(data,data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for (PlayerMP p : connectedPlayers){
            sendData(data, p.ipAddress, p.port);
        }
    }
}
