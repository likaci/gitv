package com.gala.video.app.player.albumdetail.data.job;

import android.content.Context;
import com.gala.tvapi.tv2.constants.ApiCode;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.R;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.app.player.utils.DataHelper;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.job.JobError;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class UploadCollectJob extends AlbumJob {
    private static final String TAG = "AlbumDetail/Data/UploadCollectJob";

    public UploadCollectJob(AlbumInfo albumInfo, AlbumJobListener listener) {
        super(TAG, albumInfo, listener);
    }

    public void onRun(final JobController controller) {
        final AlbumInfo albumInfo = (AlbumInfo) getData();
        final Context context = controller.getContext();
        if (albumInfo != null && albumInfo.getAlbum() != null) {
            DataHelper.updateFavData(albumInfo.getAlbum());
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, ">> onRun: subtype=" + DataHelper.favSubType + ", mSubKey=" + DataHelper.favSubKey + ", chnId=" + albumInfo.getChannelId());
            }
            if (GetInterfaceTools.getIGalaAccountManager().isLogin(context)) {
                String cookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
                UserHelper.uploadCollect.call(new IVrsCallback<ApiResultCode>() {
                    public void onSuccess(ApiResultCode result) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(UploadCollectJob.TAG, "collectUploadAsync login onSuccess");
                        }
                        GetInterfaceTools.getOpenapiReporterManager().onAddFavRecord(albumInfo.getAlbum());
                        albumInfo.setFavored(true);
                        UploadCollectJob.this.notifyJobSuccess(controller);
                    }

                    public void onException(ApiException e) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(UploadCollectJob.TAG, "collectUploadAsync login onException:" + e.getCode());
                        }
                        if (!(ApiCode.USER_INFO_CHANGED.equals(e.getCode()) || ApiCode.ERROR_USER_IP.equals(e.getCode()))) {
                            QToast.makeTextAndShow(context, R.string.fav_failed, 2000);
                        }
                        UploadCollectJob.this.notifyJobFail(controller, new JobError(e.getCode(), e));
                    }
                }, DataHelper.favSubType, DataHelper.favSubKey, cookie, String.valueOf(albumInfo.getChannelId()));
                return;
            }
            String anonymityUserId = AppRuntimeEnv.get().getDefaultUserId();
            UserHelper.uploadCollectForAnonymity.call(new IVrsCallback<ApiResultCode>() {
                public void onSuccess(ApiResultCode arg0) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(UploadCollectJob.TAG, "collectUploadAsync Anonymity onSuccess");
                    }
                    albumInfo.setFavored(true);
                    UploadCollectJob.this.notifyJobSuccess(controller);
                }

                public void onException(ApiException e) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(UploadCollectJob.TAG, "collectUploadAsync Anonymity onException:" + e.getCode());
                    }
                    if (!ApiCode.USER_INFO_CHANGED.equals(e.getCode())) {
                        QToast.makeTextAndShow(context, R.string.fav_failed, 2000);
                    }
                    UploadCollectJob.this.notifyJobFail(controller, new JobError(e.getCode(), e));
                }
            }, DataHelper.favSubType, DataHelper.favSubKey, anonymityUserId, String.valueOf(albumInfo.getChannelId()));
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(TAG, "onRun: invalid data!!");
        }
    }
}
