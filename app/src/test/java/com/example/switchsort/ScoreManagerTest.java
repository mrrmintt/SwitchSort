package com.example.switchsort;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.switchsort.backend.game.ScoreManager;

public class ScoreManagerTest {

    private ScoreManager easyManager;
    private ScoreManager mediumManager;
    private ScoreManager hardManager;

    @Before
    public void setup() {
        easyManager = new ScoreManager("EASY", false);
        mediumManager = new ScoreManager("MEDIUM", false);
        hardManager = new ScoreManager("HARD", false);
    }

    @Test
    public void testEasyScoreCalculation_FastAnswer() {
        easyManager.recordMatch(true, 1); // <= 2s → 3x multiplier
        assertEquals(300, easyManager.getCurrentScore());
    }

    @Test
    public void testMediumScoreCalculation_MediumSpeed() {
        mediumManager.recordMatch(true, 2); // 1–3s → 3x multiplier
        assertEquals(450, mediumManager.getCurrentScore()); // 100 × 1.5 × 3
    }

    @Test
    public void testHardScoreCalculation_SlowAnswer() {
        hardManager.recordMatch(true, 3); // > 1s → 3x multiplier
        assertEquals(600, hardManager.getCurrentScore()); // 100 × 2 × 3
    }

    @Test
    public void testStreakResetAfterWrongAnswer() {
        easyManager.recordMatch(true, 1);  // Streak = 1
        easyManager.recordMatch(false, 0); // Reset
        assertEquals(0, easyManager.getStreak());
    }

    @Test
    public void testStreakBonus_3CorrectAnswers() {
        for (int i = 0; i < 3; i++) {
            easyManager.recordMatch(true, 1); // jeweils 300 Punkte
        }
        // Bonus für 3er Streak: +50 beim 3. Treffer
        // 300 + 300 + (300 + 50) = 950
        assertEquals(950, easyManager.getCurrentScore());
    }

    @Test
    public void testStreakBonus_5CorrectAnswers() {
        for (int i = 0; i < 5; i++) {
            easyManager.recordMatch(true, 1);
        }
        // 5. Treffer bekommt +100 Bonus
        // Letzter Punkt: 300 + 100 = 400
        // Gesamtscore: 300*4 + 400 = 1600
        assertEquals(1650, easyManager.getCurrentScore());
    }

    @Test
    public void testStreakBonus_10CorrectAnswers() {
        for (int i = 0; i < 10; i++) {
            easyManager.recordMatch(true, 1);
        }
        // 3. +50, 5. +100, 6. keine, 10. +200
        int expected = (300 * 10) + 50 + 100 + 200; // 3000 + 350 = 3350
        assertEquals(3450, easyManager.getCurrentScore());
    }

    @Test
    public void testIncorrectMatchDoesNotIncreaseScore() {
        easyManager.recordMatch(false, 1);
        assertEquals(0, easyManager.getCurrentScore());
    }
}
