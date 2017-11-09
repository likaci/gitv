package com.gala.tvapi.tv2;

import android.annotation.SuppressLint;
import android.content.Context;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.p023c.C0253a;
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
    private static TVApiTool f948a = new TVApiTool();
    private static TVImageTool f949a = new TVImageTool();
    private static TVApiProperty f950a = new TVApiProperty();

    public static void setTVApiProperty(TVApiProperty property) {
        f950a = property;
    }

    public static TVApiProperty getTVApiProperty() {
        return f950a;
    }

    public static void setYinheCheckFlag(boolean isCheck) {
        f950a.setCheckYinHe(isCheck);
    }

    public static void setContext(Context appContext) {
        f950a.setContext(appContext);
    }

    public static void setM3u8SignLocal(boolean flag) {
        f950a.setEncodeM3u8LocalFlag(flag);
    }

    public static void setLiveProgramFlag(boolean flag) {
        f950a.setShowLiveFlag(flag);
    }

    public static void setPlatformType(PlatformType type) {
        f950a.setPlatform(type);
    }

    public static void setDebugEnable(boolean enable) {
        f950a.setDebugFlag(enable);
    }

    public static boolean isDebugEnable() {
        return f950a.isDebugEnable();
    }

    protected static String m642a() {
        return f950a.getRegisterKey();
    }

    protected static String m649b() {
        return f950a.getSecretKey();
    }

    public static boolean getOverseaFlag() {
        return f950a.isOpenOverSea();
    }

    public static void setOSVersion(String osVersion) {
        f950a.setOSVersion(osVersion);
    }

    public static TVApiTool getTVApiTool() {
        return f948a;
    }

    public static TVImageTool getTVApiImageTool() {
        return f949a;
    }

    public static void createRegisterKey(String mac, String uuid, String version) {
        if (f950a.getMacAddress().isEmpty() || f950a.getUUID().isEmpty() || f950a.getVersion().isEmpty() || f950a.getRegisterKey().isEmpty() || f950a.getSecretKey().isEmpty() || !mac.equals(f950a.getMacAddress()) || !uuid.equals(f950a.getUUID()) || !version.equals(f950a.getVersion())) {
            f950a.setMacAddress(mac);
            f950a.setUUID(uuid);
            f950a.setVersion(version);
            String a = m643a(mac);
            f950a.setRegisterKey(m650b(a + "/" + uuid + "/" + version));
            f950a.setSecretKey(m652c(a + "/a4d378f98741560decec9b9a22bb58"));
        }
    }

    private static String m643a(String str) {
        if (str != null) {
            return new StringBuilder(str.replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", "")).reverse().toString();
        }
        return "";
    }

    private static String m650b(String str) {
        try {
            return C0253a.m614a(str.getBytes(XML.CHARSET_UTF8)).toString().trim();
        } catch (Exception e) {
            return "";
        }
    }

    private static String m652c(String str) {
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
        String hideString = f950a.getHideString();
        if (!f950a.isOpenOverSea()) {
            if (f950a.getPlatform() == PlatformType.TAIWAN) {
                if (hideString.equalsIgnoreCase(keyWord)) {
                    f950a.setOverSeaFlag(true);
                }
            } else if (hideString.equalsIgnoreCase(C0214a.m580a(keyWord.toLowerCase()))) {
                f950a.setOverSeaFlag(true);
            }
        }
        C0262a.m629a("OverSea", "Oversea flag: " + f950a.isOpenOverSea() + " keyword:" + keyWord.toLowerCase() + " oversea:" + hideString);
    }

    public static void setOverSeaFlag(boolean flag) {
        f950a.setOverSeaFlag(flag);
    }

    public static void clearOverSeaFlag() {
        f950a.clearOverSeaFlag();
    }

    protected static boolean m648a(String... strArr) {
        return strArr != null && strArr.length > 0;
    }

    protected static String m646a(String str, String str2, String... strArr) {
        int i = 1;
        int i2 = 0;
        if (strArr == null) {
            String d = m653d(str2);
            return String.format(TVApiTool.parseLicenceUrl(str), new Object[]{d});
        }
        String[] strArr2 = new String[(strArr.length + 1)];
        strArr2[0] = str2;
        while (i2 < strArr.length) {
            strArr2[i] = m653d(strArr[i2]);
            i2++;
            i++;
        }
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr2);
    }

    protected static String m645a(String str, String str2, String str3, String... strArr) {
        int i = 2;
        int i2 = 0;
        if (strArr == null) {
            String d = m653d(str2);
            String d2 = m653d(str3);
            return String.format(TVApiTool.parseLicenceUrl(str), new Object[]{d, d2});
        }
        String[] strArr2 = new String[(strArr.length + 2)];
        strArr2[0] = str2;
        strArr2[1] = str3;
        while (i2 < strArr.length) {
            strArr2[i] = m653d(strArr[i2]);
            i2++;
            i++;
        }
        C0262a.m629a("TVAPI", "uuid=" + getTVApiProperty().getUUID());
        C0262a.m629a("TVAPI", "version=" + getTVApiProperty().getVersion());
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr2);
    }

    protected static String m647a(String str, String... strArr) {
        if (strArr == null || strArr.length <= 0) {
            return TVApiTool.parseLicenceUrl(str);
        }
        String[] strArr2 = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            strArr2[i] = m653d(strArr[i]);
        }
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr2);
    }

    protected static String m651b(String str, String... strArr) {
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
        String a = m643a(strArr[0]);
        strArr2[0] = m650b(a + "/" + strArr[1] + "/" + strArr[2]);
        strArr2[1] = m652c(a + "/a4d378f98741560decec9b9a22bb58");
        if (strArr2.length == 3) {
            strArr2[2] = strArr[3];
        }
        while (i < strArr2.length) {
            strArr2[i] = m653d(strArr2[i]);
            i++;
        }
        C0262a.m629a("TVAPI", "uuid=" + strArr[1]);
        C0262a.m629a("TVAPI", "version=" + strArr[2]);
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr2);
    }

    protected static String m644a(String str, String str2, String str3, String str4, String... strArr) {
        int i = 1;
        int i2 = 0;
        if (strArr == null) {
            String d = m653d(str2);
            return String.format(TVApiTool.parseLicenceUrl(str), new Object[]{d, str3, str4});
        }
        int length = strArr.length + 3;
        String[] strArr2 = new String[(strArr.length + 3)];
        strArr2[0] = str2;
        while (i2 < strArr.length) {
            strArr2[i] = m653d(strArr[i2]);
            i2++;
            i++;
        }
        strArr2[length - 2] = m653d(str3);
        strArr2[length - 1] = m653d(str4);
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr2);
    }

    private static String m653d(String str) {
        if (!(str == null || str.isEmpty())) {
            try {
                return URLEncoder.encode(str, XML.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
                C0262a.m634b("URL Encode", e);
            }
        }
        return "";
    }
}
