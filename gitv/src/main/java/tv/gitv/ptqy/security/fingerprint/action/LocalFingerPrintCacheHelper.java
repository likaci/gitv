package tv.gitv.ptqy.security.fingerprint.action;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Environment;
import android.util.Base64;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;
import tv.gitv.ptqy.security.fingerprint.LogMgr;
import tv.gitv.ptqy.security.fingerprint.constants.Consts;
import tv.gitv.ptqy.security.fingerprint.entity.FingerPrintData;
import tv.gitv.ptqy.security.fingerprint.exception.FingerPrintExpiredException;
import tv.gitv.ptqy.security.fingerprint.pingback.PingBackAgent;
import tv.gitv.ptqy.security.fingerprint.sharedpreference.PrefUtils;

public class LocalFingerPrintCacheHelper {
    private static final String DFP_FILE_NAME = ".dfp";
    private Context context;

    public LocalFingerPrintCacheHelper(Context context) {
        this.context = context;
    }

    public String readFingerPrintLocalCache() {
        try {
            String dfp = PrefUtils.getFingerprint(this.context);
            long store_time = PrefUtils.getStoreTime(this.context);
            long exp_time = PrefUtils.getExpireTime(this.context);
            if (!(dfp == null || store_time == 0 || exp_time == 0)) {
                if (isExpired(store_time, exp_time)) {
                    LogMgr.i("readFingerPrintLocalCache expire");
                    return null;
                }
                LogMgr.i("readFingerPrintLocalCache not expire dfp: " + dfp);
                return dfp;
            }
        } catch (ClassCastException e) {
            PrefUtils.removeExpireTime(this.context);
            PrefUtils.removeFingerprint(this.context);
            PrefUtils.removeStoreTime(this.context);
            LogMgr.i(Consts.TAG, "SharedPreferences storage type mismatch. Erase all the keys");
        }
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                String dfp_external;
                if (VERSION.SDK_INT >= 19) {
                    File[] dirs = this.context.getExternalFilesDirs(null);
                    if (dirs != null) {
                        for (File dir : dirs) {
                            if (dir != null) {
                                dfp_external = readFromExternal(dir.getAbsolutePath());
                                if (dfp_external != null) {
                                    return dfp_external;
                                }
                            }
                        }
                    }
                }
                File external = Environment.getExternalStorageDirectory();
                if (external != null) {
                    dfp_external = readFromExternal(external.getAbsolutePath());
                    if (dfp_external != null) {
                        return dfp_external;
                    }
                }
            }
        } catch (Exception e2) {
            LogMgr.i(e2.toString());
        }
        return null;
    }

    public void writeFingerPrintLocalCache(FingerPrintData fingerPrintData) {
        int i = 0;
        PrefUtils.setFingerprint(this.context, fingerPrintData.getFingerPrint());
        PrefUtils.setStoreTime(this.context, fingerPrintData.getStoreTime());
        PrefUtils.setExpireTime(this.context, fingerPrintData.getExpireTime());
        boolean isSaveSuccess = false;
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                int length;
                File[] dirs = null;
                if (VERSION.SDK_INT >= 19) {
                    dirs = this.context.getExternalFilesDirs(null);
                    if (dirs != null) {
                        for (File dir : dirs) {
                            if (dir != null) {
                                isSaveSuccess |= write2External(dir.getAbsolutePath(), fingerPrintData);
                            }
                        }
                    }
                }
                File external = Environment.getExternalStorageDirectory();
                if (external == null) {
                    PingBackAgent.saveSaveDfp2StorageError(this.context, "Failed to access external storage found");
                    return;
                } else if (!(isSaveSuccess | write2External(external.getAbsolutePath(), fingerPrintData))) {
                    StringBuilder sb = new StringBuilder();
                    if (dirs != null) {
                        length = dirs.length;
                        while (i < length) {
                            sb.append(dirs[i].getAbsolutePath());
                            sb.append(';');
                            i++;
                        }
                    }
                    sb.append(external.getAbsolutePath());
                    PingBackAgent.saveSaveDfp2StorageError(this.context, "Failed to save dfp to external storage. Trials: " + sb.toString());
                    return;
                } else {
                    return;
                }
            }
            PingBackAgent.saveSaveDfp2StorageError(this.context, "Failed to save dfp to external storage: no external storage found.");
        } catch (Exception e) {
            LogMgr.i(e.toString());
        }
    }

    public boolean write2External(String path, FingerPrintData fingerPrintData) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(new StringBuilder(String.valueOf(path)).append(File.separator).append(DFP_FILE_NAME).toString())));
            writer.write(Base64.encodeToString(("dfp=" + fingerPrintData.getFingerPrint() + "&" + Consts.SEG_STORE_TIME + SearchCriteria.EQ + fingerPrintData.getStoreTime() + "&" + Consts.SEG_EXP_TIME + SearchCriteria.EQ + fingerPrintData.getExpireTime()).getBytes(), 2));
            writer.flush();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String readFromExternal(String path) throws FingerPrintExpiredException {
        File dfp_file = new File(new StringBuilder(String.valueOf(path)).append(File.separator).append(DFP_FILE_NAME).toString());
        if (!dfp_file.exists()) {
            return null;
        }
        LogMgr.i("readFromExternal path: " + dfp_file.getAbsolutePath());
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dfp_file));
            StringBuilder dfp_content = new StringBuilder();
            while (true) {
                String buffer = reader.readLine();
                if (buffer == null) {
                    String line = new String(Base64.decode(dfp_content.toString(), 2));
                    reader.close();
                    return parseDfp(line);
                }
                dfp_content.append(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String parseDfp(java.lang.String r14) {
        /*
        r13 = this;
        r9 = new java.lang.StringBuilder;
        r10 = "parseDfp: ";
        r9.<init>(r10);
        r9 = r9.append(r14);
        r9 = r9.toString();
        tv.gitv.ptqy.security.fingerprint.LogMgr.i(r9);
        r1 = 0;
        r6 = 0;
        r2 = 0;
        r9 = "&";
        r0 = r14.split(r9);
        r10 = r0.length;
        r9 = 0;
    L_0x0021:
        if (r9 < r10) goto L_0x003f;
    L_0x0023:
        if (r1 == 0) goto L_0x0091;
    L_0x0025:
        r10 = 0;
        r9 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1));
        if (r9 == 0) goto L_0x0091;
    L_0x002b:
        r10 = 0;
        r9 = (r2 > r10 ? 1 : (r2 == r10 ? 0 : -1));
        if (r9 == 0) goto L_0x0091;
    L_0x0031:
        r9 = r13.isExpired(r6, r2);
        if (r9 == 0) goto L_0x003e;
    L_0x0037:
        r9 = "dfp expire";
        tv.gitv.ptqy.security.fingerprint.LogMgr.i(r9);
        r1 = 0;
    L_0x003e:
        return r1;
    L_0x003f:
        r8 = r0[r9];
        r11 = "=";
        r5 = r8.split(r11);
        r11 = r5.length;
        r12 = 2;
        if (r11 == r12) goto L_0x004f;
    L_0x004c:
        r9 = r9 + 1;
        goto L_0x0021;
    L_0x004f:
        r11 = 0;
        r4 = r5[r11];
        r11 = r4.hashCode();
        switch(r11) {
            case -1940763729: goto L_0x005a;
            case 99374: goto L_0x006f;
            case 921925483: goto L_0x007c;
            default: goto L_0x0059;
        };
    L_0x0059:
        goto L_0x004c;
    L_0x005a:
        r11 = "exp_time";
        r11 = r4.equals(r11);
        if (r11 == 0) goto L_0x004c;
    L_0x0063:
        r11 = 1;
        r11 = r5[r11];
        r11 = java.lang.Long.valueOf(r11);
        r2 = r11.longValue();
        goto L_0x004c;
    L_0x006f:
        r11 = "dfp";
        r11 = r4.equals(r11);
        if (r11 == 0) goto L_0x004c;
    L_0x0078:
        r11 = 1;
        r1 = r5[r11];
        goto L_0x004c;
    L_0x007c:
        r11 = "store_time";
        r11 = r4.equals(r11);
        if (r11 == 0) goto L_0x004c;
    L_0x0085:
        r11 = 1;
        r11 = r5[r11];
        r11 = java.lang.Long.valueOf(r11);
        r6 = r11.longValue();
        goto L_0x0063;
    L_0x0091:
        r1 = 0;
        goto L_0x003e;
        */
        throw new UnsupportedOperationException("Method not decompiled: tv.gitv.ptqy.security.fingerprint.action.LocalFingerPrintCacheHelper.parseDfp(java.lang.String):java.lang.String");
    }

    public boolean isExpired(long store_time, long exp_time) {
        return System.currentTimeMillis() > exp_time || System.currentTimeMillis() < store_time;
    }

    public String readSharedPreference(String name, String key) {
        return this.context.getSharedPreferences(name, 0).getString(key, null);
    }

    public void writeSharedPreference(String name, String key, String value) {
        Editor editor = this.context.getSharedPreferences(name, 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String readExternalFile(String fileName) {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            if (VERSION.SDK_INT >= 19) {
                File[] dirs = this.context.getExternalFilesDirs(null);
                if (dirs != null) {
                    for (File dir : dirs) {
                        if (dir != null) {
                            String dfp_external = readFromExternal(dir.getAbsolutePath(), fileName);
                            LogMgr.i("dfp_external: " + dfp_external);
                            if (dfp_external != null) {
                                return dfp_external;
                            }
                        }
                    }
                }
            }
            String content = readFromExternal(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
            if (content != null) {
                return content;
            }
        }
        return null;
    }

    public void writeExternalFile(String fileName, String content) {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            if (VERSION.SDK_INT >= 19) {
                File[] dirs = this.context.getExternalFilesDirs(null);
                if (dirs != null) {
                    for (File dir : dirs) {
                        if (dir != null) {
                            write2External(dir.getAbsolutePath(), fileName, content);
                        }
                    }
                }
            }
            write2External(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName, content);
        }
    }

    public String readFromExternal(String path, String fileName) {
        File dfp_file = new File(new StringBuilder(String.valueOf(path)).append(File.separator).append(fileName).toString());
        if (!dfp_file.exists()) {
            return null;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dfp_file));
            String line = reader.readLine();
            reader.close();
            return line;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void write2External(String path, String fileName, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(new StringBuilder(String.valueOf(path)).append(File.separator).append(fileName).toString())));
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
