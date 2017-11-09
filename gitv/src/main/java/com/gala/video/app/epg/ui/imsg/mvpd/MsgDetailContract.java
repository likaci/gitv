package com.gala.video.app.epg.ui.imsg.mvpd;

import android.graphics.Bitmap;
import com.gala.video.app.epg.ui.albumlist.base.BasePresenter;
import com.gala.video.app.epg.ui.albumlist.base.BaseView;

public interface MsgDetailContract {

    public interface Presenter extends BasePresenter {
        void onDestroy();

        void onLoadImage(String str);
    }

    public interface View extends BaseView<Presenter> {
        boolean isActive();

        void showImage(Bitmap bitmap);
    }
}
