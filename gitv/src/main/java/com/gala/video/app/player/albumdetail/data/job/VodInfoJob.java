package com.gala.video.app.player.albumdetail.data.job;

import com.gala.tvapi.vrs.BOSSHelper;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.result.ApiResultVodInfo;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class VodInfoJob extends AlbumJob {
    private static final String TAG = "AlbumDetail/AlbumDetail/VodInfoJob";

    public VodInfoJob(AlbumInfo albumInfo, AlbumJobListener listener) {
        super(TAG, albumInfo, listener);
    }

    public void onRun(final JobController jobController) {
        final AlbumInfo albumInfo = (AlbumInfo) getData();
        if (albumInfo == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.e(TAG, "onRun: invalid info!");
            }
        } else if (albumInfo.isAlbumCoupon()) {
            String cookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
            BOSSHelper.queryVodInfo.callSync(new IVrsCallback<ApiResultVodInfo>() {
                public void onSuccess(ApiResultVodInfo apiResultVodInfo) {
                    if (!(apiResultVodInfo == null || apiResultVodInfo.data == null)) {
                        String couponCount = apiResultVodInfo.data.total;
                        LogUtils.d(VodInfoJob.TAG, "vod total -> " + apiResultVodInfo.data.total);
                        albumInfo.setCouponCount(couponCount);
                    }
                    VodInfoJob.this.notifyJobSuccess(jobController);
                }

                public void onException(ApiException e) {
                    LogUtils.w(VodInfoJob.TAG, "VodInfoJob failure, " + e);
                    VodInfoJob.this.notifyJobSuccess(jobController);
                }
            }, cookie);
        } else {
            notifyJobSuccess(jobController);
        }
    }
}
