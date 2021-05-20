package com.gameengine.engine.audio;

/*
 * Controls sound clips and background audion in a game.
 * Used in Game Container.
 * @author Lebedev Daniil
 */
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;


public class SoundClip {
    private Clip clip = null; // Any sound in a game
    private FloatControl gainControl; // Controller of volume

    private MediaPlayer mediaPlayer;
    private AudioClip audioPlayer;

    private String type;

    /*
     * Constructs an sound file object.
     * This public constructor is used for create a sound clip
     * @param path - the current path of the sound clip file
     * */
    public SoundClip(String path, String type){
        this.type = type.toLowerCase();
        if(type.equalsIgnoreCase("clip")){
            this.audioPlayer = new AudioClip(new File(path).toURI().toString());
        } else {
            Media sound = new Media(new File(path).toURI().toString());
            this.mediaPlayer = new MediaPlayer(sound);
        }
    }
    /*
     * Play a sound clip.
     */
    public void play(){
        if(type.equals("clip")){
            this.audioPlayer.play();
        } else {
            this.mediaPlayer.play();
        }
    }

    /*
     * Stop a sound clip.
     */
    public void stop(){
        if(type.equals("clip")) {
            this.audioPlayer.stop();
        } else {
            this.mediaPlayer.stop();
        }
    }

    /*
     * Playing a background music.
     */
    public void loop(){
        this.mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });
    }


    /*
     * Set a volume of any sound/backqround music in a game.
     */
    public void setVolume(double value){
        if(type.equals("clip")) {
            this.audioPlayer.setVolume(value);
        } else {
            this.mediaPlayer.setVolume(value);
        }
    }

    public void addVolume(float value){
        if(type.equals("clip")) {
            this.audioPlayer.setVolume(this.audioPlayer.getVolume() + value);
        } else {
            this.mediaPlayer.setVolume(this.mediaPlayer.getVolume() + value);
        }
    }

    public double getVolume(){
        if(type.equals("clip")) {
            return this.audioPlayer.getVolume();
        } else {
            return this.mediaPlayer.getVolume();
        }
    }
}
