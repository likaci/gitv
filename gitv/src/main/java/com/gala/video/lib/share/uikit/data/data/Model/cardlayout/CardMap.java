package com.gala.video.lib.share.uikit.data.data.Model.cardlayout;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.util.HashMap;
import java.util.Map;

public class CardMap {
    private Map<Integer, CardStyle> cardMap = new HashMap();

    public CardStyle get(int i) {
        CardStyle cardStyle = (CardStyle) this.cardMap.get(Integer.valueOf(i));
        if (cardStyle == null) {
            return null;
        }
        if (cardStyle.row_nolimit == (short) 1) {
            return cardStyle.copy();
        }
        return cardStyle;
    }

    public void put(int i, CardStyle cardStyle) {
        this.cardMap.put(Integer.valueOf(i), cardStyle);
    }

    public String toString() {
        return "CardMap [cardMap=" + this.cardMap + AlbumEnterFactory.SIGN_STR;
    }
}
