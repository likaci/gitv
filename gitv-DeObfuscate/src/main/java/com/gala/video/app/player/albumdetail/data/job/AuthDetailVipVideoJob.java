package com.gala.video.app.player.albumdetail.data.job;

import com.gala.tvapi.vrs.BOSSHelper;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.result.ApiResultAuthVipVideo;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import java.util.Arrays;

public class AuthDetailVipVideoJob extends AlbumJob {
    private static final String TAG = "AlbumDetail/AlbumDetail/AuthDetailVipVideoJob";

    public AuthDetailVipVideoJob(AlbumInfo albumInfo, AlbumJobListener listener) {
        super(TAG, albumInfo, listener);
    }

    public void onRun(final JobController controller) {
        final AlbumInfo albumInfo = (AlbumInfo) getData();
        if (albumInfo != null) {
            LogRecordUtils.logd(TAG, "onRun: qpid=" + albumInfo.getAlbumId() + ", vid=" + albumInfo.getVid());
            LogRecordUtils.logd(TAG, "onRun: isAlbumVip ->" + albumInfo.isAlbumVip() + ", isAlbumCoupon -> " + albumInfo.isAlbumCoupon() + ", isAlbumSinglePay -> " + albumInfo.isAlbumSinglePay());
            if (albumInfo.isAlbumVip() || albumInfo.isAlbumSinglePay() || albumInfo.isAlbumCoupon()) {
                String defaultUserId = AppRuntimeEnv.get().getDefaultUserId();
                CharSequence cookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "fetchVipStatusData: defaultUserId=" + defaultUserId + ", cookie=" + cookie);
                }
                if (StringUtils.isEmpty(cookie)) {
                    albumInfo.setVipAuthorized(false);
                    notifyJobSuccess(controller);
                    return;
                }
                BOSSHelper.authVipVideo.call(new IVrsCallback<ApiResultAuthVipVideo>() {
                    public void onSuccess(ApiResultAuthVipVideo apiResult) {
                        if (apiResult != null) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.m1568d(AuthDetailVipVideoJob.TAG, "onSuccess: canPreview = " + apiResult.canPreview() + ", previewEpisodes = " + Arrays.toString(apiResult.getPreviewEpisodes()));
                            }
                            albumInfo.setVipAuthorized(!apiResult.canPreview());
                            AuthDetailVipVideoJob.this.notifyJobSuccess(controller);
                        } else if (LogUtils.mIsDebug) {
                            LogUtils.m1571e(AuthDetailVipVideoJob.TAG, "fetchVip success, null == result");
                        }
                    }

                    public void onException(ApiException e) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d(AuthDetailVipVideoJob.TAG, "onException: code = " + e.getCode() + ", msg = " + e.getMessage());
                        }
                        albumInfo.setVipAuthorized(false);
                        AuthDetailVipVideoJob.this.notifyJobSuccess(controller);
                    }
                }, albumInfo.getAlbumId(), albumInfo.getVid(), "0", defaultUserId, cookie);
                return;
            }
            albumInfo.setVipAuthorized(true);
            notifyJobSuccess(controller);
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1571e(TAG, "onRun: invalid info!");
        }
    }
}
