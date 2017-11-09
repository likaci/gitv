package com.gala.tvapi.vrs.model;

import java.util.List;

public class PlayAlbum extends Model {
    private static final long serialVersionUID = 1;
    public String albumName = "";
    public String albumQipuId = "";
    public int boss;
    public int categoryId = 0;
    public List<String> categoryNames;
    public String contentId = "";
    public String contentName = "";
    public String contentType = "FEATURE_FILM";
    public List<Contributors> contributors;
    public List<Contributors> creators;
    public String currentPeriod = "";
    public String drmTypes;
    public String dynamicRange;
    public int is1080P = 0;
    public int is720P = 0;
    public int isD3 = 0;
    public int isDownload = 0;
    public int isDubi = 0;
    public int isExclusive = 0;
    public int isPurchase = 0;
    public int isSeries = 0;
    public String issueTime = "";
    public int latestOrder = -1;
    public int order = -1;
    public int payMark;
    public String period = "";
    public String picUrl = "";
    public String playLength = "";
    public String postImage = "";
    public String prompt = "";
    public int purchaseType;
    public String score = "";
    public String shortTitle;
    public String sourceId = "";
    public int tvCount = 0;
    public String tvDesc = "";
    public String tvQipuId = "";
    public int tvType = 0;
    public String vid = "";
}
