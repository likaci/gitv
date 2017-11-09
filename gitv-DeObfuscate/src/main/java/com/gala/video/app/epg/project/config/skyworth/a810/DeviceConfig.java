package com.gala.video.app.epg.project.config.skyworth.a810;

import com.gala.video.app.epg.project.config.skyworth.ConfigSkyworthBase;

public class DeviceConfig extends ConfigSkyworthBase {
    public boolean shouldChangeSurfaceFormat() {
        return true;
    }

    public boolean is4kH265StreamSupported() {
        return false;
    }
}
