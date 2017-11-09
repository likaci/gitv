package com.gala.tvapi.vrs.core;

import android.annotation.SuppressLint;
import android.os.Build;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.p023c.C0253a;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.video.app.epg.ui.search.ad.Keys;
import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.gala.video.webview.utils.WebSDKConstants;
import com.push.pushservice.constants.DataConst;
import com.qidun.tvapi.NativeTmcPlayer;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.crypto.Cipher;
import org.cybergarage.http.HTTP;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public final class C0376f {
    private static String f1307a = "";
    private static String f1308b = "";

    public static String m808a(String str, String str2, String str3, String str4) {
        Map hashMap = new HashMap();
        hashMap.put(WebSDKConstants.PARAM_KEY_DEVICEID, str);
        hashMap.put("deviceName", str2);
        hashMap.put("logoUrl", str3);
        hashMap.put("extraInfo", str4);
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        return C0376f.m813a(hashMap, "tS7BdPLU2w0JD81239dh");
    }

    public static String m806a(String str, String str2) {
        Map hashMap = new HashMap();
        hashMap.put(WebSDKConstants.PARAM_KEY_DEVICEID, str);
        hashMap.put("deviceSid", str2);
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        return C0376f.m813a(hashMap, "tS7BdPLU2w0JD81239dh");
    }

    public static String m819b(String str, String str2, String str3, String str4) {
        Map hashMap = new HashMap();
        hashMap.put("authcookie", str);
        hashMap.put(WebSDKConstants.PARAM_KEY_DEVICEID, str2);
        hashMap.put("deviceSid", str3);
        hashMap.put("deviceNickName", str4);
        hashMap.put("agentType", "86");
        hashMap.put("relationType", "0");
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        return C0376f.m813a(hashMap, "tS7BdPLU2w0JD81239dh");
    }

    public static String m817b(String str, String str2) {
        Map hashMap = new HashMap();
        hashMap.put("authcookie", str);
        hashMap.put(WebSDKConstants.PARAM_KEY_DEVICEID, str2);
        hashMap.put("agentType", "86");
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        return C0376f.m813a(hashMap, "tS7BdPLU2w0JD81239dh");
    }

    public static String m823c(String str, String str2) {
        Map hashMap = new HashMap();
        hashMap.put(WebSDKConstants.PARAM_KEY_DEVICEID, str);
        hashMap.put(WebSDKConstants.PARAM_KEY_UID, str2);
        hashMap.put("agentType", "28");
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        return C0376f.m813a(hashMap, "tS7BdPLU2w0JD81239dh");
    }

    public static String m807a(String str, String str2, String str3) {
        Map hashMap = new HashMap();
        hashMap.put("authcookie", str);
        hashMap.put(WebSDKConstants.PARAM_KEY_DEVICEID, str2);
        hashMap.put("agentType", "86");
        hashMap.put("deviceNickName", str3);
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        return C0376f.m813a(hashMap, "tS7BdPLU2w0JD81239dh");
    }

    public static String m827d(String str, String str2) {
        Map hashMap = new HashMap();
        hashMap.put("authcookie", str);
        hashMap.put("agentType", "86");
        hashMap.put("relationType", "0");
        hashMap.put("status", str2);
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        return C0376f.m813a(hashMap, "tS7BdPLU2w0JD81239dh");
    }

    public static String m812a(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14) {
        Map hashMap = new HashMap();
        hashMap.put("authcookie", str);
        hashMap.put("type", str2);
        hashMap.put("files", str3);
        hashMap.put("device_id", str4);
        hashMap.put(DataConst.APP_INFO_APP_ID, str5);
        hashMap.put(DataConst.APP_INFO_APP_VER, str6);
        hashMap.put("agenttype", "86");
        hashMap.put("platform_type", "21");
        hashMap.put("msg_type", str7);
        hashMap.put("duration", str8);
        hashMap.put("msg_title", str9);
        hashMap.put("msg_content", str10);
        hashMap.put("msg_url", str11);
        hashMap.put("msg_alert_body", str12);
        hashMap.put("priority", "0");
        hashMap.put("expire", str13);
        hashMap.put("qichuanIds", str14);
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        return C0376f.m813a(hashMap, "tS7BdPLU2w0JD81239dh");
    }

    public static String m811a(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13) {
        Map hashMap = new HashMap();
        hashMap.put("authcookie", str);
        hashMap.put("device_ids", str2);
        hashMap.put(DataConst.APP_INFO_APP_ID, str3);
        hashMap.put(DataConst.APP_INFO_APP_VER, str4);
        hashMap.put("agenttype", "86");
        hashMap.put("platform_type", "21");
        hashMap.put("msg_type", str5);
        hashMap.put("duration", str6);
        hashMap.put("msg_title", str7);
        hashMap.put("msg_content", str8);
        hashMap.put("msg_url", str9);
        hashMap.put("msg_alert_body", str10);
        hashMap.put("priority", "0");
        hashMap.put("device_policy", str11);
        hashMap.put("qpid", str12);
        hashMap.put("sourceid", str13);
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        return C0376f.m813a(hashMap, "2IV17QS4H6NPM89ZGB3YLUCD0ETAXOK5RFJW");
    }

    public static String m809a(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
        Map hashMap = new HashMap();
        hashMap.put(DataConst.APP_INFO_APP_ID, str);
        hashMap.put(DataConst.APP_INFO_APP_VER, str2);
        hashMap.put("agenttype", "86");
        hashMap.put("platform_type", "21");
        hashMap.put("msg_type", str3);
        hashMap.put("duration", str4);
        hashMap.put("msg_title", str5);
        hashMap.put("msg_content", str6);
        hashMap.put("msg_url", str7);
        hashMap.put("msg_alert_body", str8);
        hashMap.put("priority", "0");
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        return C0376f.m813a(hashMap, "2IV17QS4H6NPM89ZGB3YLUCD0ETAXOK5RFJW");
    }

    public static String m824c(String str, String str2, String str3, String str4) {
        Map hashMap = new HashMap();
        hashMap.put("authcookie", str);
        hashMap.put("device_id", str2);
        hashMap.put("agenttype", str4);
        hashMap.put(Keys.TV_ID, str3);
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        return C0376f.m813a(hashMap, "jfaljluixn39012$#");
    }

    public static String m828e(String str, String str2) {
        if (str == null || str.length() < 36) {
            return "Fw0JD89dhtS7BdPLU21";
        }
        String a = C0214a.m580a(str.substring(4, 36) + "|" + str2 + "|Fw0JD89dhtS7BdPLU21");
        f1307a = a;
        return a;
    }

    public static boolean m816a(String str, String str2, String str3) {
        try {
            String str4;
            if (str2.equals("28") || str2.equals(Values.value18)) {
                str4 = "7708541588400723580891461532086585711224060917692789725607605107737089741211059681915390034098245747146820954320176953203493475924097383301845903099919097";
            } else if (str2.equals("20")) {
                str4 = "8348455200832397277815698582084455439043298581002070601968567925776371244726724825892751046434497561255125850609692273965149180339303803856552877657084441";
            } else if (str2.equals("21")) {
                str4 = "7030275091430684880455197983898536510502970284707709211601320194624399535289472259282643248491148705072503555764283431685757559494709977423553831916356709";
            } else if (str2.equals("119")) {
                str4 = "9668437968921148606767480299285589061148920994079358325441551047410384073593256681801457701617639855317486833995906399053512292431427552739445089124982229";
            } else {
                str4 = "8983556970082806072261113298370959076142893170423488416059191100210358114802049032983889493302173157165863643606239492524847800665553743035328512591065037";
            }
            Key generatePublic = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(new BigInteger(str4), new BigInteger("65537")));
            Cipher instance = Cipher.getInstance("RSA/ECB/NoPadding");
            instance.init(2, generatePublic);
            String str5 = new String(instance.doFinal(C0253a.m614a(str.getBytes("UTF8"))), "UTF8");
            C0262a.m629a("checkvip", "data_string=" + str3);
            C0262a.m629a("checkvip", "request_sign=" + f1307a);
            str4 = C0214a.m580a(str3 + f1307a);
            str5 = str5.trim();
            C0262a.m629a("Sign=", str4);
            C0262a.m629a("orginal=", str5);
            return str4.equals(str5);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String m813a(Map<String, String> map, String str) {
        SortedMap treeMap = new TreeMap(map);
        treeMap.remove("sign");
        StringBuilder stringBuilder = new StringBuilder();
        for (String str2 : treeMap.keySet()) {
            String str3 = (String) treeMap.get(str2);
            StringBuilder append = stringBuilder.append(str2).append(SearchCriteria.EQ);
            String str22 = "";
            if (str3 == null) {
                str3 = str22;
            }
            append.append(str3).append("|");
        }
        return C0214a.m580a(stringBuilder.append(str).toString());
    }

    public static String m820b(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
        Map hashMap = new HashMap();
        hashMap.put("email", str);
        hashMap.put("passwd", str2);
        hashMap.put("agenttype", str8);
        hashMap.put("port", str3);
        hashMap.put(WebSDKConstants.PARAM_KEY_MAC, str4);
        hashMap.put("imei", str5);
        hashMap.put("hid", str6);
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        hashMap.put("device_id", str7);
        hashMap.put("device_name", Build.MODEL.toString());
        SortedMap treeMap = new TreeMap(hashMap);
        StringBuilder stringBuilder = new StringBuilder();
        for (String str9 : treeMap.keySet()) {
            String str10 = (String) treeMap.get(str9);
            StringBuilder append = stringBuilder.append(str9).append(SearchCriteria.EQ);
            String str92 = "";
            if (str10 == null) {
                str10 = str92;
            }
            append.append(str10).append("|");
        }
        return C0214a.m580a(stringBuilder.append("w0JD89dhtS7BdPLU2").toString());
    }

    public static String m810a(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        Map hashMap = new HashMap();
        hashMap.put("email", str);
        hashMap.put("passwd", str2);
        hashMap.put("agenttype", str9);
        hashMap.put("QC005", str3);
        hashMap.put("vcode", str4);
        hashMap.put("port", str5);
        hashMap.put(WebSDKConstants.PARAM_KEY_MAC, str6);
        hashMap.put("imei", str7);
        hashMap.put("hid", Build.HARDWARE.toString());
        hashMap.put(WebSDKConstants.PARAM_KEY_DEVICEID, str8);
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        SortedMap treeMap = new TreeMap(hashMap);
        StringBuilder stringBuilder = new StringBuilder();
        for (String str10 : treeMap.keySet()) {
            String str11 = (String) treeMap.get(str10);
            StringBuilder append = stringBuilder.append(str10).append(SearchCriteria.EQ);
            String str102 = "";
            if (str11 == null) {
                str11 = str102;
            }
            append.append(str11).append("|");
        }
        return C0214a.m580a(stringBuilder.append("w0JD89dhtS7BdPLU2").toString());
    }

    @SuppressLint({"DefaultLocale"})
    public static void m815a(String str) {
        f1308b = C0214a.m580a(str.toLowerCase());
    }

    @SuppressLint({"DefaultLocale"})
    public static String m805a(String str) {
        if (f1308b == null || f1308b.isEmpty()) {
            return C0214a.m580a(str.toLowerCase());
        }
        return f1308b;
    }

    public static String m829f(String str, String str2) {
        return C0214a.m580a(str2 + "_890dbe91fbadca03_" + str + "_2385461878_");
    }

    @SuppressLint({"TrulyRandom"})
    public static String m830g(String str, String str2) {
        C0262a.m629a("RSA", "account = " + str);
        try {
            String str3 = "7708541588400723580891461532086585711224060917692789725607605107737089741211059681915390034098245747146820954320176953203493475924097383301845903099919097";
            if (str2.equals("119")) {
                str3 = "9668437968921148606767480299285589061148920994079358325441551047410384073593256681801457701617639855317486833995906399053512292431427552739445089124982229";
            } else if (str2.equals(Values.value18)) {
                str3 = "7708541588400723580891461532086585711224060917692789725607605107737089741211059681915390034098245747146820954320176953203493475924097383301845903099919097";
            }
            Key generatePrivate = KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateKeySpec(new BigInteger(str3), new BigInteger("65537")));
            Cipher instance = Cipher.getInstance("RSA/ECB/NoPadding");
            instance.init(1, generatePrivate);
            return new String(C0253a.m616a(instance.doFinal(str.getBytes("UTF8")), 0)).replaceAll("\n", "").replaceAll(HTTP.TAB, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String m818b(String str, String str2, String str3) {
        String str4;
        Map hashMap = new HashMap();
        hashMap.put("macid", str);
        hashMap.put("device_name", str2);
        hashMap.put("agenttype", str3);
        hashMap.put("device_id", TVApiBase.getTVApiProperty().getPassportDeviceId());
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        SortedMap treeMap = new TreeMap(hashMap);
        StringBuilder stringBuilder = new StringBuilder();
        for (String str42 : treeMap.keySet()) {
            String str5 = (String) treeMap.get(str42);
            StringBuilder append = stringBuilder.append(str42).append(SearchCriteria.EQ);
            str42 = "";
            if (str5 == null) {
                str5 = str42;
            }
            append.append(str5).append("|");
        }
        str42 = "w0JD89dhtS7BdPLU2";
        if (str3.equals("93")) {
            str42 = "w0JD89dhtsS7BdPLU2";
        } else if (str3.equals("131")) {
            str42 = "w0JD89dhtsS7TClali";
        } else if (str3.equals("132")) {
            str42 = "w0JD89dhtsS7TCL0tt";
        } else if (str3.equals("135")) {
            str42 = "yhJD89dhtsS7Tcgzyh";
        }
        return C0214a.m580a(stringBuilder.append(str42).toString());
    }

    public static String m831h(String str, String str2) {
        String str3 = "w0JD89dhtS7BdPLU2";
        if (str2.equals("119")) {
            str3 = "0wrt1scv8tdl3ome4vct6img6ashr2qb";
        }
        Map hashMap = new HashMap();
        C0262a.m629a("macid", "id=" + str);
        hashMap.put("macid", str);
        hashMap.put("agenttype", str2);
        hashMap.put("device_id", TVApiBase.getTVApiProperty().getPassportDeviceId());
        return C0376f.m821b(hashMap, str3);
    }

    public static String m814a(String... strArr) {
        Map hashMap = new HashMap();
        hashMap.put("authcookie", strArr[0]);
        hashMap.put("openid", strArr[1]);
        hashMap.put("access_token", strArr[2]);
        hashMap.put("agenttype", strArr[3]);
        hashMap.put("type", "1");
        hashMap.put("need_unlogin_uid", "0");
        hashMap.put("account_type", strArr[4]);
        hashMap.put("ott_version", strArr[5]);
        hashMap.put("device_id", strArr[6]);
        hashMap.put("platform_id", strArr[7]);
        hashMap.put("qyid", TVApiBase.getTVApiProperty().getPassportDeviceId());
        return C0376f.m821b(hashMap, "w0JD89dhtS7BdPLU2");
    }

    private static String m821b(Map<String, String> map, String str) {
        SortedMap treeMap = new TreeMap(map);
        treeMap.remove("qd_sc");
        StringBuilder stringBuilder = new StringBuilder();
        for (String str2 : treeMap.keySet()) {
            String str3 = (String) treeMap.get(str2);
            StringBuilder append = stringBuilder.append(str2).append(SearchCriteria.EQ);
            String str22 = "";
            if (str3 == null) {
                str3 = str22;
            }
            append.append(str3).append("&");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(str);
        return C0214a.m580a(stringBuilder.toString());
    }

    public static String m822b(String... strArr) {
        Map hashMap = new HashMap();
        hashMap.put("cellphoneNumber", strArr[0]);
        hashMap.put("area_code", strArr[1]);
        hashMap.put("requestType", strArr[2]);
        hashMap.put("serviceId", strArr[3]);
        hashMap.put("authCode", strArr[4]);
        hashMap.put("ptid", strArr[5]);
        hashMap.put("agenttype", strArr[6]);
        hashMap.put("device_id", strArr[7]);
        return C0376f.m825c(hashMap, strArr[5]);
    }

    public static String m826c(String... strArr) {
        Map hashMap = new HashMap();
        hashMap.put("requestType", strArr[0]);
        hashMap.put("cellphoneNumber", strArr[1]);
        hashMap.put("serviceId", strArr[2]);
        hashMap.put("area_code", strArr[3]);
        hashMap.put("agenttype", strArr[4]);
        hashMap.put("QC005", strArr[5]);
        hashMap.put("ptid", strArr[6]);
        hashMap.put("vcode", strArr[7]);
        return C0376f.m825c(hashMap, strArr[6]);
    }

    private static String m825c(Map<String, String> map, String str) {
        SortedMap treeMap = new TreeMap(map);
        treeMap.remove("qd_sc");
        StringBuilder stringBuilder = new StringBuilder();
        for (String str2 : treeMap.keySet()) {
            String str3 = (String) treeMap.get(str2);
            StringBuilder append = stringBuilder.append(str2).append(SearchCriteria.EQ);
            String str22 = "";
            if (str3 == null) {
                str3 = str22;
            }
            append.append(str3).append("&");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return new NativeTmcPlayer().qdsc(TVApiBase.getTVApiProperty().getContext(), stringBuilder.toString(), str);
    }
}
