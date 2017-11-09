package tv.gitv.ptqy.security.fingerprint.Utils;

import android.content.Context;
import android.content.pm.PackageManager;

public class PermissionUtil {
    public static boolean hasSdcardWritePerm(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        return pm.checkPermission("android.permission.MOUNT_UNMOUNT_FILESYSTEMS", ctx.getPackageName()) == 0 && pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", ctx.getPackageName()) == 0;
    }
}
