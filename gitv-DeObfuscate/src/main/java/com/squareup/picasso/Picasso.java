package com.squareup.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.ImageView;
import android.widget.RemoteViews;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import java.io.File;
import java.lang.ref.ReferenceQueue;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;

public class Picasso {
    static final Handler HANDLER = new C20251(Looper.getMainLooper());
    static final String TAG = "Picasso";
    static Picasso singleton = null;
    final Cache cache;
    private final CleanupThread cleanupThread;
    final Context context;
    final Dispatcher dispatcher;
    boolean indicatorsEnabled;
    private final Listener listener;
    volatile boolean loggingEnabled;
    final ReferenceQueue<Object> referenceQueue;
    private final RequestTransformer requestTransformer;
    boolean shutdown;
    final Stats stats;
    final Map<Object, Action> targetToAction = new WeakHashMap();
    final Map<ImageView, DeferredRequestCreator> targetToDeferredRequestCreator = new WeakHashMap();

    class C20251 extends Handler {
        C20251(Looper $anonymous0) {
            super($anonymous0);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    Action action = msg.obj;
                    action.picasso.cancelExistingRequest(action.getTarget());
                    return;
                case 8:
                    List<BitmapHunter> batch = msg.obj;
                    int n = batch.size();
                    for (int i = 0; i < n; i++) {
                        BitmapHunter hunter = (BitmapHunter) batch.get(i);
                        hunter.picasso.complete(hunter);
                    }
                    return;
                default:
                    throw new AssertionError("Unknown handler message received: " + msg.what);
            }
        }
    }

    public static class Builder {
        private Cache cache;
        private final Context context;
        private Downloader downloader;
        private boolean indicatorsEnabled;
        private Listener listener;
        private boolean loggingEnabled;
        private ExecutorService service;
        private RequestTransformer transformer;

        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }

        public Builder downloader(Downloader downloader) {
            if (downloader == null) {
                throw new IllegalArgumentException("Downloader must not be null.");
            } else if (this.downloader != null) {
                throw new IllegalStateException("Downloader already set.");
            } else {
                this.downloader = downloader;
                return this;
            }
        }

        public Builder executor(ExecutorService executorService) {
            if (executorService == null) {
                throw new IllegalArgumentException("Executor service must not be null.");
            } else if (this.service != null) {
                throw new IllegalStateException("Executor service already set.");
            } else {
                this.service = executorService;
                return this;
            }
        }

        public Builder memoryCache(Cache memoryCache) {
            if (memoryCache == null) {
                throw new IllegalArgumentException("Memory cache must not be null.");
            } else if (this.cache != null) {
                throw new IllegalStateException("Memory cache already set.");
            } else {
                this.cache = memoryCache;
                return this;
            }
        }

        public Builder listener(Listener listener) {
            if (listener == null) {
                throw new IllegalArgumentException("Listener must not be null.");
            } else if (this.listener != null) {
                throw new IllegalStateException("Listener already set.");
            } else {
                this.listener = listener;
                return this;
            }
        }

        public Builder requestTransformer(RequestTransformer transformer) {
            if (transformer == null) {
                throw new IllegalArgumentException("Transformer must not be null.");
            } else if (this.transformer != null) {
                throw new IllegalStateException("Transformer already set.");
            } else {
                this.transformer = transformer;
                return this;
            }
        }

        @Deprecated
        public Builder debugging(boolean debugging) {
            return indicatorsEnabled(debugging);
        }

        public Builder indicatorsEnabled(boolean enabled) {
            this.indicatorsEnabled = enabled;
            return this;
        }

        public Builder loggingEnabled(boolean enabled) {
            this.loggingEnabled = enabled;
            return this;
        }

        public Picasso build() {
            Context context = this.context;
            if (this.downloader == null) {
                this.downloader = Utils.createDefaultDownloader(context);
            }
            if (this.cache == null) {
                this.cache = new LruCache(context);
            }
            if (this.service == null) {
                this.service = new PicassoExecutorService();
            }
            if (this.transformer == null) {
                this.transformer = RequestTransformer.IDENTITY;
            }
            Stats stats = new Stats(this.cache);
            return new Picasso(context, new Dispatcher(context, this.service, Picasso.HANDLER, this.downloader, this.cache, stats), this.cache, this.listener, this.transformer, stats, this.indicatorsEnabled, this.loggingEnabled);
        }
    }

    private static class CleanupThread extends Thread {
        private final Handler handler;
        private final ReferenceQueue<?> referenceQueue;

        CleanupThread(ReferenceQueue<?> referenceQueue, Handler handler) {
            this.referenceQueue = referenceQueue;
            this.handler = handler;
            setDaemon(true);
            setName("Picasso-refQueue");
        }

        public void run() {
            Process.setThreadPriority(10);
            while (true) {
                try {
                    this.handler.sendMessage(this.handler.obtainMessage(3, ((RequestWeakReference) this.referenceQueue.remove()).action));
                } catch (InterruptedException e) {
                    return;
                } catch (final Exception e2) {
                    this.handler.post(new Runnable() {
                        public void run() {
                            throw new RuntimeException(e2);
                        }
                    });
                    return;
                }
            }
        }

        void shutdown() {
            interrupt();
        }
    }

    public interface Listener {
        void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception);
    }

    public enum LoadedFrom {
        MEMORY(-16711936),
        DISK(SettingConstants.ID_GROUP_MASK),
        NETWORK(-65536);
        
        final int debugColor;

        private LoadedFrom(int debugColor) {
            this.debugColor = debugColor;
        }
    }

    public interface RequestTransformer {
        public static final RequestTransformer IDENTITY = new C20271();

        class C20271 implements RequestTransformer {
            C20271() {
            }

            public Request transformRequest(Request request) {
                return request;
            }
        }

        Request transformRequest(Request request);
    }

    Picasso(Context context, Dispatcher dispatcher, Cache cache, Listener listener, RequestTransformer requestTransformer, Stats stats, boolean indicatorsEnabled, boolean loggingEnabled) {
        this.context = context;
        this.dispatcher = dispatcher;
        this.cache = cache;
        this.listener = listener;
        this.requestTransformer = requestTransformer;
        this.stats = stats;
        this.indicatorsEnabled = indicatorsEnabled;
        this.loggingEnabled = loggingEnabled;
        this.referenceQueue = new ReferenceQueue();
        this.cleanupThread = new CleanupThread(this.referenceQueue, HANDLER);
        this.cleanupThread.start();
    }

    public void cancelRequest(ImageView view) {
        cancelExistingRequest(view);
    }

    public void cancelRequest(Target target) {
        cancelExistingRequest(target);
    }

    public void cancelRequest(RemoteViews remoteViews, int viewId) {
        cancelExistingRequest(new RemoteViewsTarget(remoteViews, viewId));
    }

    public RequestCreator load(Uri uri) {
        return new RequestCreator(this, uri, 0);
    }

    public RequestCreator load(String path) {
        if (path == null || path.trim().length() == 0) {
            return new RequestCreator(this, null, 0);
        }
        return load(Uri.parse(path));
    }

    public RequestCreator load(File file) {
        if (file == null) {
            return new RequestCreator(this, null, 0);
        }
        return load(Uri.fromFile(file));
    }

    public RequestCreator load(int resourceId) {
        if (resourceId != 0) {
            return new RequestCreator(this, null, resourceId);
        }
        throw new IllegalArgumentException("Resource ID must not be zero.");
    }

    @Deprecated
    public boolean isDebugging() {
        return areIndicatorsEnabled() && isLoggingEnabled();
    }

    @Deprecated
    public void setDebugging(boolean debugging) {
        setIndicatorsEnabled(debugging);
    }

    public void setIndicatorsEnabled(boolean enabled) {
        this.indicatorsEnabled = enabled;
    }

    public boolean areIndicatorsEnabled() {
        return this.indicatorsEnabled;
    }

    public void setLoggingEnabled(boolean enabled) {
        this.loggingEnabled = enabled;
    }

    public boolean isLoggingEnabled() {
        return this.loggingEnabled;
    }

    public StatsSnapshot getSnapshot() {
        return this.stats.createSnapshot();
    }

    public void shutdown() {
        if (this == singleton) {
            throw new UnsupportedOperationException("Default singleton instance cannot be shutdown.");
        } else if (!this.shutdown) {
            this.cache.clear();
            this.cleanupThread.shutdown();
            this.stats.shutdown();
            this.dispatcher.shutdown();
            for (DeferredRequestCreator deferredRequestCreator : this.targetToDeferredRequestCreator.values()) {
                deferredRequestCreator.cancel();
            }
            this.targetToDeferredRequestCreator.clear();
            this.shutdown = true;
        }
    }

    Request transformRequest(Request request) {
        Request transformed = this.requestTransformer.transformRequest(request);
        if (transformed != null) {
            return transformed;
        }
        throw new IllegalStateException("Request transformer " + this.requestTransformer.getClass().getCanonicalName() + " returned null for " + request);
    }

    void defer(ImageView view, DeferredRequestCreator request) {
        this.targetToDeferredRequestCreator.put(view, request);
    }

    void enqueueAndSubmit(Action action) {
        Object target = action.getTarget();
        if (target != null) {
            cancelExistingRequest(target);
            this.targetToAction.put(target, action);
        }
        submit(action);
    }

    void submit(Action action) {
        this.dispatcher.dispatchSubmit(action);
    }

    Bitmap quickMemoryCacheCheck(String key) {
        Bitmap cached = this.cache.get(key);
        if (cached != null) {
            this.stats.dispatchCacheHit();
        } else {
            this.stats.dispatchCacheMiss();
        }
        return cached;
    }

    void complete(BitmapHunter hunter) {
        boolean hasMultiple;
        boolean shouldDeliver = false;
        Action single = hunter.getAction();
        List<Action> joined = hunter.getActions();
        if (joined == null || joined.isEmpty()) {
            hasMultiple = false;
        } else {
            hasMultiple = true;
        }
        if (single != null || hasMultiple) {
            shouldDeliver = true;
        }
        if (shouldDeliver) {
            Uri uri = hunter.getData().uri;
            Exception exception = hunter.getException();
            Bitmap result = hunter.getResult();
            LoadedFrom from = hunter.getLoadedFrom();
            if (single != null) {
                deliverAction(result, from, single);
            }
            if (hasMultiple) {
                int n = joined.size();
                for (int i = 0; i < n; i++) {
                    deliverAction(result, from, (Action) joined.get(i));
                }
            }
            if (this.listener != null && exception != null) {
                this.listener.onImageLoadFailed(this, uri, exception);
            }
        }
    }

    private void deliverAction(Bitmap result, LoadedFrom from, Action action) {
        if (!action.isCancelled()) {
            if (!action.willReplay()) {
                this.targetToAction.remove(action.getTarget());
            }
            if (result == null) {
                action.error();
                if (this.loggingEnabled) {
                    Utils.log("Main", "errored", action.request.logId());
                }
            } else if (from == null) {
                throw new AssertionError("LoadedFrom cannot be null.");
            } else {
                action.complete(result, from);
                if (this.loggingEnabled) {
                    Utils.log("Main", "completed", action.request.logId(), "from " + from);
                }
            }
        }
    }

    private void cancelExistingRequest(Object target) {
        Utils.checkMain();
        Action action = (Action) this.targetToAction.remove(target);
        if (action != null) {
            action.cancel();
            this.dispatcher.dispatchCancel(action);
        }
        if (target instanceof ImageView) {
            DeferredRequestCreator deferredRequestCreator = (DeferredRequestCreator) this.targetToDeferredRequestCreator.remove((ImageView) target);
            if (deferredRequestCreator != null) {
                deferredRequestCreator.cancel();
            }
        }
    }

    public static Picasso with(Context context) {
        if (singleton == null) {
            synchronized (Picasso.class) {
                if (singleton == null) {
                    singleton = new Builder(context).build();
                }
            }
        }
        return singleton;
    }
}
