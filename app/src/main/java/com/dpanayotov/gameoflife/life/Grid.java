package com.dpanayotov.gameoflife.life;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Dean Panayotov on 9/23/2016
 */

public class Grid {

    public int[][] cells;

    public int populationCount = 0;

    public int width, height;

    public Grid(int width, int height, boolean populate) {

        this.width = width;
        this.height = height;

        cells = new int[width][height];

        if (populate) {
            populate();
        } else {
            populateEmpty();
        }
    }

    public Grid(Grid grid) {
        cells = new int[grid.cells.length][];
        for (int i = 0; i < grid.cells.length; i++) {
            cells[i] = Arrays.copyOf(grid.cells[i], grid.cells[i].length);
        }
        populationCount = grid.populationCount;
        width = grid.width;
        height = grid.height;
    }

    public int getNeighboursCount(int x, int y) {

        int total = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                total += cells[(width + (x + i)) % width]
                        [(height + (y + j)) % height]; //mod is
                // used for the edge warp
            }
        }

        total -= cells[x][y];

        return total;
    }

    public void populate() {

        Random random = new Random();

        for (int i = 0; i < width; i++) {
            populationCount = 0;
            for (int j = 0; j < height; j++) {
                cells[i][j] = random.nextInt(2);
                populationCount += cells[i][j];
            }
        }
    }

    public void populateEmpty() {
        for (int[] row : cells) {
            Arrays.fill(row, 0);
        }
        populationCount = 0;
    }

    public void mirrorPopulate() {

        Random random = new Random();

        for (int i = 0; i < width / 2 + 1; i++) {
            populationCount = 0;
            for (int j = 0; j < height; j++) {
                cells[i][j] = random.nextInt(2);
                cells[width - i - 1][j] = cells[i][j];
                populationCount += cells[i][j] * 2;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        Grid grid = (Grid) obj;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (cells[i][j] != grid.cells[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Grid deriveNextState() {
        Grid nextGrid = new Grid(width, height, false);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int neighboursCount = getNeighboursCount(i, j);
                if ((cells[i][j] == 1 && neighboursCount == 2) || neighboursCount == 3) {
                    nextGrid.cells[i][j] = 1;
                    nextGrid.populationCount++;
                }
            }
        }
        return nextGrid;
    }
}
