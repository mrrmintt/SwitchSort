package com.example.switchsort.backend.game;

public class TimeRushScoreManager extends ScoreManager {
    private static final int CORRECT_MATCH_POINTS = 100;
    private static final int PHASE_MULTIPLIER_1 = 1;
    private static final int PHASE_MULTIPLIER_2 = 2;
    private static final int PHASE_MULTIPLIER_3 = 3;

    private int currentScore = 0;
    private int consecutiveCorrect = 0;

    public TimeRushScoreManager(String difficulty) {
        super(difficulty);
    }

    public void recordMatch(boolean correct, int currentPhase) {
        if (correct) {
            int points = CORRECT_MATCH_POINTS;

            // Apply phase multiplier
            switch (currentPhase) {
                case 1:
                    points *= PHASE_MULTIPLIER_1;
                    break;
                case 2:
                    points *= PHASE_MULTIPLIER_2;
                    break;
                case 3:
                    points *= PHASE_MULTIPLIER_3;
                    break;
            }

            // Add streak bonus
            consecutiveCorrect++;
            if (consecutiveCorrect >= 5) {
                points += 50;
            }

            currentScore += points;
        } else {
            consecutiveCorrect = 0;
        }
    }

    @Override
    public int getCurrentScore() {
        return currentScore;
    }
}