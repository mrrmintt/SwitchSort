package com.example.switchsort.backend.game;

public class GameManager {
    private static final float TIME_BONUS = 0.5f;
    private final String difficulty;
    private final String mode;
    private final boolean gameMode; // "CLASSIC" oder "TIMERUSH"
    private final int maxLives = 3;
    private int lives;
    private final GameBoard gameBoard;
    private final ScoreManager scoreManager;
    private long roundStartTime;
    private float currentTime;
    private final int timeLimitPerRound; // in Sekunden für TimeRush
    private boolean isGameOver;


    // Konstruktor initialisiert Spiel basierend auf Schwierigkeitsgrad, Modus und Spielmodus (Classic oder Timerush)
    public GameManager(String difficulty, String mode, boolean gameMode) {
        this.difficulty = difficulty.toUpperCase();
        this.mode = mode.toUpperCase(); // "LETTER" oder "NUMBER"
        this.gameMode = gameMode;       // true = TIMERUSH, false = CLASSIC
        this.lives = maxLives;

        int gridSize = GameConfig.getGridSize(difficulty);
        this.timeLimitPerRound = GameConfig.getTimeLimit(difficulty);
        this.gameBoard = new GameBoard(gridSize);
        this.scoreManager = new ScoreManager(difficulty, gameMode);
        this.currentTime = GameConfig.getTimeLimit(difficulty);

        startNewRound(); // startet sofort eine neue Runde
    }

    // Startet eine neue Runde, wenn das Spiel nicht vorbei ist
    public void startNewRound() {
        if (isGameOver()) return;
        System.out.println("Test1"); // vermutlich Debug-Zeile, kann weg
        gameBoard.generateNewBoard(difficulty, mode);
        roundStartTime = System.currentTimeMillis(); // Startzeit speichern
    }

    // Verarbeitet Benutzereingabe, wertet Treffer aus und startet ggf. neue Runde
    public boolean handleUserInput(int position) {
        System.out.println("POSITION1:" + position); // Debug-Zeile, optional entfernen

        long timeSpent = (System.currentTimeMillis() - roundStartTime) / 1000;

        boolean correct = gameBoard.checkPosition(position);
        scoreManager.recordMatch(correct, timeSpent);

        if (!correct) {
            lives--; // Leben abziehen bei Fehler
        }

        // Wenn Zielpunktestand erreicht ist → Spielende
        if (scoreManager.hasReachedMaxScore()) {
            isGameOver = true;
            return true; // Entscheidung, was zurückgegeben wird, hängt von Gesamtlogik ab
        }

        // Bei Treffer: ggf. Zeitbonus und neue Runde
        if (correct) {
            if (gameMode) {
                addTimeBonus();
            }
            startNewRound();
        }

        return correct;
    }

    // Prüft ob das Spiel durch Punktelimit beendet ist
    public boolean hasReachedMaxScore() {
        return scoreManager.hasReachedMaxScore();
    }

    // Fügt Zeitbonus in TIMERUSH hinzu
    public void addTimeBonus() {
        currentTime += TIME_BONUS;
    }

    // Prüft, ob das Spiel vorbei ist (Leben aufgebraucht, Zeit abgelaufen, max Score erreicht)
    public boolean isGameOver() {
        if (gameMode) {
            long timeSpent = (System.currentTimeMillis() - roundStartTime) / 1000;
            return lives <= 0 || currentTime == 0 || scoreManager.hasReachedMaxScore();
        } else {
            return lives <= 0 || scoreManager.hasReachedMaxScore();
        }
    }

    // Verringert die aktuelle Zeit in TIMERUSH-Modus
    public void updateTimer() {
        currentTime -= 0.1;
        if (currentTime <= 0) {
            currentTime = 0;
            isGameOver = true;
        }
    }

    // Gibt aktuellen Spielzustand zurück (für UI oder Statusanzeige)
    public GameState getCurrentGameState() {
        String target = gameBoard.getTargetCharacter();

        // Sonderfall: Konvertiere Hex-Zahl in dezimal bei HARD+NUMBER
        if (difficulty.equals("HARD") && mode.equals("NUMBER")) {
            try {
                int value = Integer.parseInt(target, 16);
                target = String.valueOf(value);
            } catch (NumberFormatException e) {
                target = "?"; // Fallback falls Zielwert ungültig
            }
        }

        return new GameState(
                gameBoard.getFlatBoard(),
                target,
                lives,
                scoreManager.getCurrentScore(),
                scoreManager.getStreak(),
                gameMode
                        ? String.valueOf(Math.max(0, timeLimitPerRound - ((System.currentTimeMillis() - roundStartTime) / 1000)))
                        : ""
        );
    }

    // Gibt aktuelle verbleibende Zeit als formatierten String zurück (z. B. für UI-Anzeige)
    public String getCurrentTimeFormatted() {
        return String.format("%.1f", currentTime);
    }

    // Gibt verbleibende Zeit der Runde formatiert in Sekunden zurück (nur intern genutzt)
    private String getRemainingTimeFormatted() {
        long timeSpent = (System.currentTimeMillis() - roundStartTime) / 1000;
        long remaining = Math.max(0, timeLimitPerRound - timeSpent);
        return remaining + "s";
    }

    // Gibt aktuellen Punktestand zurück
    public int getCurrentScore() {
        return scoreManager.getCurrentScore();
    }
}
