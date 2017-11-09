package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.result.ApiResultAlbum;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.NetworkHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.qiyi.tv.client.data.Media;

public abstract class BaseGetMediaInfoCommand extends ServerCommand<Media> {
    private static final String TAG = "BaseGetMediaInfoCommand";

    private static class MyListener extends NetworkHolder implements IApiCallback<ApiResultAlbum> {
        public int code;
        public Media media;

        private MyListener() {
        }

        public void onException(ApiException exception) {
            if (LogUtils.mIsDebug) {
                LogUtils.w(BaseGetMediaInfoCommand.TAG, "onException(" + exception + ")");
            }
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(exception));
            this.code = 7;
        }

        public void onSuccess(ApiResultAlbum albumResult) {
            if (LogUtils.mIsDebug) {
                LogUtils.w(BaseGetMediaInfoCommand.TAG, "onSuccess(" + albumResult + ")");
            }
            setNetworkValid(true);
            this.media = OpenApiUtils.createSdkMedia(albumResult.data);
        }
    }

    protected abstract String getId(Media media);

    public BaseGetMediaInfoCommand(Context context, int targetType, int operationType, int dataType) {
        super(context, targetType, operationType, dataType);
        setNeedNetwork(true);
    }

    protected Bundle onProcess(Bundle inParams) {
        Bundle outParams = new Bundle();
        Media media = ServerParamsHelper.parseMedia(inParams);
        if (media == null) {
            return OpenApiResultCreater.createResultBundle(6);
        }
        int code;
        Parcelable reMedia;
        if (StringUtils.isEmpty(getId(media))) {
            code = 0;
            Object reMedia2 = media;
        } else {
            MyListener listener = new MyListener();
            TVApi.albumInfo.callSync(listener, id);
            if (listener.isNetworkValid()) {
                increaseAccessCount();
            }
            code = listener.code;
            reMedia = listener.media;
        }
        ServerParamsHelper.setResultCode(outParams, code);
        ServerParamsHelper.setResultData(outParams, reMedia);
        return outParams;
    }
}
