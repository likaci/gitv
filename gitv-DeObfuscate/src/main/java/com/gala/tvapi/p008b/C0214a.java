package com.gala.tvapi.p008b;

import com.gala.tvapi.p023c.C0254b;
import com.gala.tvapi.tv2.C0287a;
import com.gala.tvapi.tv2.ITVApiServer;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.p024a.C0286c;
import com.gala.tvapi.type.PartnerLoginType;
import com.gala.tvapi.vrs.C0354a;
import com.gala.tvapi.vrs.core.C0365a;
import com.gala.tvapi.vrs.core.C0367g;
import com.gala.tvapi.vrs.core.C0368b;
import com.gala.tvapi.vrs.core.C0370c;
import com.gala.tvapi.vrs.core.C0372d;
import com.gala.tvapi.vrs.core.C0375e;
import com.gala.tvapi.vrs.core.IPushVideoServer;
import com.gala.tvapi.vrs.core.IVrsServer;
import com.gala.tvapi.vrs.p032b.C0356a;
import com.gala.tvapi.vrs.p032b.C0361f;
import com.gala.tvapi.vrs.p032b.C0362g;
import com.gala.tvapi.vrs.p032b.C0364j;
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

public class C0214a implements C0213b {
    protected String f883a = "04022001010000000000";
    protected String f884b = "2391461978";
    protected String f885c = "890dbe91fbadca03";
    protected String f886d = C0365a.f1257x;
    protected String f887e = "28";

    public final String m600a() {
        return this.f883a;
    }

    public final String m601b() {
        return this.f884b;
    }

    public final String mo831c() {
        return this.f885c;
    }

    public final String mo832d() {
        return this.f886d;
    }

    public final String mo833e() {
        return this.f887e;
    }

    public PartnerLoginType m599a() {
        return PartnerLoginType.GALA;
    }

    public static <T extends ApiResult> ITVApiServer<T> m581a(IApiUrlBuilder iApiUrlBuilder, C0286c<T> c0286c, Class<T> cls, String str, boolean z, boolean z2) {
        return new C0287a(iApiUrlBuilder, c0286c, cls, str, z, z2);
    }

    public static List<String> mo829a() {
        List<String> linkedList = new LinkedList();
        linkedList.add("qyid:" + TVApiBase.getTVApiProperty().getPassportDeviceId());
        linkedList.add("ov:" + TVApiBase.getTVApiProperty().getOSVersion());
        linkedList.add("apkVer:" + TVApiBase.getTVApiProperty().getVersion());
        return linkedList;
    }

    public static List<String> mo830b() {
        List<String> a = C0214a.mo829a();
        a.add("apiKey:" + TVApiBase.getTVApiProperty().getApiKey());
        return a;
    }

    public static String m591a(String str, String str2) throws Exception {
        if (str2 == null) {
            return null;
        }
        Key a = C0214a.m580a(str2);
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(1, a, C0214a.m580a(str2));
        return C0254b.m617a(instance.doFinal(str.getBytes(XML.CHARSET_UTF8)));
    }

    private static SecretKeySpec m595a(String str) {
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
            bArr = C0254b.m618a(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(bArr, "AES");
    }

    private static IvParameterSpec m594a(String str) {
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
            bArr = C0254b.m618a(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(bArr);
    }

    public static boolean m596a(String... strArr) {
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

    public static int m580a(String str) {
        if (str != null) {
            try {
                return Integer.parseInt(str.trim());
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public static String m590a(String str) {
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

    public static String m592a(String... strArr) {
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

    public static IApiUrlBuilder m589a(String str, boolean z) {
        return new C0364j(str, z);
    }

    public static C0354a m582a(String str) {
        return new C0356a(str);
    }

    public static IApiUrlBuilder m588a(String str) {
        return new C0361f(str);
    }

    public static IApiUrlBuilder m597b(String str) {
        return new C0362g(str);
    }

    public static <T extends ApiResult> C0367g<T> m587a(IApiUrlBuilder iApiUrlBuilder, C0286c<T> c0286c, Class<T> cls, String str, boolean z, boolean z2) {
        return new C0367g(iApiUrlBuilder, c0286c, cls, str, z, z2);
    }

    public static <T extends ApiResult> IVrsServer<T> m585a(IApiUrlBuilder iApiUrlBuilder, C0286c<T> c0286c, Class<T> cls, String str, boolean z) {
        return new C0370c(iApiUrlBuilder, c0286c, cls, str, z);
    }

    public static <T extends ApiResult> IVrsServer<T> m584a(IApiUrlBuilder iApiUrlBuilder, C0286c<T> c0286c, Class<T> cls, String str) {
        return new C0372d(iApiUrlBuilder, c0286c, cls, str);
    }

    public static <T extends ApiResult> IVrsServer<T> m586a(IApiUrlBuilder iApiUrlBuilder, Class<T> cls, String str, boolean z) {
        return new C0368b(iApiUrlBuilder, cls, str, z);
    }

    public static <T extends ApiResult> IPushVideoServer<T> m583a(C0354a c0354a, C0286c<T> c0286c, Class<T> cls, String str) {
        return new C0375e(c0354a, c0286c, cls, str);
    }
}
