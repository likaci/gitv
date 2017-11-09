package com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model;

import com.gala.tvapi.tv2.model.Album;
import java.util.List;

public class DailyLabelModel extends HomeModel {
    private static final long serialVersionUID = 1;
    public List<Album> mDailyNewModelList;
    public String mLabelArea;
    public String mLabelId;
    public String mLabelImageUrl;
    public String mLabelName;

    public DailyLabelModel(String id, String name) {
        this.mLabelId = id;
        this.mLabelName = name;
    }

    public void setArea(String area) {
        this.mLabelArea = area;
    }
}
