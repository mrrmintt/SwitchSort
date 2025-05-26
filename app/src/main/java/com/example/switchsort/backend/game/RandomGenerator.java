package com.example.switchsort.backend.game;

import java.util.Random;

public class RandomGenerator {
    private final String difficulty;
    private final Random random;
    private final String mode;

    // Buchstaben
    private static final String LATIN_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String GREEK_CHARS = "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ";
    private static final String ARABIC_CHARS = "أبتثجحخدذرزسشصضطظعغفقكلمنهوي";
    private static final String NUMBERS = "0123456789";

    // Zahlen
    private final String[] latinNumbers = {
            "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X",
            "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX"
    };

    //Konstruktor
    public RandomGenerator(String difficulty, String mode) {
        this.difficulty = difficulty;
        this.random = new Random();
        this.mode = mode;
    }

    //Wählt einen Random Character aus und macht in zu String
    public String generateTargetLetter() {
        switch (difficulty) {
            case "EASY":
                return String.valueOf(generateRandomChar(LATIN_CHARS + NUMBERS));
            case "MEDIUM":
                return String.valueOf(generateRandomChar(LATIN_CHARS + GREEK_CHARS + NUMBERS));
            case "HARD":
                return String.valueOf(generateRandomChar(LATIN_CHARS + GREEK_CHARS + ARABIC_CHARS + NUMBERS));
            default:
                return String.valueOf(generateRandomChar(LATIN_CHARS));
        }

    }

    // Lässt eine Zufallszahl egnerieren, wenn die Schwierigkeit Medium ist gibt es Römische Zahlen, bei Schwer
    // gibt es Hexadezimalzahlen und bei leicht einfach die Zahl
    public String generateTargetNumber() {
        int targetNumber = generateRandomNumber();

        switch (difficulty) {
            case "MEDIUM":
                // Wenn die Zahl 0 ist wird 0 zurückgegeben
                if (targetNumber == 0) {
                    return "0";
                }
                // wegen der Auflistung weil die bei 1 und nicht bei 0 anfängt
                return latinNumbers[targetNumber - 1];

            case "HARD":
                return Integer.toHexString(targetNumber).toUpperCase();


            default: // EASY
                return String.valueOf(targetNumber);
        }
    }

    // Löst die Generierung eines zufälligen Symbols aus (Zahl oder Buchstabe)
    public String generateRandom() {
        if(mode.equals("LETTER")) {
            return generateTargetLetter();
        } else {
            return generateTargetNumber();
        }
    }

    // Generiert einen zufälligen Character
    private char generateRandomChar(String source) {
        return source.charAt(random.nextInt(source.length()));
    }

    // Generiert eine zufällige Zahl
    public int generateRandomNumber() {
        return random.nextInt(21);
    }


    // Mapped normale Zahlen römischen zu
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
