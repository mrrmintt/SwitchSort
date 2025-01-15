package com.example.switchsort.frontend.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private char[] letters;
    private int gridSize;
    private View.OnClickListener onClickListener;
    private static final int BUTTON_SIZE = 150;  // Feste Größe für alle Buttons

    public GridAdapter(Context context, char[] letters, int gridSize, View.OnClickListener clickListener) {
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

            // Verwende die feste Größe für ALLE Buttons
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(BUTTON_SIZE, BUTTON_SIZE);
            button.setLayoutParams(params);
            button.setBackgroundColor(Color.GRAY);
            button.setTextColor(Color.WHITE);
            button.setTextSize(24);
            button.setPadding(0, 0, 0, 0);
        } else {
            button = (Button) convertView;
        }

        button.setText(String.valueOf(letters[position]));
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