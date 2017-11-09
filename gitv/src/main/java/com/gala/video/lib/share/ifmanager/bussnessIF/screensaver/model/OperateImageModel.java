package com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model;

import com.gala.tvapi.vrs.model.ChannelLabel;
import java.io.Serializable;

public class OperateImageModel implements Serializable {
    private static final long serialVersionUID = 1;
    protected ChannelLabel channelLabel = null;
    protected String imagePath = "";

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ChannelLabel getChannelLabel() {
        return this.channelLabel;
    }

    public void setChannelLabel(ChannelLabel channelLabel) {
        this.channelLabel = channelLabel;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("OperateImageModel{");
        sb.append("imagePath='").append(this.imagePath).append('\'');
        sb.append(", channelLabel=").append(this.channelLabel);
        sb.append('}');
        return sb.toString();
    }
}
