package com.gala.video.lib.framework.core.utils;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConvertUtil {
    private static final String TAG = "ConvertUtil";

    public static String getStringFromInputStream(InputStream is) {
        Exception e;
        Throwable th;
        StringBuffer sb = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while (true) {
                try {
                    String line = bufferedReader2.readLine();
                    if (line != null) {
                        sb.append(line);
                    } else {
                        try {
                            break;
                        } catch (Exception e2) {
                            Log.i(TAG, e2.getMessage());
                            bufferedReader = bufferedReader2;
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                    bufferedReader = bufferedReader2;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = bufferedReader2;
                }
            }
            bufferedReader2.close();
            bufferedReader = bufferedReader2;
        } catch (Exception e4) {
            e = e4;
            try {
                Log.i(TAG, e.getMessage());
                try {
                    bufferedReader.close();
                } catch (Exception e22) {
                    Log.i(TAG, e22.getMessage());
                }
                return sb.toString();
            } catch (Throwable th3) {
                th = th3;
                try {
                    bufferedReader.close();
                } catch (Exception e222) {
                    Log.i(TAG, e222.getMessage());
                }
                throw th;
            }
        }
        return sb.toString();
    }
}
