package com.squareup.picasso;

import android.app.Notification;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.RemoteViews;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Request.Builder;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestCreator {
    private static int nextId = 0;
    private final Builder data;
    private boolean deferred;
    private Drawable errorDrawable;
    private int errorResId;
    private boolean noFade;
    private final Picasso picasso;
    private Drawable placeholderDrawable;
    private int placeholderResId;
    private boolean skipMemoryCache;

    class C20281 implements Runnable {
        private final /* synthetic */ AtomicInteger val$id;
        private final /* synthetic */ CountDownLatch val$latch;

        C20281(AtomicInteger atomicInteger, CountDownLatch countDownLatch) {
            this.val$id = atomicInteger;
            this.val$latch = countDownLatch;
        }

        public void run() {
            this.val$id.set(RequestCreator.getRequestId());
            this.val$latch.countDown();
        }
    }

    class C20292 implements Runnable {
        private final /* synthetic */ InterruptedException val$e;

        C20292(InterruptedException interruptedException) {
            this.val$e = interruptedException;
        }

        public void run() {
            throw new RuntimeException(this.val$e);
        }
    }

    private static int getRequestId() {
        if (Utils.isMain()) {
            int i = nextId;
            nextId = i + 1;
            return i;
        }
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger id = new AtomicInteger();
        Picasso.HANDLER.post(new C20281(id, latch));
        try {
            latch.await();
        } catch (InterruptedException e) {
            Picasso.HANDLER.post(new C20292(e));
        }
        return id.get();
    }

    RequestCreator(Picasso picasso, Uri uri, int resourceId) {
        if (picasso.shutdown) {
            throw new IllegalStateException("Picasso instance already shut down. Cannot submit new requests.");
        }
        this.picasso = picasso;
        this.data = new Builder(uri, resourceId);
    }

    RequestCreator() {
        this.picasso = null;
        this.data = new Builder(null, 0);
    }

    public RequestCreator placeholder(int placeholderResId) {
        if (placeholderResId == 0) {
            throw new IllegalArgumentException("Placeholder image resource invalid.");
        } else if (this.placeholderDrawable != null) {
            throw new IllegalStateException("Placeholder image already set.");
        } else {
            this.placeholderResId = placeholderResId;
            return this;
        }
    }

    public RequestCreator placeholder(Drawable placeholderDrawable) {
        if (this.placeholderResId != 0) {
            throw new IllegalStateException("Placeholder image already set.");
        }
        this.placeholderDrawable = placeholderDrawable;
        return this;
    }

    public RequestCreator error(int errorResId) {
        if (errorResId == 0) {
            throw new IllegalArgumentException("Error image resource invalid.");
        } else if (this.errorDrawable != null) {
            throw new IllegalStateException("Error image already set.");
        } else {
            this.errorResId = errorResId;
            return this;
        }
    }

    public RequestCreator error(Drawable errorDrawable) {
        if (errorDrawable == null) {
            throw new IllegalArgumentException("Error image may not be null.");
        } else if (this.errorResId != 0) {
            throw new IllegalStateException("Error image already set.");
        } else {
            this.errorDrawable = errorDrawable;
            return this;
        }
    }

    public RequestCreator fit() {
        this.deferred = true;
        return this;
    }

    RequestCreator unfit() {
        this.deferred = false;
        return this;
    }

    public RequestCreator resizeDimen(int targetWidthResId, int targetHeightResId) {
        Resources resources = this.picasso.context.getResources();
        return resize(resources.getDimensionPixelSize(targetWidthResId), resources.getDimensionPixelSize(targetHeightResId));
    }

    public RequestCreator resize(int targetWidth, int targetHeight) {
        this.data.resize(targetWidth, targetHeight);
        return this;
    }

    public RequestCreator centerCrop() {
        this.data.centerCrop();
        return this;
    }

    public RequestCreator centerInside() {
        this.data.centerInside();
        return this;
    }

    public RequestCreator rotate(float degrees) {
        this.data.rotate(degrees);
        return this;
    }

    public RequestCreator rotate(float degrees, float pivotX, float pivotY) {
        this.data.rotate(degrees, pivotX, pivotY);
        return this;
    }

    public RequestCreator config(Config config) {
        this.data.config(config);
        return this;
    }

    public RequestCreator transform(Transformation transformation) {
        this.data.transform(transformation);
        return this;
    }

    public RequestCreator skipMemoryCache() {
        this.skipMemoryCache = true;
        return this;
    }

    public RequestCreator noFade() {
        this.noFade = true;
        return this;
    }

    public Bitmap get() throws IOException {
        long started = System.nanoTime();
        Utils.checkNotMain();
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with get.");
        } else if (!this.data.hasImage()) {
            return null;
        } else {
            Request finalData = createRequest(started);
            return BitmapHunter.forRequest(this.picasso.context, this.picasso, this.picasso.dispatcher, this.picasso.cache, this.picasso.stats, new GetAction(this.picasso, finalData, this.skipMemoryCache, Utils.createKey(finalData, new StringBuilder())), this.picasso.dispatcher.downloader).hunt();
        }
    }

    public void fetch() {
        long started = System.nanoTime();
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with fetch.");
        } else if (this.data.hasImage()) {
            Request request = createRequest(started);
            this.picasso.submit(new FetchAction(this.picasso, request, this.skipMemoryCache, Utils.createKey(request, new StringBuilder())));
        }
    }

    public void into(Target target) {
        long started = System.nanoTime();
        Utils.checkMain();
        if (target == null) {
            throw new IllegalArgumentException("Target must not be null.");
        } else if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with a Target.");
        } else {
            Drawable drawable;
            if (this.placeholderResId != 0) {
                drawable = this.picasso.context.getResources().getDrawable(this.placeholderResId);
            } else {
                drawable = this.placeholderDrawable;
            }
            if (this.data.hasImage()) {
                Request request = createRequest(started);
                String requestKey = Utils.createKey(request);
                if (!this.skipMemoryCache) {
                    Bitmap bitmap = this.picasso.quickMemoryCacheCheck(requestKey);
                    if (bitmap != null) {
                        this.picasso.cancelRequest(target);
                        target.onBitmapLoaded(bitmap, LoadedFrom.MEMORY);
                        return;
                    }
                }
                target.onPrepareLoad(drawable);
                this.picasso.enqueueAndSubmit(new TargetAction(this.picasso, target, request, this.skipMemoryCache, this.errorResId, this.errorDrawable, requestKey));
                return;
            }
            this.picasso.cancelRequest(target);
            target.onPrepareLoad(drawable);
        }
    }

    public void into(RemoteViews remoteViews, int viewId, int notificationId, Notification notification) {
        long started = System.nanoTime();
        Utils.checkMain();
        if (remoteViews == null) {
            throw new IllegalArgumentException("RemoteViews must not be null.");
        } else if (notification == null) {
            throw new IllegalArgumentException("Notification must not be null.");
        } else if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with RemoteViews.");
        } else if (this.placeholderDrawable == null && this.errorDrawable == null) {
            Request request = createRequest(started);
            RemoteViews remoteViews2 = remoteViews;
            int i = viewId;
            int i2 = notificationId;
            Notification notification2 = notification;
            performRemoteViewInto(new NotificationAction(this.picasso, request, remoteViews2, i, i2, notification2, this.skipMemoryCache, this.errorResId, Utils.createKey(request)));
        } else {
            throw new IllegalArgumentException("Cannot use placeholder or error drawables with remote views.");
        }
    }

    public void into(RemoteViews remoteViews, int viewId, int[] appWidgetIds) {
        long started = System.nanoTime();
        Utils.checkMain();
        if (remoteViews == null) {
            throw new IllegalArgumentException("remoteViews must not be null.");
        } else if (appWidgetIds == null) {
            throw new IllegalArgumentException("appWidgetIds must not be null.");
        } else if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with remote views.");
        } else if (this.placeholderDrawable == null && this.errorDrawable == null) {
            Request request = createRequest(started);
            RemoteViews remoteViews2 = remoteViews;
            int i = viewId;
            int[] iArr = appWidgetIds;
            performRemoteViewInto(new AppWidgetAction(this.picasso, request, remoteViews2, i, iArr, this.skipMemoryCache, this.errorResId, Utils.createKey(request)));
        } else {
            throw new IllegalArgumentException("Cannot use placeholder or error drawables with remote views.");
        }
    }

    public void into(ImageView target) {
        into(target, null);
    }

    public void into(ImageView target, Callback callback) {
        long started = System.nanoTime();
        Utils.checkMain();
        if (target == null) {
            throw new IllegalArgumentException("Target must not be null.");
        } else if (this.data.hasImage()) {
            if (this.deferred) {
                if (this.data.hasSize()) {
                    throw new IllegalStateException("Fit cannot be used with resize.");
                }
                int width = target.getWidth();
                int height = target.getHeight();
                if (width == 0 || height == 0) {
                    PicassoDrawable.setPlaceholder(target, this.placeholderResId, this.placeholderDrawable);
                    this.picasso.defer(target, new DeferredRequestCreator(this, target, callback));
                    return;
                }
                this.data.resize(width, height);
            }
            Request request = createRequest(started);
            String requestKey = Utils.createKey(request);
            if (!this.skipMemoryCache) {
                Bitmap bitmap = this.picasso.quickMemoryCacheCheck(requestKey);
                if (bitmap != null) {
                    this.picasso.cancelRequest(target);
                    PicassoDrawable.setBitmap(target, this.picasso.context, bitmap, LoadedFrom.MEMORY, this.noFade, this.picasso.indicatorsEnabled);
                    if (this.picasso.loggingEnabled) {
                        Utils.log("Main", "completed", request.plainId(), "from " + LoadedFrom.MEMORY);
                    }
                    if (callback != null) {
                        callback.onSuccess();
                        return;
                    }
                    return;
                }
            }
            PicassoDrawable.setPlaceholder(target, this.placeholderResId, this.placeholderDrawable);
            this.picasso.enqueueAndSubmit(new ImageViewAction(this.picasso, target, request, this.skipMemoryCache, this.noFade, this.errorResId, this.errorDrawable, requestKey, callback));
        } else {
            this.picasso.cancelRequest(target);
            PicassoDrawable.setPlaceholder(target, this.placeholderResId, this.placeholderDrawable);
        }
    }

    private Request createRequest(long started) {
        int id = getRequestId();
        Request request = this.data.build();
        request.id = id;
        request.started = started;
        boolean loggingEnabled = this.picasso.loggingEnabled;
        if (loggingEnabled) {
            Utils.log("Main", "created", request.plainId(), request.toString());
        }
        Request transformed = this.picasso.transformRequest(request);
        if (transformed != request) {
            transformed.id = id;
            transformed.started = started;
            if (loggingEnabled) {
                Utils.log("Main", "changed", transformed.logId(), "into " + transformed);
            }
        }
        return transformed;
    }

    private void performRemoteViewInto(RemoteViewsAction action) {
        if (!this.skipMemoryCache) {
            Bitmap bitmap = this.picasso.quickMemoryCacheCheck(action.getKey());
            if (bitmap != null) {
                action.complete(bitmap, LoadedFrom.MEMORY);
                return;
            }
        }
        if (this.placeholderResId != 0) {
            action.setImageResource(this.placeholderResId);
        }
        this.picasso.enqueueAndSubmit(action);
    }
}
