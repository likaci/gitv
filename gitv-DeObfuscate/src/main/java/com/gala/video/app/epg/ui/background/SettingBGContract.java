package com.gala.video.app.epg.ui.background;

import com.gala.video.app.epg.ui.albumlist.base.BasePresenter;
import com.gala.video.app.epg.ui.albumlist.base.BaseView;
import java.util.List;

public interface SettingBGContract {

    public interface Presenter extends BasePresenter {
        void onDestroy();
    }

    public interface View extends BaseView<Presenter> {
        boolean isActive();

        void setBgList(List<String> list);

        void setSelectedPostion();

        void updateBgList(List<String> list);
    }
}
