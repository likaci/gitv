package com.gala.video.lib.share.uikit.card;

import com.gala.video.albumlist.utils.LOG;
import com.gala.video.lib.share.uikit.data.data.cache.LayoutCache;
import com.gala.video.lib.share.uikit.data.data.processor.CardInfoBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig.Source;

public class CarouseChannelCard extends GridCard {
    private static final String TAG = "CarouseChannelCard";

    protected void onStop() {
        updateLocalHistoryCardModel();
    }

    void updateLocalHistoryCardModel() {
        if (this.mCardInfoModel.mSource.equals(Source.CAROUSEL_HISTORY)) {
            this.mCardInfoModel = CardInfoBuildTool.buildCarouselHistoryCard(this.mCardInfoModel, LayoutCache.getInstance().getCard().get(Integer.valueOf(this.mCardInfoModel.cardLayoutId).intValue()));
            LOG.d("mCardInfoModel = " + this.mCardInfoModel.getItemInfoModels());
            notifyDataSetChanged();
        }
    }
}
