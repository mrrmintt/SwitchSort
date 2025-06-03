package com.example.switchsort.backend.game;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.switchsort.R;

public class MusicManager {
    private float gameVolume;
    private float menuVolume;

    private MediaPlayer mediaPlayer;


    public MusicManager() {
        this.gameVolume = 1.0f;
        this.menuVolume = 1.0f;


    }

    public void startMusic(){
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(menuVolume, gameVolume);
        mediaPlayer.start();
    }



    public void setupMusic(Context context, boolean isMenu) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (isMenu) {
            mediaPlayer = MediaPlayer.create(context, R.raw.menu_music);
        } else {

            mediaPlayer = MediaPlayer.create(context, R.raw.playground);
        }

        startMusic();
    }


    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void onPause() {
        if (mediaPlayer != null) mediaPlayer.pause();
    }


    public void onResume() {
        if (mediaPlayer != null) mediaPlayer.start();
    }

    public float getGameVolume() {
        return gameVolume;
    }

    public float getMenuVolume() {
        return menuVolume;
    }

    public void setMenuVolume(int progress){
        menuVolume = progress / 100f;
    }

    public void setGameVolume(int progress){
        gameVolume = progress / 100f;
    }

    public void stopMusic(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
