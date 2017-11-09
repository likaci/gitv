package com.gala.tvapi.vr;

import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.tv2.ITVApiServer;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.property.TVApiProperty;
import com.gala.tvapi.vr.result.AlbumVRResult;
import com.gala.tvapi.vr.result.DeviceCheckVRResult;
import com.gala.tvapi.vr.result.DynamicqVRResult;
import com.gala.tvapi.vr.result.SysTimeVRResult;
import com.gala.video.api.ApiResult;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import org.cybergarage.xml.XML;

public class VRApi {
    public static final ITVApiServer<DynamicqVRResult> Dynamicq = m742a("http://api.vr.ptqy.gitv.tv/ivr/dynamicq", DynamicqVRResult.class, "dynamicqVR");
    private static String f1168a = "";
    public static final ITVApiServer<AlbumVRResult> albumInfo = m742a("http://api.vr.ptqy.gitv.tv/ivr/meta/%s", AlbumVRResult.class, "albumInfoVR");
    public static final ITVApiServer<DeviceCheckVRResult> deviceCheck = new C0322b(new C0316a("http://api.vr.ptqy.gitv.tv/ivr/reg"), DeviceCheckVRResult.class, "devRegisterVR", true);
    public static final ITVApiServer<ApiResult> playCheck = m742a("http://api.vr.ptqy.gitv.tv/ivr/playcheck/%s", ApiResult.class, "playCheckVR");
    public static final ITVApiServer<SysTimeVRResult> sysTime = m742a("http://api.vr.ptqy.gitv.tv/ivr/systime", SysTimeVRResult.class, "systemTimeVR");

    static class C0316a implements C0315a {
        private String f1166a;

        public C0316a(String str) {
            this.f1166a = str;
        }

        public final String mo864a(String... strArr) {
            return this.f1166a;
        }

        public final List<String> m738a() {
            List<String> linkedList = new LinkedList();
            linkedList.add("mac:" + TVApiBase.getTVApiProperty().getMacAddress());
            return linkedList;
        }

        public final String mo863a() {
            TVApiProperty tVApiProperty = TVApiBase.getTVApiProperty();
            return "devId=" + VRApi.m743a() + "&version=" + tVApiProperty.getVersion() + "&uuid=" + tVApiProperty.getUUID();
        }
    }

    static class C0317b implements C0315a {
        private String f1167a;

        public C0317b(String str) {
            this.f1167a = str;
        }

        public final String mo864a(String... strArr) {
            return VRApi.m745b(this.f1167a, strArr);
        }

        public final List<String> m741a() {
            List<String> linkedList = new LinkedList();
            linkedList.add("apiKey:" + VRApi.getApiKey());
            return linkedList;
        }

        public final String mo863a() {
            return null;
        }
    }

    private static String m745b(String str, String... strArr) {
        if (strArr == null || strArr.length <= 0) {
            return str;
        }
        String[] strArr2 = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            try {
                strArr2[i] = URLEncoder.encode(strArr[i], XML.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return String.format(str, (Object[]) strArr2);
    }

    private static final <T extends ApiResult> ITVApiServer<T> m742a(String str, Class<T> cls, String str2) {
        return new C0322b(new C0317b(str), cls, str2, false);
    }

    public static void setApiKey(String apiKey) {
        f1168a = apiKey;
    }

    public static String getApiKey() {
        return f1168a;
    }

    static /* synthetic */ String m743a() {
        String passportDeviceId = TVApiBase.getTVApiProperty().getPassportDeviceId();
        return !C0214a.m592a(passportDeviceId) ? passportDeviceId.replace("tv_", "vr_") : passportDeviceId;
    }
}
