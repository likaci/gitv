package com.gala.video.app.epg.ui.albumlist.base;

import android.os.Handler;
import com.gala.video.app.epg.ui.albumlist.base.UseCase.ResponseValue;
import com.gala.video.app.epg.ui.albumlist.base.UseCase.UseCaseCallback;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UseCaseThreadPoolScheduler implements IUseCaseScheduler {
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    public static final int MAX_POOL_SIZE = 4;
    public static final int POOL_SIZE = 3;
    public static final int TIMEOUT = 10;
    private final Handler mHandler = new Handler();
    private final ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(3, 4, 10, KEEP_ALIVE_TIME_UNIT, this.mWorkQueue);
    private final BlockingQueue<Runnable> mWorkQueue = new LinkedBlockingQueue(3);

    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("Runnable to execute cannot be null");
        }
        try {
            this.mThreadPoolExecutor.execute(runnable);
        } catch (RejectedExecutionException e) {
        } catch (Exception e2) {
        }
    }

    public <V extends ResponseValue> void notifyResponse(final V response, final UseCaseCallback<V> useCaseCallback) {
        this.mHandler.post(new Runnable() {
            public void run() {
                useCaseCallback.onSuccess(response);
            }
        });
    }

    public <V extends ResponseValue> void onError(final Exception e, final UseCaseCallback<V> useCaseCallback) {
        this.mHandler.post(new Runnable() {
            public void run() {
                useCaseCallback.onError(e);
            }
        });
    }
}
