package com.gala.video.app.epg.openapi.feature.favorite;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultCollectList;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.MaxCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.ResultListHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.impl.Params.DataType;
import java.util.List;

public class GetFavoriteListCommand extends MaxCommand<List<Media>> {
    private static final String TAG = "GetFavoriteListCommand";

    private class AnonymousListener extends ResultListHolder<Media> implements IVrsCallback<ApiResultCollectList> {
        public AnonymousListener(int maxCount) {
            super(maxCount);
        }

        public void onSuccess(ApiResultCollectList result) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(GetFavoriteListCommand.TAG, "AnonymousListener.onSuccess(" + result + ")");
            }
            setNetworkValid(true);
            for (Album info : result.getAlbumList()) {
                Media media = OpenApiUtils.createSdkMedia(info);
                if (media != null) {
                    add(media);
                } else {
                    LogUtils.w(GetFavoriteListCommand.TAG, "onSuccess() cannot translate to sdk media!!!");
                }
                if (isReachMax()) {
                    LogUtils.w(GetFavoriteListCommand.TAG, "onSuccess() reach max size !!!" + this);
                    return;
                }
            }
        }

        public void onException(ApiException exception) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(GetFavoriteListCommand.TAG, "AnonymousListener.onException(" + exception + ")");
            }
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(exception));
            setCode(7);
        }
    }

    private class UserListener extends ResultListHolder<Media> implements IVrsCallback<ApiResultCollectList> {
        public UserListener(int maxCount) {
            super(maxCount);
        }

        public void onSuccess(ApiResultCollectList result) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(GetFavoriteListCommand.TAG, "UserListener.onSuccess(" + result + ")");
            }
            setNetworkValid(true);
            for (Album info : result.getAlbumList()) {
                Media media = OpenApiUtils.createSdkMedia(info);
                if (media != null) {
                    add(media);
                } else {
                    LogUtils.w(GetFavoriteListCommand.TAG, "onSuccess() cannot translate to sdk media!!!");
                }
                if (isReachMax()) {
                    LogUtils.w(GetFavoriteListCommand.TAG, "onSuccess() reach max size !!!" + this);
                    return;
                }
            }
        }

        public void onException(ApiException exception) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(GetFavoriteListCommand.TAG, "UserListener.onException(" + exception + ")");
            }
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(exception));
            setCode(7);
        }
    }

    public GetFavoriteListCommand(Context context, int maxCount) {
        super(context, 10003, 20003, DataType.DATA_MEDIA_LIST, maxCount);
    }

    protected Bundle onProcess(Bundle params) {
        ResultListHolder<Media> listener;
        int pageNo = ServerParamsHelper.parsePageNo(params) - 1;
        int pageSize = ServerParamsHelper.parsePageSize(params);
        int maxCount = ServerParamsHelper.parseMaxCount(params);
        if (maxCount > getMaxCount()) {
            maxCount = getMaxCount();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "process() maxCount=" + maxCount);
        }
        ResultListHolder<Media> tlistener;
        if (GetInterfaceTools.getIGalaAccountManager().isLogin(getContext())) {
            tlistener = new UserListener(maxCount);
            UserHelper.collectList.callSync(tlistener, GetInterfaceTools.getIGalaAccountManager().getAuthCookie(), pageNo + "", pageSize + "");
            listener = tlistener;
        } else {
            tlistener = new AnonymousListener(maxCount);
            UserHelper.collectListForAnonymity.callSync(tlistener, AppRuntimeEnv.get().getDefaultUserId(), pageNo + "", pageSize + "");
            listener = tlistener;
        }
        if (listener.isNetworkValid()) {
            increaseAccessCount();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "process() pageNo=" + pageNo + ", pageSize=" + pageSize + ", maxCount=" + maxCount);
        }
        return listener.getResult();
    }
}
