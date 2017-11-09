package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.PaySDKInfo;
import com.gala.video.api.ApiResult;

public class ApiResultPaySDKInfo extends ApiResult {
    public String code;
    public PaySDKInfo data;
    public String message;
    public String orderCode;
    public String payType;
    public String serviceCode;
}
