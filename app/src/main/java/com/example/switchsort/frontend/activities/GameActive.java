package com.example.switchsort.frontend.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.switchsort.R;
import com.example.switchsort.backend.database.DatabaseHelper;
import com.example.switchsort.backend.database.Player;
import com.example.switchsort.backend.game.GameManager;
import com.example.switchsort.backend.game.GameState;
import com.example.switchsort.backend.game.ScoreManager;
import com.example.switchsort.frontend.adapters.GridAdapter;

public class GameActive extends AppCompatActivity {

    private GameManager gameManager;
    private GridAdapter gridAdapter;
    private GridView gridView;
    private TextView targetLetterView, scoreView, streakView, timerView, livesView;
    private View flashOverlay;
    private ImageButton pauseButton;
    private Dialog pauseDialog;
    private MediaPlayer mediaPlayer;
    private Handler timerHandler;
    private Runnable timerRunnable;

    private boolean isPaused = false;
    private boolean isTimeRushMode;
    private String difficulty;
    private String symbol; // default to NUMBER and grayed out until user action
    private ScoreManager scoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        difficulty = intent.getStringExtra("DIFFICULTY");
        isTimeRushMode = "TIME_RUSH".equals(intent.getStringExtra("GAME_MODE"));
        symbol = intent.getStringExtra("MODE");

        gameManager = new GameManager(difficulty, symbol, isTimeRushMode);

        initializeViews();
        setupMusic();
        updateUI();

        if (isTimeRushMode) {
            startTimeRushTimer();
        }
    }

    private void initializeViews() {
        gridView = findViewById(R.id.gameGrid);
        targetLetterView = findViewById(R.id.targetLetter);

        scoreView = findViewById(R.id.scoreView);
        streakView = findViewById(R.id.streakView);
        timerView = findViewById(R.id.timerText);
        livesView = findViewById(R.id.livesView);
        flashOverlay = findViewById(R.id.flashOverlay);
        pauseButton = findViewById(R.id.pauseButton);

        Typeface font = ResourcesCompat.getFont(this, R.font.pixar);
        int textColor = Color.parseColor("#523502");
        for (TextView view : new TextView[]{targetLetterView, scoreView, streakView, timerView, livesView}) {
            if (view != null) {
                view.setTypeface(font);
                view.setTextColor(textColor);
            }
        }

        pauseButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        pauseButton.setOnClickListener(v -> showPauseMenu());
    }

    private void updateUI() {
        GameState state = gameManager.getCurrentGameState();
        gridAdapter = new GridAdapter(this, state.getBoard(), (int) Math.sqrt(state.getBoard().length), v -> {
            int pos = (int) v.getTag();
            boolean correct = gameManager.handleUserInput(pos);
            showFlash(correct);
            updateUI();
            if (gameManager.isGameOver()) showGameOverDialog();
        });

        gridView.setNumColumns((int) Math.sqrt(state.getBoard().length));
        gridView.setAdapter(gridAdapter);

        targetLetterView.setText("Ziel: " + state.getTargetCharacter());
        scoreView.setText("Punkte: " + state.getCurrentScore());
        livesView.setText("Leben: " + state.getLives());
        streakView.setText("Streak: " + state.getStreak());

        if (isTimeRushMode) {
            timerView.setText("Zeit: " + state.getTimerText());
            timerView.setVisibility(View.VISIBLE);

        } else {
            timerView.setVisibility(View.GONE);
        }
    }

    private void startTimeRushTimer() {
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && gameManager != null && !gameManager.isGameOver() && !isPaused) {
                    gameManager.updateTimer();
                    updateTimerDisplay();

                    if (gameManager.isGameOver()) {
                        showGameOverDialog();
                    } else {
                        timerHandler.postDelayed(this, 100);  // Update every 0.1 seconds for smoother display
                    }
                }
            }
        };
        timerHandler.postDelayed(timerRunnable, 100);
    }

    private void updateTimerDisplay() {
        TextView timerView = findViewById(R.id.timerText);
        if (timerView != null) {
            timerView.setText("Time: " + gameManager.getCurrentTimeFormatted() + "s");
        }
    }


    private void showPauseMenu() {

        isPaused = true;
        pauseDialog = new Dialog(this);
        pauseDialog.setContentView(R.layout.dialog_pause_menu);
        pauseDialog.setCancelable(false);
        pauseDialog.findViewById(R.id.resumeButton).setOnClickListener(v -> resumeGame());
        pauseDialog.findViewById(R.id.mainMenuButton).setOnClickListener(v -> goToMainMenu());
        pauseDialog.show();
    }

    private void resumeGame() {
        isPaused = false;
        if (isTimeRushMode) timerHandler.postDelayed(timerRunnable, 100);
        pauseDialog.dismiss();
    }

    private void goToMainMenu() {

        if (timerHandler != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        finish();
    }

    private void showGameOverDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_game_over);
        int finalScore = gameManager.getCurrentScore();
        ((TextView) dialog.findViewById(R.id.finalScore)).setText("Final Score: " + finalScore);

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String playerName = getIntent().getStringExtra("PLAYER_NAME");
        String mode = isTimeRushMode ? "TIME_RUSH" : "CLASSIC";
        Player player = new Player(playerName, deviceId, finalScore, difficulty, mode);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        // Nur wer auch Punkte erzielt hat, soll evtl. gespeichert werden
        if (finalScore > 0){
            dbHelper.addScore(player);
        }

        dialog.findViewById(R.id.okButton).setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
        dialog.show();
    }

    private void setupMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.playground);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
            float volume = MainActivity.getGameMusicVolume();
            mediaPlayer.setVolume(volume, volume);
            mediaPlayer.start();
        }
    }

    private void showFlash(boolean correct) {
        flashOverlay.setBackground(ContextCompat.getDrawable(this, correct ? R.drawable.flash_correct : R.drawable.flash_wrong));
        flashOverlay.setVisibility(View.VISIBLE);
        flashOverlay.setAlpha(0.7f);
        flashOverlay.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        flashOverlay.setVisibility(View.INVISIBLE);
                    }
                })
                .start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}


