package com.dpanayotov.gameoflife.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dean Panayotov on 9/25/2016
 */

public class Resolution implements Parcelable {
    public int gridWidth;
    public int gridHeight;
    public int cellSize;

    public Resolution(int gridWidth, int gridHeight, int cellSize) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.cellSize = cellSize;
    }

    @Override
    public String toString() {
        return gridWidth + "x" + gridHeight;
    }

    protected Resolution(Parcel in) {
        gridWidth = in.readInt();
        gridHeight = in.readInt();
        cellSize = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(gridWidth);
        dest.writeInt(gridHeight);
        dest.writeInt(cellSize);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Resolution> CREATOR = new Parcelable.Creator<Resolution>() {
        @Override
        public Resolution createFromParcel(Parcel in) {
            return new Resolution(in);
        }

        @Override
        public Resolution[] newArray(int size) {
            return new Resolution[size];
        }
    };
}