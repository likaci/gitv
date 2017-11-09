package com.gala.video.lib.share.common.model;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.model.DailyLabel;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TabDataItem implements Serializable {
    private static final long serialVersionUID = 1;
    private DailyLabel mLabel;
    private String mLabelImageUrl;
    private String mLabelName;
    private ArrayList<Album> mTabContentAlbumsList = new ArrayList();

    public DailyLabel getDailyLabel() {
        return this.mLabel;
    }

    public ArrayList<Album> getAlbumList() {
        return this.mTabContentAlbumsList;
    }

    public void setLabel(DailyLabel label) {
        this.mLabel = label;
    }

    public void setTabContentAlbumsList(List<Album> tabContentAlbumsList) {
        this.mTabContentAlbumsList = new ArrayList(tabContentAlbumsList);
    }

    public void setLabelImageUrl(String url) {
        this.mLabelImageUrl = url;
    }

    public String getLabelImageUrl() {
        return this.mLabelImageUrl;
    }

    public void setLabelName(String name) {
        this.mLabelName = name;
    }

    public String getLabelName() {
        return this.mLabelName;
    }

    public String toString() {
        return "TabDataItem [mLabel=" + this.mLabel + ", mLabelImageUrl=" + this.mLabelImageUrl + ", mLabelName=" + this.mLabelName + (this.mTabContentAlbumsList != null ? this.mTabContentAlbumsList.size() : 0) + AlbumEnterFactory.SIGN_STR;
    }

    public TabDataItem copyFrom() {
        TabDataItem newTabDataItem = new TabDataItem();
        newTabDataItem.mLabel = this.mLabel;
        newTabDataItem.mLabelImageUrl = this.mLabelImageUrl;
        newTabDataItem.mLabelName = this.mLabelName;
        newTabDataItem.mTabContentAlbumsList = new ArrayList(this.mTabContentAlbumsList);
        return newTabDataItem;
    }
}
