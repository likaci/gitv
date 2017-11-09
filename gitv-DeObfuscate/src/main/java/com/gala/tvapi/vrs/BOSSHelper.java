package com.gala.tvapi.vrs;

import com.gala.tvapi.p008b.C0213b;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.p008b.C0218c;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.property.TVApiProperty;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.vrs.BaseHelper.C0328a;
import com.gala.tvapi.vrs.core.C0365a;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.p031a.C0336k;
import com.gala.tvapi.vrs.p032b.C0358c;
import com.gala.tvapi.vrs.p032b.C0359d;
import com.gala.tvapi.vrs.result.ApiResultActivationCodeInfo;
import com.gala.tvapi.vrs.result.ApiResultAuthCookie;
import com.gala.tvapi.vrs.result.ApiResultAuthVipVideo;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.tvapi.vrs.result.ApiResultPaySDKInfo;
import com.gala.tvapi.vrs.result.ApiResultProductRecommend;
import com.gala.tvapi.vrs.result.ApiResultVipPackage;
import com.gala.tvapi.vrs.result.ApiResultVodInfo;
import com.gala.video.api.IApiUrlBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import org.cybergarage.xml.XML;

public class BOSSHelper extends BaseHelper {
    private static C0336k f1196a = new C0336k();
    public static IVrsServer<ApiResultAuthVipVideo> authVipLive = C0214a.m581a(new C0323a(C0365a.f1231A), f1196a, ApiResultAuthVipVideo.class, "authVipLive", false, true);
    public static final IVrsServer<ApiResultAuthVipVideo> authVipLiveProgram = C0214a.m581a(new C0358c(C0365a.f1258y), f1196a, ApiResultAuthVipVideo.class, "authVipLive", false, true);
    public static final IVrsServer<ApiResultAuthVipVideo> authVipPartner = C0214a.m581a(new C0359d(C0365a.f1258y), f1196a, ApiResultAuthVipVideo.class, "authVipPartner", false, true);
    public static final IVrsServer<ApiResultAuthVipVideo> authVipVideo = C0214a.m581a(new C0327e(C0365a.f1257x), new C0336k(), ApiResultAuthVipVideo.class, "authVip", false, true);
    public static final IVrsServer<ApiResultAuthVipVideo> authVipVideo_HaiXin = C0214a.m581a(new C0327e("http://hx.api.vip.ptqy.gitv.tv/spservices/ck.action?aid=%s&cid=afbe8fd3d73448c9&vid=%s&ut=%s&utt=%s&uuid=%s&platform=%s&version=1.0&v=%s&P00001=%s&deviceId=%s"), f1196a, ApiResultAuthVipVideo.class, "authVip_haixin", false, true);
    public static final IVrsServer<ApiResultAuthVipVideo> authVipVideo_Huawei = C0214a.m581a(new C0327e(C0365a.bR), f1196a, ApiResultAuthVipVideo.class, "authVipVideo_Huawei", false, true);
    public static final IVrsServer<ApiResultActivationCodeInfo> buyProductByActivationCode = C0214a.m586a(new C0328a(C0365a.bf), ApiResultActivationCodeInfo.class, "exp_pay", false);
    public static final IVrsServer<ApiResultActivationCodeInfo> buyProductByActivationCodeOTT = C0214a.m586a(new C0328a(C0365a.bg), ApiResultActivationCodeInfo.class, "exp_pay", false);
    public static final IVrsServer<ApiResultCode> checkActivationCode = C0214a.m586a(new C0324b(C0365a.bh), ApiResultCode.class, "exp_check", false);
    public static final IVrsServer<ApiResultCode> checkTVQueryGiftVip = C0214a.m581a(new C0325c("http://openapi.vip.ptqy.gitv.tv/act/tvQueryGiftVip.action?P00001=%s&platform=%s"), f1196a, ApiResultCode.class, "checkTVQueryGiftVip", false, true);
    public static final IVrsServer<ApiResultAuthCookie> getAuthCookie = C0214a.m586a(new C0328a("http://openapi.vip.ptqy.gitv.tv/partner/authToken.action?P00001=%s"), ApiResultAuthCookie.class, "getAuthCookie", false);
    public static final IVrsServer<ApiResultProductRecommend> getProductRecommend = C0214a.m581a(new C0328a(C0365a.bS), f1196a, ApiResultProductRecommend.class, "getProductRecommend", false, true);
    public static final IVrsServer<ApiResultVipPackage> getVipPackage = C0214a.m581a(new C0326d(C0365a.f1259z), f1196a, ApiResultVipPackage.class, "getVipPackage", false, true);
    public static final IVrsServer<ApiResultPaySDKInfo> paySDK_Huawei = C0214a.m581a(new C0328a(C0365a.bT), f1196a, ApiResultPaySDKInfo.class, "paySDK_Huawei", false, true);
    public static final IVrsServer<ApiResultVodInfo> queryVodInfo = C0214a.m586a(new C0328a(C0365a.bE), ApiResultVodInfo.class, "queryVodInfo", false);
    public static final IVrsServer<ApiResultCode> useVodCoupon = C0214a.m586a(new C0328a(C0365a.bG), ApiResultCode.class, "useVodCoupon", false);

