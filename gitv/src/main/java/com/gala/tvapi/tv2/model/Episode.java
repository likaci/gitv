package com.gala.tvapi.tv2.model;

public class Episode extends Model {
    private static final long serialVersionUID = 1;
    public String albumName = "";
    public Cast cast;
    public String desc = "";
    public String drm;
    public String dynamicRanges;
    public String epProbation = "0";
    public String focus = "";
    public String len = "";
    public String name = "";
    public int order = -1;
    public String pic = "";
    public String shortName = "";
    public int tvIsPurchase = 0;
    public String tvQid = "";
    public int type = 1;
    public String vid = "";
    public VipInfo vipInfo;
    public String year = "";

    public boolean isFlower() {
        return this.type != 1;
    }
}
