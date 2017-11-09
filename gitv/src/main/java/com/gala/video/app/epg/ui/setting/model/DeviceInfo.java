package com.gala.video.app.epg.ui.setting.model;

import com.gala.video.lib.framework.core.proguard.Keep;
import java.util.List;

@Keep
public class DeviceInfo {
    private List<String> infos;

    public List<String> getInfos() {
        return this.infos;
    }

    public void setInfos(List<String> infos) {
        this.infos = infos;
    }
}
