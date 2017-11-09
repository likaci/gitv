package com.gala.video.app.epg.web.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.video.app.epg.web.model.WebBaseTypeParams;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class SubjectAction implements IWebBaseClickType {
    private static final String TAG = "SubjectAction";

    public void onClick(WebBaseTypeParams params) {
        CharSequence paramJson = params.getJsonString();
        LogUtils.d(TAG, "SubjectAction params:" + paramJson);
        if (!StringUtils.isEmpty(paramJson)) {
            try {
                JSONObject jsonObject = JSON.parseObject(paramJson);
                GetInterfaceTools.getWebEntry().gotoSubject(params.getContext(), String.valueOf(jsonObject.getInteger("chnId").intValue()), jsonObject.getString("chnName"), "", "");
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(TAG, "SubjectAction error:" + e);
            }
        }
    }
}
