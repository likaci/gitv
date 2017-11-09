package com.squareup.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import com.squareup.picasso.Picasso.LoadedFrom;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

abstract class BitmapHunter implements Runnable {
    private static final Object DECODE_LOCK = new Object();
    private static final ThreadLocal<StringBuilder> NAME_BUILDER = new C20191();
    Action action;
    List<Action> actions;
    final Cache cache;
    final Request data;
    final Dispatcher dispatcher;
    Exception exception;
    int exifRotation;
    Future<?> future;
    final String key;
    LoadedFrom loadedFrom;
    final Picasso picasso;
    Bitmap result;
    final boolean skipMemoryCache;
    final Stats stats;

    class C20191 extends ThreadLocal<StringBuilder> {
        C20191() {
        }

        protected StringBuilder initialValue() {
            return new StringBuilder("Picasso-");
        }
    }

    class C20202 implements Runnable {
        private final /* synthetic */ StringBuilder val$builder;

        C20202(StringBuilder stringBuilder) {
            this.val$builder = stringBuilder;
        }

        public void run() {
            throw new NullPointerException(this.val$builder.toString());
        }
    }

    class C20213 implements Runnable {
        private final /* synthetic */ Transformation val$transformation;

        C20213(Transformation transformation) {
            this.val$transformation = transformation;
        }

        public void run() {
            throw new IllegalStateException("Transformation " + this.val$transformation.key() + " returned input Bitmap but recycled it.");
        }
    }

    class C20224 implements Runnable {
        private final /* synthetic */ Transformation val$transformation;

        C20224(Transformation transformation) {
            this.val$transformation = transformation;
        }

        public void run() {
            throw new IllegalStateException("Transformation " + this.val$transformation.key() + " mutated input Bitmap but failed to recycle the original.");
        }
    }

    abstract Bitmap decode(Request request) throws IOException;

    BitmapHunter(Picasso picasso, Dispatcher dispatcher, Cache cache, Stats stats, Action action) {
        this.picasso = picasso;
        this.dispatcher = dispatcher;
        this.cache = cache;
        this.stats = stats;
        this.key = action.getKey();
        this.data = action.getRequest();
        this.skipMemoryCache = action.skipCache;
        this.action = action;
    }

