package com.example.switchsort.frontend.activities;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.media.MediaPlayer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.example.switchsort.R;
import com.example.switchsort.backend.game.MusicManager;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private EditText playerNameInput;
    private Button leaderboardButton;
    private Button classicButton;
    private Button timeRushButton;
    private Button letterModeButton;
    private Button numberModeButton;
    private static  MusicManager musicManager = new MusicManager();
    private String gameMode = "CLASSIC"; // CLASSIC or TIME_RUSH
    private String gameContentMode = "NUMBER"; // LETTER or NUMBER
    private boolean navigatingInternally = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        playerNameInput = findViewById(R.id.playerNameInput);

        setupDifficultyButtons();
        setupModeButtons();

        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> showSettingsDialog());
        setupButtonAnimation(settingsButton);


        Button quitButton = findViewById(R.id.quitButton);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitApp();
            }
        });
    }

    private void setupDifficultyButtons() {
        Button easyButton = findViewById(R.id.buttonEasy);
        Button mediumButton = findViewById(R.id.buttonMedium);
        Button hardButton = findViewById(R.id.buttonHard);

        Typeface customFont = ResourcesCompat.getFont(this, R.font.rubik);
        easyButton.setTypeface(customFont);
        mediumButton.setTypeface(customFont);
        hardButton.setTypeface(customFont);

        setupButtonAnimation(easyButton);
        setupButtonAnimation(mediumButton);
        setupButtonAnimation(hardButton);

        easyButton.setOnClickListener(v -> startGame("EASY"));
        mediumButton.setOnClickListener(v -> startGame("MEDIUM"));
        hardButton.setOnClickListener(v -> startGame("HARD"));

        leaderboardButton = findViewById(R.id.buttonLeaderboard);
        leaderboardButton.setTypeface(customFont);
        setupButtonAnimation(leaderboardButton);
        leaderboardButton.setOnClickListener(v -> {
            navigatingInternally = true;
            Intent intent = new Intent(this, LeaderboardActivity.class);
            startActivity(intent);
        });

        classicButton = findViewById(R.id.buttonClassic);
        timeRushButton = findViewById(R.id.buttonTimeRush);

        classicButton.setTypeface(customFont);
        timeRushButton.setTypeface(customFont);

        setupButtonAnimation(classicButton);
        setupButtonAnimation(timeRushButton);

        classicButton.setOnClickListener(v -> setGameMode("CLASSIC"));
        timeRushButton.setOnClickListener(v -> setGameMode("TIME_RUSH"));

        updateGameModeButtons();
    }

    private void setupModeButtons() {
        letterModeButton = findViewById(R.id.buttonLetter);
        numberModeButton = findViewById(R.id.buttonNumber);

        Typeface customFont = ResourcesCompat.getFont(this, R.font.rubik);
        letterModeButton.setTypeface(customFont);
        numberModeButton.setTypeface(customFont);

        setupButtonAnimation(letterModeButton);
        setupButtonAnimation(numberModeButton);

        letterModeButton.setOnClickListener(v -> setGameContentMode("LETTER"));
        numberModeButton.setOnClickListener(v -> setGameContentMode("NUMBER"));

        updateGameContentModeButtons();
    }

    private void setGameContentMode(String mode) {
        gameContentMode = mode;
        updateGameContentModeButtons();
    }

    private void updateGameContentModeButtons() {
        letterModeButton.setAlpha(gameContentMode.equals("LETTER") ? 1.0f : 0.5f);
        numberModeButton.setAlpha(gameContentMode.equals("NUMBER") ? 1.0f : 0.5f);
    }

    private void setGameMode(String mode) {
        gameMode = mode;
        updateGameModeButtons();
    }

    private void updateGameModeButtons() {
        classicButton.setAlpha(gameMode.equals("CLASSIC") ? 1.0f : 0.5f);
        timeRushButton.setAlpha(gameMode.equals("TIME_RUSH") ? 1.0f : 0.5f);
    }

    private void startGame(String difficulty) {
        String playerName = playerNameInput.getText().toString().trim();
        if (playerName.isEmpty()) {
            playerNameInput.setError("Please enter your name");
            return;
        }


        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("DIFFICULTY", difficulty);
        intent.putExtra("PLAYER_NAME", playerName);
        intent.putExtra("GAME_MODE", gameMode);
        intent.putExtra("MODE", gameContentMode);


        startActivity(intent);
    }


    private void setupButtonAnimation(View button) {
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.performClick();
                    }
                    break;
            }
            return true;
        });
    }

    private void showSettingsDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_settings);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        SeekBar menuMusicSeekBar = dialog.findViewById(R.id.menuMusicSeekBar);
        SeekBar gameMusicSeekBar = dialog.findViewById(R.id.gameMusicSeekBar);

        menuMusicSeekBar.setProgress((int)( musicManager.getMenuVolume()* 100));
        gameMusicSeekBar.setProgress((int)(musicManager.getGameVolume() * 100));

        menuMusicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                musicManager.setMenuVolume(progress);
                musicManager.updateVolume();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        gameMusicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                musicManager.setGameVolume(progress);

            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        dialog.show();
    }

    private void quitApp() {
        // Alles beenden
        finishAffinity(); // Schließt alle Activities im Stack

        // Optional: Prozess hart beenden
        System.exit(0); // Nicht schön, aber effektiv – vor allem bei Spielen üblich
    }

    public static MusicManager getMusicManager(){
        return musicManager;
    }

    // LIFECYCLE

    @Override
    protected void onRestart(){
        super.onRestart();
        // Abfrage, weil wenn Spiel beendet würde sonst einfach Player gestoppt und hier weiterspielen.
        if (musicManager.getMenuBoolean()){
            musicManager.onResume();
        } else {
            musicManager.setMenuBoolean(true);
            musicManager.setupMusic(this, true);
        }

        System.out.println("ON RESTART MAIN");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Wenn noch keine Musik läuft, dann wird der Player gestartet
        if (!musicManager.isPlaying()) {
            // Boolean hier setzen weil der sonst nicht checkt, welches Volume er nehmen muss
            musicManager.setMenuBoolean(true);
            musicManager.setupMusic(this, true);  // Spiel-Musik starten

        }
        System.out.println("ON START MAIN");
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicManager.onResume();  // Musik weiterspielen wenn pausiert
        System.out.println("ON RESUME MAIN");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Wenn keine GameActivity kommt und App nicht geschlossen wird
        if (!navigatingInternally) {
            musicManager.onPause();
        }

        navigatingInternally = false;  // Musik pausieren wenn Activity nicht mehr im Vordergrund
        System.out.println("ON PAUS MAIN");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (musicManager.getMenuBoolean()&& navigatingInternally){
        // Muss onPause weil sonst wenn kurz aus App raus läuft Musik nicht weiter sonder startet neu
        musicManager.onPause();
        System.out.println("ON STOP MAIN");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicManager.stopMusic();  // Musik komplett stoppen, wenn Activity zerstört wird
        System.out.println("ON DESTROY MAIN");
    }

}
