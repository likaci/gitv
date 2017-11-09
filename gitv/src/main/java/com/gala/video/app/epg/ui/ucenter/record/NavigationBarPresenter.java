package com.gala.video.app.epg.ui.ucenter.record;

import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.ucenter.record.contract.NavigationBarContract.Presenter;
import com.gala.video.app.epg.ui.ucenter.record.contract.NavigationBarContract.View;
import com.gala.video.app.epg.ui.ucenter.record.model.AlbumDataImpl;
import com.gala.video.app.epg.ui.ucenter.record.model.AlbumDataSource;

public class NavigationBarPresenter implements Presenter {
    private AlbumDataSource mDataSource = new AlbumDataImpl();
    private AlbumInfoModel mInfoModel;
    private View mView;

    public NavigationBarPresenter(View view, AlbumInfoModel infoModel) {
        this.mDataSource.setAlbumInfoModel(infoModel);
        this.mInfoModel = infoModel;
        this.mView = view;
        this.mView.setPresenter(this);
        this.mView.setBarLists(this.mDataSource.getBarLists());
    }

    public void start() {
        this.mView.updateNavigationBarItem(this.mInfoModel);
    }
}
