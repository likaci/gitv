package com.gala.tvapi.vrs;

import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.vrs.BaseHelper.C0328a;
import com.gala.tvapi.vrs.core.C0365a;
import com.gala.tvapi.vrs.core.C0376f;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.p031a.C0336k;
import com.gala.tvapi.vrs.result.ApiResultCloudMessage;
import com.gala.tvapi.vrs.result.ApiResultCloudTVDeviceInfo;
import com.gala.tvapi.vrs.result.ApiResultMyDevices;
import com.gala.tvapi.vrs.result.ApiResultPPQVideosForUser;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiUrlBuilder;
import java.util.List;

public class PPQHelper extends BaseHelper {
    private static C0336k f1201a = new C0336k();
    public static final IVrsServer<ApiResultMyDevices> myDevicesList = C0214a.m581a(new C0329a(C0365a.aI), f1201a, ApiResultMyDevices.class, "myDevices", false, true);
    public static final IVrsServer<ApiResult> phoneBindTV = C0214a.m581a(new C0329a(C0365a.aE), f1201a, ApiResult.class, "phoneBind", false, true);
    public static final IVrsServer<ApiResult> phoneUnbindTV = C0214a.m581a(new C0329a(C0365a.aF), f1201a, ApiResult.class, "phoneUnbind", false, true);
    public static final IVrsServer<ApiResultPPQVideosForUser> ppqVideoListForUser = C0214a.m581a(new C0328a(C0365a.aB), f1201a, ApiResultPPQVideosForUser.class, "papaqApiVideoList", false, true);
    public static final IVrsServer<ApiResultCloudMessage> sendCloudMessage = C0214a.m581a(new C0329a(C0365a.aJ), f1201a, ApiResultCloudMessage.class, "sendUgcMessage", true, true);
    public static final IVrsServer<ApiResult> sendCloudMessageP2P = C0214a.m581a(new C0329a(C0365a.aK), f1201a, ApiResult.class, "sendMessageP2P", false, true);
    public static final IVrsServer<ApiResultCloudTVDeviceInfo> tvDeviceInfo = C0214a.m581a(new C0329a(C0365a.aD), f1201a, ApiResultCloudTVDeviceInfo.class, "deviceInfo", false, true);
    public static final IVrsServer<ApiResult> tvDeviceRegister = C0214a.m581a(new C0329a(C0365a.aC), f1201a, ApiResult.class, "deviceRegister", false, true);
    public static final IVrsServer<ApiResult> tvUnbindUser = C0214a.m581a(new C0329a(C0365a.aG), f1201a, ApiResult.class, "tvUnbindUser", false, true);
    public static final IVrsServer<ApiResult> updateTvName = C0214a.m581a(new C0329a(C0365a.aH), f1201a, ApiResult.class, "updateTVNickName", true, true);

    public static final class C0329a implements IApiUrlBuilder {
        private String f1200a = null;

        public C0329a(String str) {
            this.f1200a = str;
        }

        public final String build(String... params) {
            if (params != null && params.length > 0) {
                String a;
                if (this.f1200a.contains("reg.action") && params.length == 4) {
                    a = C0376f.m808a(params[0], params[1], params[2], params[3]);
                    return BaseHelper.m757b(this.f1200a, params[0], params[1], params[2], params[3], a);
                } else if (this.f1200a.contains("device_info.action") && params.length == 2) {
                    a = C0376f.m806a(params[0], params[1]);
                    return BaseHelper.m757b(this.f1200a, params[0], params[1], a);
                } else if (this.f1200a.contains("bind.action") && params.length == 4) {
                    a = C0376f.m819b(params[0], params[1], params[2], params[3]);
                    return BaseHelper.m757b(this.f1200a, params[0], params[1], params[2], params[3], a);
                } else if (this.f1200a.contains("unbind_device.action") && params.length == 2) {
                    a = C0376f.m817b(params[0], params[1]);
                    return BaseHelper.m757b(this.f1200a, params[0], params[1], a);
                } else if (this.f1200a.contains("unbind_user.action") && params.length == 2) {
                    a = C0376f.m823c(params[0], params[1]);
                    return BaseHelper.m757b(this.f1200a, params[0], params[1], a);
                } else if (this.f1200a.contains("update_device_nick_name.action") && params.length == 3) {
                    a = C0376f.m807a(params[0], params[1], params[2]);
                    return BaseHelper.m757b(this.f1200a, params[0], params[1], params[2], a);
                } else if (this.f1200a.contains("my_devices.action") && params.length == 2) {
                    a = C0376f.m827d(params[0], params[1]);
                    return BaseHelper.m757b(this.f1200a, params[0], params[1], a);
                } else if (this.f1200a.contains("user/device/send_ugc_message.action") && params.length == 14) {
                    a = C0376f.m812a(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13]);
                    return BaseHelper.m757b(this.f1200a, params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13], a);
                } else if (this.f1200a.contains("push_to_me.action") && params.length == 13) {
                    a = C0376f.m811a(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12]);
                    return BaseHelper.m757b(this.f1200a, params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], a);
                } else if (this.f1200a.contains("push_to_all.action") && params.length == 8) {
                    a = C0376f.m809a(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7]);
                    return BaseHelper.m757b(this.f1200a, params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], a);
                }
            }
            TVApiTool tVApiTool = BaseHelper.f1195a;
            return TVApiTool.parseLicenceUrl(this.f1200a);
        }

        public final List<String> header() {
            return null;
        }
    }
}
