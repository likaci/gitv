package com.gala.tvapi.vrs.result;

import android.os.Build;
import android.util.Log;
import com.gala.tvapi.b.c;
import com.gala.tvapi.log.a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.vrs.model.QuickLogin;
import com.gala.video.api.ApiResult;

public class ApiResultQuickLogin extends ApiResult {
    private static String a = "TVApi/QuickLoginUrl";
    public QuickLogin data;

    public String getTCServiceNoLogin() {
        if (this.data == null || this.data.token == null || this.data.token.isEmpty()) {
            return "";
        }
        String str = "http://cserver.ptqy.gitv.tv/mobile/chat.html?locale=zh-tw&bu=tw-tv&token=" + this.data.token;
        Log.d(a, str);
        return str;
    }

    public String getQuickMarkStringNoLogin() {
        if (this.data == null || this.data.token == null || this.data.token.isEmpty()) {
            return "";
        }
        String parseLicenceUrl = TVApiTool.parseLicenceUrl("http://passport.igala.com/apis/qrcode/token_login.action?agenttype=" + a() + "&Code_type=0&token=" + this.data.token + b() + c());
        Log.d(a, parseLicenceUrl);
        return parseLicenceUrl;
    }

    public String getQuickMarkStringLogin() {
        if (this.data == null || this.data.token == null || this.data.token.isEmpty()) {
            return "";
        }
        String parseLicenceUrl = TVApiTool.parseLicenceUrl("http://passport.igala.com/apis/qrcode/opt/request_login.action?agenttype=" + a() + "&Code_type=1&token=" + this.data.token + b() + c());
        Log.d(a, parseLicenceUrl);
        return parseLicenceUrl;
    }

    public String getQuickMarkStringNoLogin(String str) {
        if (this.data == null || this.data.token == null || this.data.token.isEmpty()) {
            return "";
        }
        String parseLicenceUrl = TVApiTool.parseLicenceUrl("http://passport.igala.com/apis/qrcode/token_login.action?agenttype=" + a() + "&Code_type=0&token=" + this.data.token + b() + c());
        Log.d(a, parseLicenceUrl);
        return parseLicenceUrl;
    }

    public String getQuickMarkStringLogin(String str) {
        if (this.data == null || this.data.token == null || this.data.token.isEmpty()) {
            return "";
        }
        String parseLicenceUrl = TVApiTool.parseLicenceUrl("http://passport.igala.com/apis/qrcode/opt/request_login.action?agenttype=" + a() + "&Code_type=1&token=" + this.data.token + b() + c());
        Log.d(a, parseLicenceUrl);
        return parseLicenceUrl;
    }

    private static String a() {
        if (TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN) {
            return PlatformType.TAIWAN.getAgentType();
        }
        return c.a(TVApiBase.getTVApiProperty().getPlatform()).e();
    }

    private static String b() {
        if (TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN) {
            return "";
        }
        StringBuilder append = new StringBuilder("&device_id=").append(TVApiBase.getTVApiProperty().getPassportDeviceId()).append("&extra=");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ui:" + TVApiBase.getTVApiProperty().getUUID() + ";").append("ak:" + TVApiBase.getTVApiProperty().getApiKey() + ";").append("ai:" + TVApiBase.getTVApiProperty().getAuthId() + ";").append("av:" + TVApiBase.getTVApiProperty().getVersion() + ";").append("cv:" + Build.MODEL.toString().replaceAll(" ", ""));
        a.a("QuickLogin", "extra=" + stringBuilder);
        return append.append(stringBuilder.toString()).toString();
    }

    private static String c() {
        if (TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN) {
            return "&version=" + TVApiBase.getTVApiProperty().getVersion();
        }
        return "";
    }
}
