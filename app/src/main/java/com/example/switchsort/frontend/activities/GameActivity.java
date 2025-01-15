package com.example.switchsort.frontend.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.switchsort.R;
import com.example.switchsort.frontend.adapters.GridAdapter;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private GridView gridView;
    private TextView targetLetterView;
    private GridAdapter gridAdapter;
    private MediaPlayer mediaPlayer;
    private TextView roundCounterView;
    private char targetLetter;
    private int currentRound = 1;
    private final int TOTAL_ROUNDS = 20;
    private char[] currentLetters;
    private String difficulty;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize and start game music with volume setting
        mediaPlayer = MediaPlayer.create(this, R.raw.playground);
        mediaPlayer.setLooping(true);
        float volume = MainActivity.getGameMusicVolume();  // Get volume setting from MainActivity
        mediaPlayer.setVolume(volume, volume);            // Set the volume
        mediaPlayer.start();

        difficulty = getIntent().getStringExtra("DIFFICULTY");
        setupViews();
        startNewRound();
    }

    private void setupViews() {
        gridView = findViewById(R.id.gameGrid);
        targetLetterView = findViewById(R.id.targetLetter);
        roundCounterView = findViewById(R.id.roundCounter);

        int gridSize = getGridSize();
        currentLetters = new char[gridSize * gridSize];

        // Erstelle Click Listener
        View.OnClickListener clickListener = v -> {
            int position = (int) v.getTag();
            checkAnswer(position);
        };

        // Übergebe gridSize und clickListener an den Adapter
        GridAdapter adapter = new GridAdapter(this, currentLetters, gridSize, clickListener);
        gridView.setNumColumns(gridSize);
        gridView.setAdapter(adapter);
    }

    private void startNewRound() {
        currentRound++;
        roundCounterView.setText("Runde: " + currentRound + "/20");

        // Animate round counter
        roundCounterView.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(200)
                .withEndAction(() ->
                        roundCounterView.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(200)
                                .start()
                )
                .start();

        generateNewBoard();
        showTargetLetter();
    }

    private void generateNewBoard() {
        // Generate target letter
        targetLetter = generateRandomLetter();
        targetLetterView.setText(String.valueOf(targetLetter));

        // Fill grid with random letters
        int correctPosition = random.nextInt(currentLetters.length);

        for (int i = 0; i < currentLetters.length; i++) {
            if (i == correctPosition) {
                currentLetters[i] = targetLetter;
            } else {
                currentLetters[i] = generateRandomLetter();
            }
        }

        // Update grid
        ((GridAdapter) gridView.getAdapter()).notifyDataSetChanged();  // Korrigierter Cast
    }

    private void checkAnswer(int position) {
        if (currentLetters[position] == targetLetter) {
            //Toast.makeText(this, "Richtig!", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Falsch!", Toast.LENGTH_SHORT).show();
        }

        currentRound++;
        if (currentRound <= TOTAL_ROUNDS) {
            startNewRound();
        } else {
            finish();
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
            letters += "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ"; // Griechische Buchstaben
        } else if (difficulty.equals("HARD")) {
            letters += "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ"; // Griechische Buchstaben
            letters += "أبتثجحخدذرزسشصضطظعغفقكلمنهوي"; // Arabische Buchstaben
        }
        return letters.charAt(random.nextInt(letters.length()));
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

}
