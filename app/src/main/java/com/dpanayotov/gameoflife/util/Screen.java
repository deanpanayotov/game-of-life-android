package com.dpanayotov.gameoflife.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.dpanayotov.gameoflife.life.Constants;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dean Panayotov on 9/23/2016
 */

public class Screen {

    private static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context
                .WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point screenSize = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            //new pleasant way to get real metrics
            DisplayMetrics realMetrics = new DisplayMetrics();
            display.getRealMetrics(realMetrics);
            screenSize.x = realMetrics.widthPixels;
            screenSize.y = realMetrics.heightPixels;

        } else if (Build.VERSION.SDK_INT >= 14) {
            //reflection for this weird in-between time
            try {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                screenSize.x = (Integer) mGetRawW.invoke(display);
                screenSize.y = (Integer) mGetRawH.invoke(display);
            } catch (Exception e) {
                //this may not be 100% accurate, but it's all we've got
                screenSize.x = display.getWidth();
                screenSize.y = display.getHeight();
                Log.e("Screen Info", "Couldn't use reflection to get the real display metrics.");
            }

        } else {
            //This should be close, as lower API devices should not have window navigation bars
            screenSize.x = display.getWidth();
            screenSize.y = display.getHeight();
        }

        return screenSize;
    }

    private static List<Integer> getDivisors(int number, int minResult, int minDivisor) {

        List<Integer> divisors = new ArrayList<>();

        for (int i = minDivisor; i <= number / 2; i += 2) {
            if (number % i == 0 && number / i > minResult) {
                divisors.add(i);
            }
        }
        return divisors;
    }

    private static List<Integer> getCommonDivisors(List<Integer> a, List<Integer> b) {
        List<Integer> commonDivisors = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (a.get(i) == b.get(j)) {
                    commonDivisors.add(a.get(i));
                    b.remove(j);
                    break;
                }
            }
        }
        return commonDivisors;
    }

    public static List<Integer> getAvailableCellRadiuses(Context context) {
        return getCommonDivisors(getDivisors(getScreenSize(context).x,
                Constants.MIN_GRID_WIDTH, Constants.MIN_CELL_RADIUS * 2), getDivisors
                (getScreenSize(context).y, Constants.MIN_GRID_HEIGHT, Constants.MIN_CELL_RADIUS *
                        2));
    }

}
