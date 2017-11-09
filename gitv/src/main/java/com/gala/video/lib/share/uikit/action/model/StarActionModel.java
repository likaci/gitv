package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.tv2.model.Star;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;

public class StarActionModel extends BaseActionModel<Star> {
    private String from;
    private String mId;
    private String mItemId;
    private String mPingback;
    private String mTitle;

    public StarActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public BaseActionModel buildActionModel(Star star) {
        this.mTitle = star.name;
        this.mItemId = star.id;
        this.from = PingBackUtils.getTabName() + "_明星";
        this.mId = star.id;
        return this;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getItemId() {
        return this.mItemId;
    }

    public String getFrom() {
        return this.from;
    }

    public String getId() {
        return this.mId;
    }
}
