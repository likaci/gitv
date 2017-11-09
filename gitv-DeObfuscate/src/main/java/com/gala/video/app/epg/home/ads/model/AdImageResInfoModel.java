package com.gala.video.app.epg.home.ads.model;

import java.util.Arrays;

public class AdImageResInfoModel {
    private AdResInfo[] data;
    private String version = "";

    public AdImageResInfoModel(AdResInfo[] data, String version) {
        this.data = data;
        this.version = version;
    }

    public AdResInfo[] getData() {
        return this.data;
    }

    public void setData(AdResInfo[] data) {
        this.data = data;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String toString() {
        return "AdImageResInfoModel{data=" + Arrays.toString(this.data) + ", version='" + this.version + '\'' + '}';
    }
}
