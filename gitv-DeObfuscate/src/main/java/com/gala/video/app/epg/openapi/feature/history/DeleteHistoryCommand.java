package com.gala.video.app.epg.openapi.feature.history;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiManager;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.NetworkHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.data.Album;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.data.Video;
import com.qiyi.tv.client.impl.Params.OperationType;

public class DeleteHistoryCommand extends ServerCommand<Void> {
    private static final String TAG = "DeleteHistoryCommand";
    private Media mMedia;

    private class MyListener extends NetworkHolder implements IVrsCallback<ApiResultCode> {
        public int code;

        private MyListener() {
            this.code = 1;
        }

        public void onException(ApiException apiException) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(DeleteHistoryCommand.TAG, "onException(" + apiException + ")");
            }
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(apiException));
            this.code = 7;
        }

        public void onSuccess(ApiResultCode apiResultCode) {
            setNetworkValid(true);
            GetInterfaceTools.getIHistoryCacheManager().synchronizeHistoryListFromCloud();
            if (DeleteHistoryCommand.this.mMedia instanceof Album) {
                GetInterfaceTools.getIHistoryCacheManager().deleteHistory(((Album) DeleteHistoryCommand.this.mMedia).getId(), ((Album) DeleteHistoryCommand.this.mMedia).getVideoId());
            } else if (DeleteHistoryCommand.this.mMedia instanceof Video) {
                GetInterfaceTools.getIHistoryCacheManager().deleteHistory(((Video) DeleteHistoryCommand.this.mMedia).getAlbumId(), ((Video) DeleteHistoryCommand.this.mMedia).getId());
            }
            OpenApiManager mOpenApiManager = OpenApiManager.instance();
            if (mOpenApiManager.isAuthSuccess()) {
                LogUtils.m1577w(DeleteHistoryCommand.TAG, "reportFavoriteChanged");
                mOpenApiManager.getHistoryChangedReporter().reportHistoryChanged(2, DeleteHistoryCommand.this.mMedia);
            }
            this.code = 0;
        }
    }

    protected DeleteHistoryCommand(Context context, int target, int operation, int dataType) {
        super(context, target, operation, dataType);
        setNeedNetwork(true);
    }

    public DeleteHistoryCommand(Context context) {
        super(context, 10001, OperationType.OP_DELETE_HISTORY, 30000);
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
        MyListener listener = new MyListener();
        if (isLogin()) {
            UserHelper.deleteHistoryAlbum.callSync(listener, GetInterfaceTools.getIGalaAccountManager().getAuthCookie(), this.mMedia.getId());
        } else {
            UserHelper.deleteHistoryAlbumForForAnonymity.callSync(listener, AppRuntimeEnv.get().getDefaultUserId(), this.mMedia.getId());
        }
        return OpenApiResultCreater.createResultBundle(listener.code);
    }
}
