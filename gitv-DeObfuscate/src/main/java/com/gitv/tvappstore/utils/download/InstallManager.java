package com.gitv.tvappstore.utils.download;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.push.mqttv3.internal.ClientDefaults;
import java.io.File;
import java.lang.reflect.Method;

public class InstallManager {
    private static final String TAG = "InstallManager";
    private BroadcastReceiver mBroadcastReceiver = new C19051();
    private Context mContext;
    private InstallListener mListener;

    class C19051 extends BroadcastReceiver {
        C19051() {
        }

        public void onReceive(Context context, Intent intent) {
            Log.d(InstallManager.TAG, "InstallManager---mBroadcastReceiver >>>> ---onReceive()---");
            String action = intent.getAction();
            String packageName = InstallManager.this.getReceiverPackageName(intent.getDataString(), 8);
            if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
                InstallManager.this.mListener.onAdd(packageName);
            } else if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
                InstallManager.this.mListener.onRemove(packageName);
            } else if ("android.intent.action.PACKAGE_REPLACED".equals(action)) {
                InstallManager.this.mListener.onReplace(packageName);
            }
        }
    }

    public InstallManager(Context context, InstallListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public void register() {
        Log.d(TAG, "InstallManager---initBroadcastReceiver() >>>> ---");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addDataScheme("package");
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
    }

    public void unregister() {
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
    }

    public boolean install(String path) {
        if (!runCommand("chmod 775 " + path + "\n")) {
            Log.d(TAG, "未获得权限，安装失败！");
            return false;
        } else if (silentInstall(path)) {
            return true;
        } else {
            try {
                File file = new File(path);
                Intent intent = new Intent();
                intent.addFlags(ClientDefaults.MAX_MSG_SIZE);
                intent.setAction("android.intent.action.VIEW");
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                this.mContext.startActivity(intent);
                return false;
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this.mContext, "安装失败！", 0).show();
                return false;
            }
        }
    }

    private boolean silentInstall(String path) {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Object packageManager = getMethod(activityThreadClass, "getPackageManager").invoke(activityThreadClass, new Object[0]);
            getMethod(packageManager.getClass(), "installPackage").invoke(packageManager, new Object[]{getPackageUri(path), null, Integer.valueOf(0), null});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Method getMethod(Class<?> cls, String methodName) {
        Method method = null;
        try {
            Method[] methods = cls.getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals(methodName)) {
                    method = cls.getMethod(methodName, methods[i].getParameterTypes());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return method;
    }

    private Uri getPackageUri(String path) {
        return Uri.fromFile(new File(path));
    }

    private String getReceiverPackageName(String packageName, int start) {
        if (packageName == null || !packageName.isEmpty()) {
            return packageName.substring(start);
        }
        Log.d(TAG, "InstallManager---getReceiverPackageName() >>>> packageName is null---");
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean runCommand(java.lang.String r6) {
        /*
        r1 = 0;
        r2 = java.lang.Runtime.getRuntime();	 Catch:{ Exception -> 0x0027 }
        r1 = r2.exec(r6);	 Catch:{ Exception -> 0x0027 }
        r2 = "RootCmdUtils";
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0027 }
        r4 = "The Command is : ";
        r3.<init>(r4);	 Catch:{ Exception -> 0x0027 }
        r3 = r3.append(r6);	 Catch:{ Exception -> 0x0027 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x0027 }
        android.util.Log.i(r2, r3);	 Catch:{ Exception -> 0x0027 }
        r1.waitFor();	 Catch:{ Exception -> 0x0027 }
        r1.destroy();	 Catch:{ Exception -> 0x0084 }
    L_0x0025:
        r2 = 1;
    L_0x0026:
        return r2;
    L_0x0027:
        r0 = move-exception;
        r2 = "RootCmdUtils ";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0063 }
        r4 = "Unexpected error - ";
        r3.<init>(r4);	 Catch:{ all -> 0x0063 }
        r4 = r0.getMessage();	 Catch:{ all -> 0x0063 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x0063 }
        r3 = r3.toString();	 Catch:{ all -> 0x0063 }
        android.util.Log.w(r2, r3);	 Catch:{ all -> 0x0063 }
        r1.destroy();	 Catch:{ Exception -> 0x0047 }
    L_0x0045:
        r2 = 0;
        goto L_0x0026;
    L_0x0047:
        r0 = move-exception;
        r2 = "RootCmdUtils ";
        r3 = new java.lang.StringBuilder;
        r4 = "Unexpected error - ";
        r3.<init>(r4);
        r4 = r0.getMessage();
        r3 = r3.append(r4);
        r3 = r3.toString();
        android.util.Log.w(r2, r3);
        goto L_0x0045;
    L_0x0063:
        r2 = move-exception;
        r1.destroy();	 Catch:{ Exception -> 0x0068 }
    L_0x0067:
        throw r2;
    L_0x0068:
        r0 = move-exception;
        r3 = "RootCmdUtils ";
        r4 = new java.lang.StringBuilder;
        r5 = "Unexpected error - ";
        r4.<init>(r5);
        r5 = r0.getMessage();
        r4 = r4.append(r5);
        r4 = r4.toString();
        android.util.Log.w(r3, r4);
        goto L_0x0067;
    L_0x0084:
        r0 = move-exception;
        r2 = "RootCmdUtils ";
        r3 = new java.lang.StringBuilder;
        r4 = "Unexpected error - ";
        r3.<init>(r4);
        r4 = r0.getMessage();
        r3 = r3.append(r4);
        r3 = r3.toString();
        android.util.Log.w(r2, r3);
        goto L_0x0025;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gitv.tvappstore.utils.download.InstallManager.runCommand(java.lang.String):boolean");
    }
}
