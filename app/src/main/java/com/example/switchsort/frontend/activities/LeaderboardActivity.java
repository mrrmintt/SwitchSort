package com.example.switchsort.frontend.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Typeface;
import androidx.core.content.res.ResourcesCompat;

import com.example.switchsort.R;
import com.example.switchsort.backend.database.DatabaseHelper;
import com.example.switchsort.backend.database.Player;
import com.example.switchsort.frontend.adapters.LeaderboardAdapter;

import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private Button easyButton, mediumButton, hardButton;
    private Button classicButton, timeRushButton;
    private TextView titleTextView;
    private String currentDifficulty = "EASY";
    private String currentGameMode = "CLASSIC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initializeViews();
        setupButtons();
        updateLeaderboard();
    }

    private void initializeViews() {
        // Difficulty buttons
        easyButton = findViewById(R.id.leaderboardEasyButton);
        mediumButton = findViewById(R.id.leaderboardMediumButton);
        hardButton = findViewById(R.id.leaderboardHardButton);

        // Game mode buttons
        classicButton = findViewById(R.id.leaderboardClassicButton);
        timeRushButton = findViewById(R.id.leaderboardTimeRushButton);

        titleTextView = findViewById(R.id.leaderboardTitle);

        // Set custom font
        Typeface pixarFont = ResourcesCompat.getFont(this, R.font.pixar);
        easyButton.setTypeface(pixarFont);
        mediumButton.setTypeface(pixarFont);
        hardButton.setTypeface(pixarFont);
        classicButton.setTypeface(pixarFont);
        timeRushButton.setTypeface(pixarFont);
        titleTextView.setTypeface(pixarFont);
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        backButton.setOnClickListener(v -> finish());
    }

    private void setupButtons() {
        easyButton.setOnClickListener(v -> {
            currentDifficulty = "EASY";
            updateLeaderboard();
        });
        mediumButton.setOnClickListener(v -> {
            currentDifficulty = "MEDIUM";
            updateLeaderboard();
        });
        hardButton.setOnClickListener(v -> {
            currentDifficulty = "HARD";
            updateLeaderboard();
        });

        classicButton.setOnClickListener(v -> {
            currentGameMode = "CLASSIC";
            updateLeaderboard();
        });
        timeRushButton.setOnClickListener(v -> {
            currentGameMode = "TIME_RUSH";
            updateLeaderboard();
        });
    }

    private void updateLeaderboard() {
        // Update button appearances
        easyButton.setAlpha(currentDifficulty.equals("EASY") ? 1f : 0.5f);
        mediumButton.setAlpha(currentDifficulty.equals("MEDIUM") ? 1f : 0.5f);
        hardButton.setAlpha(currentDifficulty.equals("HARD") ? 1f : 0.5f);

        classicButton.setAlpha(currentGameMode.equals("CLASSIC") ? 1f : 0.5f);
        timeRushButton.setAlpha(currentGameMode.equals("TIME_RUSH") ? 1f : 0.5f);

        // Get and display scores
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Player> topPlayers = dbHelper.getTopScores(currentDifficulty, currentGameMode, 10);

        ListView leaderboardList = findViewById(R.id.leaderboardList);
        LeaderboardAdapter adapter = new LeaderboardAdapter(this, topPlayers);
        leaderboardList.setAdapter(adapter);
    }
}