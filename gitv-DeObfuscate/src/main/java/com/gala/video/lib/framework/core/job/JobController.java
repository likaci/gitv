package com.gala.video.lib.framework.core.job;

import android.content.Context;

public interface JobController {
    void cancel();

    Context getContext();

    boolean isCancelled();
}
