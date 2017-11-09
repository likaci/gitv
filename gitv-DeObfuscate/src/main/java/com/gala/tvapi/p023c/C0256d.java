package com.gala.tvapi.p023c;

import android.util.Base64;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public final class C0256d {
    private static String f908a = "RSA/ECB/PKCS1Padding";

    public static String m621a(String str, String str2) {
        String str3 = "";
        try {
            Key a = C0256d.m622a(str2);
            Cipher instance = Cipher.getInstance(f908a);
            instance.init(1, a);
            str3 = C0254b.m617a(instance.doFinal(str.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str3;
    }

    public static String m623b(String str, String str2) {
        String str3 = "";
        try {
            Key a = C0256d.m622a(str2);
            Cipher instance = Cipher.getInstance(f908a);
            instance.init(2, a);
            str3 = C0254b.m617a(instance.doFinal(C0254b.m618a(str)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str3;
    }

    private static PublicKey m622a(String str) throws Exception {
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(str, 0)));
    }
}
