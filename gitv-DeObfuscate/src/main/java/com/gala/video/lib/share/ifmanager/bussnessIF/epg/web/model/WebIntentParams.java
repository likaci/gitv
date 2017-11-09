package com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model;

import com.gala.tvapi.tv2.model.Album;
import java.io.Serializable;

public class WebIntentParams implements Serializable {
    private static final long serialVersionUID = 1;
    public Album albumInfo;
    public String buyFrom;
    public String buySource;
    public int buyVip = 0;
    public String couponActivityCode;
    public String couponSignKey;
    public int enterType;
    public String eventId;
    public String from;
    public String id;
    public String incomesrc;
    public String name;
    public int pageType;
    public String pageUrl;
    public int requestCode;
    public String resGroupId;
    public String state;
    public String tabSrc;
}
