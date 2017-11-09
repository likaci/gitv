package com.gala.video.app.epg.ui.albumlist.common;

import com.gala.albumprovider.model.ILanguage;
import com.gala.video.app.epg.R;
import com.gala.video.lib.share.utils.ResourceUtil;

public class USALanguages implements ILanguage {
    private String getString(int id) {
        return ResourceUtil.getStr(id);
    }

    public String get3DName() {
        return getString(R.string.name_3D);
    }

    public String get7DayName() {
        return getString(R.string.name_7Day);
    }

    public String getFavouritesName() {
        return getString(R.string.name_favourite);
    }

    public String getPlayHistoryName() {
        return getString(R.string.name_playhistory);
    }

    public String getSearchName() {
        return getString(R.string.search_title);
    }

    public String getVipName() {
        return getString(R.string.name_vip);
    }

    public String getHotName() {
        return getString(R.string.name_hot);
    }

    public String getPraiseName() {
        return getString(R.string.name_praise);
    }

    public String getTagAllName() {
        return getString(R.string.name_tagall);
    }

    public String getTagHotName() {
        return getString(R.string.name_taghot);
    }

    public String getTagPraiseName() {
        return getString(R.string.name_tagpraise);
    }

    public String getOfflineName() {
        return getString(R.string.name_offline_film);
    }

    public String getWeekendName() {
        return "";
    }

    public String getSubscribeName() {
        return null;
    }
}
