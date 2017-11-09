package com.gala.video.lib.share.uikit.item;

import com.gala.tvapi.type.LivePlayingType;
import com.gala.video.lib.share.uikit.contract.StandardItemContract.Presenter;
import com.gala.video.lib.share.uikit.contract.StandardItemContract.View;
import com.gala.video.lib.share.uikit.data.data.processor.Item.CornerBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.view.widget.livecorner.LiveCornerListener;
import com.gala.video.lib.share.uikit.view.widget.livecorner.LiveCornerObserver;
import java.util.HashMap;

public class StandardItem extends Item implements Presenter {
    private LiveCornerObserver mLiveCornerObserver;
    private View mView;

    class C18051 implements LiveCornerListener {
        C18051() {
        }

        public void showBefore() {
            ((HashMap) StandardItem.this.mItemInfoModel.getCuteViewDatas().get("ID_CORNER_R_T")).put(UIKitConfig.KEY_LIVE_PLAYING_TYPE, LivePlayingType.BEFORE.name());
            StandardItem.this.mView.showLiveCorner(UIKitConfig.KEY_LIVE_RES_BEFORE);
        }

        public void showPlaying() {
            ((HashMap) StandardItem.this.mItemInfoModel.getCuteViewDatas().get("ID_CORNER_R_T")).put(UIKitConfig.KEY_LIVE_PLAYING_TYPE, LivePlayingType.PLAYING.name());
            StandardItem.this.mView.showLiveCorner(UIKitConfig.KEY_LIVE_RES_ING);
        }

        public void showEnd() {
            ((HashMap) StandardItem.this.mItemInfoModel.getCuteViewDatas().get("ID_CORNER_R_T")).put(UIKitConfig.KEY_LIVE_PLAYING_TYPE, LivePlayingType.END.name());
            StandardItem.this.mView.showLiveCorner(UIKitConfig.KEY_LIVE_RES_END);
        }
    }

    public void setView(View view) {
        this.mView = view;
    }

    public void setPlayingGif(boolean showGif) {
        if (this.mItemInfoModel != null) {
            CornerBuildTool.buildGifPlayingCorner(this.mItemInfoModel, showGif);
        }
        if (this.mView != null) {
            this.mView.updatePlayingGifUI();
        }
    }

    public void removeLiveCornerObserver() {
        if (this.mLiveCornerObserver != null) {
            this.mLiveCornerObserver.removeObserver();
        }
    }

    public void addLiveCornerObserver() {
        if (this.mLiveCornerObserver == null) {
            this.mLiveCornerObserver = new LiveCornerObserver();
        }
        this.mLiveCornerObserver.addObserver(this.mItemInfoModel, new C18051());
    }

    public int getType() {
        return 201;
    }
}
