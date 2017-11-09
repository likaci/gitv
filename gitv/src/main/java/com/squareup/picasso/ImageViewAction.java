package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.squareup.picasso.Picasso.LoadedFrom;

class ImageViewAction extends Action<ImageView> {
    Callback callback;

    ImageViewAction(Picasso picasso, ImageView imageView, Request data, boolean skipCache, boolean noFade, int errorResId, Drawable errorDrawable, String key, Callback callback) {
        super(picasso, imageView, data, skipCache, noFade, errorResId, errorDrawable, key);
        this.callback = callback;
    }

    public void complete(Bitmap result, LoadedFrom from) {
        if (result == null) {
            throw new AssertionError(String.format("Attempted to complete action with no result!\n%s", new Object[]{this}));
        }
        ImageView target = (ImageView) this.target.get();
        if (target != null) {
            Bitmap bitmap = result;
            LoadedFrom loadedFrom = from;
            PicassoDrawable.setBitmap(target, this.picasso.context, bitmap, loadedFrom, this.noFade, this.picasso.indicatorsEnabled);
            if (this.callback != null) {
                this.callback.onSuccess();
            }
        }
    }

    public void error() {
        ImageView target = (ImageView) this.target.get();
        if (target != null) {
            if (this.errorResId != 0) {
                target.setImageResource(this.errorResId);
            } else if (this.errorDrawable != null) {
                target.setImageDrawable(this.errorDrawable);
            }
            if (this.callback != null) {
                this.callback.onError();
            }
        }
    }

    void cancel() {
        super.cancel();
        if (this.callback != null) {
            this.callback = null;
        }
    }
}
