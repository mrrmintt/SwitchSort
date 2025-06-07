package com.example.switchsort.backend.game;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.switchsort.R;

public class MusicManager {
    private float gameVolume;
    private float menuVolume;

    private boolean isMenuBoolean;
    private MediaPlayer mediaPlayer;


    public MusicManager() {
        this.gameVolume = 1.0f;
        this.menuVolume = 1.0f;


    }

    // Startet Musik
    public void startMusic(){
        mediaPlayer.setLooping(true);
        updateVolume();
        mediaPlayer.start();
    }


    // Checkt ob es einen Mediplayer gibt, wenn ja wird der zerstört und ein neuer Player mit der korrekten Musik wird erstellt
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

    // Gibt den MediaPlayer zurück
    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    // Gibt wahr, wenn Mediaplayer definiert und spielt
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    // Mediaplayer wird pausiert
    public void onPause() {
        if (mediaPlayer != null) mediaPlayer.pause();
    }


    // Mediaplayer wird wieder gestartet
    public void onResume() {
        if (mediaPlayer != null) mediaPlayer.start();
    }

    // Gibt Spielmusik lautstärke zurück
    public float getGameVolume() {
        return gameVolume;
    }

    // Gibt Menü Musik Lautstärke zurück
    public float getMenuVolume() {
        return menuVolume;
    }

    // Setzt Menü Lautstärke
    public void setMenuVolume(int progress){
        menuVolume = progress / 100f;
    }

    // Setzt Spiel Lautstärke
    public void setGameVolume(int progress){
        gameVolume = progress / 100f;
    }

    // Stopppt Musik komplett und setzt Player auf NULL
    public void stopMusic(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    public void setMenuBoolean(boolean isMenu){
        isMenuBoolean = isMenu;
    }
    public boolean getMenuBoolean(){
        return isMenuBoolean;
    }

    public void updateVolume() {
        if (mediaPlayer != null) {
            float volume = isMenuBoolean ? menuVolume : gameVolume;
            mediaPlayer.setVolume(volume, volume);
        }
    }

}
