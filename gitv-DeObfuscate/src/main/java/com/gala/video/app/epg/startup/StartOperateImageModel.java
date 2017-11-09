package com.gala.video.app.epg.startup;

import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.OperateImageModel;
import java.io.Serializable;

public class StartOperateImageModel extends OperateImageModel implements Serializable {
    private static final long serialVersionUID = 1;

    public String toString() {
        StringBuilder sb = new StringBuilder("ExitOperateImageModel{");
        sb.append("imagePath='").append(this.imagePath).append('\'');
        sb.append(", channelLabel=").append(this.channelLabel);
        sb.append('}');
        return sb.toString();
    }
}
