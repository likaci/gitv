package com.gala.tvapi.vrs;

import android.os.Build;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.p008b.C0218c;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.p026c.C0289c;
import com.gala.tvapi.type.PartnerLoginType;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.type.QuickMarkType;
import com.gala.tvapi.vrs.core.C0365a;
import com.gala.tvapi.vrs.core.C0376f;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.p031a.C0336k;
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
    public static final IVrsServer<ApiResultUserInfo> bfLogin = C0214a.m586a(new C0330a(C0365a.bA), ApiResultUserInfo.class, "partnerBfLogin", false);
    public static final IVrsServer<ApiResultCheckAccountRegister> checkAccountRegister = C0214a.m586a(new C0331b(C0365a.bl), ApiResultCheckAccountRegister.class, "checkAccount", false);
    public static final IVrsServer<ApiResultCode> checkPhoneScan = C0214a.m586a(new C0331b(C0365a.aY), ApiResultCode.class, "optIsLoginRequested", false);
    public static final IVrsServer<ApiResultCode> checkSendPhoneCode = C0214a.m586a(new C0331b(C0365a.bm), ApiResultCode.class, "sendCellphoneAuthcode", false);
    public static final IVrsServer<ApiResultCode> checkSendPhoneCodeWithVCode = C0214a.m586a(new C0330a(C0365a.bn), ApiResultCode.class, "sendCellphoneAuthcode", false);
    public static final IVrsServer<ApiResultUserInfo> checkTVConfirmLogin = C0214a.m586a(new C0331b(C0365a.aZ), ApiResultUserInfo.class, "optIsLoginConfirmed", false);
    public static final IVrsServer<ApiResultUserInfo> checkTVLogin = C0214a.m586a(new C0331b(C0365a.aV), ApiResultUserInfo.class, "isTokenLogin", false);
    public static final IVrsServer<ApiResultCode> confirmPhoneLogin = C0214a.m586a(new C0331b(C0365a.ba), ApiResultCode.class, "optLoginConfirm", false);
    public static final IVrsServer<ApiResultCode> confirmTVLogin = C0214a.m586a(new C0331b(C0365a.aW), ApiResultCode.class, "tokenLoginConfirm", false);
    public static final IVrsServer<ApiResultQuickLogin> getPhoneLoginToken = C0214a.m586a(new C0331b(C0365a.aX), ApiResultQuickLogin.class, "optGenLoginToken", false);
    public static final IVrsServer<ApiResultShorten> getShortenUrl = C0214a.m581a(new C0331b("http://71.am/apis/shorten?authKey=a5deb9684ab8f8fb26eb97cc86f0778a&clientId=IDD_Login&url=%s"), new C0336k(), ApiResultShorten.class, "shortenUrl", false, true);
    public static final IVrsServer<ApiResultQuickLogin> getTVLoginToken = C0214a.m586a(new C0331b(C0365a.aU), ApiResultQuickLogin.class, "genLoginToken", false);
    public static final IVrsServer<ApiResultUserInfo> loginWithCode = C0214a.m586a(new C0330a(C0365a.bx), ApiResultUserInfo.class, "tvLogin", false);
    public static final IVrsServer<ApiResultCode> loginWithCookie = C0214a.m586a(new C0331b(C0365a.bc), ApiResultCode.class, "authlogin", false);
    public static final IVrsServer<ApiResultUserInfo> loginWithEMail = C0214a.m586a(new C0330a(C0365a.bj), ApiResultUserInfo.class, "emailLogin", false);
    public static final IVrsServer<ApiResultUserInfo> login_MX = C0214a.m586a(new C0330a(C0365a.bC), ApiResultUserInfo.class, "login_MX", false);
    public static final IVrsServer<ApiResultCode> logout = C0214a.m586a(new C0331b(C0365a.bk), ApiResultCode.class, "logout", false);
    public static final IVrsServer<ApiResultPartnerLogin> partnerLogin = C0214a.m586a(new C0330a(C0365a.bB), ApiResultPartnerLogin.class, "partnerLogin", false);
    public static final IVrsServer<ApiResultUserInfo> partyLogin = C0214a.m586a(new C0331b(C0365a.bz), ApiResultUserInfo.class, "thirdpartyThirdSsoLogin", false);
    public static final IVrsServer<ApiResultCode> registerByPhone = C0214a.m586a(new C0330a(C0365a.bq), ApiResultCode.class, "cellphoneRegister", false);
    public static final IVrsServer<ApiResultAuthCookiePhoneCode> registerByPhoneCode = C0214a.m586a(new C0330a(C0365a.bp), ApiResultAuthCookiePhoneCode.class, "registerByPhoneCode", false);
    public static final IVrsServer<ApiResultUserInfo> registerByPhoneNew = C0214a.m586a(new C0330a(C0365a.bs), ApiResultUserInfo.class, "cellphoneRegister", false);
    public static final IVrsServer<ApiResultUserInfo> registerEMailByVCode = C0214a.m586a(new C0330a(C0365a.br), ApiResultUserInfo.class, "emailRegister", false);
    public static final IVrsServer<ApiResultData> renew_authcookie = C0214a.m586a(new C0330a(C0365a.bb), ApiResultData.class, "renewAuthcookie", true);
    public static final IVrsServer<ApiResultCode> sendPhoneCode = C0214a.m586a(new C0330a(C0365a.bo), ApiResultCode.class, "SendPhoneCode", false);
    public static final IVrsServer<ApiResultUserInfo> subaccountRegisterAndLogin = C0214a.m586a(new C0330a(C0365a.by), ApiResultUserInfo.class, "subaccountLogin", false);
    public static final IVrsServer<ApiResultUserInfo> thirdPartyLogin = C0214a.m586a(new C0331b(C0365a.bv), ApiResultUserInfo.class, "thirdpartySaveAuthToken", false);
    public static final IVrsServer<ApiResultUserIconList> userIconList = C0214a.m586a(new C0331b(C0365a.bu), ApiResultUserIconList.class, IconList.ELEM_NAME, false);
    public static final IVrsServer<ApiResultUserInfo> userInfo = C0214a.m586a(new C0331b(C0365a.bd), ApiResultUserInfo.class, "userInfo", false);
    public static final IVrsServer<ApiResultUserInfo> userInfo_agenttype = C0214a.m586a(new C0331b(C0365a.be), ApiResultUserInfo.class, "userInfo_agenttype", false);

    public static final class C0330a implements IApiUrlBuilder {
        private String f1202a = null;

        public C0330a(String str) {
            this.f1202a = str;
        }

        public final String build(String... params) {
            if (params != null && params.length > 0) {
                String passportDeviceId = TVApiBase.getTVApiProperty().getPassportDeviceId();
                PlatformType platform = TVApiBase.getTVApiProperty().getPlatform();
                String e = C0218c.m605a(platform).mo833e();
                String passportDeviceId2;
                if (this.f1202a.contains("partner/login.action") && params.length == 3) {
                    PartnerLoginType a = C0218c.m605a(platform).mo829a();
                    String c = C0218c.m605a(platform).mo831c();
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
                        strArr[8] = C0376f.m814a(strArr);
                    } else if (a == PartnerLoginType.OPENID) {
                        strArr[0] = "";
                        strArr[1] = params[0];
                        strArr[2] = "";
                        strArr[3] = e;
                        strArr[4] = "";
                        strArr[5] = params[1];
                        strArr[6] = passportDeviceId2;
                        strArr[7] = c;
                        strArr[8] = C0376f.m814a(strArr);
                    } else if (a == PartnerLoginType.ACCESS_TOKEN) {
                        strArr[0] = "";
                        strArr[1] = "";
                        strArr[2] = params[0];
                        strArr[3] = e;
                        strArr[4] = platform.getType();
                        strArr[5] = params[1];
                        strArr[6] = passportDeviceId2;
                        strArr[7] = c;
                        strArr[8] = C0376f.m814a(strArr);
                    }
                    return BaseHelper.m757b(this.f1202a, strArr);
                } else if (this.f1202a.contains("user/mobile/login.action") && params.length == 3) {
                    passportDeviceId2 = C0376f.m820b(params[0], params[1], "", params[2], "", Build.HARDWARE.toString(), passportDeviceId, e);
                    return BaseHelper.m757b(this.f1202a, params[0], params[1], e, passportDeviceId2, params[2], Build.HARDWARE.toString(), passportDeviceId, Build.MODEL.toString());
                } else if (this.f1202a.contains("phone/cellphone_reg.action") && params.length == 4) {
                    return BaseHelper.m757b(this.f1202a, params[0], params[1], params[2], e, params[3], Build.HARDWARE.toString(), passportDeviceId);
                } else if (this.f1202a.contains("reglogin/tv_cellphone_reg.action") && params.length == 4) {
                    passportDeviceId2 = C0376f.m830g(params[2], e);
                    return BaseHelper.m757b(this.f1202a, params[0], params[1], passportDeviceId2, e, params[3], Build.HARDWARE.toString());
                } else if (this.f1202a.contains("user/mobile/sregister.action") && params.length == 5) {
                    passportDeviceId2 = C0376f.m810a(params[0], params[1], passportDeviceId, params[2], "", params[3], params[4], passportDeviceId, e);
                    return BaseHelper.m757b(this.f1202a, params[0], params[1], e, passportDeviceId2, C0376f.m805a(params[2]), params[2], params[3], params[4], Build.HARDWARE.toString(), passportDeviceId);
                } else if (this.f1202a.contains("reglogin/tv_login.action") && params.length == 5) {
                    passportDeviceId2 = C0376f.m830g(params[1], e);
                    return BaseHelper.m757b(this.f1202a, params[0], passportDeviceId2, params[2], e, C0376f.m805a(params[3]), passportDeviceId, Build.MODEL.toString(), params[3], params[4], Build.HARDWARE.toString(), "userinfo,vip_info,tv_vip_info,insecure_account");
                } else if (this.f1202a.contains("subaccount/login.action")) {
                    passportDeviceId2 = C0376f.m818b(params[0], params[1], params[2]);
                    return BaseHelper.m757b(this.f1202a, params[0], params[1], params[2], passportDeviceId2, TVApiBase.getTVApiProperty().getPassportDeviceId());
                } else if (this.f1202a.contains("partner/bf_login.action") && params.length == 1) {
                    String h = C0376f.m831h(C0376f.m830g("bf_" + params[0], e), e);
                    return BaseHelper.m757b(this.f1202a, passportDeviceId2, e, h, TVApiBase.getTVApiProperty().getPassportDeviceId());
                } else if (this.f1202a.contains("phone/send_cellphone_authcode_vcode.action") && params.length == 4) {
                    return BaseHelper.m757b(this.f1202a, params[0], params[1], params[2], C0376f.m805a(params[3]));
                } else if (this.f1202a.contains("reglogin/cellphone_authcode_login.action") && params.length == 4) {
                    return BaseHelper.m757b(this.f1202a, params[0], params[1], params[2], "5", params[3], r1.mo829a(), e, passportDeviceId, C0376f.m822b(r2));
                } else if (this.f1202a.contains("phone/secure_send_cellphone_authcode.action") && params.length == 4) {
                    return BaseHelper.m757b(this.f1202a, params[0], C0376f.m830g(params[1], e), "5", params[2], e, C0376f.m805a(TVApiBase.getTVApiProperty().getMacAddress()), r1.mo829a(), params[3], C0376f.m826c(r2));
                } else if (this.f1202a.contains("xm_sso.action") && params.length == 2) {
                    return BaseHelper.m757b(this.f1202a, e, params[0], params[1], passportDeviceId);
                } else if (this.f1202a.contains("renew_authcookie.action") && params.length == 3) {
                    return BaseHelper.m757b(this.f1202a, params[0], params[1], params[2], passportDeviceId, e, r1.mo829a());
                }
            }
            TVApiTool tVApiTool = BaseHelper.f1195a;
            return TVApiTool.parseLicenceUrl(this.f1202a);
        }

        public final List<String> header() {
            return null;
        }
    }

    static final class C0331b implements IApiUrlBuilder {
        private String f1203a = null;

        public C0331b(String str) {
            this.f1203a = str;
        }

        public final String build(String... params) {
            if (params == null || params.length <= 0) {
                TVApiTool tVApiTool = BaseHelper.f1195a;
                return TVApiTool.parseLicenceUrl(this.f1203a);
            }
            String[] strArr = new String[(params.length + 1)];
            for (int i = 0; i < params.length; i++) {
                strArr[i] = params[i];
            }
            strArr[params.length] = TVApiBase.getTVApiProperty().getPassportDeviceId();
            String b = BaseHelper.m757b(this.f1203a, strArr);
            if (TVApiBase.getTVApiProperty().getPlatform() != PlatformType.NORMAL) {
                return b.replace("agenttype=28", "agenttype=" + C0218c.m605a(TVApiBase.getTVApiProperty().getPlatform()).mo833e());
            }
            return b;
        }

        public final List<String> header() {
            return null;
        }
    }

    public static final String getRegisterEMailVCode(int height, int width, String mac) {
        try {
            C0376f.m805a(mac);
            String format = String.format(C0365a.bt, new Object[]{String.valueOf(height), String.valueOf(width), C0218c.m605a(TVApiBase.getTVApiProperty().getPlatform()).mo833e(), C0376f.m805a(mac), TVApiBase.getTVApiProperty().getPassportDeviceId()});
            C0289c c0289c = new C0289c();
            return C0289c.m686b(format);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final String getBroadcastQuickMark(QuickMarkType type, String cookie, int width, String uuid, String albumId, String code) {
        String valueOf = String.valueOf(System.currentTimeMillis());
        valueOf = valueOf.substring(0, valueOf.length() - 2);
        C0262a.m629a("passport", "fc = " + type.toString());
        valueOf = String.format(C0365a.bw, new Object[]{code, type.toString(), String.valueOf(width), valueOf, C0376f.m829f(valueOf, code), uuid, cookie});
        if (albumId == null || albumId.equals("")) {
            valueOf = valueOf + "&qyid=" + TVApiBase.getTVApiProperty().getPassportDeviceId();
        } else {
            valueOf = valueOf + "&aid=" + albumId + "&qyid=" + TVApiBase.getTVApiProperty().getPassportDeviceId();
        }
        C0262a.m629a("passport", valueOf);
        return valueOf;
    }
}