    public static class C0323a implements IApiUrlBuilder {
        private String f1190a;

        public C0323a(String str) {
            this.f1190a = str;
        }

        public final String build(String... params) {
            if (params == null || params.length != 14) {
                return null;
            }
            TVApiProperty tVApiProperty = TVApiBase.getTVApiProperty();
            r0 = new String[18];
            String valueOf = String.valueOf(System.currentTimeMillis());
            r0[1] = valueOf;
            r0[2] = tVApiProperty.getPassportDeviceId();
            r0[3] = params[1];
            r0[4] = params[2];
            C0213b a = C0218c.m605a(tVApiProperty.getPlatform());
            r0[5] = a.mo831c();
            r0[6] = BaseHelper.m756a(C0214a.m580a(params[0] + "_afbe8fd3d73448c9_" + valueOf + "_" + a.mo830b()));
            r0[7] = params[3];
            r0[8] = params[4];
            r0[9] = params[5];
            r0[10] = params[6];
            r0[11] = params[7];
            r0[12] = params[8];
            r0[13] = params[9];
            r0[14] = params[10];
            r0[15] = params[11];
            r0[16] = params[12];
            r0[17] = params[13];
            TVApiTool tVApiTool = BaseHelper.f1195a;
            return String.format(TVApiTool.parseLicenceUrl(this.f1190a), (Object[]) r0);
        }

        public final List<String> header() {
            return null;
        }
    }

    public static class C0324b implements IApiUrlBuilder {
        private String f1191a = null;

        public C0324b(String str) {
            this.f1191a = str;
        }

        public final String build(String... params) {
            if (params == null || params.length < 2) {
                TVApiTool tVApiTool = BaseHelper.f1195a;
                return TVApiTool.parseLicenceUrl(this.f1191a);
            }
            String[] strArr = new String[(params.length + 3)];
            strArr[0] = BaseHelper.m756a(params[0]);
            strArr[1] = BaseHelper.m756a(params[1]);
            PlatformType platform = TVApiBase.getTVApiProperty().getPlatform();
            C0213b a = C0218c.m605a(platform);
            strArr[2] = platform == PlatformType.TAIWAN ? "tw" : "cn";
            strArr[3] = a.mo831c();
            strArr[4] = TVApiBase.getTVApiProperty().getVersion();
            TVApiTool tVApiTool2 = BaseHelper.f1195a;
            return String.format(TVApiTool.parseLicenceUrl(this.f1191a), (Object[]) strArr);
        }

        public final List<String> header() {
            return null;
        }
    }

    public static class C0325c implements IApiUrlBuilder {
        private String f1192a = null;

        public C0325c(String str) {
            this.f1192a = str;
        }

