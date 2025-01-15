package com.example.switchsort.frontend.backend.utils;

import java.util.Random;

public class CharacterGenerator {
    private final String difficulty;
    private final Random random;

    private static final String LATIN_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String GREEK_CHARS = "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ";
    private static final String ARABIC_CHARS = "أبتثجحخدذرزسشصضطظعغفقكلمنهوي";
    private static final String NUMBERS = "0123456789";

    public CharacterGenerator(String difficulty) {
        this.difficulty = difficulty;
        this.random = new Random();
    }

    public char generateTarget() {
        switch (difficulty) {
            case "EASY":
                return getRandomChar(LATIN_CHARS + NUMBERS);
            case "MEDIUM":
                return getRandomChar(LATIN_CHARS + GREEK_CHARS + NUMBERS);
            case "HARD":
                return getRandomChar(LATIN_CHARS + GREEK_CHARS + ARABIC_CHARS + NUMBERS);
            default:
                return getRandomChar(LATIN_CHARS);
        }
    }

    public char generateRandom() {
        return generateTarget();
    }

    private char getRandomChar(String source) {
        return source.charAt(random.nextInt(source.length()));
    }
}