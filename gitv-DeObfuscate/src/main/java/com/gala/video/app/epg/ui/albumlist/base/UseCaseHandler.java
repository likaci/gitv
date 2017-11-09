package com.gala.video.app.epg.ui.albumlist.base;

import com.gala.video.app.epg.ui.albumlist.base.UseCase.RequestValues;
import com.gala.video.app.epg.ui.albumlist.base.UseCase.ResponseValue;
import com.gala.video.app.epg.ui.albumlist.base.UseCase.UseCaseCallback;

public class UseCaseHandler {
    private static UseCaseHandler INSTANCE;
    private final IUseCaseScheduler mUseCaseScheduler;

    private static final class UiCallbackWrapper<V extends ResponseValue> implements UseCaseCallback<V> {
        private final UseCaseCallback<V> mCallback;
        private final UseCaseHandler mUseCaseHandler;

        public UiCallbackWrapper(UseCaseCallback<V> callback, UseCaseHandler useCaseHandler) {
            this.mCallback = callback;
            this.mUseCaseHandler = useCaseHandler;
        }

        public void onSuccess(V response) {
            this.mUseCaseHandler.notifyResponse(response, this.mCallback);
        }

        public void onError(Exception e) {
            this.mUseCaseHandler.notifyError(e, this.mCallback);
        }
    }

    public static UseCaseHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UseCaseHandler(new UseCaseThreadPoolScheduler());
        }
        return INSTANCE;
    }

    public UseCaseHandler(IUseCaseScheduler useCaseScheduler) {
        this.mUseCaseScheduler = useCaseScheduler;
    }

    public <T extends RequestValues, R extends ResponseValue> void execute(final UseCase<T, R> useCase, T values, UseCaseCallback<R> callback) {
        useCase.setRequestValues(values);
        useCase.setUseCaseCallback(new UiCallbackWrapper(callback, this));
        this.mUseCaseScheduler.execute(new Runnable() {
            public void run() {
                useCase.run();
            }
        });
    }

    public <V extends ResponseValue> void notifyResponse(V response, UseCaseCallback<V> useCaseCallback) {
        this.mUseCaseScheduler.notifyResponse(response, useCaseCallback);
    }

    private <V extends ResponseValue> void notifyError(Exception e, UseCaseCallback<V> useCaseCallback) {
        this.mUseCaseScheduler.onError(e, useCaseCallback);
    }
}
