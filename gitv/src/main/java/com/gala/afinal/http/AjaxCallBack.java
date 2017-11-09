package com.gala.afinal.http;

public abstract class AjaxCallBack<T> {
    private boolean progress = true;
    private int rate = 1000;

    public boolean isProgress() {
        return this.progress;
    }

    public int getRate() {
        return this.rate;
    }

    public AjaxCallBack<T> progress(boolean progress, int rate) {
        this.progress = progress;
        this.rate = rate;
        return this;
    }

    public void onStart() {
    }

    public void onLoading(long j, long j2) {
    }

    public void onSuccess(T t) {
    }

    public void onFailure(Throwable th, int i, String str) {
    }
}
