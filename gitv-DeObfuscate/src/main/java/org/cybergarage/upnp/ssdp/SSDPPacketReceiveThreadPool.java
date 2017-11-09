package org.cybergarage.upnp.ssdp;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SSDPPacketReceiveThreadPool {
    private static final int CORE_POOL_SIZE = 4;
    private static final int KEEP_ALIVE_TIME = 600;
    private static final int MAX_POOL_SIZE = 12;
    private static ThreadPoolExecutor mExecutor;
    private static SSDPPacketReceiveThreadPool mInstance;
    private BlockingQueue<Runnable> mWorkQueue = new LinkedBlockingQueue();

    private SSDPPacketReceiveThreadPool() {
        mExecutor = new ThreadPoolExecutor(4, 12, 600, TimeUnit.SECONDS, this.mWorkQueue);
    }

    public static SSDPPacketReceiveThreadPool getInstance() {
        if (mInstance == null) {
            mInstance = new SSDPPacketReceiveThreadPool();
        }
        return mInstance;
    }

    public void execute(Runnable task) {
        if (mExecutor == null) {
            mExecutor = new ThreadPoolExecutor(4, 12, 600, TimeUnit.SECONDS, this.mWorkQueue);
        }
        mExecutor.execute(task);
    }

    public void stop() {
        if (mExecutor != null) {
            mExecutor.shutdown();
            mExecutor = null;
        }
    }
}
