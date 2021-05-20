package com.gameengine.game.server;

import com.gameengine.game.GameManager;
import com.gameengine.game.gameobjects.Multiplayer.PlayerMP;
import com.gameengine.game.mapobjects.TiledMap;
import com.gameengine.game.server.packets.*;
import com.gameengine.game.server.packets.Packet.PacketTypes;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class GameServer extends Thread
{
    private static final Logger logger = Logger.getLogger(String.valueOf(GameServer.class));

    public static Logger getLogger() {
        return logger;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public List<PlayerMP> getConnectedPlayers() {
        return connectedPlayers;
    }

    private GameManager gm;
    private DatagramSocket socket;
    private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();

    public GameServer(GameManager gm){
        try {
            this.gm = gm;
            this.socket = new DatagramSocket(1331);
        } catch (SocketException e) {
            logger.finest("Port is used "+ e);
        }
    }

    public void run(){
        while(true){
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                logger.finest("Don't receive "+ e);
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
                logger.info("["+address.getHostAddress()+":"+port+"] "+((Packet01Disconnect)packet).getUsername() + " has left!");
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
                logger.info("Data before:" + new String(packet.getData()));
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
        logger.info("["+address.getHostAddress()+":"+port+"] "+(packet).getUsername() + " has connected!");
        try {
            PlayerMP player = null;
            player = new PlayerMP((int)packet.getX(), (int)packet.getY(), (packet).getUsername(), address, port);
            logger.info(player.getUsername()+"'s location is:" + player.getPosX()+","+player.getPosY());
            this.addConnection(player, (packet));
        } catch (IOException e) {
            logger.finest("Player doesn't connected!" + e);
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
            logger.finest("Data don't send " + e);
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for (PlayerMP p : connectedPlayers){
            sendData(data, p.ipAddress, p.port);
        }
    }
}
