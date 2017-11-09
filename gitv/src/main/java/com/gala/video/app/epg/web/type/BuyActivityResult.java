package com.gala.video.app.epg.web.type;

import android.content.Context;
import com.alibaba.fastjson.JSONObject;
import com.gala.video.app.epg.web.model.WebBaseTypeParams;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.LoginParam4H5;
import com.gala.video.lib.share.utils.DataUtils;
import com.gala.video.webview.utils.WebSDKConstants;

public class BuyActivityResult implements IWebBaseClickType {
    private static final String TAG = "EPG/web/BuyActivityResult";

    public BuyActivityResult(Context context) {
    }

    public void onClick(WebBaseTypeParams params) {
        CharSequence paramJson = params.getJsonString();
        LogUtils.d(TAG, "BuyActivityResult params:" + paramJson);
        if (StringUtils.isEmpty(paramJson) || paramJson.equalsIgnoreCase("undefined")) {
            LogUtils.e(TAG, "paramJson is null or undefined!");
            return;
        }
        JSONObject jsonObject = DataUtils.parseToJsonObject(paramJson);
        if (jsonObject != null) {
            String cookie = jsonObject.getString(WebSDKConstants.PARAM_KEY_COOKIE);
            String uid = jsonObject.getString(WebSDKConstants.PARAM_KEY_UID);
            String account = jsonObject.getString(WebSDKConstants.PARAM_KEY_USER_ACCOUNT);
            String nickName = jsonObject.getString(WebSDKConstants.PARAM_KEY_USER_NAME);
            int userTypeH5 = jsonObject.getIntValue(WebSDKConstants.PARAM_KEY_USER_TYPE);
            boolean isLitchiH5 = jsonObject.getBooleanValue(WebSDKConstants.PARAM_KEY_VIP_ISLITCHI);
            LoginParam4H5 param = new LoginParam4H5();
            param.cookie = cookie;
            param.uid = uid;
            param.account = account;
            param.nickName = nickName;
            param.userTypeH5 = userTypeH5;
            param.isLitchiH5 = isLitchiH5;
            GetInterfaceTools.getIGalaAccountManager().saveAccountInfoForH5(param);
        }
    }
}
