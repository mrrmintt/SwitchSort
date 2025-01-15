package com.example.switchsort.frontend.activities;

import android.os.Bundle;
import android.view.View;
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

        // Verstecke den Action Bar (Title)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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
        roundCounterView.setText("Runde: " + currentRound + "/" + TOTAL_ROUNDS);
        generateNewBoard();
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
}
