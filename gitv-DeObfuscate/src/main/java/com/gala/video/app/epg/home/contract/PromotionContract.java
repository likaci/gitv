package com.gala.video.app.epg.home.contract;

import android.view.KeyEvent;
import com.gala.video.app.epg.home.promotion.BasePresenter;
import com.gala.video.app.epg.home.promotion.BaseView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionMessage;

public interface PromotionContract {

    public interface Presenter extends BasePresenter {
        void destroy();

        int getPreFocusId();

        void invisiblePromotion();

        boolean isPromotionViewVisibility();

        boolean onKey(android.view.View view, int i, KeyEvent keyEvent);

        void onStop();

        void setNextFocusDownId(int i);

        void setOnFocus(boolean z);

        void setPreFocusId(int i);

        void showPromotion(PromotionMessage promotionMessage);
    }

    public interface View extends BaseView<Presenter> {
        void invisiblePromotion();

        boolean isVisibility();

        void setNextFocusDownId(int i);

        void showPromotion(PromotionMessage promotionMessage);
    }
}
