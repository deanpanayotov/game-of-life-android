package com.dpanayotov.gameoflife.life;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.dpanayotov.gameoflife.preferences.Preferences;
import com.dpanayotov.gameoflife.util.Resolution;

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

    private int screenWidth;
    private int screenHeight;
    private SurfaceHolder surfaceHolder;

    private Resolution resolution;
    private int halfCell;
    private int tickRate;
    private boolean isometricProjection;
    private boolean highlife;
    private int populationPercentage;


    private Paint primaryPaint, secondaryPaint, backgroundPaint;

    public Life(int screenWidth, int screenHeight, SurfaceHolder surfaceHolder) {

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.surfaceHolder = surfaceHolder;

        initPaint(Preferences.getColor(Preferences.Colors.PRIMARY), primaryPaint);
        initPaint(Preferences.getColor(Preferences.Colors.SECONDARY), secondaryPaint);
        initPaint(Preferences.getColor(Preferences.Colors.BACKGROUND), backgroundPaint);

        getPreferences();

        reset();
    }

    private void initPaint(int color, Paint paint) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
    }

    public void update() {

        Grid nextGrid = grid.deriveNextState(highlife);

        if (nextGrid.populationCount < populationPercentage || previousGrid.equals(nextGrid)) {
            reset();
        } else {

            queue.add(0, new Grid(grid));
            queue.remove(queue.size() - 1);

            previousGrid = grid;
            grid = new Grid(nextGrid);
            summedGrid = new Grid(grid);

            for (int i = 0; i < resolution.gridWidth; i++) {
                for (int j = 0; j < resolution.gridHeight; j++) {
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

        draw();
    }

    private void resetQueue() {
        queue = new ArrayList<>();
        for (int i = 0; i < Constants.QUEUE_SIZE; i++) {
            queue.add(new Grid(resolution.gridWidth, resolution.gridHeight, false));
        }
    }

    private void reset() {
        previousGrid = new Grid(resolution.gridWidth, resolution.gridHeight, false);
        grid = new Grid(resolution.gridWidth, resolution.gridHeight, true);
        resetQueue();
    }

    private void getPreferences() {
        resolution = Preferences.getResolutions().get(Preferences.getResolution());
        halfCell = resolution.cellSize / 2;
        tickRate = Preferences.getTickRates().get(Preferences.getTickRate());
        isometricProjection = Preferences.getIsometricProjection();
        highlife = Preferences.getHighlife();
        populationPercentage = Preferences.getPopulationPercentages().get(Preferences
                .getPopulationPercentage());

    }

    private void draw() {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawRect(0, 0, screenWidth, screenHeight, backgroundPaint);
                for (int i = 0; i < resolution.gridWidth; i++) {
                    for (int j = 0; j < resolution.gridHeight; j++) {
                        if (summedGrid.cells[i][j] > 0) {
                            int cellX = i * resolution.cellSize + halfCell;
                            int cellY = j * resolution.cellSize + halfCell;
                            if (isometricProjection) {
                                cellY = (cellY + i * halfCell) % screenHeight;
                            }
                            secondaryPaint.setAlpha((int) (255 * (1 / (float) summedGrid
                                    .cells[i][j])));
                            int radius = (halfCell - Constants.CELL_PADDING) - Constants
                                    .RADIUS_STEP * (summedGrid.cells[i][j] - 1);
                            if (summedGrid.cells[i][j] > 1) {
                                canvas.drawCircle(cellX, cellY, radius, secondaryPaint);
                            } else {
                                canvas.drawCircle(cellX, cellY, radius, primaryPaint);
                            }
                        }
                    }
                }
            }
        } finally {
            if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private final Handler handler = new Handler();
    private final Runnable drawRunner = new Runnable() {
        @Override
        public void run() {
            update();
            handler.postDelayed(drawRunner, tickRate);
        }
    };

    public void start() {
        handler.post(drawRunner);
    }

    public void stop() {
        handler.removeCallbacks(drawRunner);
    }


}
