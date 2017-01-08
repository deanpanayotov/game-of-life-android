package com.dpanayotov.gameoflife.preferences.custom;

import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dpanayotov.gameoflife.R;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.List;

/**
 * Created by Dean Panayotov on 1/2/2017
 */

public class ColorValuesAdapter extends DragItemAdapter<Pair<Integer, Integer>, ColorValuesAdapter
        .ColorValuesViewHolder> {

    List<Pair<Integer, Integer>> colors;

    OnItemClickedListener onItemClickedListener;

    public ColorValuesAdapter(List<Pair<Integer, Integer>> colors, OnItemClickedListener
            onItemClickedListener) {
        this.colors = colors;
        this.onItemClickedListener = onItemClickedListener;

        setHasStableIds(true);
        setItemList(colors);
    }

    @Override
    public ColorValuesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColorValuesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .view_item_color_value, parent, false));
    }

    @Override
    public void onBindViewHolder(ColorValuesViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.value.setBackgroundColor(colors.get(position).second);
        holder.value.setTag(position);
    }

    @Override
    public long getItemId(int position) {
        return colors.get(position).first;
    }

    class ColorValuesViewHolder extends DragItemAdapter.ViewHolder {

        View value;

        public ColorValuesViewHolder(View itemView) {
            super(itemView, R.id.grab_handle, false);
            value = itemView;
        }

        @Override
        public void onItemClicked(View view) {
            super.onItemClicked(view);
            onItemClickedListener.onItemClicked((int) value.getTag());
        }
    }

    public interface OnItemClickedListener {
        void onItemClicked(int position);
    }
}
