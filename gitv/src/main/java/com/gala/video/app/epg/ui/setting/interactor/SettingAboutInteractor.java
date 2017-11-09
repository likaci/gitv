package com.gala.video.app.epg.ui.setting.interactor;

import java.util.List;
import java.util.Map;

public interface SettingAboutInteractor {
    String getCableMac();

    List<String> getCustomData();

    String getDNS();

    String getPrivateIP();

    String getPublicIP();

    String getSoftVersion();

    Map<String, String> getSystemData();

    String getSystemVersion();

    String getWirelessMac();

    void savePublicIP(String str);
}
