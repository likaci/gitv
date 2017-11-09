package com.gala.afinal.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileNameGenerator {
    public static String generator(String key) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(key.getBytes());
            return bytesToHexString(instance.digest());
        } catch (NoSuchAlgorithmException e) {
            return String.valueOf(key.hashCode());
        }
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            String toHexString = Integer.toHexString(b & 255);
            if (toHexString.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(toHexString);
        }
        return stringBuilder.toString();
    }
}
