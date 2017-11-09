package com.gala.video.app.epg.project.config.skyworth.i71s_plus;

import com.gala.sdk.player.constants.PlayerCodecType;
import com.gala.video.app.epg.project.config.skyworth.ConfigSkyworthBase;

public class DeviceConfig extends ConfigSkyworthBase {
    public boolean shouldChangeSurfaceFormat() {
        return true;
    }

    public boolean is4kH265StreamSupported() {
        return false;
    }

    public String getCommonSettingJsonRoot() {
        if (isSupportContentProvider()) {
            return super.getCommonSettingJsonRoot();
        }
        return "setting/home/i71s_plus/";
    }

    public PlayerCodecType getDecodeType() {
        return PlayerCodecType.ACC_By_MediaCodec;
    }

    public String getPlaySettingJsonPath() {
        if (isSupportContentProvider()) {
            return super.getPlaySettingJsonPath();
        }
        return isHomeVersion() ? getCommonSettingJsonRoot() : getSettingJsonRoot();
    }
}
