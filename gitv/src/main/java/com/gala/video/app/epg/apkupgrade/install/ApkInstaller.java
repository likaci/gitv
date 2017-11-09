package com.gala.video.app.epg.apkupgrade.install;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.push.mqttv3.internal.ClientDefaults;
import java.io.File;
import java.io.IOException;

public class ApkInstaller {
    private final String TAG = "ApkInstaller";

    public boolean install(Context context, String path, long fileLength) {
        LogUtils.d("ApkInstaller", "path=" + path + ",filelength=" + fileLength);
        File file = new File(path);
        try {
            if (!file.exists()) {
                return false;
            }
            if (file.length() != fileLength) {
                file.delete();
                return false;
            }
            esureWritePermission(file);
            if (Project.getInstance().getBuild().isHomeVersion() && checkInstallTool(context)) {
                LogUtils.d("ApkInstaller", "find install pakeage!");
                installByPackage(file, context);
            } else {
                installBySystem(file, context);
            }
            return true;
        } catch (Exception e) {
            file.delete();
            e.printStackTrace();
            return false;
        }
    }

    private void esureWritePermission(File file) throws IOException {
        Runtime.getRuntime().exec("chmod 777 " + file.getAbsolutePath());
    }

    private boolean checkInstallTool(Context context) {
        if (context.getPackageManager().queryIntentActivities(new Intent("com.gala.video.tools.installer.INSTALL_APK"), 0).isEmpty()) {
            return false;
        }
        return true;
    }

    private void installBySystem(File file, Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        PageIOUtils.activityIn(context, intent);
    }

    private void installByPackage(File file, Context context) {
        Intent intent = new Intent("com.gala.video.tools.installer.INSTALL_APK");
        intent.putExtra("apk_path", file.getAbsolutePath());
        intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
        PageIOUtils.activityIn(context, intent);
    }
}
