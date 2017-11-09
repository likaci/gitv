package com.gala.video.app.player.albumdetail.data.job;

import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.Package;
import com.gala.tvapi.vrs.result.ApiResultPackageContent;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumJobListener;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class PackageContentJob extends AlbumJob {
    private static final String TAG = "AlbumDetail/AlbumDetail/PackageContentJob";

    public PackageContentJob(AlbumInfo albumInfo, AlbumJobListener listener) {
        super(TAG, albumInfo, listener);
    }

    public void onRun(final JobController jobController) {
        final AlbumInfo albumInfo = (AlbumInfo) getData();
        if (albumInfo == null) {
            LogRecordUtils.loge(TAG, "onRun: invalid info!");
        } else if (albumInfo.isAlbumSinglePay()) {
            String cookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
            String aid = ((AlbumInfo) getData()).getAlbumId();
            VrsHelper.packageContentOfAlbum.call(new IVrsCallback<ApiResultPackageContent>() {
                public void onSuccess(ApiResultPackageContent apiResultPackageContent) {
                    LogRecordUtils.logd(PackageContentJob.TAG, "onsuccess");
                    if (!(apiResultPackageContent == null || ListUtils.isEmpty(apiResultPackageContent.getPackages()))) {
                        String originPrice = ((Package) apiResultPackageContent.getPackages().get(0)).originPrice;
                        String price = ((Package) apiResultPackageContent.getPackages().get(0)).price;
                        albumInfo.setAlbumOriginPrice(originPrice);
                        albumInfo.setAlbumPrice(price);
                        LogRecordUtils.logd(PackageContentJob.TAG, "onRun(): originPrice -> " + originPrice + ", price -> " + price);
                    }
                    PackageContentJob.this.notifyJobSuccess(jobController);
                }

                public void onException(ApiException e) {
                    PackageContentJob.this.notifyJobSuccess(jobController);
                }
            }, aid, cookie);
        } else {
            LogRecordUtils.logd(TAG, "singlePay -> " + albumInfo.isAlbumSinglePay());
            notifyJobSuccess(jobController);
        }
    }
}
