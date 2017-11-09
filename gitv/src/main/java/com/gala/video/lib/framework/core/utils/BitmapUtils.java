package com.gala.video.lib.framework.core.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.renderscript.Allocation;
import android.renderscript.Allocation.MipmapControl;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;
import java.lang.reflect.Array;

public class BitmapUtils {
    private static final String TAG = "BitmapUtils";

    public enum CornerCut {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        ALL
    }

    static class MergeBitmapTask extends AsyncTask<Object, Void, Object> {
        MergeBitmapTask() {
        }

        protected Object doInBackground(Object... params) {
            Bitmap cornerBitmap = BitmapUtils.mergeBitmap(params[0], BitmapUtils.getBitmap(params[1], ((Integer) params[2]).intValue()));
            return new Object[]{params[3], cornerBitmap};
        }

        protected void onPostExecute(Object result) {
            Object[] objects = (Object[]) result;
            ImageView view = objects[0];
            view.setImageBitmap((Bitmap) objects[1]);
            view.invalidate();
        }
    }

    public enum ScalingLogic {
        CROP,
        FIT
    }

    public static Drawable getDrawable(Context context, int resId) {
        try {
            Options opt = new Options();
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            return new BitmapDrawable(context.getResources(), BitmapFactory.decodeResource(context.getResources(), resId, opt));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void releaseDrawable(View view) {
        try {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) view.getBackground();
            view.setBackgroundResource(0);
            bitmapDrawable.setCallback(null);
            bitmapDrawable.getBitmap().recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            if (((float) srcWidth) / ((float) srcHeight) > ((float) dstWidth) / ((float) dstHeight)) {
                return srcWidth / dstWidth;
            }
            return srcHeight / dstHeight;
        } else if (((float) srcWidth) / ((float) srcHeight) > ((float) dstWidth) / ((float) dstHeight)) {
            return srcHeight / dstHeight;
        } else {
            return srcWidth / dstWidth;
        }
    }

    public static Bitmap decodeResource(Resources res, int resId, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, scalingLogic);
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap createReflectedImage(Bitmap originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, 0, width, height, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, height + height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(originalImage, 0.0f, 0.0f, null);
        canvas.drawRect(0.0f, (float) height, (float) width, (float) (height + 0), new Paint());
        canvas.drawBitmap(reflectionImage, 0.0f, (float) (height + 0), null);
        Paint paint = new Paint();
        paint.setShader(new LinearGradient(0.0f, (float) originalImage.getHeight(), 0.0f, (float) (bitmapWithReflection.getHeight() + 0), 0, 0, TileMode.CLAMP));
        canvas.drawRect(0.0f, (float) height, (float) width, (float) (bitmapWithReflection.getHeight() + 0), paint);
        return bitmapWithReflection;
    }

    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Config.ARGB_8888);
        new Canvas(scaledBitmap).drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(2));
        return scaledBitmap;
    }

    public static Bitmap takeScreenShot(Activity activity) {
        Log.d(TAG, "takeScreenShot begin");
        View view = activity.getWindow().getDecorView();
        Log.d(TAG, "takeScreenShot begin----getDecorView");
        view.setDrawingCacheEnabled(true);
        Log.d(TAG, "takeScreenShot begin----setDrawingCacheEnabled");
        view.buildDrawingCache();
        Log.d(TAG, "takeScreenShot begin----buildDrawingCache");
        Bitmap b1 = view.getDrawingCache();
        Log.d(TAG, "takeScreenShot begin----getDrawingCache" + b1);
        Bitmap b = Bitmap.createBitmap(b1, 0, 0, activity.getWindowManager().getDefaultDisplay().getWidth(), activity.getWindowManager().getDefaultDisplay().getHeight());
        Log.d(TAG, "takeScreenShot begin----createBitmap");
        view.destroyDrawingCache();
        Log.d(TAG, "takeScreenShot end----destroyDrawingCache");
        return b;
    }

    public static Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Config.ARGB_4444);
        Canvas c = new Canvas(screenshot);
        c.translate((float) (-v.getScrollX()), (float) (-v.getScrollY()));
        v.draw(c);
        return screenshot;
    }

    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic != ScalingLogic.FIT) {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
        float srcAspect = ((float) srcWidth) / ((float) srcHeight);
        if (srcAspect > ((float) dstWidth) / ((float) dstHeight)) {
            return new Rect(0, 0, dstWidth, (int) (((float) dstWidth) / srcAspect));
        }
        return new Rect(0, 0, (int) (((float) dstHeight) * srcAspect), dstHeight);
    }

    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic != ScalingLogic.CROP) {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
        float dstAspect = ((float) dstWidth) / ((float) dstHeight);
        if (((float) srcWidth) / ((float) srcHeight) > dstAspect) {
            int srcRectWidth = (int) (((float) srcHeight) * dstAspect);
            int srcRectLeft = (srcWidth - srcRectWidth) / 2;
            return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
        }
        int srcRectHeight = (int) (((float) srcWidth) / dstAspect);
        int scrRectTop = (srcHeight - srcRectHeight) / 2;
        return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
    }

    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap, int desWidth, int desHeight) {
        return createReflectedImage(createScaledBitmap(bitmap, desWidth, desHeight, ScalingLogic.CROP));
    }

    public static int pxToDip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
    }

    public static Bitmap getReflectedImage(Bitmap bitmap, Context context) {
        if (bitmap != null) {
            return applyReflectionOnImage(bitmap, pxToDip(context, 260.0f), pxToDip(context, 180.0f));
        }
        Log.e(TAG, "getReflectedImage: src image is NULL!");
        return null;
    }

    private static Bitmap applyReflectionOnImage(Bitmap bitmap, int desWidth, int desHeight) {
        return createReflectedImageFast(createExtendedBitmap(bitmap, desWidth, desHeight, ScalingLogic.CROP));
    }

    private static Bitmap createExtendedBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height() * 2, Config.ARGB_8888);
        new Canvas(scaledBitmap).drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(2));
        return scaledBitmap;
    }

    private static Bitmap createReflectedImageFast(Bitmap extendedImage) {
        int width = extendedImage.getWidth();
        int height = extendedImage.getHeight();
        Canvas canvas = new Canvas(extendedImage);
        canvas.drawRect(0.0f, (float) height, (float) width, (float) (height + 0), new Paint());
        canvas.save();
        canvas.scale(1.0f, -1.0f);
        canvas.translate(0.0f, (float) (-height));
        canvas.drawBitmap(extendedImage, new Rect(0, 0, width, height / 2), new Rect(0, 0, width, height / 2), null);
        canvas.restore();
        return extendedImage;
    }

    public static Bitmap getBitmap(Context context, int resId) {
        Drawable drawable = getDrawable(context, resId);
        if (drawable == null) {
            return null;
        }
        return ((BitmapDrawable) drawable).getBitmap();
    }

    private static Bitmap mergeBitmap(Bitmap first, Bitmap second) {
        if (second == null) {
            return first;
        }
        float scaleWidth = ((float) first.getWidth()) / ((float) second.getWidth());
        float scaleHeight = ((float) first.getHeight()) / ((float) second.getHeight());
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(second, 0, 0, second.getWidth(), second.getHeight(), matrix, true);
        Bitmap bitmap = Bitmap.createBitmap(first.getWidth(), first.getHeight(), first.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(first, new Matrix(), null);
        canvas.drawBitmap(resizedBitmap, 0.0f, 0.0f, null);
        canvas.save(31);
        canvas.restore();
        return bitmap;
    }

    public static Bitmap getInvertedImage(Bitmap src, int height, int startAlpha, int endAlpha) {
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        if (height > srcHeight) {
            height = srcHeight;
        }
        Config config = src.getConfig();
        if (config == null) {
            LogUtils.w(TAG, "getInvertedImage: src bitmap has no config!!");
            config = Config.ARGB_8888;
        }
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, height, config);
        Canvas canvas = new Canvas(bitmap);
        float d_alpha = ((float) (endAlpha - startAlpha)) / ((float) height);
        Paint paint = new Paint();
        for (int i = 0; i < height; i++) {
            paint.setAlpha((int) (((float) startAlpha) + (((float) i) * d_alpha)));
            canvas.drawBitmap(src, new Rect(0, (srcHeight - i) - 1, srcWidth, srcHeight - i), new Rect(0, i, srcWidth, i + 1), paint);
        }
        return bitmap;
    }

    public static Bitmap getBitmap4BigPicture(Context context, int resId, int inSampleSize) {
        Options opt = new Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inSampleSize = inSampleSize;
        return BitmapFactory.decodeStream(context.getResources().openRawResource(resId), null, opt);
    }

    public static Bitmap getBitmap4BigPicture(Context context, int resId) {
        return getBitmap4BigPicture(context, resId, 1);
    }

    @SuppressLint({"NewApi"})
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {
        Log.e("###", "VERSION.SDK_INT " + VERSION.SDK_INT);
        if (context == null) {
            Log.e(TAG, "BitmapUtils-----fastblur >> context is null ----");
            return null;
        } else if (VERSION.SDK_INT > 16) {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            RenderScript rs = RenderScript.create(context);
            Allocation input = Allocation.createFromBitmap(rs, sentBitmap, MipmapControl.MIPMAP_NONE, 1);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius((float) radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            if (radius < 1) {
                return null;
            }
            int i;
            int y;
            int bsum;
            int gsum;
            int rsum;
            int boutsum;
            int goutsum;
            int routsum;
            int binsum;
            int ginsum;
            int rinsum;
            int p;
            int[] sir;
            int rbs;
            int stackpointer;
            int x;
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            int[] pix = new int[(w * h)];
            Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);
            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = (radius + radius) + 1;
            int[] r = new int[wh];
            int[] g = new int[wh];
            int[] b = new int[wh];
            int[] vmin = new int[Math.max(w, h)];
            int divsum = (div + 1) >> 1;
            divsum *= divsum;
            int[] dv = new int[(divsum * 256)];
            for (i = 0; i < divsum * 256; i++) {
                dv[i] = i / divsum;
            }
            int yi = 0;
            int yw = 0;
            int[][] stack = (int[][]) Array.newInstance(Integer.TYPE, new int[]{div, 3});
            int r1 = radius + 1;
            for (y = 0; y < h; y++) {
                bsum = 0;
                gsum = 0;
                rsum = 0;
                boutsum = 0;
                goutsum = 0;
                routsum = 0;
                binsum = 0;
                ginsum = 0;
                rinsum = 0;
                for (i = -radius; i <= radius; i++) {
                    p = pix[Math.min(wm, Math.max(i, 0)) + yi];
                    sir = stack[i + radius];
                    sir[0] = (16711680 & p) >> 16;
                    sir[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & p) >> 8;
                    sir[2] = p & 255;
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;
                for (x = 0; x < w; x++) {
                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];
                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;
                    sir = stack[((stackpointer - radius) + div) % div];
                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];
                    if (y == 0) {
                        vmin[x] = Math.min((x + radius) + 1, wm);
                    }
                    p = pix[vmin[x] + yw];
                    sir[0] = (16711680 & p) >> 16;
                    sir[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & p) >> 8;
                    sir[2] = p & 255;
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;
                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer % div];
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];
                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                bsum = 0;
                gsum = 0;
                rsum = 0;
                boutsum = 0;
                goutsum = 0;
                routsum = 0;
                binsum = 0;
                ginsum = 0;
                rinsum = 0;
                int yp = (-radius) * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;
                    sir = stack[i + radius];
                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];
                    rbs = r1 - Math.abs(i);
                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    pix[yi] = (((-16777216 & pix[yi]) | (dv[rsum] << 16)) | (dv[gsum] << 8)) | dv[bsum];
                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;
                    sir = stack[((stackpointer - radius) + div) % div];
                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];
                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];
                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;
                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];
                    yi += w;
                }
            }
            Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.setPixels(pix, 0, w, 0, 0, w, h);
            return bitmap;
        }
    }

    public static Bitmap scale(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap getRoundCornerBitmap(Bitmap bitmap, int roundPixel, CornerCut direction) {
        int roundPx = roundPixel;
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            int heightOffset = 0;
            int widthOffset = 0;
            int topX = 0;
            int topY = 0;
            switch (direction) {
                case TOP:
                    heightOffset = 0 + roundPx;
                    break;
                case BOTTOM:
                    topY = 0 + roundPx;
                    break;
                case LEFT:
                    widthOffset = 0 + roundPx;
                    break;
                case RIGHT:
                    topX = 0 + roundPx;
                    break;
            }
            RectF rectF = new RectF(new Rect(topX, topY, bitmap.getWidth() + widthOffset, bitmap.getHeight() + heightOffset));
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(-1);
            canvas.drawRoundRect(rectF, (float) roundPx, (float) roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, new Rect(topX, topY, bitmap.getWidth() + widthOffset, bitmap.getHeight() + heightOffset), rect, paint);
            switch (direction) {
                case BOTTOM:
                    return Bitmap.createBitmap(output, 0, topY, bitmap.getWidth(), bitmap.getHeight());
                case RIGHT:
                    return Bitmap.createBitmap(output, topX, 0, bitmap.getWidth(), bitmap.getHeight());
                default:
                    return Bitmap.createBitmap(output, 0, 0, bitmap.getWidth(), bitmap.getHeight());
            }
        } catch (Exception e) {
            return bitmap;
        }
    }

    public static Bitmap createReflectionImageWithOrigin(View view, int shapeId) {
        view.setDrawingCacheEnabled(true);
        view.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, 0, w, bitmap.getHeight(), matrix, false);
        Canvas canvas = new Canvas(reflectionImage);
        Paint paint = new Paint();
        paint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, (float) h, new int[]{822083583, 100663295, 16777215}, new float[]{0.0f, 0.7f, 1.0f}, TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0.0f, 0.0f, (float) w, (float) h, paint);
        return reflectionImage;
    }

    public static Bitmap clipSquareImage(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        try {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap == null) {
                return null;
            }
            Bitmap newBitmap;
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            int[] pixels;
            if (height < width) {
                pixels = new int[(height * height)];
                bitmap.getPixels(pixels, 0, height, (width - height) / 2, 0, height, height);
                newBitmap = Bitmap.createBitmap(height, height, Config.ARGB_8888);
                if (newBitmap != null) {
                    newBitmap.setPixels(pixels, 0, height, 0, 0, height, height);
                }
            } else {
                pixels = new int[(width * width)];
                bitmap.getPixels(pixels, 0, width, 0, (height - width) / 2, width, width);
                newBitmap = Bitmap.createBitmap(width, width, Config.ARGB_8888);
                if (newBitmap != null) {
                    newBitmap.setPixels(pixels, 0, width, 0, 0, width, width);
                }
            }
            if (newBitmap != null) {
                return newBitmap;
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
