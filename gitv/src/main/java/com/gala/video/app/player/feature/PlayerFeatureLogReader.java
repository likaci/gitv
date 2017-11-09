package com.gala.video.app.player.feature;

import android.os.Process;
import android.util.Log;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class PlayerFeatureLogReader {
    private static final String TAG = "LogcatReader";

    public String getLogcatBuffer() {
        Log.i(TAG, "getLogcatBufferStr");
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            List<String> args = getLogcatArgs();
            args.add("-d");
            Process process = getProcess(args);
            reader = getBufferReader(process);
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line).append("\n");
            }
            destroy(process);
            Log.v(TAG, "destroyed dump logcat process");
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.v(TAG, "unexpected exception", e);
                }
            }
        } catch (IOException e2) {
            Log.v(TAG, "unexpected exception", e2);
            destroy(null);
            Log.v(TAG, "destroyed dump logcat process");
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e22) {
                    Log.v(TAG, "unexpected exception", e22);
                }
            }
        } catch (Throwable th) {
            destroy(null);
            Log.v(TAG, "destroyed dump logcat process");
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e222) {
                    Log.v(TAG, "unexpected exception", e222);
                }
            }
        }
        return sb.toString();
    }

    private static void destroy(Process process) {
        if (process != null) {
            int pid = getProcessId(process);
            if (pid != 0) {
                try {
                    Process.killProcess(pid);
                } catch (Exception e) {
                    try {
                        process.destroy();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private static int getProcessId(Process process) {
        String str = process.toString();
        try {
            return Integer.parseInt(str.substring(str.indexOf(SearchCriteria.EQ) + 1, str.indexOf(AlbumEnterFactory.SIGN_STR)));
        } catch (Exception e) {
            return 0;
        }
    }

    private static BufferedReader getBufferReader(Process process) throws IOException {
        return new BufferedReader(new InputStreamReader(process.getInputStream()), 8192);
    }

    private static Process getProcess(List<String> args) throws IOException {
        Log.i(TAG, "args = " + args);
        return Runtime.getRuntime().exec((String[]) toArray(args, String.class));
    }

    private static <T> T[] toArray(List<T> list, Class<T> clazz) {
        Object[] result = (Object[]) ((Object[]) Array.newInstance(clazz, list.size()));
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    private static List<String> getLogcatArgs() {
        return new ArrayList(Arrays.asList(new String[]{"logcat", "-v", "threadtime"}));
    }
}
