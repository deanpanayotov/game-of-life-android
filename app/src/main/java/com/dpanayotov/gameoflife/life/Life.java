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
    private int width, height, minPopulation;
    private boolean highlife;

    public Life(int width, int height, boolean highlife, int populationPercentage) {

        this.width = width;
        this.height = height;
        this.highlife = highlife;
        
        this.minPopulation = (width * height) * (populationPercentage / 100);

        reset();
    }

    public void update() {

        Grid nextGrid = grid.deriveNextState(highlife);

        if (nextGrid.populationCount < minPopulation || previousGrid.equals
                (nextGrid)) {
            reset();
        } else {

            queue.add(0, new Grid(grid));
            queue.remove(queue.size() - 1);

            previousGrid = grid;
            grid = new Grid(nextGrid);
            summedGrid = new Grid(grid);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
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
            queue.add(new Grid(width, height, false));
        }
    }

    private void reset() {
        previousGrid = new Grid(width, height, false);
        grid = new Grid(width, height, true);
        resetQueue();
    }
}
