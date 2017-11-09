package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.squareup.picasso.Picasso.LoadedFrom;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

abstract class Action<T> {
    boolean cancelled;
    final Drawable errorDrawable;
    final int errorResId;
    final String key;
    final boolean noFade;
    final Picasso picasso;
    final Request request;
    final boolean skipCache;
    final WeakReference<T> target;
    boolean willReplay;

    static class RequestWeakReference<T> extends WeakReference<T> {
        final Action action;

        public RequestWeakReference(Action action, T referent, ReferenceQueue<? super T> q) {
            super(referent, q);
            this.action = action;
        }
    }

    abstract void complete(Bitmap bitmap, LoadedFrom loadedFrom);

    abstract void error();

    Action(Picasso picasso, T target, Request request, boolean skipCache, boolean noFade, int errorResId, Drawable errorDrawable, String key) {
        this.picasso = picasso;
        this.request = request;
        this.target = new RequestWeakReference(this, target, picasso.referenceQueue);
        this.skipCache = skipCache;
        this.noFade = noFade;
        this.errorResId = errorResId;
        this.errorDrawable = errorDrawable;
        this.key = key;
    }

    void cancel() {
        this.cancelled = true;
    }

    Request getRequest() {
        return this.request;
    }

    T getTarget() {
        return this.target.get();
    }

    String getKey() {
        return this.key;
    }

    boolean isCancelled() {
        return this.cancelled;
    }

    boolean willReplay() {
        return this.willReplay;
    }

    Picasso getPicasso() {
        return this.picasso;
    }
}
