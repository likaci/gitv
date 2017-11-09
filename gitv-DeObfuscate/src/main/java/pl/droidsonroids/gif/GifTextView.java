package pl.droidsonroids.gif;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import java.io.IOException;

@SuppressLint({"NewApi"})
public class GifTextView extends TextView {
    private boolean mFreezesAnimation;

    public GifTextView(Context context) {
        super(context);
    }

    public GifTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public GifTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle, 0);
    }

    public GifTextView(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
        init(attrs, defStyle, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyle, int defStyleRes) {
        if (attrs != null) {
            Drawable gifOrDefaultDrawable = getGifOrDefaultDrawable(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableLeft", 0));
            Drawable gifOrDefaultDrawable2 = getGifOrDefaultDrawable(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableTop", 0));
            Drawable gifOrDefaultDrawable3 = getGifOrDefaultDrawable(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableRight", 0));
            Drawable gifOrDefaultDrawable4 = getGifOrDefaultDrawable(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableBottom", 0));
            Drawable gifOrDefaultDrawable5 = getGifOrDefaultDrawable(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableStart", 0));
            Drawable gifOrDefaultDrawable6 = getGifOrDefaultDrawable(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableEnd", 0));
            if (VERSION.SDK_INT >= 17) {
                if (getLayoutDirection() == 0) {
                    if (gifOrDefaultDrawable5 == null) {
                        gifOrDefaultDrawable5 = gifOrDefaultDrawable;
                    }
                    if (gifOrDefaultDrawable6 == null) {
                        gifOrDefaultDrawable6 = gifOrDefaultDrawable3;
                    }
                } else {
                    if (gifOrDefaultDrawable5 == null) {
                        gifOrDefaultDrawable5 = gifOrDefaultDrawable3;
                    }
                    if (gifOrDefaultDrawable6 == null) {
                        gifOrDefaultDrawable6 = gifOrDefaultDrawable;
                    }
                }
                setCompoundDrawablesRelativeWithIntrinsicBounds(gifOrDefaultDrawable5, gifOrDefaultDrawable2, gifOrDefaultDrawable6, gifOrDefaultDrawable4);
            }
            setCompoundDrawablesWithIntrinsicBounds(gifOrDefaultDrawable, gifOrDefaultDrawable2, gifOrDefaultDrawable3, gifOrDefaultDrawable4);
            setBackgroundInternal(getGifOrDefaultDrawable(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", SettingConstants.BACKGROUND, 0)));
        }
        this.mFreezesAnimation = GifViewUtils.isFreezingAnimation(this, attrs, defStyle, defStyleRes);
    }

    private void setBackgroundInternal(Drawable bg) {
        if (VERSION.SDK_INT >= 16) {
            setBackground(bg);
        } else {
            setBackgroundDrawable(bg);
        }
    }

    public void setBackgroundResource(int resId) {
        setBackgroundInternal(getGifOrDefaultDrawable(resId));
    }

    private Drawable getGifOrDefaultDrawable(int resId) {
        if (resId == 0) {
            return null;
        }
        Resources resources = getResources();
        if (!isInEditMode() && "drawable".equals(resources.getResourceTypeName(resId))) {
            try {
                return new GifDrawable(resources, resId);
            } catch (IOException e) {
            } catch (NotFoundException e2) {
            }
        }
        if (VERSION.SDK_INT >= 21) {
            return resources.getDrawable(resId, getContext().getTheme());
        }
        return resources.getDrawable(resId);
    }

    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int start, int top, int end, int bottom) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(getGifOrDefaultDrawable(start), getGifOrDefaultDrawable(top), getGifOrDefaultDrawable(end), getGifOrDefaultDrawable(bottom));
    }

    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {
        setCompoundDrawablesWithIntrinsicBounds(getGifOrDefaultDrawable(left), getGifOrDefaultDrawable(top), getGifOrDefaultDrawable(right), getGifOrDefaultDrawable(bottom));
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        hideCompoundDrawables(getCompoundDrawables());
        if (VERSION.SDK_INT >= 17) {
            hideCompoundDrawables(getCompoundDrawablesRelative());
        }
    }

    private void hideCompoundDrawables(Drawable[] drawables) {
        for (Drawable drawable : drawables) {
            if (drawable != null) {
                drawable.setVisible(false, false);
            }
        }
    }

    public Parcelable onSaveInstanceState() {
        Drawable[] drawableArr = new Drawable[7];
        if (this.mFreezesAnimation) {
            Object compoundDrawables = getCompoundDrawables();
            System.arraycopy(compoundDrawables, 0, drawableArr, 0, compoundDrawables.length);
            if (VERSION.SDK_INT >= 17) {
                Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();
                drawableArr[4] = compoundDrawablesRelative[0];
                drawableArr[5] = compoundDrawablesRelative[2];
            }
            drawableArr[6] = getBackground();
        }
        return new GifViewSavedState(super.onSaveInstanceState(), drawableArr);
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof GifViewSavedState) {
            GifViewSavedState gifViewSavedState = (GifViewSavedState) state;
            super.onRestoreInstanceState(gifViewSavedState.getSuperState());
            Drawable[] compoundDrawables = getCompoundDrawables();
            gifViewSavedState.restoreState(compoundDrawables[0], 0);
            gifViewSavedState.restoreState(compoundDrawables[1], 1);
            gifViewSavedState.restoreState(compoundDrawables[2], 2);
            gifViewSavedState.restoreState(compoundDrawables[3], 3);
            if (VERSION.SDK_INT >= 17) {
                compoundDrawables = getCompoundDrawablesRelative();
                gifViewSavedState.restoreState(compoundDrawables[0], 4);
                gifViewSavedState.restoreState(compoundDrawables[2], 5);
            }
            gifViewSavedState.restoreState(getBackground(), 6);
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void setFreezesAnimation(boolean freezesAnimation) {
        this.mFreezesAnimation = freezesAnimation;
    }
}
