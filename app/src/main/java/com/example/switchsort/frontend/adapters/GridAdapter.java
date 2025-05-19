package com.example.switchsort.frontend.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.view.MotionEvent;
import android.graphics.Typeface;
import androidx.core.content.res.ResourcesCompat;
import android.view.animation.OvershootInterpolator;
import androidx.core.content.ContextCompat;

import com.example.switchsort.R;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private String[] letters;  // Changed from char[] to String[]
    private int gridSize;
    private View.OnClickListener onClickListener;
    private static final int BUTTON_SIZE = 150;  // Feste Größe für alle Buttons

    public GridAdapter(Context context, String[] letters, int gridSize, View.OnClickListener clickListener) {  // Changed parameter type
        this.context = context;
        this.letters = letters;
        this.gridSize = gridSize;
        this.onClickListener = clickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null) {
            button = new Button(context);

            // Button size
            int buttonSize = 150;
            button.setLayoutParams(new ViewGroup.LayoutParams(buttonSize, buttonSize));

            // Style
            button.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_button));
            button.setTextColor(Color.WHITE);
            button.setPadding(0, 0, 0, 0);

            // Font
            Typeface customFont = ResourcesCompat.getFont(context, R.font.pixar);
            button.setTypeface(customFont);

            // Animation beim Klicken
            button.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.animate()
                                .scaleX(0.9f)
                                .scaleY(0.9f)
                                .setDuration(100)
                                .start();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .start();
                        break;
                }
                return false;
            });
        } else {
            button = (Button) convertView;
        }

        // Handle text content and size
        String buttonText = letters[position];  // No need for String.valueOf anymore
        button.setText(buttonText != null ? buttonText : "");  // Null check added

        // Adjust text size based on content length
        if (buttonText != null) {
            if (buttonText.length() > 3) {
                button.setTextSize(16);  // Smallest size for long numbers (e.g., "XVIII")
            } else if (buttonText.length() > 2) {
                button.setTextSize(18);  // Medium size for 3-char numbers
            } else {
                button.setTextSize(24);  // Original size for 1-2 char numbers
            }
        }

        button.setTag(position);
        button.setOnClickListener(onClickListener);

        return button;
    }

    @Override
    public int getCount() {
        return letters.length;
    }

    @Override
    public Object getItem(int position) {
        return letters[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}