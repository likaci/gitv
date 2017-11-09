package com.gala.video.app.epg.home.data.model;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.util.List;

public class AppDataSet {
    private List<AppDataInfo> app_detail_list;
    private AppDataInfo appstore;
    private List<CollectAppData> collection_list;
    private List<FocusAppData> focus_info_list;
    private AppDataInfo gamestore;

    public List<CollectAppData> getCollectionAppList() {
        return this.collection_list;
    }

    public List<FocusAppData> getFocusApplist() {
        return this.focus_info_list;
    }

    public AppDataInfo getGamestore() {
        return this.gamestore;
    }

    public AppDataInfo getAppstore() {
        return this.appstore;
    }

    public List<AppDataInfo> getAppDataInfolist() {
        return this.app_detail_list;
    }

    public void setCollection_list(List<CollectAppData> collection_list) {
        this.collection_list = collection_list;
    }

    public List<FocusAppData> getFocus_info_list() {
        return this.focus_info_list;
    }

    public void setFocus_info_list(List<FocusAppData> focus_info_list) {
        this.focus_info_list = focus_info_list;
    }

    public void setApp_detail_list(List<AppDataInfo> app_detail_list) {
        this.app_detail_list = app_detail_list;
    }

    public void setGamestore(AppDataInfo gamestore) {
        this.gamestore = gamestore;
    }

    public void setAppstore(AppDataInfo appstore) {
        this.appstore = appstore;
    }

    public String toString() {
        return "AppDataSet [collection_list=" + this.collection_list + ", focus_info_list=" + this.focus_info_list + ", gamestore=" + this.gamestore + ", appstore=" + this.appstore + ", app_detail_list=" + this.app_detail_list + AlbumEnterFactory.SIGN_STR;
    }
}
