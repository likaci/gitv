package com.tvos.appmanager.jni;

import android.os.Bundle;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import java.util.Observable;
import java.util.Observer;

public class AppManagerObserver implements Observer {
    public void update(Observable observable, Object data) {
        Bundle bd = (Bundle) data;
        if (!bd.isEmpty()) {
            AppManagerJNI.updatePkgStatus(bd.getString(SettingConstants.ACTION_TYPE_PACKAGE_NAME), bd.getInt("action"));
        }
    }
}
