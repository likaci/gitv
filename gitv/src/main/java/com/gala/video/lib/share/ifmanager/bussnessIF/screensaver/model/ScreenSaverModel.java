package com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model;

import java.io.Serializable;

public class ScreenSaverModel extends OperateImageModel implements Serializable {
    private static final long serialVersionUID = 1;
    private String mImageName = "";

    public String getImageName() {
        return this.mImageName;
    }

    public void setImageName(String imageName) {
        this.mImageName = imageName;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ScreenSaverModel{");
        sb.append("imagePath='").append(this.imagePath).append('\'');
        sb.append("imageName='").append(this.mImageName).append('\'');
        sb.append(", channelLabel=").append(this.channelLabel);
        sb.append('}');
        return sb.toString();
    }
}
