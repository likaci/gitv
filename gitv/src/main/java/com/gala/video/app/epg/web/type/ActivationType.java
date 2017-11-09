package com.gala.video.app.epg.web.type;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.gala.video.app.epg.web.model.WebBaseTypeParams;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class ActivationType implements IWebBaseClickType {
    private static final String TAG = "EPG/web/ActivationType";
    private Context mContext;
    private String mParamJson;

    public ActivationType(Context context) {
        this.mContext = context;
    }

    public void onClick(WebBaseTypeParams params) {
        this.mParamJson = params.getJsonString();
        LogUtils.d(TAG, "gotoActivation params:" + this.mParamJson);
        if (this.mParamJson != null && !this.mParamJson.equalsIgnoreCase("undefined") && !this.mParamJson.equalsIgnoreCase("null")) {
            try {
                GetInterfaceTools.getLoginProvider().startActivateActivity(this.mContext, JSON.parseObject(this.mParamJson).getString("from"), 9);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(TAG, "startActivateActivity error:" + e);
            }
        }
    }
}
