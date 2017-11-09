package com.gala.video.lib.framework.core.utils.font;

import android.content.Context;
import android.graphics.Typeface;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.lang.reflect.Field;

public class FontManager {
    private static final String DEFAULT_FONT_PATH = "fonts/DEFAULT.TTF";
    private static final String TAG = FontManager.class.getSimpleName();
    private static FontManager sSingleton = null;
    private FontPair mCurrentTypeFace = new FontPair(DEFAULT_FONT_PATH, null);

    private FontManager() {
    }

    public static FontManager getInstance() {
        if (sSingleton == null) {
            synchronized (FontManager.class) {
                if (sSingleton == null) {
                    sSingleton = new FontManager();
                }
            }
        }
        return sSingleton;
    }

    private Typeface createTypefaceFromAsset(Context context, String fontPath) {
        if (StringUtils.isEmpty((CharSequence) fontPath)) {
            fontPath = DEFAULT_FONT_PATH;
        }
        if (!fontPath.equalsIgnoreCase(this.mCurrentTypeFace.getFontPath()) || this.mCurrentTypeFace.getTypeface() == null) {
            this.mCurrentTypeFace.setFontPath(fontPath);
            this.mCurrentTypeFace.setTypeface(Typeface.createFromAsset(context.getAssets(), fontPath));
        }
        return this.mCurrentTypeFace.getTypeface();
    }

    private Typeface createTypefaceFromFile(String fontPath) {
        if (StringUtils.isEmpty((CharSequence) fontPath)) {
            fontPath = DEFAULT_FONT_PATH;
        }
        if (!fontPath.equalsIgnoreCase(this.mCurrentTypeFace.getFontPath()) || this.mCurrentTypeFace.getTypeface() == null) {
            this.mCurrentTypeFace.setFontPath(fontPath);
            this.mCurrentTypeFace.setTypeface(Typeface.createFromFile(fontPath));
        }
        return this.mCurrentTypeFace.getTypeface();
    }

    public void replaceSystemDefaultFontFromAsset(Context context, String fontPath) {
        replaceSystemDefaultFont(createTypefaceFromAsset(context, fontPath));
    }

    public void replaceSystemDefaultFontFromAsset(Context context) {
        replaceSystemDefaultFont(createTypefaceFromAsset(context, DEFAULT_FONT_PATH));
    }

    public Typeface getTypeface(Context context, String fontPath) {
        return createTypefaceFromAsset(context, fontPath);
    }

    public Typeface getTypeface(Context context) {
        return createTypefaceFromAsset(context, DEFAULT_FONT_PATH);
    }

    public void replaceSystemDefaultFontFromFile(Context context, String fontPath) {
        replaceSystemDefaultFont(createTypefaceFromFile(fontPath));
    }

    private void replaceSystemDefaultFont(Typeface typeface) {
        modifyObjectField(null, "MONOSPACE", typeface);
    }

    private void modifyObjectField(Object obj, String fieldName, Object value) {
        try {
            Field defaultField = Typeface.class.getDeclaredField(fieldName);
            defaultField.setAccessible(true);
            defaultField.set(obj, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }
}
