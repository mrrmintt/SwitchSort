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
import android.media.MediaPlayer;  // Add this import
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.example.switchsort.R;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;  // Add this field
    private EditText playerNameInput;
    private Button leaderboardButton;
    private Button classicButton;
    private Button timeRushButton;
    private String gameMode = "CLASSIC";
    private float menuMusicVolume = 1.0f;    // Deklaration der Menümusik-Lautstärke
    private static float gameMusicVolume = 1.0f;  // Deklaration der Spielmusik-Lautstärke als static
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide ActionBar (remove the black box)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        playerNameInput = findViewById(R.id.playerNameInput);
        setupDifficultyButtons();
        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> showSettingsDialog());

        setupButtonAnimation(settingsButton);
    }

    private void setupDifficultyButtons() {
        Button easyButton = findViewById(R.id.buttonEasy);
        Button mediumButton = findViewById(R.id.buttonMedium);
        Button hardButton = findViewById(R.id.buttonHard);

        // Custom Font für alle Buttons
        Typeface customFont = ResourcesCompat.getFont(this, R.font.pixar);  // Stelle sicher, dass deine Font hier richtig benannt ist
        easyButton.setTypeface(customFont);
        mediumButton.setTypeface(customFont);
        hardButton.setTypeface(customFont);

        // Click Animation für alle Buttons
        setupButtonAnimation(easyButton);
        setupButtonAnimation(mediumButton);
        setupButtonAnimation(hardButton);

        // Click Listener
        easyButton.setOnClickListener(v -> startGame("EASY"));
        mediumButton.setOnClickListener(v -> startGame("MEDIUM"));
        hardButton.setOnClickListener(v -> startGame("HARD"));
        leaderboardButton = findViewById(R.id.buttonLeaderboard);
        leaderboardButton.setTypeface(customFont);
        setupButtonAnimation(leaderboardButton);
        leaderboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LeaderboardActivity.class);
            startActivity(intent);
        });
        // Setup game mode buttons
        classicButton = findViewById(R.id.buttonClassic);
        timeRushButton = findViewById(R.id.buttonTimeRush);

        // Set custom font
        classicButton.setTypeface(customFont);
        timeRushButton.setTypeface(customFont);

        // Setup animations
        setupButtonAnimation(classicButton);
        setupButtonAnimation(timeRushButton);

        // Setup click listeners
        classicButton.setOnClickListener(v -> setGameMode("CLASSIC"));
        timeRushButton.setOnClickListener(v -> setGameMode("TIME_RUSH"));

        // Initial state
        updateGameModeButtons();
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

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("DIFFICULTY", difficulty);
        intent.putExtra("PLAYER_NAME", playerName);
        intent.putExtra("GAME_MODE", gameMode);
        startActivity(intent);
    }

    // Add these lifecycle methods to handle the music properly
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Initialize and start music if it's not already playing
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.menu_music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } else if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    private void setupButtonAnimation(View button) {  // Änderung von Button zu View
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate()
                            .scaleX(0.9f)
                            .scaleY(0.9f)
                            .setDuration(100)
                            .start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start();
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

        // Set initial progress
        menuMusicSeekBar.setProgress((int)(menuMusicVolume * 100));
        gameMusicSeekBar.setProgress((int)(gameMusicVolume * 100));

        // Handle menu music volume changes
        menuMusicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                menuMusicVolume = progress / 100f;
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(menuMusicVolume, menuMusicVolume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Handle game music volume changes
        gameMusicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gameMusicVolume = progress / 100f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        dialog.show();
    }

    // Add getter for game music volume
    public static float getGameMusicVolume() {
        return gameMusicVolume;
    }
}