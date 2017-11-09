package com.gala.sdk.utils.job;

import android.content.Context;

public interface JobController {
    void cancel();

    Context getContext();

    boolean isCancelled();
}
