package pl.droidsonroids.gif;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import com.gala.imageprovider.a;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.xbill.DNS.Message;

@SuppressLint({"NewApi"})
final class GifViewUtils {
    static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
    static final List<String> SUPPORTED_RESOURCE_TYPE_NAMES = Arrays.asList(new String[]{"raw", "drawable", "mipmap"});

    static class InitResult {
        final int mBackgroundResId;
        final boolean mFreezesAnimation;
        final int mSourceResId;

        InitResult(int sourceResId, int backgroundResId, boolean freezesAnimation) {
            this.mSourceResId = sourceResId;
            this.mBackgroundResId = backgroundResId;
            this.mFreezesAnimation = freezesAnimation;
        }
    }

    private GifViewUtils() {
    }

    static InitResult initImageView(ImageView view, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs == null || view.isInEditMode()) {
            return new InitResult(0, 0, false);
        }
        return new InitResult(getResourceId(view, attrs, true), getResourceId(view, attrs, false), isFreezingAnimation(view, attrs, defStyleAttr, defStyleRes));
    }

    private static int getResourceId(ImageView view, AttributeSet attrs, boolean isSrc) {
        int attributeResourceValue = attrs.getAttributeResourceValue(ANDROID_NS, isSrc ? "src" : SettingConstants.BACKGROUND, 0);
        if (attributeResourceValue > 0) {
            if (SUPPORTED_RESOURCE_TYPE_NAMES.contains(view.getResources().getResourceTypeName(attributeResourceValue)) && !setResource(view, isSrc, attributeResourceValue)) {
                return attributeResourceValue;
            }
        }
        return 0;
    }

    static boolean setResource(ImageView view, boolean isSrc, int resId) {
        Resources resources = view.getResources();
        if (resources != null) {
            try {
                Drawable gifDrawable = new GifDrawable(resources, resId);
                if (isSrc) {
                    view.setImageDrawable(gifDrawable);
                } else if (VERSION.SDK_INT >= 16) {
                    view.setBackground(gifDrawable);
                } else {
                    view.setBackgroundDrawable(gifDrawable);
                }
                return true;
            } catch (IOException e) {
            } catch (NotFoundException e2) {
            }
        }
        return false;
    }

    static boolean isFreezingAnimation(View view, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray obtainStyledAttributes = view.getContext().obtainStyledAttributes(attrs, a.b, defStyleAttr, defStyleRes);
        boolean z = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
        return z;
    }

    static boolean setGifImageUri(ImageView imageView, Uri uri) {
        if (uri != null) {
            try {
                imageView.setImageDrawable(new GifDrawable(imageView.getContext().getContentResolver(), uri));
                return true;
            } catch (IOException e) {
            }
        }
        return false;
    }

    static float getDensityScale(Resources res, int id) {
        TypedValue typedValue = new TypedValue();
        res.getValue(id, typedValue, true);
        int i = typedValue.density;
        if (i == 0) {
            i = 160;
        } else if (i == Message.MAXLENGTH) {
            i = 0;
        }
        int i2 = res.getDisplayMetrics().densityDpi;
        if (i <= 0 || i2 <= 0) {
            return 1.0f;
        }
        return ((float) i2) / ((float) i);
    }
}
