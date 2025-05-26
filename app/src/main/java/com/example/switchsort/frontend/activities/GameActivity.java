package com.example.switchsort.frontend.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import android.widget.ImageButton;
import android.graphics.Color;

import com.example.switchsort.R;
import com.example.switchsort.backend.database.DatabaseHelper;
import com.example.switchsort.backend.database.Player;
import com.example.switchsort.backend.game.TimeRushGameManager;
import com.example.switchsort.backend.game.TimeRushNumberGenerator;
import com.example.switchsort.backend.game.TimeRushScoreManager;
import com.example.switchsort.frontend.adapters.GridAdapter;
import com.example.switchsort.backend.game.ScoreManager;
import android.content.res.ColorStateList;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

/*
    private GridView gridView;
    private GridAdapter gridAdapter;
    private TextView targetLetterView;
    private TextView roundCounterView;
    private TextView scoreView;
    private TextView streakView;
    private MediaPlayer mediaPlayer;
    private View flashOverlay;
    private ScoreManager scoreManager;
    private String difficulty;
    private String[] currentLetters;
    private char targetLetter;
    private int currentRound = 0;  // Start bei 0, da startNewRound() den Counter erhöht
    private final int TOTAL_ROUNDS = 20;
    private Random random = new Random();
    private long roundStartTime;
    private boolean isTimeRushMode;
    private TimeRushGameManager timeRushManager;

    private TimeRushNumberGenerator numberGenerator;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private boolean isPaused = false;
    private Dialog pauseDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get difficulty and game mode first
        difficulty = getIntent().getStringExtra("DIFFICULTY");
        isTimeRushMode = getIntent().getStringExtra("GAME_MODE").equals("TIME_RUSH");

        // Set the correct layout based on game mode
        setContentView(R.layout.activity_game);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize appropriate managers based on game mode
        if (isTimeRushMode) {
            numberGenerator = new TimeRushNumberGenerator(difficulty);
            timeRushManager = new TimeRushGameManager(difficulty);
            scoreManager = new TimeRushScoreManager(difficulty);
        } else {
            scoreManager = new ScoreManager(difficulty);
        }


        initializeViews();
        initializeGameState();
        setupGame();
        setupMusic();
    }
    private void initializeGameState() {
        if (isTimeRushMode) {
            TextView timerText = findViewById(R.id.timerText);
            timerText.setVisibility(View.VISIBLE);
            String targetNumber = numberGenerator.generateTargetNumber();
            if (targetLetterView != null) {
                targetLetterView.setText(targetNumber);
                targetLetterView.setVisibility(View.VISIBLE);
            }
            updateScoreDisplay();
            updateStreakDisplay();
            updateLivesDisplay();
            updateTimerDisplay();
            // Start timer after initial setup
            startTimeRushTimer();
        }
    }
    private void startTimeRushTimer() {
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && timeRushManager != null && !timeRushManager.isGameOver() && !isPaused) {
                    timeRushManager.updateTimer();
                    updateTimerDisplay();

                    if (timeRushManager.isGameOver()) {
                        showGameOverDialog();
                    } else {
                        timerHandler.postDelayed(this, 100);  // Update every 0.1 seconds for smoother display
                    }
                }
            }
        };
        timerHandler.postDelayed(timerRunnable, 100);
    }

    //test
    public void onBackPressed() {
        super.onBackPressed();
        if (!isPaused) {
            showPauseMenu();
        }
    }
    private void updateTimerDisplay() {
        TextView timerView = findViewById(R.id.timerText);
        if (timerView != null) {
            timerView.setText("Time: " + timeRushManager.getCurrentTimeFormatted() + "s");
        }
    }
    private void initializeViews() {
        gridView = findViewById(R.id.gameGrid);
        targetLetterView = findViewById(R.id.targetLetter);
        roundCounterView = findViewById(R.id.roundCounter);
        scoreView = findViewById(R.id.scoreView);
        streakView = findViewById(R.id.streakView);
        flashOverlay = findViewById(R.id.flashOverlay);
        TextView livesView = findViewById(R.id.livesView);
        TextView timerView = findViewById(R.id.timerText);
        ImageButton pauseButton = findViewById(R.id.pauseButton);
        if (targetLetterView != null) {
            targetLetterView.setVisibility(View.VISIBLE);
        }
        pauseButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        pauseButton.setOnClickListener(v -> showPauseMenu());
        Typeface pixarFont = ResourcesCompat.getFont(this, R.font.pixar);
        int textColor = Color.parseColor("#523502");

        if (scoreView != null) {
            scoreView.setTypeface(pixarFont);
            scoreView.setTextColor(textColor);
        }

        if (streakView != null) {
            streakView.setTypeface(pixarFont);
            streakView.setTextColor(textColor);
        }

        if (livesView != null) {
            livesView.setTypeface(pixarFont);
            livesView.setTextColor(textColor);
        }

        if (timerView != null) {
            timerView.setTypeface(pixarFont);
            timerView.setTextColor(textColor);
        }
        if (isTimeRushMode && timeRushManager != null) {
            if (roundCounterView != null) roundCounterView.setVisibility(View.GONE);
            if (livesView != null) {
                livesView.setVisibility(View.VISIBLE);
                livesView.setText("Lives: " + timeRushManager.getLives());
            }
            if (timerView != null) timerView.setVisibility(View.VISIBLE);
        } else {
            if (roundCounterView != null) roundCounterView.setVisibility(View.VISIBLE);
            if (livesView != null) livesView.setVisibility(View.GONE);
            if (timerView != null) timerView.setVisibility(View.GONE);
        }
    }

    private void showPauseMenu() {
        isPaused = true;

        pauseDialog = new Dialog(this);
        pauseDialog.setContentView(R.layout.dialog_pause_menu);
        pauseDialog.setCancelable(false);

        Button resumeButton = pauseDialog.findViewById(R.id.resumeButton);
        Button mainMenuButton = pauseDialog.findViewById(R.id.mainMenuButton);

        resumeButton.setOnClickListener(v -> resumeGame());
        mainMenuButton.setOnClickListener(v -> goToMainMenu());

        pauseDialog.show();
    }
    private void resumeGame() {
        isPaused = false;
        if (isTimeRushMode && timerHandler != null && timerRunnable != null) {
            timerHandler.postDelayed(timerRunnable, 100);
        }
        pauseDialog.dismiss();
    }

    private void goToMainMenu() {
        // Stop any timer/music
        if (timerHandler != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        finish();  // Return to main menu
    }
    private void updateScoreDisplay() {
        if (scoreView != null) {
            int currentScore = isTimeRushMode ? timeRushManager.getScore() : scoreManager.getCurrentScore();
            scoreView.setText("Score: " + currentScore);
        }
    }
    private void renderGameState(GameState state) {
        gridAdapter.updateGrid(state.board);
        targetLetterView.setText(state.target);
        scoreView.setText("Score: " + state.score);
        streakView.setText("Streak: " + state.streak);
        roundCounterView.setText("Runde: " + state.round + "/" + state.totalRounds);

        if (isTimeRushMode) {
            updateLives(state.lives);
            updateTimer(state.timerText);
        }
    }
    private void setupGame() {
        gameManager.nextRound();
        renderGameState(gameManager.getCurrentGameState());
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

    private void updateLivesDisplay() {
        TextView livesView = findViewById(R.id.livesView);
        if (livesView != null) {
            livesView.setText("Lives: " + timeRushManager.getLives());
        }
    }



    private void showFlash(boolean correct) {
        flashOverlay.setBackground(
                ContextCompat.getDrawable(this,
                        correct ? R.drawable.flash_correct : R.drawable.flash_wrong)
        );
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




    private void showGameOverDialog(GameState state) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_game_over);
        ((TextView) dialog.findViewById(R.id.finalScore)).setText("Final Score: " + state.score);

        Button okButton = dialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    private void handleAnswer(int position) {
        gameManager.handleAnswer(position);
        GameState state = gameManager.getCurrentGameState();
        renderGameState(state);

        if (gameManager.isGameOver()) {
            showGameOverDialog(state);
        }
    }
    private void updateStreakDisplay() {
        if (streakView != null) {
            if (isTimeRushMode) {
                streakView.setText("Streak: " + timeRushManager.getCurrentStreak());
            } else {
                streakView.setText("Streak: " + scoreManager.getStreak());
            }
        }
    }
    private int getGridSize() {
        switch (difficulty) {
            case "MEDIUM":
                return 5;
            case "HARD":
                return 7;
            default:
                return 3;
        }
    }

    private char generateRandomLetter() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (difficulty.equals("MEDIUM")) {
            letters += "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ";
        } else if (difficulty.equals("HARD")) {
            letters += "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ";
            letters += "أبتثجحخدذرزسشصضطظعغفقكلمنهوي";
        }
        return letters.charAt(random.nextInt(letters.length()));
    }

    private void showTargetLetter() {
        targetLetterView.setAlpha(0f);
        targetLetterView.setScaleX(0.5f);
        targetLetterView.setScaleY(0.5f);

        targetLetterView.setText(String.valueOf(targetLetter));

        targetLetterView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

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
        if (mediaPlayer != null) {
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
}

 */
}