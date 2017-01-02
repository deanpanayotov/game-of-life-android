package com.dpanayotov.gameoflife.preferences.custom;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dpanayotov.gameoflife.R;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.List;

/**
 * Created by Dean Panayotov on 1/2/2017
 */

public class ColorValuesAdapter extends DragItemAdapter<Integer, ColorValuesAdapter
        .ColorValuesViewHolder> {

    List<Integer> colors;

    public ColorValuesAdapter(List<Integer> colors) {
        this.colors = colors;

        setHasStableIds(true);
        setItemList(colors);
    }

    @Override
    public ColorValuesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColorValuesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .view_item_color_value, parent, false));
    }

    @Override
    public int getItemCount() {
        Log.d("zxc", "getItemCount: "+colors.size());
        return colors.size();
    }

    @Override
    public void onBindViewHolder(ColorValuesViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.value.setBackgroundColor(colors.get(position));
        holder.value.setVisibility(View.VISIBLE);
    }

    class ColorValuesViewHolder extends DragItemAdapter.ViewHolder {

        View value;

        public ColorValuesViewHolder(View itemView) {
            super(itemView, R.id.grab_handle, false);
            value = itemView;
        }
    }
}
