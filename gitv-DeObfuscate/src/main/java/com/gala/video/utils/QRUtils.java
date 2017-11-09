package com.gala.video.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import com.gala.video.utils.zxing.BarcodeFormat;
import com.gala.video.utils.zxing.BitMatrix;
import com.gala.video.utils.zxing.EncodeHintType;
import com.gala.video.utils.zxing.MultiFormatWriter;
import java.util.Hashtable;
import org.cybergarage.xml.XML;

@SuppressLint({"NewApi"})
public class QRUtils {
    private static final int BLACK = -16777216;
    private static final int DEFAULT_HEIGHT = 450;
    private static final int DEFAULT_WIDTH = 450;
    private static final int PADDING_SIZE_MIN = 1;
    private static final String TAG = "QRUtils";

    public static Bitmap createQRImage(String url, int width, int height) {
        long stime = System.currentTimeMillis();
        Log.i(TAG, "createQRImage-start");
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, XML.CHARSET_UTF8);
            BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
            int mWidth = matrix.getWidth();
            int mHeight = matrix.getHeight();
            int[] pixels = new int[(width * height)];
            boolean isFirstBlackPoint = false;
            int startX = 0;
            int startY = 0;
            for (int y = 0; y < mHeight; y++) {
                for (int x = 0; x < mWidth; x++) {
                    if (matrix.get(x, y)) {
                        if (!isFirstBlackPoint) {
                            isFirstBlackPoint = true;
                            startX = x;
                            startY = y;
                        }
                        pixels[(y * width) + x] = BLACK;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, mWidth, mHeight);
            if (startX <= 1) {
                return bitmap;
            }
            int x1 = startX - 1;
            int y1 = startY - 1;
            if (x1 < 0 || y1 < 0) {
                return bitmap;
            }
            Bitmap bitmapQR = Bitmap.createBitmap(bitmap, x1, y1, width - (x1 * 2), height - (y1 * 2));
            Log.i(TAG, "createQRImage-end-duration: " + (System.currentTimeMillis() - stime));
            return bitmapQR;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap createQRImage(String content) {
        return createQRImage(content, 450, 450);
    }
}
