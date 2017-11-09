package com.gala.video.lib.share.uikit.data.data.Model.itemstyle;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class ItemData {
    public String id;
    public Style style;
    public String type;
    public short z_order;

    public String toString() {
        return "ItemData [id=" + this.id + ", type=" + this.type + ", z_order=" + this.z_order + ", style=" + this.style + AlbumEnterFactory.SIGN_STR;
    }
}
