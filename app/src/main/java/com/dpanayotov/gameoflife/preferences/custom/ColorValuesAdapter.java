package com.dpanayotov.gameoflife.preferences.custom;

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
//        mLayoutId = layoutId;
//        mGrabHandleId = grabHandleId;
//        mDragOnLongPress = dragOnLongPress;
        setHasStableIds(true);
        setItemList(colors);
    }

    @Override
    public ColorValuesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColorValuesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .view_item_color_value, parent, false), 0, false);
    }

    @Override
    public void onBindViewHolder(ColorValuesViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.value.setBackgroundColor(colors.get(position));
    }

    class ColorValuesViewHolder extends DragItemAdapter.ViewHolder {

        View value;

        public ColorValuesViewHolder(View itemView, int handleResId, boolean dragOnLongPress) {
            super(itemView, handleResId, dragOnLongPress);
            value = itemView;
        }
    }
}
