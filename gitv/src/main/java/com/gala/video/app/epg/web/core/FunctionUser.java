package com.gala.video.app.epg.web.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.alibaba.fastjson.JSON;
import com.gala.video.app.epg.web.function.WebFunContract.IFunUser;
import com.gala.video.app.epg.web.model.WebBaseTypeParams;
import com.gala.video.app.epg.web.type.BuyActivityResult;
import com.gala.video.app.epg.web.type.IWebBaseClickType;
import com.gala.video.app.epg.web.type.LoginSuccessType;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.project.Project;

public class FunctionUser implements IFunUser {
    private static final String TAG = "EPG/web/FunctionUser";
    private Context mContext;

    public FunctionUser(Context context) {
        this.mContext = context;
    }

    public void onLoginSuccess(String userInfo) {
        LogUtils.d(TAG, "H5 onLoginSuccess");
        IWebBaseClickType type = new LoginSuccessType();
        WebBaseTypeParams typeParams = new WebBaseTypeParams();
        typeParams.setJsonString(userInfo);
        typeParams.setContext(this.mContext);
        type.onClick(typeParams);
    }

    public void setActivityResult(String result, int resultCode) {
        LogUtils.d(TAG, "H5 setActivityResult resultCode:" + resultCode);
        IWebBaseClickType type = new BuyActivityResult(this.mContext);
        WebBaseTypeParams typeParams = new WebBaseTypeParams();
        typeParams.setJsonString(result);
        type.onClick(typeParams);
        if (this.mContext instanceof Activity) {
            LogUtils.d(TAG, "H5 setActivityResult setResult:" + resultCode);
            ((Activity) this.mContext).setResult(resultCode, new Intent().putExtra("result", result));
        }
    }

    public void onPushMsg(String msg) {
        if (StringUtils.isEmpty((CharSequence) msg)) {
            LogUtils.e(TAG, "onPushMsg msg is null");
            return;
        }
        IMsgContent msgContent = null;
        try {
            msgContent = (IMsgContent) JSON.parseObject(msg, IMsgContent.class);
        } catch (Exception e) {
            LogUtils.e(TAG, "parse message content occur exception :", e);
        }
        if (msgContent == null) {
            LogUtils.e(TAG, "onPushMsg msgContent is null");
            return;
        }
        int i;
        msgContent.app_id = IMsgUtils.getAppId(Project.getInstance().getBuild().getPingbackP2());
        msgContent.msg_level = 1;
        msgContent.msg_type = msgContent.msg_type != -2 ? msgContent.msg_type : -1;
        if (msgContent.page_jumping != 0) {
            i = msgContent.page_jumping;
        } else {
            i = 1;
        }
        msgContent.page_jumping = i;
        msgContent.is_detailpage = 0;
        msgContent.msg_id = msgContent.msg_id != 0 ? msgContent.msg_id : 66666;
        msgContent.msg_title = StringUtils.isEmpty(msgContent.msg_title) ? "这才是使用会员特权最正确的方式，赶快来试" : msgContent.msg_title;
        msgContent.msg_template_id = 2;
        GetInterfaceTools.getMsgCenter().pushMsgToApp(this.mContext, msgContent);
    }
}
