package com.gala.video.app.epg.ui.star.model;

import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.model.AlbumIntentModel;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;

public class StarsInfoModel extends AlbumInfoModel {
    private static final long serialVersionUID = 1;
    private final String f1954e = PingBackUtils.createEventId();

    public String getE() {
        return this.f1954e;
    }

    public StarsInfoModel(AlbumIntentModel m) {
        super(m);
    }

    public String toString() {
        return "StarsInfoModel{e='" + this.f1954e + '\'' + '}';
    }
}
