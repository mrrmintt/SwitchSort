package com.example.switchsort.frontend.activities;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import com.example.switchsort.R;
import com.example.switchsort.backend.database.DatabaseHelper;
import com.example.switchsort.backend.database.Player;
import com.example.switchsort.backend.game.GameManager;
import com.example.switchsort.backend.game.GameState;
import com.example.switchsort.backend.game.MusicManager;
import com.example.switchsort.frontend.adapters.GridAdapter;

public class GameActivity extends AppCompatActivity {

    private GameManager gameManager;
    private GridAdapter gridAdapter;
    private GridView gridView;
    private TextView targetLetterView, scoreView, streakView, timerView, livesView;
    private View flashOverlay;
    private ImageButton pauseButton;
    private Dialog pauseDialog;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private boolean isPaused = false;
    private boolean isTimeRushMode;
    private String difficulty;
    private String symbol;
    private MusicManager musicManager;
    private boolean isAppSwitch = false;

    // Einstiegsfunktion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Layout für das Activity setzen
        setContentView(R.layout.activity_game);

        // Intent auslesen, um Parameter zu holen
        Intent intent = getIntent();
        difficulty = intent.getStringExtra("DIFFICULTY");  // Schwierigkeitsgrad holen
        isTimeRushMode = "TIME_RUSH".equals(intent.getStringExtra("GAME_MODE")); // Spielmodus prüfen
        symbol = intent.getStringExtra("MODE"); // Spielsymbol holen

        // GameManager mit den Parametern initialisieren
        gameManager = new GameManager(difficulty, symbol, isTimeRushMode);
        // Musikmanager aus MainActivity holen (vermutlich Singleton oder statische Methode)
        musicManager = MainActivity.getMusicManager();

        // Views initialisieren (Buttons, Textfelder etc.)
        initializeViews();
        // UI mit aktuellem Status updaten
        updateUI();

