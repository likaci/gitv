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
import com.gala.video.lib.framework.core.job.JobError;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class CancelCollectJob extends AlbumJob {
    private static final String TAG = "AlbumDetail/Data/CancelCollectJob";

    public CancelCollectJob(AlbumInfo albumInfo, AlbumJobListener listener) {
        super(TAG, albumInfo, listener);
    }

    public void onRun(final JobController controller) {
        String subType = DataHelper.favSubType;
        String subKey = DataHelper.favSubKey;
        final AlbumInfo albumInfo = (AlbumInfo) getData();
        Context context = controller.getContext();
        DataHelper.updateFavData(albumInfo.getAlbum());
        LogUtils.d(TAG, ">>onRun: subtype=" + subType + ", subKey=" + subKey);
        String cookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
        if (GetInterfaceTools.getIGalaAccountManager().isLogin(context)) {
            UserHelper.cancelCollect.call(new IVrsCallback<ApiResultCode>() {
                public void onSuccess(ApiResultCode result) {
                    LogUtils.d(CancelCollectJob.TAG, "onRun: login.onSuccess");
                    albumInfo.setFavored(false);
                    CancelCollectJob.this.notifyJobSuccess(controller);
                }

                public void onException(ApiException exception) {
                    LogUtils.d(CancelCollectJob.TAG, "onRun: login.onException, code=" + exception.getCode());
                    CancelCollectJob.this.notifyJobFail(controller, new JobError(exception.getCode(), exception));
                }
            }, subType, subKey, cookie, String.valueOf(albumInfo.getChannelId()));
            return;
        }
        String anonymityUserId = AppRuntimeEnv.get().getDefaultUserId();
        UserHelper.cancelCollectForAnonymity.call(new IVrsCallback<ApiResultCode>() {
            public void onSuccess(ApiResultCode arg0) {
                LogUtils.d(CancelCollectJob.TAG, "onRun: anonym.onSuccess");
                albumInfo.setFavored(false);
                CancelCollectJob.this.notifyJobSuccess(controller);
            }

            public void onException(ApiException e) {
                LogUtils.d(CancelCollectJob.TAG, "onRun: anonym.onException, code=" + e.getCode());
                CancelCollectJob.this.notifyJobFail(controller, new JobError(e.getCode(), e));
            }
        }, subType, subKey, anonymityUserId, String.valueOf(albumInfo.getChannelId()));
    }
}
