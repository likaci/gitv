package com.gala.tvapi.vr;

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
    public static final ITVApiServer<DynamicqVRResult> Dynamicq = a("http://api.vr.ptqy.gitv.tv/ivr/dynamicq", DynamicqVRResult.class, "dynamicqVR");
    private static String a = "";
    public static final ITVApiServer<AlbumVRResult> albumInfo = a("http://api.vr.ptqy.gitv.tv/ivr/meta/%s", AlbumVRResult.class, "albumInfoVR");
    public static final ITVApiServer<DeviceCheckVRResult> deviceCheck = new b(new a("http://api.vr.ptqy.gitv.tv/ivr/reg"), DeviceCheckVRResult.class, "devRegisterVR", true);
    public static final ITVApiServer<ApiResult> playCheck = a("http://api.vr.ptqy.gitv.tv/ivr/playcheck/%s", ApiResult.class, "playCheckVR");
    public static final ITVApiServer<SysTimeVRResult> sysTime = a("http://api.vr.ptqy.gitv.tv/ivr/systime", SysTimeVRResult.class, "systemTimeVR");

    static class a implements a {
        private String a;

        public a(String str) {
            this.a = str;
        }

        public final String a(String... strArr) {
            return this.a;
        }

        public final List<String> m95a() {
            List<String> linkedList = new LinkedList();
            linkedList.add("mac:" + TVApiBase.getTVApiProperty().getMacAddress());
            return linkedList;
        }

        public final String a() {
            TVApiProperty tVApiProperty = TVApiBase.getTVApiProperty();
            return "devId=" + VRApi.a() + "&version=" + tVApiProperty.getVersion() + "&uuid=" + tVApiProperty.getUUID();
        }
    }

    static class b implements a {
        private String a;

        public b(String str) {
            this.a = str;
        }

        public final String a(String... strArr) {
            return VRApi.b(this.a, strArr);
        }

        public final List<String> m96a() {
            List<String> linkedList = new LinkedList();
            linkedList.add("apiKey:" + VRApi.getApiKey());
            return linkedList;
        }

        public final String a() {
            return null;
        }
    }

    private static String b(String str, String... strArr) {
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

    private static final <T extends ApiResult> ITVApiServer<T> a(String str, Class<T> cls, String str2) {
        return new b(new b(str), cls, str2, false);
    }

    public static void setApiKey(String apiKey) {
        a = apiKey;
    }

    public static String getApiKey() {
        return a;
    }

    static /* synthetic */ String a() {
        String passportDeviceId = TVApiBase.getTVApiProperty().getPassportDeviceId();
        return !com.gala.tvapi.b.a.a(passportDeviceId) ? passportDeviceId.replace("tv_", "vr_") : passportDeviceId;
    }
}
