package com.squareup.picasso;

import android.net.NetworkInfo;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class PicassoExecutorService extends ThreadPoolExecutor {
    private static final int DEFAULT_THREAD_COUNT = 3;

    PicassoExecutorService() {
        super(3, 3, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new PicassoThreadFactory());
    }

    void adjustThreadCount(NetworkInfo info) {
        if (info == null || !info.isConnectedOrConnecting()) {
            setThreadCount(3);
            return;
        }
        switch (info.getType()) {
            case 0:
                switch (info.getSubtype()) {
                    case 1:
                    case 2:
                        setThreadCount(1);
                        return;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 12:
                        setThreadCount(2);
                        return;
                    case 13:
                    case 14:
                    case 15:
                        setThreadCount(3);
                        return;
                    default:
                        setThreadCount(3);
                        return;
                }
            case 1:
            case 6:
            case 9:
                setThreadCount(4);
                return;
            default:
                setThreadCount(3);
                return;
        }
    }

    private void setThreadCount(int threadCount) {
        setCorePoolSize(threadCount);
        setMaximumPoolSize(threadCount);
    }
}
