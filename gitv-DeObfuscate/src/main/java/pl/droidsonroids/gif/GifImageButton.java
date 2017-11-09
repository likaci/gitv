package pl.droidsonroids.gif;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageButton;

@SuppressLint({"NewApi"})
public class GifImageButton extends ImageButton {
    private boolean mFreezesAnimation;

    public GifImageButton(Context context) {
        super(context);
    }

    public GifImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        postInit(GifViewUtils.initImageView(this, attrs, 0, 0));
    }

    public GifImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        postInit(GifViewUtils.initImageView(this, attrs, defStyle, 0));
    }

    public GifImageButton(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
        postInit(GifViewUtils.initImageView(this, attrs, defStyle, defStyleRes));
    }

    private void postInit(InitResult result) {
        this.mFreezesAnimation = result.mFreezesAnimation;
        if (result.mSourceResId > 0) {
            super.setImageResource(result.mSourceResId);
        }
        if (result.mBackgroundResId > 0) {
            super.setBackgroundResource(result.mBackgroundResId);
        }
    }

    public void setImageURI(Uri uri) {
        if (!GifViewUtils.setGifImageUri(this, uri)) {
            super.setImageURI(uri);
        }
    }

    public void setImageResource(int resId) {
        if (!GifViewUtils.setResource(this, true, resId)) {
            super.setImageResource(resId);
        }
    }

    public void setBackgroundResource(int resId) {
        if (!GifViewUtils.setResource(this, false, resId)) {
            super.setBackgroundResource(resId);
        }
    }

    public Parcelable onSaveInstanceState() {
        Drawable drawable;
        Drawable drawable2 = null;
        if (this.mFreezesAnimation) {
            drawable = getDrawable();
        } else {
            drawable = null;
        }
        if (this.mFreezesAnimation) {
            drawable2 = getBackground();
        }
        return new GifViewSavedState(super.onSaveInstanceState(), drawable, drawable2);
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof GifViewSavedState) {
            GifViewSavedState gifViewSavedState = (GifViewSavedState) state;
            super.onRestoreInstanceState(gifViewSavedState.getSuperState());
            gifViewSavedState.restoreState(getDrawable(), 0);
            gifViewSavedState.restoreState(getBackground(), 1);
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void setFreezesAnimation(boolean freezesAnimation) {
        this.mFreezesAnimation = freezesAnimation;
    }
}
