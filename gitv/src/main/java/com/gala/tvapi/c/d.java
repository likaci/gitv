package com.gala.tvapi.c;

import android.util.Base64;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public final class d {
    private static String a = "RSA/ECB/PKCS1Padding";

    public static String a(String str, String str2) {
        String str3 = "";
        try {
            Key a = a(str2);
            Cipher instance = Cipher.getInstance(a);
            instance.init(1, a);
            str3 = b.a(instance.doFinal(str.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str3;
    }

    public static String b(String str, String str2) {
        String str3 = "";
        try {
            Key a = a(str2);
            Cipher instance = Cipher.getInstance(a);
            instance.init(2, a);
            str3 = b.a(instance.doFinal(b.a(str)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str3;
    }

    private static PublicKey a(String str) throws Exception {
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(str, 0)));
    }
}
