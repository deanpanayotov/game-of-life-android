package com.dpanayotov.gameoflife.life;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dean Panayotov on 9/23/2016
 */

public class Life {

    private boolean first = true;

    private List<Grid> queue = new ArrayList<>();

    private Grid grid;

    public Grid summedGrid;

    public Life() {
        grid = new Grid(true);
        for (int i = 0; i < Constants.QUEUE_SIZE; i++) {
            queue.add(new Grid(false));
        }
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
        if(first){
            first = false;
        }else {
            queue.add(0, new Grid(grid));
            queue.remove(queue.size() - 1);
        }

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
