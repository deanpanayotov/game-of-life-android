package com.dpanayotov.gameoflife.life;

/**
 * Created by Dean Panayotov on 9/23/2016
 */

public class Life {

    public Grid grid;


    public Life() {
        grid = new Grid(true);
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

        grid = nextGrid;
    }
}
