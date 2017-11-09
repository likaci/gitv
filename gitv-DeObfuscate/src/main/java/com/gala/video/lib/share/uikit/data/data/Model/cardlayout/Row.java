package com.gala.video.lib.share.uikit.data.data.Model.cardlayout;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.util.List;

public class Row {
    public List<Item> items;

    public String toString() {
        return "Row [items=" + this.items + AlbumEnterFactory.SIGN_STR;
    }
}
