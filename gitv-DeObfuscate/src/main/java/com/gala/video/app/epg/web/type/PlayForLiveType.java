package com.gala.video.app.epg.web.type;

import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.web.model.WebBaseTypeParams;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.model.player.LivePlayParamBuilder;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;
import com.gala.video.lib.share.utils.DataUtils;
import com.gala.video.webview.utils.WebSDKConstants;
import java.util.ArrayList;

public class PlayForLiveType implements IWebBaseClickType {
    private static final String TAG = "EPG/web/PlayForLiveType";
    private String mParamJson;
    private WebViewDataImpl mWebViewDataImpl;

    public void onClick(WebBaseTypeParams params) {
        this.mParamJson = params.getJsonString();
        this.mWebViewDataImpl = params.getWebViewDataImpl();
        LogUtils.m1568d(TAG, "PlayForLiveType params:" + this.mParamJson);
        JSONObject jsonObject = DataUtils.parseToJsonObject(this.mParamJson);
        if (jsonObject != null) {
            try {
                String from = this.mWebViewDataImpl.getFrom();
                String buySource = this.mWebViewDataImpl.getBuySource();
                String albumJson = jsonObject.getString("album");
                String albumListJson = jsonObject.getString(WebSDKConstants.PARAM_KEY_LIVE_FLOWERLIST);
                String tabSource = PingBackUtils.getTabSrc();
                Album albumInfo = DataUtils.parseToAlbum(albumJson);
                ArrayList<Album> flowerList = DataUtils.parseToAlbumList(albumListJson);
                LivePlayParamBuilder builder = new LivePlayParamBuilder();
                builder.setLiveAlbum(albumInfo).setFlowerList(flowerList).setFrom(from).setBuySource(buySource).setTabSource(tabSource);
                GetInterfaceTools.getPlayerPageProvider().startLivePlayerPage(params.getContext(), builder);
            } catch (Exception e) {
                LogUtils.m1571e(TAG, "PlayForLiveType error:" + e);
            }
        }
    }
}
