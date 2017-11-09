package com.gala.video.app.epg.web.type;

import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.web.model.WebBaseTypeParams;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.utils.DataUtils;
import com.gala.video.webview.utils.WebSDKConstants;

public class MemberPackageType implements IWebBaseClickType {
    private static final String TAG = "EPG/web/MemberPackageType";

    public void onClick(WebBaseTypeParams params) {
        String paramJson = params.getJsonString();
        LogUtils.d(TAG, "MemberPackageType paramJson:" + paramJson);
        JSONObject jsonObject = DataUtils.parseToJsonObject(paramJson);
        if (jsonObject == null) {
            LogUtils.e(TAG, "MemberPackageType paramJson is null");
            return;
        }
        try {
            Album albumInfo = DataUtils.parseToAlbum(jsonObject.getString("album"));
            WebIntentParams intentParams = new WebIntentParams();
            intentParams.pageType = jsonObject.getIntValue(WebSDKConstants.PARAM_KEY_PAGE_TYPE);
            intentParams.enterType = jsonObject.getIntValue(WebSDKConstants.PARAM_KEY_ENTER_TYPE);
            intentParams.from = jsonObject.getString("from");
            intentParams.buySource = jsonObject.getString("buy_source");
            intentParams.albumInfo = albumInfo;
            intentParams.requestCode = 0;
            intentParams.state = jsonObject.getString("state");
            intentParams.eventId = jsonObject.getString(WebSDKConstants.PARAM_KEY_EVENTID);
            intentParams.buyFrom = jsonObject.getString("buy_from");
            GetInterfaceTools.getWebEntry().startPurchasePage(params.getContext(), intentParams);
        } catch (Exception e) {
            LogUtils.e(TAG, "goto MemberPackageType:" + e);
        }
    }
}
