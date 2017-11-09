package com.gala.video.app.epg.ui.imsg.utils;

import android.content.Context;
import android.content.ContextWrapper;

public class Permissionschecker {
    private final Context mContext;
    private ContextWrapper mContextWrapper = new ContextWrapper(this.mContext);

    public Permissionschecker(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean lacksPermission(String permission) {
        return this.mContextWrapper.checkCallingOrSelfPermission(permission) == -1;
    }
}
