package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.squareup.picasso.Picasso.LoadedFrom;

final class TargetAction extends Action<Target> {
    TargetAction(Picasso picasso, Target target, Request data, boolean skipCache, int errorResId, Drawable errorDrawable, String key) {
        super(picasso, target, data, skipCache, false, errorResId, errorDrawable, key);
    }

    void complete(Bitmap result, LoadedFrom from) {
        if (result == null) {
            throw new AssertionError(String.format("Attempted to complete action with no result!\n%s", new Object[]{this}));
        }
        Target target = (Target) getTarget();
        if (target != null) {
            target.onBitmapLoaded(result, from);
            if (result.isRecycled()) {
                throw new IllegalStateException("Target callback must not recycle bitmap!");
            }
        }
    }

    void error() {
        Target target = (Target) getTarget();
        if (target == null) {
            return;
        }
        if (this.errorResId != 0) {
            target.onBitmapFailed(this.picasso.context.getResources().getDrawable(this.errorResId));
        } else {
            target.onBitmapFailed(this.errorDrawable);
        }
    }
}
