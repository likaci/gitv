package com.gala.multiscreen.dmr.logic.listener;

import com.gala.android.dlna.sdk.mediarenderer.MediaRenderer;
import com.gala.android.dlna.sdk.mediarenderer.service.infor.AVTransportConstStr;
import com.gala.android.dlna.sdk.mediarenderer.service.infor.RenderingControlConstStr;

public class StandardListener {
    private MediaRenderer mMediaRenderer;

    public void init(MediaRenderer mediaRenderer) {
        this.mMediaRenderer = mediaRenderer;
    }

    public void setMute(boolean isMute) {
        this.mMediaRenderer.getLastChangeListener().lastChange("urn:schemas-upnp-org:service:RenderingControl:1", RenderingControlConstStr.MUTE, Boolean.valueOf(isMute));
    }

    public void setVolume(int volume) {
        this.mMediaRenderer.getLastChangeListener().lastChange("urn:schemas-upnp-org:service:RenderingControl:1", RenderingControlConstStr.VOLUME, Integer.valueOf(volume));
    }

    public void setSeek(long seek) {
        this.mMediaRenderer.getLastChangeListener().lastChange("urn:schemas-upnp-org:service:AVTransport:1", AVTransportConstStr.SEEK, Long.valueOf(seek));
    }
}
