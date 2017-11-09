package com.gala.video.app.epg.ui.albumlist.base;

import com.gala.video.app.epg.ui.albumlist.base.UseCase.ResponseValue;
import com.gala.video.app.epg.ui.albumlist.base.UseCase.UseCaseCallback;
import java.util.concurrent.Executor;

public interface IUseCaseScheduler extends Executor {
    <V extends ResponseValue> void notifyResponse(V v, UseCaseCallback<V> useCaseCallback);

    <V extends ResponseValue> void onError(Exception exception, UseCaseCallback<V> useCaseCallback);
}
