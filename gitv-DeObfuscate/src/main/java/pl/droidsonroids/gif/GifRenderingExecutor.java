package pl.droidsonroids.gif;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;

final class GifRenderingExecutor extends ScheduledThreadPoolExecutor {

    static final class InstanceHolder {
        private static final GifRenderingExecutor INSTANCE = new GifRenderingExecutor();

        private InstanceHolder() {
        }
    }

    static GifRenderingExecutor getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private GifRenderingExecutor() {
        super(1, new DiscardPolicy());
    }
}
