package com.gala.video.app.epg.web.subject.api;

import android.util.Log;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.BOSSHelper;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.result.ApiResultAuthVipVideo;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.web.subject.api.IApi.IStatusCallback;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.ifimpl.web.config.WebPingback;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class VipLiveApi {
    private static final int STATUS_0 = 0;
    private static final int STATUS_1 = 1;
    private static final String TAG = "EPG/Web/VipLiveApi";

    public void query(Album album, final IStatusCallback callback) {
        WebAlbumInfo info = new WebAlbumInfo(album);
        if (info.isPurchase()) {
            String defaultUserId = AppRuntimeEnv.get().getDefaultUserId();
            String cookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
            String uid = GetInterfaceTools.getIGalaAccountManager().getUID();
            Log.d(TAG, "query() ->  defaultUserId=" + defaultUserId + ", cookie=" + cookie + ",album live_channelId:" + info.getLiveChannelId());
            BOSSHelper.authVipLiveProgram.call(new IVrsCallback<ApiResultAuthVipVideo>() {
                public void onSuccess(ApiResultAuthVipVideo apiResult) {
                    Log.e(VipLiveApi.TAG, "query() ->  success");
                    callback.onStatus(0);
                }

                public void onException(ApiException e) {
                    Log.e(VipLiveApi.TAG, "query() -> onException() e:" + e);
                    new WebPingback().error(e, "BOSSHelper.authVipLiveProgram");
                    callback.onStatus(1);
                }
            }, info.getTVQid(), defaultUserId, uid, cookie);
            return;
        }
        Log.e(TAG, "query() ->  is not Purchase!!!");
        callback.onStatus(0);
    }
}
