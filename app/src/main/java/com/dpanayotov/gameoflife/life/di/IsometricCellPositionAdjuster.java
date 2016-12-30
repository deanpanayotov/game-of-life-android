package com.dpanayotov.gameoflife.life.di;

import android.graphics.Point;

/**
 * Created by Dean Panayotov on 12/4/2016
 */

public class IsometricCellPositionAdjuster implements CellPositionAdjuster{
    @Override
    public void adjustPosition(Point cellPosition){
        cellPosition.y = (cellPosition.y + i * halfCell) % screenHeight;
    };
}