        public final String build(String... params) {
            this.f1192a = TVApiTool.parseLicenceUrl(this.f1192a);
            if (params == null || params.length != 1) {
                return this.f1192a;
            }
            String[] strArr = new String[(params.length + 1)];
            strArr[0] = params[0];
            strArr[1] = C0218c.m605a(TVApiBase.getTVApiProperty().getPlatform()).mo831c();
            TVApiTool tVApiTool = BaseHelper.f1195a;
            return String.format(TVApiTool.parseLicenceUrl(this.f1192a), (Object[]) strArr);
        }

        public final List<String> header() {
            return null;
        }
    }

    public static class C0326d implements IApiUrlBuilder {
        private String f1193a = null;

        public C0326d(String str) {
            this.f1193a = str;
        }

        public final String build(String... params) {
            if (params == null || params.length < 2) {
                TVApiTool tVApiTool = BaseHelper.f1195a;
                return TVApiTool.parseLicenceUrl(this.f1193a);
            }
            String[] strArr = new String[(params.length + 1)];
            strArr[0] = BaseHelper.m756a(params[0]);
            strArr[1] = BaseHelper.m756a(params[1]);
            C0213b a = C0218c.m605a(TVApiBase.getTVApiProperty().getPlatform());
            String str = "04022001010000000000";
            if (a != null) {
                str = a.mo831c();
            }
            strArr[2] = str;
            TVApiTool tVApiTool2 = BaseHelper.f1195a;
            return String.format(TVApiTool.parseLicenceUrl(this.f1193a), (Object[]) strArr);
        }

        public final List<String> header() {
            return null;
        }
    }

    public static class C0327e implements IApiUrlBuilder {
        private String f1194a = null;

        public C0327e(String str) {
            this.f1194a = str;
        }

        public final String build(String... params) {
            return BOSSHelper.m758a(this.f1194a, params);
        }

        public final List<String> header() {
            return null;
        }
    }

    protected static String m758a(String str, String... strArr) {
        if (strArr == null || strArr.length < 5) {
            return TVApiTool.parseLicenceUrl(str);
        }
        String str2;
        C0213b a = C0218c.m605a(TVApiBase.getTVApiProperty().getPlatform());
        String str3 = "2391461978";
        String str4 = "04022001010000000000";
        if (a != null) {
            str = a.mo832d();
            str3 = a.mo830b();
            str4 = a.mo831c();
            str2 = str3;
            str3 = str4;
        } else {
            str2 = str3;
            str3 = str4;
        }
        String[] strArr2 = new String[(strArr.length + 4)];
        strArr2[0] = BaseHelper.m756a(strArr[0]);
        strArr2[1] = BaseHelper.m756a(strArr[1]);
        long currentTimeMillis = System.currentTimeMillis();
        strArr2[2] = BaseHelper.m756a(String.valueOf(currentTimeMillis));
        strArr2[3] = BaseHelper.m756a(String.valueOf(((((int) (currentTimeMillis % 1000)) * ((int) (((double) currentTimeMillis) / Math.pow(10.0d, (double) (String.valueOf(currentTimeMillis).length() - 2))))) + 100) + Integer.valueOf(strArr[2]).intValue()));
        strArr2[4] = BaseHelper.m756a(strArr[3]);
        str2 = strArr2[0] + "_afbe8fd3d73448c9_" + strArr2[1] + "_" + strArr2[2] + "_" + strArr2[3] + "_" + str2;
        strArr2[5] = str3;
        strArr2[6] = BaseHelper.m756a(C0214a.m580a(str2));
        strArr2[7] = BaseHelper.m756a(strArr[4]);
        strArr2[8] = BaseHelper.m756a(TVApiBase.getTVApiProperty().getPassportDeviceId());
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr2);
    }

    public static final String getVerificationCode(int width, int height, String cookie) {
        TVApiBase.getTVApiTool();
        return String.format(TVApiTool.parseLicenceUrl(C0365a.bi), new Object[]{String.valueOf(width), String.valueOf(height), cookie});
    }

    public static String getTvodImg(String width, String cookie, String url) {
        try {
            return new C0336k().mo852a(String.format(C0365a.bF, new Object[]{width, cookie, URLEncoder.encode(url, XML.CHARSET_UTF8)}));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
