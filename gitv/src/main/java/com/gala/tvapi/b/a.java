package com.gala.tvapi.b;

import com.gala.tvapi.c.b;
import com.gala.tvapi.tv2.ITVApiServer;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.a.c;
import com.gala.tvapi.type.PartnerLoginType;
import com.gala.tvapi.vrs.b.f;
import com.gala.tvapi.vrs.b.g;
import com.gala.tvapi.vrs.b.j;
import com.gala.tvapi.vrs.core.IPushVideoServer;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.core.d;
import com.gala.tvapi.vrs.core.e;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiUrlBuilder;
import java.security.Key;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.cybergarage.xml.XML;

public class a implements b {
    protected String a = "04022001010000000000";
    protected String b = "2391461978";
    protected String c = "890dbe91fbadca03";
    protected String d = com.gala.tvapi.vrs.core.a.x;
    protected String e = "28";

    public final String m41a() {
        return this.a;
    }

    public final String m42b() {
        return this.b;
    }

    public final String c() {
        return this.c;
    }

    public final String d() {
        return this.d;
    }

    public final String e() {
        return this.e;
    }

    public PartnerLoginType m40a() {
        return PartnerLoginType.GALA;
    }

    public static <T extends ApiResult> ITVApiServer<T> a(IApiUrlBuilder iApiUrlBuilder, c<T> cVar, Class<T> cls, String str, boolean z, boolean z2) {
        return new com.gala.tvapi.tv2.a(iApiUrlBuilder, cVar, cls, str, z, z2);
    }

    public static List<String> a() {
        List<String> linkedList = new LinkedList();
        linkedList.add("qyid:" + TVApiBase.getTVApiProperty().getPassportDeviceId());
        linkedList.add("ov:" + TVApiBase.getTVApiProperty().getOSVersion());
        linkedList.add("apkVer:" + TVApiBase.getTVApiProperty().getVersion());
        return linkedList;
    }

    public static List<String> b() {
        List<String> a = a();
        a.add("apiKey:" + TVApiBase.getTVApiProperty().getApiKey());
        return a;
    }

    public static String a(String str, String str2) throws Exception {
        if (str2 == null) {
            return null;
        }
        Key a = a(str2);
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(1, a, a(str2));
        return b.a(instance.doFinal(str.getBytes(XML.CHARSET_UTF8)));
    }

    private static SecretKeySpec m38a(String str) {
        byte[] bArr = null;
        if (str == null) {
            str = "";
        }
        StringBuffer stringBuffer = new StringBuffer(16);
        stringBuffer.append(str);
        while (stringBuffer.length() < 16) {
            stringBuffer.append("0");
        }
        if (stringBuffer.length() > 16) {
            stringBuffer.setLength(16);
        }
        try {
            bArr = b.a(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(bArr, "AES");
    }

    private static IvParameterSpec m37a(String str) {
        byte[] bArr = null;
        if (str == null) {
            str = "";
        }
        StringBuffer stringBuffer = new StringBuffer(16);
        stringBuffer.append(str);
        while (stringBuffer.length() < 16) {
            stringBuffer.append("0");
        }
        if (stringBuffer.length() > 16) {
            stringBuffer.setLength(16);
        }
        try {
            bArr = b.a(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(bArr);
    }

    public static boolean m39a(String... strArr) {
        if (strArr == null) {
            return true;
        }
        for (String str : strArr) {
            if (str != null && !str.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static int a(String str) {
        if (str != null) {
            try {
                return Integer.parseInt(str.trim());
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public static String m36a(String str) {
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

    public static String a(String... strArr) {
        if (strArr == null || strArr.length <= 0) {
            return "";
        }
        String str = "params=";
        int length = strArr.length;
        int i = 0;
        while (i < length) {
            i++;
            str = str + strArr[i] + "-";
        }
        if (str.length() > 1) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static IApiUrlBuilder a(String str, boolean z) {
        return new j(str, z);
    }

    public static com.gala.tvapi.vrs.a m33a(String str) {
        return new com.gala.tvapi.vrs.b.a(str);
    }

    public static IApiUrlBuilder m35a(String str) {
        return new f(str);
    }

    public static IApiUrlBuilder b(String str) {
        return new g(str);
    }

    public static <T extends ApiResult> com.gala.tvapi.vrs.core.g<T> m34a(IApiUrlBuilder iApiUrlBuilder, c<T> cVar, Class<T> cls, String str, boolean z, boolean z2) {
        return new com.gala.tvapi.vrs.core.g(iApiUrlBuilder, cVar, cls, str, z, z2);
    }

    public static <T extends ApiResult> IVrsServer<T> a(IApiUrlBuilder iApiUrlBuilder, c<T> cVar, Class<T> cls, String str, boolean z) {
        return new com.gala.tvapi.vrs.core.c(iApiUrlBuilder, cVar, cls, str, z);
    }

    public static <T extends ApiResult> IVrsServer<T> a(IApiUrlBuilder iApiUrlBuilder, c<T> cVar, Class<T> cls, String str) {
        return new d(iApiUrlBuilder, cVar, cls, str);
    }

    public static <T extends ApiResult> IVrsServer<T> a(IApiUrlBuilder iApiUrlBuilder, Class<T> cls, String str, boolean z) {
        return new com.gala.tvapi.vrs.core.b(iApiUrlBuilder, cls, str, z);
    }

    public static <T extends ApiResult> IPushVideoServer<T> a(com.gala.tvapi.vrs.a aVar, c<T> cVar, Class<T> cls, String str) {
        return new e(aVar, cVar, cls, str);
    }
}
