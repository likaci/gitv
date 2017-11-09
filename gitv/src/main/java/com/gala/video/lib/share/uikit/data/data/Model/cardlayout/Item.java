package com.gala.video.lib.share.uikit.data.data.Model.cardlayout;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class Item {
    public short h;
    public float scale;
    public short space_h;
    public short space_v;
    public String style;
    public short w;

    public String toString() {
        return "Item [w=" + this.w + ", h=" + this.h + ", scale=" + this.scale + ", space_v=" + this.space_v + ", space_h=" + this.space_h + ", style=" + this.style + AlbumEnterFactory.SIGN_STR;
    }
}
