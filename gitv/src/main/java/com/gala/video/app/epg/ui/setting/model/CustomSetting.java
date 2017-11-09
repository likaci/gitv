package com.gala.video.app.epg.ui.setting.model;

import com.gala.video.lib.framework.core.proguard.Keep;
import java.util.List;

@Keep
public class CustomSetting {
    public List<CustomSettingItem> about;
    public List<String> aboutdev;
    public List<CustomSettingItem> common;
    public List<CustomSettingItem> display;
    public List<CustomSettingItem> feedback;
    public List<CustomSettingItem> network;
    public List<CustomSettingItem> play;
    public List<CustomSettingItem> signal;
    public String suffix;
    public List<CustomSettingItem> upgrade;
    public String ver;
}
