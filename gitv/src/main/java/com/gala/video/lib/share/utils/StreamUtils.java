package com.gala.video.lib.share.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class StreamUtils {
    public static String convertStreamToString(InputStream is) {
        try {
            Scanner s = new Scanner(is).useDelimiter("\\A");
            String out = s.hasNext() ? s.next() : "";
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            return out;
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e42) {
                    e42.printStackTrace();
                }
            }
        }
    }
}