    protected void setExifRotation(int exifRotation) {
        this.exifRotation = exifRotation;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r5 = this;
        r2 = r5.data;	 Catch:{ ResponseException -> 0x0038, IOException -> 0x004b, OutOfMemoryError -> 0x005e, Exception -> 0x008d }
        updateThreadName(r2);	 Catch:{ ResponseException -> 0x0038, IOException -> 0x004b, OutOfMemoryError -> 0x005e, Exception -> 0x008d }
        r2 = r5.picasso;	 Catch:{ ResponseException -> 0x0038, IOException -> 0x004b, OutOfMemoryError -> 0x005e, Exception -> 0x008d }
        r2 = r2.loggingEnabled;	 Catch:{ ResponseException -> 0x0038, IOException -> 0x004b, OutOfMemoryError -> 0x005e, Exception -> 0x008d }
        if (r2 == 0) goto L_0x0018;
    L_0x000b:
        r2 = "Hunter";
        r3 = "executing";
        r4 = com.squareup.picasso.Utils.getLogIdsForHunter(r5);	 Catch:{ ResponseException -> 0x0038, IOException -> 0x004b, OutOfMemoryError -> 0x005e, Exception -> 0x008d }
        com.squareup.picasso.Utils.log(r2, r3, r4);	 Catch:{ ResponseException -> 0x0038, IOException -> 0x004b, OutOfMemoryError -> 0x005e, Exception -> 0x008d }
    L_0x0018:
        r2 = r5.hunt();	 Catch:{ ResponseException -> 0x0038, IOException -> 0x004b, OutOfMemoryError -> 0x005e, Exception -> 0x008d }
        r5.result = r2;	 Catch:{ ResponseException -> 0x0038, IOException -> 0x004b, OutOfMemoryError -> 0x005e, Exception -> 0x008d }
        r2 = r5.result;	 Catch:{ ResponseException -> 0x0038, IOException -> 0x004b, OutOfMemoryError -> 0x005e, Exception -> 0x008d }
        if (r2 != 0) goto L_0x0032;
    L_0x0022:
        r2 = r5.dispatcher;	 Catch:{ ResponseException -> 0x0038, IOException -> 0x004b, OutOfMemoryError -> 0x005e, Exception -> 0x008d }
        r2.dispatchFailed(r5);	 Catch:{ ResponseException -> 0x0038, IOException -> 0x004b, OutOfMemoryError -> 0x005e, Exception -> 0x008d }
    L_0x0027:
        r2 = java.lang.Thread.currentThread();
        r3 = "Picasso-Idle";
        r2.setName(r3);
    L_0x0031:
        return;
    L_0x0032:
        r2 = r5.dispatcher;	 Catch:{ ResponseException -> 0x0038, IOException -> 0x004b, OutOfMemoryError -> 0x005e, Exception -> 0x008d }
        r2.dispatchComplete(r5);	 Catch:{ ResponseException -> 0x0038, IOException -> 0x004b, OutOfMemoryError -> 0x005e, Exception -> 0x008d }
        goto L_0x0027;
    L_0x0038:
        r0 = move-exception;
        r5.exception = r0;	 Catch:{ all -> 0x00a0 }
        r2 = r5.dispatcher;	 Catch:{ all -> 0x00a0 }
        r2.dispatchFailed(r5);	 Catch:{ all -> 0x00a0 }
        r2 = java.lang.Thread.currentThread();
        r3 = "Picasso-Idle";
        r2.setName(r3);
        goto L_0x0031;
    L_0x004b:
        r0 = move-exception;
        r5.exception = r0;	 Catch:{ all -> 0x00a0 }
        r2 = r5.dispatcher;	 Catch:{ all -> 0x00a0 }
        r2.dispatchRetry(r5);	 Catch:{ all -> 0x00a0 }
        r2 = java.lang.Thread.currentThread();
        r3 = "Picasso-Idle";
        r2.setName(r3);
        goto L_0x0031;
    L_0x005e:
        r0 = move-exception;
        r1 = new java.io.StringWriter;	 Catch:{ all -> 0x00a0 }
        r1.<init>();	 Catch:{ all -> 0x00a0 }
        r2 = r5.stats;	 Catch:{ all -> 0x00a0 }
        r2 = r2.createSnapshot();	 Catch:{ all -> 0x00a0 }
        r3 = new java.io.PrintWriter;	 Catch:{ all -> 0x00a0 }
        r3.<init>(r1);	 Catch:{ all -> 0x00a0 }
        r2.dump(r3);	 Catch:{ all -> 0x00a0 }
        r2 = new java.lang.RuntimeException;	 Catch:{ all -> 0x00a0 }
        r3 = r1.toString();	 Catch:{ all -> 0x00a0 }
        r2.<init>(r3, r0);	 Catch:{ all -> 0x00a0 }
        r5.exception = r2;	 Catch:{ all -> 0x00a0 }
        r2 = r5.dispatcher;	 Catch:{ all -> 0x00a0 }
        r2.dispatchFailed(r5);	 Catch:{ all -> 0x00a0 }
        r2 = java.lang.Thread.currentThread();
        r3 = "Picasso-Idle";
        r2.setName(r3);
        goto L_0x0031;
    L_0x008d:
        r0 = move-exception;
        r5.exception = r0;	 Catch:{ all -> 0x00a0 }
        r2 = r5.dispatcher;	 Catch:{ all -> 0x00a0 }
        r2.dispatchFailed(r5);	 Catch:{ all -> 0x00a0 }
        r2 = java.lang.Thread.currentThread();
        r3 = "Picasso-Idle";
        r2.setName(r3);
        goto L_0x0031;
    L_0x00a0:
        r2 = move-exception;
        r3 = java.lang.Thread.currentThread();
        r4 = "Picasso-Idle";
        r3.setName(r4);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.picasso.BitmapHunter.run():void");
    }

    Bitmap hunt() throws IOException {
        Bitmap bitmap;
        if (!this.skipMemoryCache) {
            bitmap = this.cache.get(this.key);
            if (bitmap != null) {
                this.stats.dispatchCacheHit();
                this.loadedFrom = LoadedFrom.MEMORY;
                if (this.picasso.loggingEnabled) {
                    Utils.log("Hunter", "decoded", this.data.logId(), "from cache");
                }
                return bitmap;
            }
        }
        bitmap = decode(this.data);
        if (bitmap != null) {
            if (this.picasso.loggingEnabled) {
                Utils.log("Hunter", "decoded", this.data.logId());
            }
            this.stats.dispatchBitmapDecoded(bitmap);
            if (this.data.needsTransformation() || this.exifRotation != 0) {
                synchronized (DECODE_LOCK) {
                    if (this.data.needsMatrixTransform() || this.exifRotation != 0) {
                        bitmap = transformResult(this.data, bitmap, this.exifRotation);
                        if (this.picasso.loggingEnabled) {
                            Utils.log("Hunter", "transformed", this.data.logId());
                        }
                    }
                    if (this.data.hasCustomTransformations()) {
                        bitmap = applyCustomTransformations(this.data.transformations, bitmap);
                        if (this.picasso.loggingEnabled) {
                            Utils.log("Hunter", "transformed", this.data.logId(), "from custom transformations");
                        }
                    }
                }
                if (bitmap != null) {
                    this.stats.dispatchBitmapTransformed(bitmap);
                }
            }
        }
        return bitmap;
    }

