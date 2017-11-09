package com.gala.video.lib.share.pingback;

public class HomeAdPingbackModel {
    public static final int DEFAULT_H5_ENTER_TYPE = -1024;
    private static final String TAG = "pingback/HomeAdPingbackModel";
    private String carouselFrom = "";
    private String carouselTabSource = "";
    private int h5EnterType = DEFAULT_H5_ENTER_TYPE;
    private String h5From = "";
    private String h5TabSrc = "";
    private String plBuySource = "";
    private String plFrom = "";
    private String plTabSrc = "";
    private String videoBuySource = "";
    private String videoFrom = "";
    private String videoTabSource = "";

    public String getH5From() {
        return this.h5From;
    }

    public void setH5From(String h5From) {
        this.h5From = h5From;
    }

    public String getPlFrom() {
        return this.plFrom;
    }

    public void setPlFrom(String plFrom) {
        this.plFrom = plFrom;
    }

    public String getPlBuySource() {
        return this.plBuySource;
    }

    public void setPlBuySource(String plBuySource) {
        this.plBuySource = plBuySource;
    }

    public String getVideoFrom() {
        return this.videoFrom;
    }

    public void setVideoFrom(String videoFrom) {
        this.videoFrom = videoFrom;
    }

    public String getVideoBuySource() {
        return this.videoBuySource;
    }

    public void setVideoBuySource(String videoBuySource) {
        this.videoBuySource = videoBuySource;
    }

    public String getVideoTabSource() {
        return this.videoTabSource;
    }

    public void setVideoTabSource(String videoTabSource) {
        this.videoTabSource = videoTabSource;
    }

    public String getCarouselTabSource() {
        return this.carouselTabSource;
    }

    public void setCarouselTabSource(String carouselTabSource) {
        this.carouselTabSource = carouselTabSource;
    }

    public String getCarouselFrom() {
        return this.carouselFrom;
    }

    public void setCarouselFrom(String carouselFrom) {
        this.carouselFrom = carouselFrom;
    }

    public int getH5EnterType() {
        return this.h5EnterType;
    }

    public void setH5EnterType(int h5EnterType) {
        this.h5EnterType = h5EnterType;
    }

    public String getPlTabSrc() {
        return this.plTabSrc;
    }

    public void setPlTabSrc(String plTabSrc) {
        this.plTabSrc = plTabSrc;
    }

    public String getH5TabSrc() {
        return this.h5TabSrc;
    }

    public void setH5TabSrc(String h5TabSrc) {
        this.h5TabSrc = h5TabSrc;
    }

    public String toString() {
        return "HomeAdPingbackModel{h5From='" + this.h5From + '\'' + ", h5EnterType=" + this.h5EnterType + ", plFrom='" + this.plFrom + '\'' + ", plBuySource='" + this.plBuySource + '\'' + ", videoFrom='" + this.videoFrom + '\'' + ", videoBuySource='" + this.videoBuySource + '\'' + ", videoTabSource='" + this.videoTabSource + '\'' + ", carouselTabSource='" + this.carouselTabSource + '\'' + ", carouselFrom='" + this.carouselFrom + '\'' + '}';
    }
}
