package com.dpanayotov.gameoflife.life;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.dpanayotov.gameoflife.life.di.SyncHandlerWrapper;
import com.dpanayotov.gameoflife.life.di.AsyncHandlerWrapper;
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
    private Boolean destroyed = false;

    private int screenWidth;
    private int screenHeight;
    private SurfaceHolder surfaceHolder;

    private Resolution resolution;
    private int halfCell;
    private int tickRate;
    private boolean isometricProjection;
    private boolean highlife;
    private int initialPopulationDensity;
    private int minPopulationDensity;
    private int minPopulationCount;

    private final Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (destroyed) {
                if (!destroyed) {
                    update();
                    handlerWrapper.get().postDelayed(drawRunnable, tickRate);
                }
            }
        }
    };

    //DI
    private SyncHandlerWrapper handlerWrapper;


    private Paint primaryPaint = new Paint(),
            secondaryPaint = new Paint(),
            backgroundPaint = new Paint();

    public Life(int screenWidth, int screenHeight, SurfaceHolder surfaceHolder, boolean preview) {
        handlerWrapper = preview ? new AsyncHandlerWrapper() : new SyncHandlerWrapper();
        handlerWrapper.init();

        initPaint(Preferences.getColor(Preferences.Colors.PRIMARY), primaryPaint);
        initPaint(Preferences.getColor(Preferences.Colors.SECONDARY), secondaryPaint);
        initPaint(Preferences.getColor(Preferences.Colors.BACKGROUND), backgroundPaint);

        getPreferences();

        this.screenWidth = (int) ceil(screenWidth, resolution.cellSize);
        this.screenHeight = (int) ceil(screenHeight, resolution.cellSize);
        this.surfaceHolder = surfaceHolder;

        resolution.gridWidth = this.screenWidth / resolution.cellSize;
        resolution.gridHeight = this.screenHeight / resolution.cellSize;

        minPopulationCount = Math.round(resolution.gridWidth * resolution.gridHeight *
                (minPopulationDensity / 100f));

        reset();
    }

    private void initPaint(int color, Paint paint) {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
    }

    public void update() {
        Grid nextGrid = grid.deriveNextState(highlife);

        if (nextGrid.populationCount < minPopulationCount || previousGrid.equals(nextGrid)) {
            reset();
            drawRestart();
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
            draw();
        }
    }

    private void resetQueue() {
        queue = new ArrayList<>();
        for (int i = 0; i < Constants.QUEUE_SIZE; i++) {
            queue.add(new Grid(resolution.gridWidth, resolution.gridHeight, 0));
        }
    }

    private void reset() {
        previousGrid = new Grid(resolution.gridWidth, resolution.gridHeight, 0);
        grid = new Grid(resolution.gridWidth, resolution.gridHeight, initialPopulationDensity);
        resetQueue();
    }

    private void getPreferences() {
        resolution = Preferences.getResolutions().get(Preferences.getResolution());
        halfCell = resolution.cellSize / 2;
        tickRate = Preferences.getTickRates().get(Preferences.getTickRate());
        isometricProjection = Preferences.getIsometricProjection();
        highlife = Preferences.getHighlife();
        minPopulationDensity = Preferences.getMinPopulationDensityOptions().get(Preferences
                .getMinPopulationDensity());
        initialPopulationDensity = Preferences.getInitialPopulationDensityOptions().get
                (Preferences.getInitialPopulationDensity());
    }

    private void drawRestart() {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawRect(0, 0, screenWidth, screenHeight, backgroundPaint);
            }
        } finally {
            if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas);
        }
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
                            drawCell(cellX, cellY, summedGrid.cells[i][j], canvas);
                        }
                    }
                }

                int j, value;

                //The last cell of every odd column should be duplicated on top of the screen.
                // This way half of the cell is displayed on top and half - on bottom.
                for (int i = 1; i < resolution.gridWidth; i += 2) {
                    j = resolution.gridHeight - 1 - ((i - 1) / 2);
                    value = summedGrid.cells[i][j];
                    if (value > 0) {
                        int cellX = i * resolution.cellSize + halfCell;
                        int cellY = j * resolution.cellSize + halfCell;
                        cellY = (cellY + i * halfCell);
                        drawCell(cellX, cellY, value, canvas);
                    }
                }
            }
        } finally {
            if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawCell(int x, int y, int value, Canvas canvas) {
        secondaryPaint.setAlpha((int) (255 * (1 / (float) value)));
        int radius = (halfCell - Constants.CELL_PADDING) - Constants
                .RADIUS_STEP * (value - 1);
        if (value > 1) {
            canvas.drawCircle(x, y, radius, secondaryPaint);
        } else {
            canvas.drawCircle(x, y, radius, primaryPaint);
        }
    }

    public void start() {
        synchronized (destroyed) {
            if (!destroyed) {
                handlerWrapper.get().post(drawRunnable);
            }
        }
    }

    public void stop() {
        synchronized (destroyed) {
            if (!destroyed) {
                handlerWrapper.get().removeCallbacks(drawRunnable);
            }
        }
    }

    public static double ceil(double input, double step) {
        return Math.ceil(input / step) * step;
    }

    public void destroy() {
        synchronized (destroyed) {
            stop();
            handlerWrapper.destroy();
            destroyed = true;
        }
    }
}
