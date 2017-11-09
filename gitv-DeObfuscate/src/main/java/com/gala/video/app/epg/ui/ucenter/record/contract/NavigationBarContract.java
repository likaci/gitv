package com.gala.video.app.epg.ui.ucenter.record.contract;

import android.view.View.OnFocusChangeListener;
import com.gala.albumprovider.model.Tag;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.app.epg.ui.albumlist.base.BasePresenter;
import com.gala.video.app.epg.ui.albumlist.base.BaseView;
import com.gala.video.app.epg.ui.albumlist.enums.IFootEnum.FootLeftRefreshPage;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.ucenter.record.NavigationBarFragment.LabelChangedListener;
import java.util.List;

public class NavigationBarContract {

    public interface View extends BaseView<Presenter> {
        FootLeftRefreshPage getPage();

        void requestFocus();

        void setBarLists(List<Tag> list);

        void setDefaultPage(FootLeftRefreshPage footLeftRefreshPage);

        void setFocusLeaveForbidden(int i);

        void setFocusPosition(int i);

        void setLabelChangedListener(LabelChangedListener labelChangedListener);

        void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener);

        void setOnItemClickListener(OnItemClickListener onItemClickListener);

        void updateNavigationBarItem(AlbumInfoModel albumInfoModel);
    }

    public interface Presenter extends BasePresenter {
    }
}
