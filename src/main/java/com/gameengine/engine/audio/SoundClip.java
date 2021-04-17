package com.gameengine.engine.audio;

/*
 * Controls sound clips and background audion in a game.
 * Used in Game Container.
 * @author Lebedev Daniil
 */
import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;


public class SoundClip {
    private Clip clip = null; // Any sound in a game
    private FloatControl gainControl; // Controller of volume

    // Set up a audio and creating audio format.
    public SoundClip(String path){
        try {
            InputStream audioSrc = SoundClip.class.getResourceAsStream(path);
            InputStream bufferedIn = new BufferedInputStream(audioSrc);

            if(audioSrc == null){
                System.out.println("SADDDDDDDD");
            }

            AudioInputStream audioINStream = AudioSystem.getAudioInputStream(bufferedIn);
            AudioFormat baseFormat = audioINStream.getFormat();
            AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16, baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);

            AudioInputStream decodeAudioINStream = AudioSystem.getAudioInputStream(decodeFormat, audioINStream);

            clip = AudioSystem.getClip();
            clip.open(decodeAudioINStream);

            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e){
            e.printStackTrace();
        }
    }

    /*
     * Play a sound clip.
     */
    public void play(){
        if (clip == null){
            return;
        }

        stop();
        clip.setFramePosition(0); // Setting beginning of a clip
        while(!clip.isRunning()){
            clip.start();
        }
    }

    /*
     * Stop a sound clip.
     */
    public void stop(){
        if(clip.isRunning()){
            clip.stop();
        }
    }

    /*
     * Close a sound clip.
     */
    public void close(){
        stop();
        clip.drain();
        clip.close();
    }

    /*
     * Playing a background music.
     */
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        play();
    }


    /*
     * Set a volume of any sound/backqround music in a game.
     */
    public void setVolume(float vlaue){
        gainControl.setValue(vlaue);
    }


    /*
     * Check if is a sound clip is running.
     */
    public boolean isRunning() {
        return clip.isRunning();
    }
}
