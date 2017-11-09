package com.gala.sdk.utils.job;

import android.content.Context;
import java.lang.ref.WeakReference;

public class JobControllerImpl implements JobController {
    private WeakReference<Context> a;
    private boolean f366a;

    public JobControllerImpl(Context context) {
        this.a = new WeakReference(context);
    }

    public Context getContext() {
        return (Context) this.a.get();
    }

    public boolean isCancelled() {
        return this.f366a;
    }

    public void cancel() {
        this.f366a = true;
    }
}
