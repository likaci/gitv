package com.gala.video.app.epg.ui.albumlist.base;

public abstract class UseCase<Q extends RequestValues, P extends ResponseValue> {
    private Q mRequestValues;
    private UseCaseCallback<P> mUseCaseCallback;

    public interface RequestValues {
    }

    public interface ResponseValue {
    }

    public interface UseCaseCallback<R> {
        void onError(Exception exception);

        void onSuccess(R r);
    }

    protected abstract void executeUseCase(Q q);

    public void setRequestValues(Q requestValues) {
        this.mRequestValues = requestValues;
    }

    public Q getRequestValues() {
        return this.mRequestValues;
    }

    public UseCaseCallback<P> getUseCaseCallback() {
        return this.mUseCaseCallback;
    }

    public void setUseCaseCallback(UseCaseCallback<P> useCaseCallback) {
        this.mUseCaseCallback = useCaseCallback;
    }

    void run() {
        executeUseCase(this.mRequestValues);
    }
}
