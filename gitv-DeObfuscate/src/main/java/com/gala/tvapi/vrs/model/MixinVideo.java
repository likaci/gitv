package com.gala.tvapi.vrs.model;

import java.util.List;

public class MixinVideo extends Model {
    private static final long serialVersionUID = 1;
    public String albumId = "";
    public String albumImageUrl = "";
    public String albumName = "";
    public String albumQipuId = "";
    public List<Categorie> categories;
    public int channelId = -1;
    public int contentType = 1;
    public String description = "";
    public int dimension = 2;
    public int dolby = 0;
    public int downloadAllowed = 0;
    public String drmTypes;
    public String duration = "";
    public int exclusive = 0;
    public String focus = "";
    public int isPurchase = 0;
    public String issueTime = "";
    public int latestOrder = -1;
    public int mode1080p = 0;
    public int mode720p = 0;
    public String name = "";
    public int order = -1;
    public int payMark = 0;
    public String period = "";
    public String playCount = "";
    public String posterUrl = "";
    public int purchaseType = 0;
    public String qipuId = "";
    public String score = "";
    public int series = 0;
    public String shortTitle = "";
    public String sourceId = "";
    public String sourceName = "";
    public String vid = "";
    public int videoCount = 0;
    public String videoImageUrl = "";
}
