package com.gala.tvapi.vrs;

import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.vrs.a.k;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.core.f;
import com.gala.tvapi.vrs.result.ApiResultCloudMessage;
import com.gala.tvapi.vrs.result.ApiResultCloudTVDeviceInfo;
import com.gala.tvapi.vrs.result.ApiResultMyDevices;
import com.gala.tvapi.vrs.result.ApiResultPPQVideosForUser;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiUrlBuilder;
import java.util.List;

public class PPQHelper extends BaseHelper {
    private static k a = new k();
    public static final IVrsServer<ApiResultMyDevices> myDevicesList = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.aI), a, ApiResultMyDevices.class, "myDevices", false, true);
    public static final IVrsServer<ApiResult> phoneBindTV = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.aE), a, ApiResult.class, "phoneBind", false, true);
    public static final IVrsServer<ApiResult> phoneUnbindTV = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.aF), a, ApiResult.class, "phoneUnbind", false, true);
    public static final IVrsServer<ApiResultPPQVideosForUser> ppqVideoListForUser = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.aB), a, ApiResultPPQVideosForUser.class, "papaqApiVideoList", false, true);
    public static final IVrsServer<ApiResultCloudMessage> sendCloudMessage = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.aJ), a, ApiResultCloudMessage.class, "sendUgcMessage", true, true);
    public static final IVrsServer<ApiResult> sendCloudMessageP2P = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.aK), a, ApiResult.class, "sendMessageP2P", false, true);
    public static final IVrsServer<ApiResultCloudTVDeviceInfo> tvDeviceInfo = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.aD), a, ApiResultCloudTVDeviceInfo.class, "deviceInfo", false, true);
    public static final IVrsServer<ApiResult> tvDeviceRegister = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.aC), a, ApiResult.class, "deviceRegister", false, true);
    public static final IVrsServer<ApiResult> tvUnbindUser = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.aG), a, ApiResult.class, "tvUnbindUser", false, true);
    public static final IVrsServer<ApiResult> updateTvName = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.aH), a, ApiResult.class, "updateTVNickName", true, true);

    public static final class a implements IApiUrlBuilder {
        private String a = null;

        public a(String str) {
            this.a = str;
        }

        public final String build(String... params) {
            if (params != null && params.length > 0) {
                String a;
                if (this.a.contains("reg.action") && params.length == 4) {
                    a = f.a(params[0], params[1], params[2], params[3]);
                    return BaseHelper.b(this.a, params[0], params[1], params[2], params[3], a);
                } else if (this.a.contains("device_info.action") && params.length == 2) {
                    a = f.a(params[0], params[1]);
                    return BaseHelper.b(this.a, params[0], params[1], a);
                } else if (this.a.contains("bind.action") && params.length == 4) {
                    a = f.b(params[0], params[1], params[2], params[3]);
                    return BaseHelper.b(this.a, params[0], params[1], params[2], params[3], a);
                } else if (this.a.contains("unbind_device.action") && params.length == 2) {
                    a = f.b(params[0], params[1]);
                    return BaseHelper.b(this.a, params[0], params[1], a);
                } else if (this.a.contains("unbind_user.action") && params.length == 2) {
                    a = f.c(params[0], params[1]);
                    return BaseHelper.b(this.a, params[0], params[1], a);
                } else if (this.a.contains("update_device_nick_name.action") && params.length == 3) {
                    a = f.a(params[0], params[1], params[2]);
                    return BaseHelper.b(this.a, params[0], params[1], params[2], a);
                } else if (this.a.contains("my_devices.action") && params.length == 2) {
                    a = f.d(params[0], params[1]);
                    return BaseHelper.b(this.a, params[0], params[1], a);
                } else if (this.a.contains("user/device/send_ugc_message.action") && params.length == 14) {
                    a = f.a(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13]);
                    return BaseHelper.b(this.a, params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13], a);
                } else if (this.a.contains("push_to_me.action") && params.length == 13) {
                    a = f.a(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12]);
                    return BaseHelper.b(this.a, params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], a);
                } else if (this.a.contains("push_to_all.action") && params.length == 8) {
                    a = f.a(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7]);
                    return BaseHelper.b(this.a, params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], a);
                }
            }
            TVApiTool tVApiTool = BaseHelper.a;
            return TVApiTool.parseLicenceUrl(this.a);
        }

        public final List<String> header() {
            return null;
        }
    }
}