    void attach(Action action) {
        boolean loggingEnabled = this.picasso.loggingEnabled;
        Request request = action.request;
        if (this.action == null) {
            this.action = action;
            if (!loggingEnabled) {
                return;
            }
            if (this.actions == null || this.actions.isEmpty()) {
                Utils.log("Hunter", "joined", request.logId(), "to empty hunter");
                return;
            } else {
                Utils.log("Hunter", "joined", request.logId(), Utils.getLogIdsForHunter(this, "to "));
                return;
            }
        }
        if (this.actions == null) {
            this.actions = new ArrayList(3);
        }
        this.actions.add(action);
        if (loggingEnabled) {
            Utils.log("Hunter", "joined", request.logId(), Utils.getLogIdsForHunter(this, "to "));
        }
    }

    void detach(Action action) {
        if (this.action == action) {
            this.action = null;
        } else if (this.actions != null) {
            this.actions.remove(action);
        }
        if (this.picasso.loggingEnabled) {
            Utils.log("Hunter", "removed", action.request.logId(), Utils.getLogIdsForHunter(this, "from "));
        }
    }

    boolean cancel() {
        if (this.action == null) {
            return (this.actions == null || this.actions.isEmpty()) && this.future != null && this.future.cancel(false);
        } else {
            return false;
        }
    }

    boolean isCancelled() {
        return this.future != null && this.future.isCancelled();
    }

    boolean shouldSkipMemoryCache() {
        return this.skipMemoryCache;
    }

    boolean shouldRetry(boolean airplaneMode, NetworkInfo info) {
        return false;
    }

    boolean supportsReplay() {
        return false;
    }

    Bitmap getResult() {
        return this.result;
    }

    String getKey() {
        return this.key;
    }

    Request getData() {
        return this.data;
    }

    Action getAction() {
        return this.action;
    }

    Picasso getPicasso() {
        return this.picasso;
    }

    List<Action> getActions() {
        return this.actions;
    }

    Exception getException() {
        return this.exception;
    }

    LoadedFrom getLoadedFrom() {
        return this.loadedFrom;
    }

    static void updateThreadName(Request data) {
        String name = data.getName();
        StringBuilder builder = (StringBuilder) NAME_BUILDER.get();
        builder.ensureCapacity("Picasso-".length() + name.length());
        builder.replace("Picasso-".length(), builder.length(), name);
        Thread.currentThread().setName(builder.toString());
    }

    static BitmapHunter forRequest(Context context, Picasso picasso, Dispatcher dispatcher, Cache cache, Stats stats, Action action, Downloader downloader) {
        if (action.getRequest().resourceId != 0) {
            return new ResourceBitmapHunter(context, picasso, dispatcher, cache, stats, action);
        }
        Uri uri = action.getRequest().uri;
        String scheme = uri.getScheme();
        if ("content".equals(scheme)) {
            if (Contacts.CONTENT_URI.getHost().equals(uri.getHost()) && !uri.getPathSegments().contains("photo")) {
                return new ContactsPhotoBitmapHunter(context, picasso, dispatcher, cache, stats, action);
            }
            if ("media".equals(uri.getAuthority())) {
                return new MediaStoreBitmapHunter(context, picasso, dispatcher, cache, stats, action);
            }
            return new ContentStreamBitmapHunter(context, picasso, dispatcher, cache, stats, action);
        } else if ("file".equals(scheme)) {
            if (uri.getPathSegments().isEmpty() || !"android_asset".equals(uri.getPathSegments().get(0))) {
                return new FileBitmapHunter(context, picasso, dispatcher, cache, stats, action);
            }
            return new AssetBitmapHunter(context, picasso, dispatcher, cache, stats, action);
        } else if ("android.resource".equals(scheme)) {
            return new ResourceBitmapHunter(context, picasso, dispatcher, cache, stats, action);
        } else {
            return new NetworkBitmapHunter(picasso, dispatcher, cache, stats, action, downloader);
        }
    }

