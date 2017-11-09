package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.result.ApiResultRecommendListQipu;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.provider.RecommendQuitApkProvider;
import com.gala.video.app.epg.ui.albumlist.utils.UserUtil;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class RecommendQuitApkTask extends BaseRequestTask {
    public void invoke() {
        VrsHelper.recommendListQipu.callSync(new IVrsCallback<ApiResultRecommendListQipu>() {
            public void onSuccess(ApiResultRecommendListQipu apiResultRecommendListQipu) {
                if (apiResultRecommendListQipu != null) {
                    RecommendQuitApkProvider.getInstance().writeCache(apiResultRecommendListQipu.getAlbumList());
                }
            }

            public void onException(ApiException e) {
                String str;
                IHomePingback addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008");
                String str2 = "pfec";
                if (e == null) {
                    str = "";
                } else {
                    str = e.getCode();
                }
                addItem = addItem.addItem(str2, str);
                str2 = Keys.ERRURL;
                if (e == null) {
                    str = "";
                } else {
                    str = e.getUrl();
                }
                addItem = addItem.addItem(str2, str).addItem(Keys.APINAME, "recommendListQipu");
                str2 = Keys.ERRDETAIL;
                if (e == null) {
                    str = "";
                } else {
                    str = e.getMessage();
                }
                addItem.addItem(str2, str).addItem("activity", "HomeActivity").addItem(Keys.T, "0").setOthersNull().post();
            }
        }, UserUtil.getLoginUserId(), UserUtil.getLogoutUserId(), "10", "-1", "t_skunk", "0");
    }

    public void onOneTaskFinished() {
    }
}
