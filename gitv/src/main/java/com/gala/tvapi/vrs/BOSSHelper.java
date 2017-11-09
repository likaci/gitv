package com.gala.tvapi.vrs;

import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.property.TVApiProperty;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.vrs.a.k;
import com.gala.tvapi.vrs.core.IVrsServer;
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
    private static k a = new k();
    public static IVrsServer<ApiResultAuthVipVideo> authVipLive = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.A), a, ApiResultAuthVipVideo.class, "authVipLive", false, true);
    public static final IVrsServer<ApiResultAuthVipVideo> authVipLiveProgram = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.b.c(com.gala.tvapi.vrs.core.a.y), a, ApiResultAuthVipVideo.class, "authVipLive", false, true);
    public static final IVrsServer<ApiResultAuthVipVideo> authVipPartner = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.b.d(com.gala.tvapi.vrs.core.a.y), a, ApiResultAuthVipVideo.class, "authVipPartner", false, true);
    public static final IVrsServer<ApiResultAuthVipVideo> authVipVideo = com.gala.tvapi.b.a.a(new e(com.gala.tvapi.vrs.core.a.x), new k(), ApiResultAuthVipVideo.class, "authVip", false, true);
    public static final IVrsServer<ApiResultAuthVipVideo> authVipVideo_HaiXin = com.gala.tvapi.b.a.a(new e("http://hx.api.vip.ptqy.gitv.tv/spservices/ck.action?aid=%s&cid=afbe8fd3d73448c9&vid=%s&ut=%s&utt=%s&uuid=%s&platform=%s&version=1.0&v=%s&P00001=%s&deviceId=%s"), a, ApiResultAuthVipVideo.class, "authVip_haixin", false, true);
    public static final IVrsServer<ApiResultAuthVipVideo> authVipVideo_Huawei = com.gala.tvapi.b.a.a(new e(com.gala.tvapi.vrs.core.a.bR), a, ApiResultAuthVipVideo.class, "authVipVideo_Huawei", false, true);
    public static final IVrsServer<ApiResultActivationCodeInfo> buyProductByActivationCode = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.bf), ApiResultActivationCodeInfo.class, "exp_pay", false);
    public static final IVrsServer<ApiResultActivationCodeInfo> buyProductByActivationCodeOTT = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.bg), ApiResultActivationCodeInfo.class, "exp_pay", false);
    public static final IVrsServer<ApiResultCode> checkActivationCode = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.bh), ApiResultCode.class, "exp_check", false);
    public static final IVrsServer<ApiResultCode> checkTVQueryGiftVip = com.gala.tvapi.b.a.a(new c("http://openapi.vip.ptqy.gitv.tv/act/tvQueryGiftVip.action?P00001=%s&platform=%s"), a, ApiResultCode.class, "checkTVQueryGiftVip", false, true);
    public static final IVrsServer<ApiResultAuthCookie> getAuthCookie = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a("http://openapi.vip.ptqy.gitv.tv/partner/authToken.action?P00001=%s"), ApiResultAuthCookie.class, "getAuthCookie", false);
    public static final IVrsServer<ApiResultProductRecommend> getProductRecommend = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.bS), a, ApiResultProductRecommend.class, "getProductRecommend", false, true);
    public static final IVrsServer<ApiResultVipPackage> getVipPackage = com.gala.tvapi.b.a.a(new d(com.gala.tvapi.vrs.core.a.z), a, ApiResultVipPackage.class, "getVipPackage", false, true);
    public static final IVrsServer<ApiResultPaySDKInfo> paySDK_Huawei = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.bT), a, ApiResultPaySDKInfo.class, "paySDK_Huawei", false, true);
    public static final IVrsServer<ApiResultVodInfo> queryVodInfo = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.bE), ApiResultVodInfo.class, "queryVodInfo", false);
    public static final IVrsServer<ApiResultCode> useVodCoupon = com.gala.tvapi.b.a.a(new com.gala.tvapi.vrs.BaseHelper.a(com.gala.tvapi.vrs.core.a.bG), ApiResultCode.class, "useVodCoupon", false);

    public static class a implements IApiUrlBuilder {
        private String a;

        public a(String str) {
            this.a = str;
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
            com.gala.tvapi.b.b a = com.gala.tvapi.b.c.a(tVApiProperty.getPlatform());
            r0[5] = a.c();
            r0[6] = BaseHelper.a(com.gala.tvapi.b.a.a(params[0] + "_afbe8fd3d73448c9_" + valueOf + "_" + a.b()));
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
            TVApiTool tVApiTool = BaseHelper.a;
            return String.format(TVApiTool.parseLicenceUrl(this.a), (Object[]) r0);
        }

        public final List<String> header() {
            return null;
        }
    }

    public static class b implements IApiUrlBuilder {
        private String a = null;

        public b(String str) {
            this.a = str;
        }

        public final String build(String... params) {
            if (params == null || params.length < 2) {
                TVApiTool tVApiTool = BaseHelper.a;
                return TVApiTool.parseLicenceUrl(this.a);
            }
            String[] strArr = new String[(params.length + 3)];
            strArr[0] = BaseHelper.a(params[0]);
            strArr[1] = BaseHelper.a(params[1]);
            PlatformType platform = TVApiBase.getTVApiProperty().getPlatform();
            com.gala.tvapi.b.b a = com.gala.tvapi.b.c.a(platform);
            strArr[2] = platform == PlatformType.TAIWAN ? "tw" : "cn";
            strArr[3] = a.c();
            strArr[4] = TVApiBase.getTVApiProperty().getVersion();
            TVApiTool tVApiTool2 = BaseHelper.a;
            return String.format(TVApiTool.parseLicenceUrl(this.a), (Object[]) strArr);
        }

        public final List<String> header() {
            return null;
        }
    }

    public static class c implements IApiUrlBuilder {
        private String a = null;

        public c(String str) {
            this.a = str;
        }

        public final String build(String... params) {
            this.a = TVApiTool.parseLicenceUrl(this.a);
            if (params == null || params.length != 1) {
                return this.a;
            }
            String[] strArr = new String[(params.length + 1)];
            strArr[0] = params[0];
            strArr[1] = com.gala.tvapi.b.c.a(TVApiBase.getTVApiProperty().getPlatform()).c();
            TVApiTool tVApiTool = BaseHelper.a;
            return String.format(TVApiTool.parseLicenceUrl(this.a), (Object[]) strArr);
        }

        public final List<String> header() {
            return null;
        }
    }

    public static class d implements IApiUrlBuilder {
        private String a = null;

        public d(String str) {
            this.a = str;
        }

        public final String build(String... params) {
            if (params == null || params.length < 2) {
                TVApiTool tVApiTool = BaseHelper.a;
                return TVApiTool.parseLicenceUrl(this.a);
            }
            String[] strArr = new String[(params.length + 1)];
            strArr[0] = BaseHelper.a(params[0]);
            strArr[1] = BaseHelper.a(params[1]);
            com.gala.tvapi.b.b a = com.gala.tvapi.b.c.a(TVApiBase.getTVApiProperty().getPlatform());
            String str = "04022001010000000000";
            if (a != null) {
                str = a.c();
            }
            strArr[2] = str;
            TVApiTool tVApiTool2 = BaseHelper.a;
            return String.format(TVApiTool.parseLicenceUrl(this.a), (Object[]) strArr);
        }

        public final List<String> header() {
            return null;
        }
    }

    public static class e implements IApiUrlBuilder {
        private String a = null;

        public e(String str) {
            this.a = str;
        }

        public final String build(String... params) {
            return BOSSHelper.a(this.a, params);
        }

        public final List<String> header() {
            return null;
        }
    }

    protected static String a(String str, String... strArr) {
        if (strArr == null || strArr.length < 5) {
            return TVApiTool.parseLicenceUrl(str);
        }
        String str2;
        com.gala.tvapi.b.b a = com.gala.tvapi.b.c.a(TVApiBase.getTVApiProperty().getPlatform());
        String str3 = "2391461978";
        String str4 = "04022001010000000000";
        if (a != null) {
            str = a.d();
            str3 = a.b();
            str4 = a.c();
            str2 = str3;
            str3 = str4;
        } else {
            str2 = str3;
            str3 = str4;
        }
        String[] strArr2 = new String[(strArr.length + 4)];
        strArr2[0] = BaseHelper.a(strArr[0]);
        strArr2[1] = BaseHelper.a(strArr[1]);
        long currentTimeMillis = System.currentTimeMillis();
        strArr2[2] = BaseHelper.a(String.valueOf(currentTimeMillis));
        strArr2[3] = BaseHelper.a(String.valueOf(((((int) (currentTimeMillis % 1000)) * ((int) (((double) currentTimeMillis) / Math.pow(10.0d, (double) (String.valueOf(currentTimeMillis).length() - 2))))) + 100) + Integer.valueOf(strArr[2]).intValue()));
        strArr2[4] = BaseHelper.a(strArr[3]);
        str2 = strArr2[0] + "_afbe8fd3d73448c9_" + strArr2[1] + "_" + strArr2[2] + "_" + strArr2[3] + "_" + str2;
        strArr2[5] = str3;
        strArr2[6] = BaseHelper.a(com.gala.tvapi.b.a.a(str2));
        strArr2[7] = BaseHelper.a(strArr[4]);
        strArr2[8] = BaseHelper.a(TVApiBase.getTVApiProperty().getPassportDeviceId());
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr2);
    }

    public static final String getVerificationCode(int width, int height, String cookie) {
        TVApiBase.getTVApiTool();
        return String.format(TVApiTool.parseLicenceUrl(com.gala.tvapi.vrs.core.a.bi), new Object[]{String.valueOf(width), String.valueOf(height), cookie});
    }

    public static String getTvodImg(String width, String cookie, String url) {
        try {
            return new k().a(String.format(com.gala.tvapi.vrs.core.a.bF, new Object[]{width, cookie, URLEncoder.encode(url, XML.CHARSET_UTF8)}));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
