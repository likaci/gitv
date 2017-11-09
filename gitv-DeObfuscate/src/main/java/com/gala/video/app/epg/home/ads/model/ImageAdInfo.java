package com.gala.video.app.epg.home.ads.model;

import com.mcto.ads.CupidAd;

public class ImageAdInfo {
    private int mAdId;
    private CupidAd mCupiAd;
    private String mImageUrl;
    private String mSkippable = "";

    public ImageAdInfo(String imageUrl, int adid, CupidAd ad, String skippable) {
        this.mImageUrl = imageUrl;
        this.mAdId = adid;
        this.mCupiAd = ad;
        this.mSkippable = skippable;
    }

    public String getImageUrl() {
        return this.mImageUrl;
    }

    public int getAdid() {
        return this.mAdId;
    }

    public boolean canSkip() {
        if (this.mSkippable == null || !this.mSkippable.equals("true")) {
            return false;
        }
        return true;
    }

    public CupidAd getCupiAd() {
        return this.mCupiAd;
    }

    public String toString() {
        return "ImageAdInfo{ adid = " + this.mAdId + ",imageurl = " + this.mImageUrl + ", can skip" + this.mSkippable + "}";
    }
}
