package com.dpanayotov.gameoflife.life;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Dean Panayotov on 9/23/2016
 */

public class Grid {

    public int[][] cells = new int[Constants.GRID_WIDTH][Constants.GRID_HEIGHT];

    public Grid(boolean populate) {
        if (populate) {
            populate();
        } else {
            for (int[] row : cells) {
                Arrays.fill(row, 0);
            }
        }
    }

    public Grid(Grid grid) {
        System.arraycopy(grid.cells, 0, cells, 0, grid.cells.length);
    }

    public int getNeighboursCount(int x, int y) {

        int total = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                total += cells[(Constants.GRID_WIDTH + (x + i)) % Constants.GRID_WIDTH]
                        [(Constants.GRID_HEIGHT + (y + j)) % Constants.GRID_HEIGHT]; //mod is used for the edge warp
            }
        }

        total -= cells[x][y];

        return total;
    }

    public void populate() {

        Random random = new Random();

        for (int i = 0; i < Constants.GRID_WIDTH; i++) {
            for (int j = 0; j < Constants.GRID_HEIGHT; j++) {
                cells[i][j] = random.nextInt(2);
            }
        }
    }


}
