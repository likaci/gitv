package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.result.ApiResultHotWords;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.ResultStringListHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.impl.Params.DataType;
import java.util.List;

public class GetSearchHotCommand extends ServerCommand<List<Media>> {
    private static final String TAG = "GetSearchHotCommand";

    private class MyListener extends ResultStringListHolder implements IApiCallback<ApiResultHotWords> {
        private MyListener() {
        }

        public void onException(ApiException exception) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GetSearchHotCommand.TAG, "onException(" + exception + ")");
            }
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(exception));
            setCode(7);
        }

        public void onSuccess(ApiResultHotWords result) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GetSearchHotCommand.TAG, "onSuccess(" + result + ")");
            }
            setNetworkValid(true);
            if (result != null && result.data != null && result.data.hotwords != null) {
                for (String keyword : result.data.hotwords) {
                    add(keyword);
                }
            }
        }
    }

    public GetSearchHotCommand(Context context) {
        super(context, 10008, 20003, DataType.DATA_SEARCH_HOT);
        setNeedNetwork(true);
    }

    public Bundle onProcess(Bundle params) {
        MyListener listener = new MyListener();
        TVApi.hotWords.callSync(listener, new String[0]);
        if (listener.isNetworkValid()) {
            increaseAccessCount();
        }
        return listener.getResult();
    }
}
