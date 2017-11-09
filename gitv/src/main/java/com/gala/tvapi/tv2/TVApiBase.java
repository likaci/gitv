package com.gala.tvapi.tv2;

import android.annotation.SuppressLint;
import android.content.Context;
import com.gala.tvapi.c.a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tools.TVImageTool;
import com.gala.tvapi.tv2.property.TVApiProperty;
import com.gala.tvapi.type.PlatformType;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import org.cybergarage.soap.SOAP;
import org.cybergarage.xml.XML;

@SuppressLint({"DefaultLocale"})
public class TVApiBase {
    private static TVApiTool a = new TVApiTool();
    private static TVImageTool f470a = new TVImageTool();
    private static TVApiProperty f471a = new TVApiProperty();

    public static void setTVApiProperty(TVApiProperty property) {
        f471a = property;
    }

    public static TVApiProperty getTVApiProperty() {
        return f471a;
    }

    public static void setYinheCheckFlag(boolean isCheck) {
        f471a.setCheckYinHe(isCheck);
    }

    public static void setContext(Context appContext) {
        f471a.setContext(appContext);
    }

    public static void setM3u8SignLocal(boolean flag) {
        f471a.setEncodeM3u8LocalFlag(flag);
    }

    public static void setLiveProgramFlag(boolean flag) {
        f471a.setShowLiveFlag(flag);
    }

    public static void setPlatformType(PlatformType type) {
        f471a.setPlatform(type);
    }

    public static void setDebugEnable(boolean enable) {
        f471a.setDebugFlag(enable);
    }

    public static boolean isDebugEnable() {
        return f471a.isDebugEnable();
    }

    protected static String a() {
        return f471a.getRegisterKey();
    }

    protected static String b() {
        return f471a.getSecretKey();
    }

    public static boolean getOverseaFlag() {
        return f471a.isOpenOverSea();
    }

    public static void setOSVersion(String osVersion) {
        f471a.setOSVersion(osVersion);
    }

    public static TVApiTool getTVApiTool() {
        return a;
    }

    public static TVImageTool getTVApiImageTool() {
        return f470a;
    }

    public static void createRegisterKey(String mac, String uuid, String version) {
        if (f471a.getMacAddress().isEmpty() || f471a.getUUID().isEmpty() || f471a.getVersion().isEmpty() || f471a.getRegisterKey().isEmpty() || f471a.getSecretKey().isEmpty() || !mac.equals(f471a.getMacAddress()) || !uuid.equals(f471a.getUUID()) || !version.equals(f471a.getVersion())) {
            f471a.setMacAddress(mac);
            f471a.setUUID(uuid);
            f471a.setVersion(version);
            String a = a(mac);
            f471a.setRegisterKey(b(a + "/" + uuid + "/" + version));
            f471a.setSecretKey(c(a + "/a4d378f98741560decec9b9a22bb58"));
        }
    }