    static Options createBitmapOptions(Request data) {
        boolean justBounds = data.hasSize();
        boolean hasConfig = data.config != null;
        Options options = null;
        if (justBounds || hasConfig) {
            options = new Options();
            options.inJustDecodeBounds = justBounds;
            if (hasConfig) {
                options.inPreferredConfig = data.config;
            }
        }
        options.inInputShareable = true;
        return options;
    }

    static boolean requiresInSampleSize(Options options) {
        return options != null && options.inJustDecodeBounds;
    }

    static void calculateInSampleSize(int reqWidth, int reqHeight, Options options) {
        calculateInSampleSize(reqWidth, reqHeight, options.outWidth, options.outHeight, options);
    }

    static void calculateInSampleSize(int reqWidth, int reqHeight, int width, int height, Options options) {
        int sampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = (int) Math.floor((double) (((float) height) / ((float) reqHeight)));
            int widthRatio = (int) Math.floor((double) (((float) width) / ((float) reqWidth)));
            if (heightRatio < widthRatio) {
                sampleSize = heightRatio;
            } else {
                sampleSize = widthRatio;
            }
        }
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
    }

    static Bitmap applyCustomTransformations(List<Transformation> transformations, Bitmap result) {
        int i = 0;
        int count = transformations.size();
        while (i < count) {
            Transformation transformation = (Transformation) transformations.get(i);
            Bitmap newResult = transformation.transform(result);
            if (newResult == null) {
                StringBuilder builder = new StringBuilder().append("Transformation ").append(transformation.key()).append(" returned null after ").append(i).append(" previous transformation(s).\n\nTransformation list:\n");
                for (Transformation t : transformations) {
                    builder.append(t.key()).append('\n');
                }
                Picasso.HANDLER.post(new C20202(builder));
                return null;
            } else if (newResult == result && result.isRecycled()) {
                Picasso.HANDLER.post(new C20213(transformation));
                return null;
            } else if (newResult == result || result.isRecycled()) {
                result = newResult;
                i++;
            } else {
                Picasso.HANDLER.post(new C20224(transformation));
                return null;
            }
        }
        return result;
    }

    static Bitmap transformResult(Request data, Bitmap result, int exifRotation) {
        int inWidth = result.getWidth();
        int inHeight = result.getHeight();
        int drawX = 0;
        int drawY = 0;
        int drawWidth = inWidth;
        int drawHeight = inHeight;
        Matrix matrix = new Matrix();
        if (data.needsMatrixTransform()) {
            int targetWidth = data.targetWidth;
            int targetHeight = data.targetHeight;
            float targetRotation = data.rotationDegrees;
            if (targetRotation != 0.0f) {
                if (data.hasRotationPivot) {
                    matrix.setRotate(targetRotation, data.rotationPivotX, data.rotationPivotY);
                } else {
                    matrix.setRotate(targetRotation);
                }
            }
            float widthRatio;
            float heightRatio;
            float scale;
            if (data.centerCrop) {
                widthRatio = ((float) targetWidth) / ((float) inWidth);
                heightRatio = ((float) targetHeight) / ((float) inHeight);
                int newSize;
                if (widthRatio > heightRatio) {
                    scale = widthRatio;
                    newSize = (int) Math.ceil((double) (((float) inHeight) * (heightRatio / widthRatio)));
                    drawY = (inHeight - newSize) / 2;
                    drawHeight = newSize;
                } else {
                    scale = heightRatio;
                    newSize = (int) Math.ceil((double) (((float) inWidth) * (widthRatio / heightRatio)));
                    drawX = (inWidth - newSize) / 2;
                    drawWidth = newSize;
                }
                matrix.preScale(scale, scale);
            } else if (data.centerInside) {
                widthRatio = ((float) targetWidth) / ((float) inWidth);
                heightRatio = ((float) targetHeight) / ((float) inHeight);
                if (widthRatio < heightRatio) {
                    scale = widthRatio;
                } else {
                    scale = heightRatio;
                }
                matrix.preScale(scale, scale);
            } else if (!(targetWidth == 0 || targetHeight == 0 || (targetWidth == inWidth && targetHeight == inHeight))) {
                matrix.preScale(((float) targetWidth) / ((float) inWidth), ((float) targetHeight) / ((float) inHeight));
            }
        }
        if (exifRotation != 0) {
            matrix.preRotate((float) exifRotation);
        }
        Bitmap newResult = Bitmap.createBitmap(result, drawX, drawY, drawWidth, drawHeight, matrix, true);
        if (newResult == result) {
            return result;
        }
        result.recycle();
        return newResult;
    }
}
