package com.example.switchsort.frontend.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.switchsort.R;
import com.example.switchsort.backend.database.Player;
import java.util.List;

public class LeaderboardAdapter extends ArrayAdapter<Player> {
    public LeaderboardAdapter(Context context, List<Player> players) {
        super(context, 0, players);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.leaderboard_item, parent, false);
        }

        Player player = getItem(position);

        TextView rankText = convertView.findViewById(R.id.rankText);
        TextView nameText = convertView.findViewById(R.id.nameText);
        TextView scoreText = convertView.findViewById(R.id.scoreText);

        rankText.setText(String.valueOf(position + 1));
        nameText.setText(player.getName());
        scoreText.setText(String.valueOf(player.getScore()));

        return convertView;
    }
}