    private static String a(String str) {
        if (str != null) {
            return new StringBuilder(str.replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", "")).reverse().toString();
        }
        return "";
    }

    private static String b(String str) {
        try {
            return a.a(str.getBytes(XML.CHARSET_UTF8)).toString().trim();
        } catch (Exception e) {
            return "";
        }
    }

    private static String c(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(str.getBytes("UTF-8"));
            byte[] digest = instance.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                if (Integer.toHexString(digest[i] & 255).length() == 1) {
                    stringBuffer.append("0").append(Integer.toHexString(digest[i] & 255));
                } else {
                    stringBuffer.append(Integer.toHexString(digest[i] & 255));
                }
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static void setOverseaFlag(String keyWord) {
        String hideString = f471a.getHideString();
        if (!f471a.isOpenOverSea()) {
            if (f471a.getPlatform() == PlatformType.TAIWAN) {
                if (hideString.equalsIgnoreCase(keyWord)) {
                    f471a.setOverSeaFlag(true);
                }
            } else if (hideString.equalsIgnoreCase(com.gala.tvapi.b.a.a(keyWord.toLowerCase()))) {
                f471a.setOverSeaFlag(true);
            }
        }
        com.gala.tvapi.log.a.a("OverSea", "Oversea flag: " + f471a.isOpenOverSea() + " keyword:" + keyWord.toLowerCase() + " oversea:" + hideString);
    }

    public static void setOverSeaFlag(boolean flag) {
        f471a.setOverSeaFlag(flag);
    }

    public static void clearOverSeaFlag() {
        f471a.clearOverSeaFlag();
    }

    protected static boolean a(String... strArr) {
        return strArr != null && strArr.length > 0;
    }

    protected static String a(String str, String str2, String... strArr) {
        int i = 1;
        int i2 = 0;
        if (strArr == null) {
            String d = d(str2);
            return String.format(TVApiTool.parseLicenceUrl(str), new Object[]{d});
        }
        String[] strArr2 = new String[(strArr.length + 1)];
        strArr2[0] = str2;
        while (i2 < strArr.length) {
            strArr2[i] = d(strArr[i2]);
            i2++;
            i++;
        }
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr2);
    }

    protected static String a(String str, String str2, String str3, String... strArr) {
        int i = 2;
        int i2 = 0;
        if (strArr == null) {
            String d = d(str2);
            String d2 = d(str3);
            return String.format(TVApiTool.parseLicenceUrl(str), new Object[]{d, d2});
        }
        String[] strArr2 = new String[(strArr.length + 2)];
        strArr2[0] = str2;
        strArr2[1] = str3;
        while (i2 < strArr.length) {
            strArr2[i] = d(strArr[i2]);
            i2++;
            i++;
        }
        com.gala.tvapi.log.a.a("TVAPI", "uuid=" + getTVApiProperty().getUUID());
        com.gala.tvapi.log.a.a("TVAPI", "version=" + getTVApiProperty().getVersion());
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr2);
    }

    protected static String a(String str, String... strArr) {
        if (strArr == null || strArr.length <= 0) {
            return TVApiTool.parseLicenceUrl(str);
        }
        String[] strArr2 = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            strArr2[i] = d(strArr[i]);
        }
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr2);
    }

    protected static String b(String str, String... strArr) {
        int i = 0;
        if (strArr == null || strArr.length <= 0) {
            return TVApiTool.parseLicenceUrl(str);
        }
        String[] strArr2 = null;
        if (strArr.length == 3) {
            strArr2 = new String[2];
        } else if (strArr.length == 4) {
            strArr2 = new String[3];
        }
        if (strArr2 == null) {
            return TVApiTool.parseLicenceUrl(str);
        }
        String a = a(strArr[0]);
        strArr2[0] = b(a + "/" + strArr[1] + "/" + strArr[2]);
        strArr2[1] = c(a + "/a4d378f98741560decec9b9a22bb58");
        if (strArr2.length == 3) {
            strArr2[2] = strArr[3];
        }
        while (i < strArr2.length) {
            strArr2[i] = d(strArr2[i]);
            i++;
        }
        com.gala.tvapi.log.a.a("TVAPI", "uuid=" + strArr[1]);
        com.gala.tvapi.log.a.a("TVAPI", "version=" + strArr[2]);
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr2);
    }

    protected static String a(String str, String str2, String str3, String str4, String... strArr) {
        int i = 1;
        int i2 = 0;
        if (strArr == null) {
            String d = d(str2);
            return String.format(TVApiTool.parseLicenceUrl(str), new Object[]{d, str3, str4});
        }
        int length = strArr.length + 3;
        String[] strArr2 = new String[(strArr.length + 3)];
        strArr2[0] = str2;
        while (i2 < strArr.length) {
            strArr2[i] = d(strArr[i2]);
            i2++;
            i++;
        }
        strArr2[length - 2] = d(str3);
        strArr2[length - 1] = d(str4);
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr2);
    }

    private static String d(String str) {
        if (!(str == null || str.isEmpty())) {
            try {
                return URLEncoder.encode(str, XML.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
                com.gala.tvapi.log.a.b("URL Encode", e);
            }
        }
        return "";
    }
}
