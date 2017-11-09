package com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting;

import java.util.List;

public interface ISetting {
    List<String> getAllDeviceName();

    List<String> getAllDreamTime();

    List<String> getAllScreenSaveTime();

    List<String> getAudioOutputEntries();

    String getCurrAudioOutputMode();

    String getCurrDRCMode();

    String getCurrDeviceName();

    String getCurrDreamTime();

    String getCurrOutput();

    String getCurrScreenSaveTime();

    List<String> getDRCEntries();

    SystemInfo getInfo();

    List<String> getOutputEntries();

    boolean goToAutoTest();

    void goToNetworkSettings();

    void goToPositionSetting();

    void restoreFactory();

    void setAudioOutputMode(String str);

    void setDRCMode(String str);

    void setDeviceName(String str);

    void setDreamTime(String str);

    void setOutputDisplay(String str);

    void setScreenSaverTime(String str);
}
