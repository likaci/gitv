package com.gala.video.app.epg.home.ads.model;

import java.util.Arrays;

public class AdResInfo {
    private String creativeId = "";
    private String[] creativeUrl;
    private VideoResUrl dynamicUrl;
    private String expires = "";
    private String templateType = "";

    public static class VideoResUrl {
        public String[] video;

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (this.video != null) {
                for (String item : this.video) {
                    sb.append("dynamic url : ").append(item).append(",");
                }
            }
            return sb.toString();
        }
    }

    public AdResInfo(String templateType, String expires, String creativeId, String[] creativeUrl) {
        this.templateType = templateType;
        this.expires = expires;
        this.creativeId = creativeId;
        this.creativeUrl = creativeUrl;
    }

    public String getTemplateType() {
        return this.templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getExpires() {
        return this.expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getCreativeId() {
        return this.creativeId;
    }

    public void setCreativeId(String creativeId) {
        this.creativeId = creativeId;
    }

    public String[] getCreativeUrl() {
        return this.creativeUrl;
    }

    public void setCreativeUrl(String[] creativeUrl) {
        this.creativeUrl = creativeUrl;
    }

    public void setDynamicUrl(VideoResUrl creativeUrl) {
        this.dynamicUrl = creativeUrl;
    }

    public VideoResUrl getDynamicUrl() {
        return this.dynamicUrl;
    }

    public String toString() {
        return "AdResInfo{templateType='" + this.templateType + '\'' + ", expires='" + this.expires + '\'' + ", creativeId='" + this.creativeId + '\'' + ", creativeUrl=" + Arrays.toString(this.creativeUrl) + ", dynamicUrl=" + this.dynamicUrl + '}';
    }
}
