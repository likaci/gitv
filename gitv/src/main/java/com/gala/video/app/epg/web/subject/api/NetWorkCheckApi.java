package com.gala.video.app.epg.web.subject.api;

import com.alibaba.fastjson.JSONObject;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.web.subject.api.IApi.IExceptionCallback;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import com.gala.video.lib.share.utils.DataUtils;
import com.mcto.ads.internal.net.PingbackConstants;

public class NetWorkCheckApi {
    private static final String TAG = "EPG/NetWorkCheckApi";

    public void check(String msg, IExceptionCallback callback) {
        String errorInfo;
        ApiException apiException;
        LogUtils.d(TAG, "NetWorkCheckApi check() -> msg :" + msg);
        if (msg == null) {
            errorInfo = "";
        } else {
            errorInfo = msg;
        }
        JSONObject jsonObject = DataUtils.parseToJsonObject(errorInfo);
        CharSequence code = jsonObject != null ? jsonObject.getString(PingbackConstants.CODE) : "";
        CharSequence errorMsg = jsonObject != null ? jsonObject.getString("msg") : "";
        if (StringUtils.isEmpty(code) && StringUtils.isEmpty(errorMsg)) {
            apiException = new ApiException("", "", "-50", "");
        } else if (StringUtils.isEmpty(code)) {
            apiException = new ApiException(errorMsg, "");
        } else {
            apiException = new ApiException("", code, ErrorEvent.HTTP_CODE_SUCCESS, "");
        }
        callback.getStateResult(apiException);
    }
}
