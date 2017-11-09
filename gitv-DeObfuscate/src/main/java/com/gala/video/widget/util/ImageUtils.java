package com.gala.video.widget.util;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import java.util.HashMap;

public class ImageUtils {
    private static HashMap<Float, ColorMatrix> mColorMatrixMap = new HashMap();

    public static void changeBrightness(Drawable drawable, float brightness) {
        ColorMatrix matrix;
        if (mColorMatrixMap.containsKey(Float.valueOf(brightness))) {
            matrix = (ColorMatrix) mColorMatrixMap.get(Float.valueOf(brightness));
            if (matrix == null) {
                mColorMatrixMap.remove(Float.valueOf(brightness));
                matrix = getNewBrightnessMatrix(brightness);
            }
        } else {
            matrix = getNewBrightnessMatrix(brightness);
        }
        if (drawable != null) {
            drawable.setColorFilter(new ColorMatrixColorFilter(matrix));
        }
    }

    private static ColorMatrix getNewBrightnessMatrix(float brightness) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{1.0f, 0.0f, 0.0f, 0.0f, brightness, 0.0f, 1.0f, 0.0f, 0.0f, brightness, 0.0f, 0.0f, 1.0f, 0.0f, brightness, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        mColorMatrixMap.put(Float.valueOf(brightness), matrix);
        return matrix;
    }

    private static ColorMatrix getNewContrastMatrix(float contrast) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{contrast, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, contrast, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, contrast, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        mColorMatrixMap.put(Float.valueOf(contrast), matrix);
        return matrix;
    }

    public static void changeContrast(Drawable drawable, float contrast) {
        ColorMatrix matrix;
        if (mColorMatrixMap.containsKey(Float.valueOf(contrast))) {
            matrix = (ColorMatrix) mColorMatrixMap.get(Float.valueOf(contrast));
            if (matrix == null) {
                mColorMatrixMap.remove(Float.valueOf(contrast));
                matrix = getNewContrastMatrix(contrast);
            }
        } else {
            matrix = getNewContrastMatrix(contrast);
        }
        matrix.set(new float[]{contrast, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, contrast, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, contrast, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        if (drawable != null) {
            drawable.setColorFilter(new ColorMatrixColorFilter(matrix));
        }
    }

    public static void changeSaturation(Drawable drawable, float saturation) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(saturation);
        if (drawable != null) {
            drawable.setColorFilter(new ColorMatrixColorFilter(matrix));
        }
    }
}
