package com.gala.video.app.epg.ui.star.presenter;

import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.tv2.model.Star;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.base.BasePresenter;
import com.gala.video.app.epg.ui.albumlist.base.BaseView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import java.util.List;
import java.util.Map;

public interface StarsContract {

    public interface View extends BaseView<Presenter> {
        void setDetails(Star star);

        void showDatas(Map<String, List<IData>> map, List<Tag> list);

        void showFullView(int i);

        void showHasResultPanel();

        void showNoResultPanel(ErrorKind errorKind, ApiException apiException);

        void showProgressBar();

        void showTopView(boolean z);
    }

    public interface Presenter extends BasePresenter {
        void onDestroy();

        void onPause();

        void onResume();
    }
}
