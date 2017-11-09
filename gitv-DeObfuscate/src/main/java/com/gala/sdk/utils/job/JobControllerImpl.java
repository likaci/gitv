package com.gala.sdk.utils.job;

import android.content.Context;
import java.lang.ref.WeakReference;

public class JobControllerImpl implements JobController {
    private WeakReference<Context> f724a;
    private boolean f725a;

    public JobControllerImpl(Context context) {
        this.f724a = new WeakReference(context);
    }

    public Context getContext() {
        return (Context) this.f724a.get();
    }

    public boolean isCancelled() {
        return this.f725a;
    }

    public void cancel() {
        this.f725a = true;
    }
}
