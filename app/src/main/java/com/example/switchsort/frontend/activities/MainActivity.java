package com.example.switchsort.frontend.activities;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import com.example.switchsort.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDifficultyButtons();
    }

    private void setupDifficultyButtons() {
        Button easyButton = findViewById(R.id.buttonEasy);
        Button mediumButton = findViewById(R.id.buttonMedium);
        Button hardButton = findViewById(R.id.buttonHard);

        easyButton.setOnClickListener(v -> startGame("EASY"));
        mediumButton.setOnClickListener(v -> startGame("MEDIUM"));
        hardButton.setOnClickListener(v -> startGame("HARD"));
    }

    private void startGame(String difficulty) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("DIFFICULTY", difficulty);
        startActivity(intent);
    }
}