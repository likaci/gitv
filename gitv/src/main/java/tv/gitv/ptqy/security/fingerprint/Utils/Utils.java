package tv.gitv.ptqy.security.fingerprint.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import java.net.URLEncoder;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.cybergarage.xml.XML;

public class Utils {
    protected static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static native int getEmuInfo(Context context);

    static {
        System.loadLibrary("fingerprint");
    }

    public static String md5(String content) {
        if (content == null) {
            return "";
        }
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            hash = digest.digest(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes2HexString(hash);
    }

    public static String bytes2HexString(byte[] bytes) {
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 15];
        }
        return new String(hexChars);
    }

    public static String encode(String content) {
        if (content == null) {
            return "";
        }
        try {
            return URLEncoder.encode(content, XML.CHARSET_UTF8).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return content;
        }
    }

    @SuppressLint({"NewApi"})
    public static String xorEncrypt(String input, int xorFactor) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        char[] output = input.toCharArray();
        for (int i = 0; i < output.length; i++) {
            output[i] = (char) (output[i] ^ xorFactor);
        }
        return new String(output);
    }

    public static String xorEncryptDefault(String input) {
        return xorEncrypt(input, 11);
    }

    public static String calcHmac(String src) {
        String key = "eade56028e252b77f7a0b8792e58b9cc";
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key.getBytes(), mac.getAlgorithm()));
            return bytes2HexString(mac.doFinal(src.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String notNullString(String s) {
        return !TextUtils.isEmpty(s) ? s : "";
    }
}
