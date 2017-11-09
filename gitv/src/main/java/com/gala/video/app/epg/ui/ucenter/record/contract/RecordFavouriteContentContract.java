package com.gala.video.app.epg.ui.ucenter.record.contract;

import android.view.View.OnFocusChangeListener;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.base.BasePresenter;
import com.gala.video.app.epg.ui.albumlist.base.BaseView;
import com.gala.video.app.epg.ui.albumlist.enums.IFootEnum.FootLeftRefreshPage;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.ucenter.record.RecordFavouriteContentPresenter.OnStatusListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import java.util.List;

public class RecordFavouriteContentContract {

    public interface View extends BaseView<Presenter> {
        void deleteAll();

        void enterDeleteMode();

        List<IData> getList();

        void hideLoading();

        boolean isActive();

        boolean isDeleteMode();

        boolean isEmpty();

        boolean isFocusable();

        void leaveDeleteMode();

        void notifyDataSetChanged();

        void notifyItemRemoved(int i);

        void requestFocus();

        void setIsFromLoginView(boolean z);

        void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener);

        void setPage(FootLeftRefreshPage footLeftRefreshPage);

        void setTotalSize(int i);

        void showClearAllDialog();

        void showErrorView(ErrorKind errorKind, ApiException apiException);

        void showLoading(boolean z);

        void showLogin(boolean z);

        void updateData(List<IData> list, boolean z);

        void updateMenuDesc();
    }

    public interface Presenter extends BasePresenter {
        void clearAll();

        void doOnItemClick(int i, IData iData);

        AlbumInfoModel getInfoModel();

        FootLeftRefreshPage getPage();

        boolean isLogin();

        boolean isLoginChanged();

        void loadData();

        void loadDefaultPage(FootLeftRefreshPage footLeftRefreshPage);

        void loadMore(int i, int i2);

        void reloadData();

        void setOnStatusListener(OnStatusListener onStatusListener);

        void start(FootLeftRefreshPage footLeftRefreshPage);
    }
}
