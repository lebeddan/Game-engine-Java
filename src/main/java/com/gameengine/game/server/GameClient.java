package com.gameengine.game.server;

import com.gameengine.game.GameManager;
import com.gameengine.game.gameobjects.Multiplayer.PlayerMP;
import com.gameengine.game.server.packets.*;

import java.io.IOException;
import java.net.*;
import java.util.logging.Logger;

public class GameClient extends Thread
{
    private static final Logger logger = Logger.getLogger(String.valueOf(GameClient.class));

    public static Logger getLogger() {
        return logger;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    private DatagramSocket socket;
    private InetAddress ipAddress;
    private GameManager gm;

    public GameClient(String ipAddress, GameManager gm){
        try {
            this.gm = gm;
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (SocketException | UnknownHostException e) {
            logger.finest("SocketException and UnknownHostException catch " + e);
        }
    }

    public GameClient(String ipAddress){
        try {
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (UnknownHostException e) {
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
                logger.finest("IOException catch " + e);
            }

            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
//            System.out.println("SERVER > " + new String(packet.getData()));
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type){
            default:
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                logger.info("["+address.getHostAddress()+":"+port+"] "+((Packet00Login)packet).getUsername() + " has joined!!");
                try {
                    PlayerMP player = null;
                    System.out.println((int)((Packet00Login)packet).getY());
                    player = new PlayerMP((int)((Packet00Login)packet).getX()/64, (int)((Packet00Login)packet).getY()/64,
                            ((Packet00Login)packet).getUsername(), address, port);
                    gm.addObject(player);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                logger.info("["+address.getHostAddress()+":"+port+"] "+((Packet01Disconnect)packet).getUsername() + " has left the game!");
                gm.removePlayerMP(((Packet01Disconnect)packet).getUsername());
                break;

            case MOVE:
                packet = new Packet02Move(data);
                handleMove((Packet02Move)packet);
                break;

            case BULLET:
                packet = new Packet03Bullet(data);
                handleBullet((Packet03Bullet)packet);
                break;
        }
    }

    private void handleBullet(Packet03Bullet packet){
        PlayerMP player = (PlayerMP) this.gm.getObject(packet.getUsername());
        if(!gm.getUsername().equals(player.getUsername())) {
            logger.info("[" + player.port + "]" + "Sent a bullet: " + this.socket.getLocalPort() + " " + player.port);
            logger.info(""+this.socket.getLocalPort());
            player.fireBullet(gm);
        }
    }

    private void handleMove(Packet02Move packet) {
        PlayerMP player = (PlayerMP) this.gm.getObject(packet.getUsername());
        player.setPosX(packet.getX());
        player.setPosY(packet.getY());
        player.setRotation(packet.getRotation());
        player.setMouseRotation(packet.getMouseRot());
    }

    public void sendData(byte[] data){
        DatagramPacket packet = new DatagramPacket(data,data.length, ipAddress, 1331);
        try {
            socket.send(packet);
        } catch (IOException e) {
            logger.finest("Packet to send not exist " + e);
        }
    }
}
