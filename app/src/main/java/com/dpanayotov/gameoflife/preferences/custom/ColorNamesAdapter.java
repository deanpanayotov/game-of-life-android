package com.dpanayotov.gameoflife.preferences.custom;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dpanayotov.gameoflife.R;
import com.dpanayotov.gameoflife.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dean Panayotov on 1/1/2017
 */

public class ColorNamesAdapter extends RecyclerView.Adapter<ColorNamesAdapter
        .ColorNamesViewHolder> {

    List<Integer> names;

    public ColorNamesAdapter() {
        this.names = Preferences.Color.getResIds();
    }

    @Override
    public ColorNamesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColorNamesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .view_item_color_name, parent, false));
    }

    @Override
    public void onBindViewHolder(ColorNamesViewHolder holder, int position) {
        holder.name.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class ColorNamesViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ColorNamesViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView;
        }
    }
}
