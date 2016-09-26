package com.dpanayotov.gameoflife.util;

/**
 * Created by Dean Panayotov on 9/25/2016
 */

public class Resolution {
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
}