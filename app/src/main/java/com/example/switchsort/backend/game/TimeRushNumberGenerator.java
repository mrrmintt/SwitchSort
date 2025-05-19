package com.example.switchsort.backend.game;

import java.util.Random;

public class TimeRushNumberGenerator {
    private final String difficulty;
    private final Random random = new Random();

    private final String[] latinNumbers = {
            "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X",
            "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX"
    };

    public TimeRushNumberGenerator(String difficulty) {
        this.difficulty = difficulty;
    }


    public String generateTargetNumber() {
        return String.valueOf(random.nextInt(21));
    }


    public String generateButtonNumber(String targetNumber) {
        int targetValue = Integer.parseInt(targetNumber);

        switch (difficulty) {
            case "MEDIUM":
                if (targetValue == 0) {
                    return "0";
                }
                return latinNumbers[targetValue - 1];
            case "HARD":
                return Integer.toHexString(targetValue).toUpperCase();
            default: // EASY
                return targetNumber;
        }
    }
    public static int convertLatinToNumber(String latin) {
        switch(latin) {
            case "I": return 1;
            case "II": return 2;
            case "III": return 3;
            case "IV": return 4;
            case "V": return 5;
            case "VI": return 6;
            case "VII": return 7;
            case "VIII": return 8;
            case "IX": return 9;
            case "X": return 10;
            case "XI": return 11;
            case "XII": return 12;
            case "XIII": return 13;
            case "XIV": return 14;
            case "XV": return 15;
            case "XVI": return 16;
            case "XVII": return 17;
            case "XVIII": return 18;
            case "XIX": return 19;
            case "XX": return 20;
            default: return 0;
        }
    }
}