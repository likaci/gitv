package com.gala.tvapi.vrs;

import android.os.Build;
import com.gala.tvapi.b.c;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PartnerLoginType;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.type.QuickMarkType;
import com.gala.tvapi.vrs.a.k;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.core.f;
import com.gala.tvapi.vrs.result.ApiResultAuthCookiePhoneCode;
import com.gala.tvapi.vrs.result.ApiResultCheckAccountRegister;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.tvapi.vrs.result.ApiResultData;
import com.gala.tvapi.vrs.result.ApiResultPartnerLogin;
import com.gala.tvapi.vrs.result.ApiResultQuickLogin;
import com.gala.tvapi.vrs.result.ApiResultShorten;
import com.gala.tvapi.vrs.result.ApiResultUserIconList;
import com.gala.tvapi.vrs.result.ApiResultUserInfo;
import com.gala.video.api.IApiUrlBuilder;
import java.util.List;
import org.cybergarage.upnp.IconList;

public class PassportTVHelper extends BaseHelper {
    public static final IVrsServer<ApiResultUserInfo> bfLogin = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bA), ApiResultUserInfo.class, "partnerBfLogin", false);
    public static final IVrsServer<ApiResultCheckAccountRegister> checkAccountRegister = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.bl), ApiResultCheckAccountRegister.class, "checkAccount", false);
    public static final IVrsServer<ApiResultCode> checkPhoneScan = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.aY), ApiResultCode.class, "optIsLoginRequested", false);
    public static final IVrsServer<ApiResultCode> checkSendPhoneCode = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.bm), ApiResultCode.class, "sendCellphoneAuthcode", false);
    public static final IVrsServer<ApiResultCode> checkSendPhoneCodeWithVCode = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bn), ApiResultCode.class, "sendCellphoneAuthcode", false);
    public static final IVrsServer<ApiResultUserInfo> checkTVConfirmLogin = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.aZ), ApiResultUserInfo.class, "optIsLoginConfirmed", false);
    public static final IVrsServer<ApiResultUserInfo> checkTVLogin = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.aV), ApiResultUserInfo.class, "isTokenLogin", false);
    public static final IVrsServer<ApiResultCode> confirmPhoneLogin = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.ba), ApiResultCode.class, "optLoginConfirm", false);
    public static final IVrsServer<ApiResultCode> confirmTVLogin = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.aW), ApiResultCode.class, "tokenLoginConfirm", false);
    public static final IVrsServer<ApiResultQuickLogin> getPhoneLoginToken = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.aX), ApiResultQuickLogin.class, "optGenLoginToken", false);
    public static final IVrsServer<ApiResultShorten> getShortenUrl = com.gala.tvapi.b.a.a(new b("http://71.am/apis/shorten?authKey=a5deb9684ab8f8fb26eb97cc86f0778a&clientId=IDD_Login&url=%s"), new k(), ApiResultShorten.class, "shortenUrl", false, true);
    public static final IVrsServer<ApiResultQuickLogin> getTVLoginToken = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.aU), ApiResultQuickLogin.class, "genLoginToken", false);
    public static final IVrsServer<ApiResultUserInfo> loginWithCode = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bx), ApiResultUserInfo.class, "tvLogin", false);
    public static final IVrsServer<ApiResultCode> loginWithCookie = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.bc), ApiResultCode.class, "authlogin", false);
    public static final IVrsServer<ApiResultUserInfo> loginWithEMail = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bj), ApiResultUserInfo.class, "emailLogin", false);
    public static final IVrsServer<ApiResultUserInfo> login_MX = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bC), ApiResultUserInfo.class, "login_MX", false);
    public static final IVrsServer<ApiResultCode> logout = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.bk), ApiResultCode.class, "logout", false);
    public static final IVrsServer<ApiResultPartnerLogin> partnerLogin = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bB), ApiResultPartnerLogin.class, "partnerLogin", false);
    public static final IVrsServer<ApiResultUserInfo> partyLogin = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.bz), ApiResultUserInfo.class, "thirdpartyThirdSsoLogin", false);
    public static final IVrsServer<ApiResultCode> registerByPhone = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bq), ApiResultCode.class, "cellphoneRegister", false);
    public static final IVrsServer<ApiResultAuthCookiePhoneCode> registerByPhoneCode = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bp), ApiResultAuthCookiePhoneCode.class, "registerByPhoneCode", false);
    public static final IVrsServer<ApiResultUserInfo> registerByPhoneNew = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bs), ApiResultUserInfo.class, "cellphoneRegister", false);
    public static final IVrsServer<ApiResultUserInfo> registerEMailByVCode = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.br), ApiResultUserInfo.class, "emailRegister", false);
    public static final IVrsServer<ApiResultData> renew_authcookie = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bb), ApiResultData.class, "renewAuthcookie", true);
    public static final IVrsServer<ApiResultCode> sendPhoneCode = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.bo), ApiResultCode.class, "SendPhoneCode", false);
    public static final IVrsServer<ApiResultUserInfo> subaccountRegisterAndLogin = com.gala.tvapi.b.a.a(new a(com.gala.tvapi.vrs.core.a.by), ApiResultUserInfo.class, "subaccountLogin", false);
    public static final IVrsServer<ApiResultUserInfo> thirdPartyLogin = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.bv), ApiResultUserInfo.class, "thirdpartySaveAuthToken", false);
    public static final IVrsServer<ApiResultUserIconList> userIconList = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.bu), ApiResultUserIconList.class, IconList.ELEM_NAME, false);
    public static final IVrsServer<ApiResultUserInfo> userInfo = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.bd), ApiResultUserInfo.class, "userInfo", false);
    public static final IVrsServer<ApiResultUserInfo> userInfo_agenttype = com.gala.tvapi.b.a.a(new b(com.gala.tvapi.vrs.core.a.be), ApiResultUserInfo.class, "userInfo_agenttype", false);

    public static final class a implements IApiUrlBuilder {
        private String a = null;

        public a(String str) {
            this.a = str;
        }

        public final String build(String... params) {
            if (params != null && params.length > 0) {
                String passportDeviceId = TVApiBase.getTVApiProperty().getPassportDeviceId();
                PlatformType platform = TVApiBase.getTVApiProperty().getPlatform();
                String e = c.a(platform).e();
                String passportDeviceId2;
                if (this.a.contains("partner/login.action") && params.length == 3) {
                    PartnerLoginType a = c.a(platform).a();
                    String c = c.a(platform).c();
                    if (params[2].equals("")) {
                        passportDeviceId2 = TVApiBase.getTVApiProperty().getPassportDeviceId();
                    } else {
                        passportDeviceId2 = params[2];
                    }
                    String[] strArr = new String[9];
                    if (a == PartnerLoginType.GALA) {
                        strArr[0] = params[0];
                        strArr[1] = "";
                        strArr[2] = "";
                        strArr[3] = e;
                        strArr[4] = "";
                        strArr[5] = params[1];
                        strArr[6] = passportDeviceId2;
                        strArr[7] = c;
                        strArr[8] = f.a(strArr);
                    } else if (a == PartnerLoginType.OPENID) {
                        strArr[0] = "";
                        strArr[1] = params[0];
                        strArr[2] = "";
                        strArr[3] = e;
                        strArr[4] = "";
                        strArr[5] = params[1];
                        strArr[6] = passportDeviceId2;
                        strArr[7] = c;
                        strArr[8] = f.a(strArr);
                    } else if (a == PartnerLoginType.ACCESS_TOKEN) {
                        strArr[0] = "";
                        strArr[1] = "";
                        strArr[2] = params[0];
                        strArr[3] = e;
                        strArr[4] = platform.getType();
                        strArr[5] = params[1];
                        strArr[6] = passportDeviceId2;
                        strArr[7] = c;
                        strArr[8] = f.a(strArr);
                    }
                    return BaseHelper.b(this.a, strArr);
                } else if (this.a.contains("user/mobile/login.action") && params.length == 3) {
                    passportDeviceId2 = f.b(params[0], params[1], "", params[2], "", Build.HARDWARE.toString(), passportDeviceId, e);
                    return BaseHelper.b(this.a, params[0], params[1], e, passportDeviceId2, params[2], Build.HARDWARE.toString(), passportDeviceId, Build.MODEL.toString());
                } else if (this.a.contains("phone/cellphone_reg.action") && params.length == 4) {
                    return BaseHelper.b(this.a, params[0], params[1], params[2], e, params[3], Build.HARDWARE.toString(), passportDeviceId);
                } else if (this.a.contains("reglogin/tv_cellphone_reg.action") && params.length == 4) {
                    passportDeviceId2 = f.g(params[2], e);
                    return BaseHelper.b(this.a, params[0], params[1], passportDeviceId2, e, params[3], Build.HARDWARE.toString());
                } else if (this.a.contains("user/mobile/sregister.action") && params.length == 5) {
                    passportDeviceId2 = f.a(params[0], params[1], passportDeviceId, params[2], "", params[3], params[4], passportDeviceId, e);
                    return BaseHelper.b(this.a, params[0], params[1], e, passportDeviceId2, f.a(params[2]), params[2], params[3], params[4], Build.HARDWARE.toString(), passportDeviceId);
                } else if (this.a.contains("reglogin/tv_login.action") && params.length == 5) {
                    passportDeviceId2 = f.g(params[1], e);
                    return BaseHelper.b(this.a, params[0], passportDeviceId2, params[2], e, f.a(params[3]), passportDeviceId, Build.MODEL.toString(), params[3], params[4], Build.HARDWARE.toString(), "userinfo,vip_info,tv_vip_info,insecure_account");
                } else if (this.a.contains("subaccount/login.action")) {
                    passportDeviceId2 = f.b(params[0], params[1], params[2]);
                    return BaseHelper.b(this.a, params[0], params[1], params[2], passportDeviceId2, TVApiBase.getTVApiProperty().getPassportDeviceId());
                } else if (this.a.contains("partner/bf_login.action") && params.length == 1) {
                    String h = f.h(f.g("bf_" + params[0], e), e);
                    return BaseHelper.b(this.a, passportDeviceId2, e, h, TVApiBase.getTVApiProperty().getPassportDeviceId());
                } else if (this.a.contains("phone/send_cellphone_authcode_vcode.action") && params.length == 4) {
                    return BaseHelper.b(this.a, params[0], params[1], params[2], f.a(params[3]));
                } else if (this.a.contains("reglogin/cellphone_authcode_login.action") && params.length == 4) {
                    return BaseHelper.b(this.a, params[0], params[1], params[2], "5", params[3], r1.a(), e, passportDeviceId, f.b(r2));
                } else if (this.a.contains("phone/secure_send_cellphone_authcode.action") && params.length == 4) {
                    return BaseHelper.b(this.a, params[0], f.g(params[1], e), "5", params[2], e, f.a(TVApiBase.getTVApiProperty().getMacAddress()), r1.a(), params[3], f.c(r2));
                } else if (this.a.contains("xm_sso.action") && params.length == 2) {
                    return BaseHelper.b(this.a, e, params[0], params[1], passportDeviceId);
                } else if (this.a.contains("renew_authcookie.action") && params.length == 3) {
                    return BaseHelper.b(this.a, params[0], params[1], params[2], passportDeviceId, e, r1.a());
                }
            }
            TVApiTool tVApiTool = BaseHelper.a;
            return TVApiTool.parseLicenceUrl(this.a);
        }

        public final List<String> header() {
            return null;
        }
    }

    static final class b implements IApiUrlBuilder {
        private String a = null;

        public b(String str) {
            this.a = str;
        }

        public final String build(String... params) {
            if (params == null || params.length <= 0) {
                TVApiTool tVApiTool = BaseHelper.a;
                return TVApiTool.parseLicenceUrl(this.a);
            }
            String[] strArr = new String[(params.length + 1)];
            for (int i = 0; i < params.length; i++) {
                strArr[i] = params[i];
            }
            strArr[params.length] = TVApiBase.getTVApiProperty().getPassportDeviceId();
            String b = BaseHelper.b(this.a, strArr);
            if (TVApiBase.getTVApiProperty().getPlatform() != PlatformType.NORMAL) {
                return b.replace("agenttype=28", "agenttype=" + c.a(TVApiBase.getTVApiProperty().getPlatform()).e());
            }
            return b;
        }

        public final List<String> header() {
            return null;
        }
    }

    public static final String getRegisterEMailVCode(int height, int width, String mac) {
        try {
            f.a(mac);
            String format = String.format(com.gala.tvapi.vrs.core.a.bt, new Object[]{String.valueOf(height), String.valueOf(width), c.a(TVApiBase.getTVApiProperty().getPlatform()).e(), f.a(mac), TVApiBase.getTVApiProperty().getPassportDeviceId()});
            com.gala.tvapi.tv2.c.c cVar = new com.gala.tvapi.tv2.c.c();
            return com.gala.tvapi.tv2.c.c.b(format);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final String getBroadcastQuickMark(QuickMarkType type, String cookie, int width, String uuid, String albumId, String code) {
        String valueOf = String.valueOf(System.currentTimeMillis());
        valueOf = valueOf.substring(0, valueOf.length() - 2);
        com.gala.tvapi.log.a.a("passport", "fc = " + type.toString());
        valueOf = String.format(com.gala.tvapi.vrs.core.a.bw, new Object[]{code, type.toString(), String.valueOf(width), valueOf, f.f(valueOf, code), uuid, cookie});
        if (albumId == null || albumId.equals("")) {
            valueOf = valueOf + "&qyid=" + TVApiBase.getTVApiProperty().getPassportDeviceId();
        } else {
            valueOf = valueOf + "&aid=" + albumId + "&qyid=" + TVApiBase.getTVApiProperty().getPassportDeviceId();
        }
        com.gala.tvapi.log.a.a("passport", valueOf);
        return valueOf;
    }
}
