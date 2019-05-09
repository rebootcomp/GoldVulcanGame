package com.app.vulkanmonet;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtils {

    public static final double BUTTON_MARGIN_BOTTOM_LONG = 0.3;
    public static final double BUTTON_HEIGHT_LONG = 0.139;

    public static final double BUTTON_MARGIN_BOTTOM_NORMAL = 0.20;
    public static final double BUTTON_HEIGHT_NORMAL = 0.156;

    public static final double LONG_SCREEN_RATIO = 2.05;
    public static final double NORMAL_SCREEN_RATIO = 1.778;

    public static boolean isLongScreen(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        double screenWidth = dm.widthPixels;
        double screenHeight = dm.heightPixels;

        double screenRatio = screenHeight / screenWidth;

        if (Math.abs(screenRatio - LONG_SCREEN_RATIO) < Math.abs(screenRatio - NORMAL_SCREEN_RATIO)) {
            return true;
        }
        return false;
    }

    public static double getWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        return dm.widthPixels;
    }

    public static double getHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        return dm.heightPixels;
    }

    public static int pixToDp(double pixel, Context context) {
        double densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        return (int) (pixel / (densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}