package com.gala.video.lib.share.uikit.data.data.cache;

import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.data.Model.cardlayout.CardMap;
import com.gala.video.lib.share.uikit.data.data.Model.itemstyle.ItemMap;
import com.gala.video.lib.share.uikit.data.data.loader.LayoutLoader;
import java.util.ArrayList;
import java.util.List;

public class LayoutCache {
    private static CardMap card;
    private static ItemMap item;
    private static final LayoutCache mCache = new LayoutCache();
    private static final LayoutLoader mLoader = new LayoutLoader();
    public List<CardInfoModel> cardInfoModels = new ArrayList();

    public static LayoutCache getInstance() {
        return mCache;
    }

    public synchronized CardMap getCard() {
        if (card == null) {
            card = mLoader.loadCard();
        }
        return card;
    }

    public synchronized ItemMap getItem() {
        if (item == null) {
            item = mLoader.loadItem();
        }
        return item;
    }
}
