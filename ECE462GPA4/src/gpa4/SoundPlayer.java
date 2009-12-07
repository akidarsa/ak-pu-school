package gpa4;

import java.io.File;
import javax.sound.sampled.*;

/**
 *
 * @author Jake
 */

/*  Creates an object that plays .wav files, the titles of which are
 *  specified outside of the object.  This is so the game can totally
 *  entrust this class with playing whatever sound object it knows the
 *  name of.
 */
public class SoundPlayer{

    private static final long serialVersionUID = 1L;
    private static int count = 0;
    private Clip clip;
    private String sound;
    private Runnable soundPlayer;

    SoundPlayer(String filename)
    {
        sound = filename;

        soundPlayer = new Runnable() {
            public void run() {

                try {
                    File soundFile =
                            new File(System.getProperty("user.dir")
                            +sound);
                    AudioInputStream audioInputStream = AudioSystem
                        .getAudioInputStream(soundFile);
                    AudioFormat audioFormat = audioInputStream
                        .getFormat();
                    DataLine.Info dataLineInfo = new DataLine.Info(
                       Clip.class, audioFormat);
                    clip = (Clip) AudioSystem
                        .getLine(dataLineInfo);
                    clip.open(audioInputStream);
                    clip.loop(clip.LOOP_CONTINUOUSLY); // Play song repeatedly

                    while(clip.isActive()) {}
                    synchronized(this) {
                        count--;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void playStartupSound() {


    Thread soundPlayingThread = new Thread(soundPlayer);
    soundPlayingThread.start();
 }


    public void playSoundLooped(){
        clip.loop(clip.LOOP_CONTINUOUSLY); // Play song repeatedly
    }

    public void playSoundOnce(){
        clip.start(); // Play once
    }
}

