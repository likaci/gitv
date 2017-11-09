package retrofit;

import java.util.concurrent.Executor;

abstract class CallbackRunnable<T> implements Runnable {
    private final Callback<T> callback;
    private final Executor callbackExecutor;
    private final ErrorHandler errorHandler;

    public abstract ResponseWrapper obtainResponse();

    CallbackRunnable(Callback<T> callback, Executor callbackExecutor, ErrorHandler errorHandler) {
        this.callback = callback;
        this.callbackExecutor = callbackExecutor;
        this.errorHandler = errorHandler;
    }

    public final void run() {
        try {
            final ResponseWrapper wrapper = obtainResponse();
            this.callbackExecutor.execute(new Runnable() {
                public void run() {
                    CallbackRunnable.this.callback.success(wrapper.responseBody, wrapper.response);
                }
            });
        } catch (Throwable e) {
            Throwable cause = this.errorHandler.handleError(e);
            final RetrofitError handled = cause == e ? e : RetrofitError.unexpectedError(e.getUrl(), cause);
            this.callbackExecutor.execute(new Runnable() {
                public void run() {
                    CallbackRunnable.this.callback.failure(handled);
                }
            });
        }
    }
}
