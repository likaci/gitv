package com.gala.albumprovider.logic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlbumProviderProperty {
    private ExecutorService f60a = Executors.newSingleThreadExecutor();
    private boolean f61a = true;
    private boolean f62b = false;

    public void setHasMyMovieTagFlag(boolean flag) {
        this.f61a = flag;
    }

    public boolean isHasMyMovieTag() {
        return this.f61a;
    }

    public void setDebugFlag(boolean flag) {
        this.f62b = flag;
    }

    public boolean isDebug() {
        return this.f62b;
    }

    public ExecutorService getExecutorService() {
        return this.f60a;
    }
}
