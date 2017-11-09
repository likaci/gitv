package com.gala.tvapi.tv2.model;

import java.util.List;

public class StreamKind extends Model {
    private static final long serialVersionUID = 1;
    public String albumName;
    public int albumType = 0;
    public String endTime = "";
    public int exclusivePlay = 0;
    public String isMore;
    public int isPurchase = 0;
    public String sourceCode = "";
    public String startTime = "";
    public int total;
    public String tvName;
    public int update;
    public List<Version> versions;
    public String videoOrder;
    public String vrsAlbumId;
    public String vrsChnId;
    public String vrsTvId;

    public boolean isPurchase() {
        return this.isPurchase != 0;
    }

    public boolean isExclusive() {
        return this.exclusivePlay != 0;
    }
}
