package com.gala.albumprovider.logic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlbumProviderProperty {
    private ExecutorService a = Executors.newSingleThreadExecutor();
    private boolean f49a = true;
    private boolean b = false;

    public void setHasMyMovieTagFlag(boolean flag) {
        this.f49a = flag;
    }

    public boolean isHasMyMovieTag() {
        return this.f49a;
    }

    public void setDebugFlag(boolean flag) {
        this.b = flag;
    }

    public boolean isDebug() {
        return this.b;
    }

    public ExecutorService getExecutorService() {
        return this.a;
    }
}
