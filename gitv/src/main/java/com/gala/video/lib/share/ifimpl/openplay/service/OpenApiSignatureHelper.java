package com.gala.video.lib.share.ifimpl.openplay.service;

import java.util.Random;

class OpenApiSignatureHelper {
    private static final char[] CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final char[] CHAR_MAP = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '.', '_'};
    private static final String DEFAULT_ACTIONKEY = "k9hdn739j5shrqid";
    private static final String DEFAULT_CUSTOMERNAME = "gala";
    private static final String DETAULT_SIGNATUREKEY = "k9hdn739j5shrqidyqdy49bq1ef48xlnjv1pt8kouogobvrv";
    private static final char[] ENCRYPT = new char[]{'v', 'r', 'n', 'k', '4', 'b', 'e', 'q', '3', 'x', 'p', 'h', 'y', 'i', '8', '7', '#', 'm', 'o', 'a', '&', 'w', 'j', 'd', '1', 's', 'g', '9', '2', '5', 't', '6', 'z', 'c', 'u', 'f', 'l', '0'};
    private static final int SERVER_ACTION_KEY_LENGTH = 16;
    private static final int SIGNATURE_COUNT_LENGTH = 2;
    private static final int SIGNATURE_LENGTH = 28;
    private static final String TAG = "OpenApiSignatureHelper";
    private static final String VERSION = "01";

    public static class Signature {
        private String mSignature4Client;
        private String mSignature4Pak;

        public Signature(String client, String apk) {
            this.mSignature4Client = client;
            this.mSignature4Pak = apk;
        }

        public String getSignature4Client() {
            return this.mSignature4Client;
        }

        public String getSignature4Pak() {
            return this.mSignature4Pak;
        }
    }

    OpenApiSignatureHelper() {
    }

    public static Signature createSignature(String customerName) {
        String actionKey = DEFAULT_ACTIONKEY;
        String signatureKey = DETAULT_SIGNATUREKEY;
        try {
            actionKey = getRandomStr(CHARS, 16);
            signatureKey = actionKey + encrypt(customerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Signature(signatureKey, actionKey);
    }

    public static String decryptCustomerName(String signature) {
        String customerName = "gala";
        try {
            customerName = decrypt(signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerName;
    }

    private static String encrypt(String customerName) {
        int customerNameLength = customerName.length();
        if (customerNameLength < 28) {
            customerName = customerName + getRandomStr(CHAR_MAP, 28 - customerNameLength);
        }
        String reverseStr = reverse(VERSION + intToHexString(customerNameLength) + customerName);
        int length = reverseStr.length();
        char[] encryptChars = new char[length];
        for (int i = 0; i < length; i++) {
            encryptChars[i] = ENCRYPT[indexOf(CHAR_MAP, reverseStr.charAt(i))];
        }
        return new String(encryptChars);
    }

    private static String decrypt(String str) {
        int length = str.length();
        char[] decryptChars = new char[length];
        for (int i = 0; i < length; i++) {
            decryptChars[i] = CHAR_MAP[indexOf(ENCRYPT, str.charAt(i))];
        }
        String decryptStr = reverse(new String(decryptChars));
        String versionStr = decryptStr.substring(0, VERSION.length());
        return decryptStr.substring(VERSION.length() + 2, (VERSION.length() + 2) + Integer.valueOf(decryptStr.substring(VERSION.length(), VERSION.length() + 2), 16).intValue());
    }

    private static String getRandomStr(char[] chars, int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            builder.append(chars[random.nextInt(chars.length)]);
        }
        return builder.toString();
    }

    private static String intToHexString(int value) {
        String str = Integer.toHexString(value);
        if (str == null || str.length() >= 2) {
            return str;
        }
        return "0" + str;
    }

    private static int indexOf(char[] chars, char c) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) {
                return i;
            }
        }
        return -1;
    }

    private static String reverse(String source) {
        int length = source.length();
        char[] target = new char[length];
        for (int i = 0; i < length; i++) {
            target[i] = source.charAt((length - 1) - i);
        }
        return new String(target);
    }
}
