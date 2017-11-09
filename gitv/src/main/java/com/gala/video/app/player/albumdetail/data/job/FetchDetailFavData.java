package com.gala.video.app.player.albumdetail.data.job;

import android.content.Context;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.app.player.utils.DataHelper;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class FetchDetailFavData extends AlbumJob {
    private static final String TAG = "AlbumDetail/AlbumDetail/FetchDetailFavData";

    public FetchDetailFavData(AlbumInfo albumInfo, AlbumJobListener listener) {
        super(TAG, albumInfo, listener);
    }

    public void onRun(final JobController controller) {
        final AlbumInfo albumInfo = (AlbumInfo) getData();
        Context context = controller.getContext();
        if (albumInfo != null) {
            DataHelper.updateFavData(albumInfo.getAlbum());
            String subKey = DataHelper.favSubKey;
            String subType = DataHelper.favSubType;
            boolean isLogin = GetInterfaceTools.getIGalaAccountManager().isLogin(context);
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "onRun: subKey=" + subKey + ", subType=" + subType + ", isLogin=" + isLogin);
            }
            if (isLogin) {
                String cookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
                if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "onRun: cookie=" + cookie);
                }
                UserHelper.checkCollect.call(new IVrsCallback<ApiResultCode>() {
                    public void onSuccess(ApiResultCode result) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(FetchDetailFavData.TAG, "logged in.onSuccess");
                        }
                        albumInfo.setFavored(true);
                        FetchDetailFavData.this.notifyJobSuccess(controller);
                    }

                    public void onException(ApiException e) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.e(FetchDetailFavData.TAG, "logged in.onException code =  " + e.getCode() + ", msg = " + e.getMessage());
                        }
                        albumInfo.setFavored(false);
                        FetchDetailFavData.this.notifyJobSuccess(controller);
                    }
                }, subType, subKey, cookie, String.valueOf(albumInfo.getChannelId()));
                return;
            }
            String anonymityUserId = AppRuntimeEnv.get().getDefaultUserId();
            UserHelper.checkCollectForAnonymity.call(new IVrsCallback<ApiResultCode>() {
                public void onSuccess(ApiResultCode result) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(FetchDetailFavData.TAG, "anonymity.onSuccess");
                    }
                    albumInfo.setFavored(true);
                    FetchDetailFavData.this.notifyJobSuccess(controller);
                }

                public void onException(ApiException e) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.e(FetchDetailFavData.TAG, "anonymity.onException code =  " + e.getCode() + ", msg = " + e.getMessage());
                    }
                    albumInfo.setFavored(false);
                    FetchDetailFavData.this.notifyJobSuccess(controller);
                }
            }, subType, subKey, anonymityUserId, String.valueOf(albumInfo.getChannelId()));
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(TAG, ">> onRun: invalid info!");
        }
    }
}
