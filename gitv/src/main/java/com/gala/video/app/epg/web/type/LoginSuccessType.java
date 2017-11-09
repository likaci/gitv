package com.gala.video.app.epg.web.type;

import com.alibaba.fastjson.JSONObject;
import com.gala.video.app.epg.web.model.WebBaseTypeParams;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.LoginParam4H5;
import com.gala.video.lib.share.utils.DataUtils;
import com.gala.video.webview.utils.WebSDKConstants;

public class LoginSuccessType implements IWebBaseClickType {
    private static final String TAG = "EPG/web/LoginSuccessType";

    public void onClick(WebBaseTypeParams params) {
        JSONObject jsonObject = DataUtils.parseToJsonObject(params.getJsonString());
        if (jsonObject != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "onLoginSuccess --- save user info and set account type");
            }
            String cookie = jsonObject.getString(WebSDKConstants.PARAM_KEY_COOKIE);
            String uid = jsonObject.getString(WebSDKConstants.PARAM_KEY_UID);
            String account = jsonObject.getString(WebSDKConstants.PARAM_KEY_USER_ACCOUNT);
            String nickName = jsonObject.getString(WebSDKConstants.PARAM_KEY_USER_NAME);
            String vipDate = jsonObject.getString(WebSDKConstants.PARAM_KEY_VIP_DATE);
            String vipTime = jsonObject.getString(WebSDKConstants.PARAM_KEY_VIP_TIME);
            int userTypeH5 = jsonObject.getIntValue(WebSDKConstants.PARAM_KEY_USER_TYPE);
            boolean isLitchiH5 = jsonObject.getBooleanValue(WebSDKConstants.PARAM_KEY_VIP_ISLITCHI);
            LoginParam4H5 param = new LoginParam4H5();
            param.cookie = cookie;
            param.uid = uid;
            param.account = account;
            param.nickName = nickName;
            param.vipDate = vipDate;
            param.userTypeH5 = userTypeH5;
            param.isLitchiH5 = isLitchiH5;
            param.vip_time = vipTime;
            GetInterfaceTools.getIGalaAccountManager().loginForH5(param);
        }
    }
}
