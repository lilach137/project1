package com.example.project1.objects;



import android.content.ContextWrapper;
import android.media.MediaPlayer;

public class SoundsUtils {

    private MediaPlayer mp;

    public SoundsUtils() {
        MediaPlayer mp = new MediaPlayer();
    }

    public void setMpAndPlay(ContextWrapper cw, int sample) {
        this.mp = MediaPlayer.create(cw,sample);
        this.mp.start();
    }

    public void stopMp(){
        mp.stop();
    }
}
