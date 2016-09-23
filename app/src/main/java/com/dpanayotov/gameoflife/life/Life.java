package com.dpanayotov.gameoflife.life;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dean Panayotov on 9/23/2016
 */

public class Life {

    private List<Grid> queue;

    private Grid grid;

    private Grid previousGrid;

    public Grid summedGrid;

    public Life() {
        previousGrid = new Grid(false);
        grid = new Grid(true);
        resetQueue();
    }

    public void update() {

        Grid nextGrid = new Grid(false);

        for (int i = 0; i < Constants.GRID_WIDTH; i++) {
            for (int j = 0; j < Constants.GRID_HEIGHT; j++) {
                int neighboursCount = grid.getNeighboursCount(i, j);
                if ((grid.cells[i][j] == 1 && neighboursCount == 2) || neighboursCount == 3) {
                    nextGrid.cells[i][j] = 1;
                }
            }
        }

        if (previousGrid.equals(nextGrid)) {
            previousGrid = new Grid(false);
            grid = new Grid(true);
            resetQueue();
        } else {
            previousGrid = grid;
            grid = new Grid(nextGrid);
            summedGrid = new Grid(grid);

            for (int i = 0; i < Constants.GRID_WIDTH; i++) {
                for (int j = 0; j < Constants.GRID_HEIGHT; j++) {
                    if (summedGrid.cells[i][j] == 0) {
                        for (int k = 0; k < Constants.QUEUE_SIZE; k++) {
                            if (queue.get(k).cells[i][j] == 1) {
                                summedGrid.cells[i][j] = k + 2;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void resetQueue() {
        queue = new ArrayList<>();
        for (int i = 0; i < Constants.QUEUE_SIZE; i++) {
            queue.add(new Grid(false));
        }
    }
}
