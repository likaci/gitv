package com.gala.video.lib.framework.core.job;

import android.content.Context;
import java.lang.ref.WeakReference;

public class JobControllerImpl implements JobController {
    private boolean mCancelled;
    private WeakReference<Context> mContext;

    public JobControllerImpl(Context context) {
        this.mContext = new WeakReference(context);
    }

    public Context getContext() {
        return (Context) this.mContext.get();
    }

    public boolean isCancelled() {
        return this.mCancelled;
    }

    public void cancel() {
        this.mCancelled = true;
    }
}
