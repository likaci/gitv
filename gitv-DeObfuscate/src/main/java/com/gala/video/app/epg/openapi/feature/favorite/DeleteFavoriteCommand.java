package com.gala.video.app.epg.openapi.feature.favorite;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.LocalUserTags;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiManager;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.NetworkHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.impl.Params.OperationType;

public class DeleteFavoriteCommand extends ServerCommand<Void> {
    private static final String TAG = "DeleteFavoriteCommand";
    private Album mAlbum;
    private Media mMedia;

    private class MyListener extends NetworkHolder implements IVrsCallback<ApiResultCode> {
        public int code;

        private MyListener() {
            this.code = 1;
        }

        public void onException(ApiException apiException) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(DeleteFavoriteCommand.TAG, "onException(" + apiException + ")");
            }
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(apiException));
            this.code = 7;
        }

        public void onSuccess(ApiResultCode arg0) {
            DeleteFavoriteCommand.this.setNeedNetwork(true);
            OpenApiManager mOpenApiManager = OpenApiManager.instance();
            if (mOpenApiManager.isAuthSuccess()) {
                mOpenApiManager.getFavoriteChangedReporter().reportFavoriteChanged(2, DeleteFavoriteCommand.this.mMedia);
            }
            this.code = 0;
        }
    }

    protected DeleteFavoriteCommand(Context context, int target, int operation, int dataType) {
        super(context, target, operation, dataType);
    }

    public DeleteFavoriteCommand(Context context) {
        super(context, 10003, OperationType.OP_DELETE_FAVORITE, 30000);
        setNeedNetwork(true);
    }

    protected boolean isLogin() {
        return GetInterfaceTools.getIGalaAccountManager().isLogin(getContext());
    }

    protected Bundle onProcess(Bundle inParams) {
        this.mMedia = ServerParamsHelper.parseMedia(inParams);
        if (this.mMedia == null) {
            return OpenApiResultCreater.createResultBundle(6);
        }
        this.mAlbum = OpenApiUtils.createAlbum(this.mMedia);
        MyListener listener = new MyListener();
        if (isLogin()) {
            String mCookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
            UserHelper.cancelCollect.callSync(listener, LocalUserTags.getSubType(this.mMedia.getUserTags()), LocalUserTags.getSubKey(this.mMedia.getUserTags()), mCookie, String.valueOf(this.mAlbum.chnId));
        } else {
            String anonymityUserId = AppRuntimeEnv.get().getDefaultUserId();
            UserHelper.cancelCollectForAnonymity.callSync(listener, LocalUserTags.getSubType(this.mMedia.getUserTags()), LocalUserTags.getSubKey(this.mMedia.getUserTags()), anonymityUserId, String.valueOf(this.mAlbum.chnId));
        }
        if (!listener.isNetworkValid()) {
            increaseAccessCount();
        }
        return OpenApiResultCreater.createResultBundle(listener.code);
    }
}