        // Falls Zeitmodus aktiv, Timer starten
        if (isTimeRushMode) {
            startTimeRushTimer();
        }
    }

    private void initializeViews() {
        // Views aus dem Layout per ID holen
        gridView = findViewById(R.id.gameGrid);
        targetLetterView = findViewById(R.id.targetLetter);
        scoreView = findViewById(R.id.scoreView);
        streakView = findViewById(R.id.streakView);
        timerView = findViewById(R.id.timerText);
        livesView = findViewById(R.id.livesView);
        flashOverlay = findViewById(R.id.flashOverlay);
        pauseButton = findViewById(R.id.pauseButton);

        // Schriftart Rubik laden (extern, über ResourcesCompat)
        Typeface font = ResourcesCompat.getFont(this, R.font.rubik);
        int textColor = Color.parseColor("#FFFFFF"); // Weißer Text

        // Für wichtige TextViews Schriftart und Farbe setzen, falls sie nicht null sind
        for (TextView view : new TextView[]{targetLetterView, scoreView, streakView, timerView, livesView}) {
            if (view != null) {
                view.setTypeface(font);
                view.setTextColor(textColor);
            }
        }

        // Pause-Button weiß einfärben
        pauseButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        // Klicklistener, der das Pausenmenü öffnet
        pauseButton.setOnClickListener(v -> showPauseMenu());
    }

    private void updateUI() {
        // Aktuellen Spielzustand holen
        GameState state = gameManager.getCurrentGameState();

        // GridAdapter neu erstellen mit aktuellem Spielbrett und Click-Listener für jedes Feld
        gridAdapter = new GridAdapter(this, state.getBoard(), (int) Math.sqrt(state.getBoard().length), v -> {
            int pos = (int) v.getTag(); // Position des geklickten Felds
            boolean correct = gameManager.handleUserInput(pos); // Eingabe verarbeiten (richtig/falsch)
            showFlash(correct); // Feedback geben (z.B. blinkender Effekt)
            updateUI(); // UI neu updaten (Rekursion, könnte performance-kritisch sein)
            if (gameManager.isGameOver())
                showGameOverDialog(); // Spielende prüfen und Dialog anzeigen
        });

        // GridView auf korrekte Spaltenzahl einstellen (Quadratwurzel der Boardgröße)
        gridView.setNumColumns((int) Math.sqrt(state.getBoard().length));
        gridView.setAdapter(gridAdapter);

        // UI-Elemente mit aktuellen Werten füllen
        targetLetterView.setText(state.getTargetCharacter());
        scoreView.setText("Points: " + state.getCurrentScore());
        livesView.setText("Lives: " + state.getLives());
        streakView.setText("Streak: " + state.getStreak());

        // Timer nur anzeigen, wenn Zeitmodus aktiv ist
        if (isTimeRushMode) {
            timerView.setText("Time: " + state.getTimerText());
            timerView.setVisibility(View.VISIBLE);
        } else {
            timerView.setVisibility(View.GONE);
        }
    }


    private void startTimeRushTimer() {
        timerHandler = new Handler(); // Handler für wiederkehrende Aufgaben
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                // Nur ausführen, wenn Activity nicht beendet wird, gameManager existiert,
                // das Spiel nicht vorbei ist und nicht pausiert wurde
                if (!isFinishing() && gameManager != null && !gameManager.isGameOver() && !isPaused) {
                    gameManager.updateTimer(); // Timer im Spiel aktualisieren
                    updateTimerDisplay();      // UI-Timer anzeigen aktualisieren

                    if (gameManager.isGameOver()) {
                        showGameOverDialog();  // Spielende anzeigen
                    } else {
                        // Runnable in 100 ms erneut ausführen (regelmäßiges Update)
                        timerHandler.postDelayed(this, 100);
                    }
                }
            }
        };
        // Erste Ausführung nach 100 ms starten
        timerHandler.postDelayed(timerRunnable, 100);
    }


    private void updateTimerDisplay() {
        if (timerView != null) {
            // Timer-Text mit formatiertem aktuellen Spielzeit-Wert setzen
            timerView.setText("Time: " + gameManager.getCurrentTimeFormatted() + "s");
        }
    }

    private void showPauseMenu() {
        isPaused = true; // Spielstatus auf pausiert setzen

        // Dialog mit transparentem Stil erstellen
        pauseDialog = new Dialog(this, R.style.TransparentDialog);
        pauseDialog.setContentView(R.layout.dialog_pause_menu); // Layout für Pause-Menü setzen
        pauseDialog.setCancelable(false); // Dialog nicht über Back-Button schließbar machen

        // Resume-Button: Spiel fortsetzen
        pauseDialog.findViewById(R.id.resumeButton).setOnClickListener(v -> resumeGame());

        // Hauptmenü-Button: Zurück zum Hauptmenü wechseln
        pauseDialog.findViewById(R.id.mainMenuButton).setOnClickListener(v -> goToMainMenu());

        pauseDialog.show(); // Dialog anzeigen
    }

    private void resumeGame() {
        isPaused = false; // Spiel wird fortgesetzt

        // Wenn Zeitmodus aktiv und TimerHandler existiert, Timer neu starten
        if (isTimeRushMode && timerHandler != null) {
            timerHandler.postDelayed(timerRunnable, 100);
        }

        // Pause-Dialog schließen, falls sichtbar
        if (pauseDialog != null) pauseDialog.dismiss();
    }

    private void goToMainMenu() {
        // Timer-Callbacks entfernen, um keine weiteren Timer-Events zu bekommen
        if (timerHandler != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }

        // Musik stoppen (komplett, nicht nur pausieren)
        musicManager.stopMusic();

        // Activity beenden und zurück zum Hauptmenü (vermutlich vorheriger Screen)
        finish();
    }

    private void showGameOverDialog() {
        Dialog dialog = new Dialog(this, R.style.TransparentDialog);
        dialog.setContentView(R.layout.dialog_game_over);

        // Wichtig: Dialog darf nicht durch Touch außerhalb oder Back-Button geschlossen werden
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        int finalScore = gameManager.getCurrentScore();
        ((TextView) dialog.findViewById(R.id.finalScore)).setText("Final Score: " + finalScore);

        TextView gameOverMessage = dialog.findViewById(R.id.gameOverMessage);
        if (gameManager.hasReachedMaxScore()) {
            gameOverMessage.setText("Max Score reached! \nYou beat the game!");
            gameOverMessage.setVisibility(View.VISIBLE);
        } else {
            gameOverMessage.setVisibility(View.GONE);
        }

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String playerName = getIntent().getStringExtra("PLAYER_NAME");
        String mode = isTimeRushMode ? "TIME_RUSH" : "CLASSIC";
        Player player = new Player(playerName, deviceId, finalScore, difficulty, mode);

        if (finalScore > 0) {
            new DatabaseHelper(this).addScore(player);
        }

        dialog.findViewById(R.id.okButton).setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }


    private void showFlash(boolean correct) {
        // Hintergrund des Overlays je nach Ergebnis setzen (richtig/falsch)
        flashOverlay.setBackground(ContextCompat.getDrawable(this, correct ? R.drawable.flash_correct : R.drawable.flash_wrong));
        flashOverlay.setVisibility(View.VISIBLE); // Overlay sichtbar machen
        flashOverlay.setAlpha(0.7f); // Transparenz initial setzen

        // Animation: Alpha von 0.7 auf 0 in 200 ms, dann Overlay unsichtbar machen
        flashOverlay.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        flashOverlay.setVisibility(View.INVISIBLE); // Overlay nach Animation verstecken
                    }
                })
                .start();
    }


    // Lifecycle

    @Override
    protected void onRestart() {
        super.onRestart();
        musicManager.onResume(); // Musik-Manager wieder aktivieren (Fortsetzen)
        System.out.println("ON RESTART GAME");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Musik starten, wenn noch nicht spielend
        if (!musicManager.isPlaying()) {
            musicManager.setMenuBoolean(false); // Status setzen, dass Spielmodus aktiv ist
            musicManager.setupMusic(this, false); // Musik initialisieren
        }
        System.out.println("ON START GAME");
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicManager.onResume(); // Musik fortsetzen, wenn Activity sichtbar wird
        System.out.println("ON RESUME GAME");
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Wenn App durch User aktiv verlassen wird (isAppSwitch), Pause-Menü anzeigen
        if (isAppSwitch) {
            showPauseMenu();
            isAppSwitch = false; // Reset Flag
        }

        musicManager.onPause(); // Musik anhalten
        System.out.println("ON PAUSE GAME");
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Nur Musik pausieren, wenn nicht im Menü (sonst startet sie neu beim Zurückkehren)
        if (!musicManager.getMenuBoolean()) {
            musicManager.onPause();
            System.out.println("ON STOP GAME");
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        // Flag setzen, wenn User die App aktiv verlässt (Home, Anruf, etc.)
        isAppSwitch = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Timer stoppen und Handler/Runnable freigeben, um Memory-Leaks zu verhindern
        if (timerHandler != null) {
            timerHandler.removeCallbacks(timerRunnable);
            timerRunnable = null;
            timerHandler = null;
        }

        // Pause-Dialog schließen, falls noch offen, und Referenz freigeben
        if (pauseDialog != null && pauseDialog.isShowing()) {
            pauseDialog.dismiss();
            pauseDialog = null;
        }
        System.out.println("ON DESTROY GAME");
    }
}


