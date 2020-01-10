package com.ystudios.main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {

    private AudioClip clip;
    public static final Sound musicBackground = new Sound("/res/background.wav");
    public static final Sound shot = new Sound("/res/shot.wav");

    public Sound(String name) {
        try {
            clip = Applet.newAudioClip(Sound.class.getResource(name));
        } catch (Throwable error) {
        }
    }

    public void loop() {
        try {
            new Thread() {
                public void run() {
                    clip.loop();
                }
            }.start();
        } catch (Throwable error) {
        }
    }

    public void play() {
        try {
            new Thread() {
                public void run() {
                    clip.play();
                }
            }.start();
        } catch (Throwable error) {
        }
    }
}
