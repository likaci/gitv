package com.gala.video.app.epg.ui.albumlist.fragment.left;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.enums.IAlbumEnum.AlbumFragmentLocation;
import com.gala.video.app.epg.ui.albumlist.fragment.AlbumBaseFragment;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;

public abstract class AlbumBaseLeftFragment extends AlbumBaseFragment {
    protected boolean mRefreshImmediately;

    protected abstract int getLayoutResId();

    protected abstract void initView();

    protected abstract void loadData();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mLayoutResId = getLayoutResId();
        this.mMainView = inflater.inflate(this.mLayoutResId, null);
        if (this.mDataApi == null || this.mInfoModel == null) {
            log("onCreateView--getActivity()=" + getActivity() + ", mDataApi=" + this.mDataApi + ",mInfoModel=" + this.mInfoModel);
            if (getActivity() != null) {
                log("onCreateView--其他崩溃后，导致进入列表页异常，主动关闭");
                getActivity().finish();
            }
            return this.mMainView;
        }
        initView();
        loadData();
        return this.mMainView;
    }

    public AlbumFragmentLocation getLocationType() {
        return AlbumFragmentLocation.LEFT;
    }

    public void showHasResultPanel() {
        setLeftFragmentHasData(true);
        super.showHasResultPanel();
    }

    public Bitmap showNoResultPanel(ErrorKind kind, ApiException e) {
        setLeftFragmentHasData(false);
        return super.showNoResultPanel(kind, e);
    }

    public void showProgress() {
        setLeftFragmentHasData(false);
        super.showProgress();
    }

    public void showProgressWithoutDelay() {
        setLeftFragmentHasData(false);
        super.showProgressWithoutDelay();
    }

    public void handlerMessage2Left(Message msg) {
    }

    public void handlerMessage2Right(Message msg) {
        String str = null;
        if (this.mIAlbumBaseEvent == null || msg == null) {
            if (!NOLOG) {
                str = "--handlerMessage2Right---error---mIAlbumBaseEvent=" + this.mIAlbumBaseEvent + "---msg=" + msg;
            }
            log(str);
            return;
        }
        if (!NOLOG) {
            str = "--handlerMessage2Right---success";
        }
        log(str);
        this.mIAlbumBaseEvent.handlerMessage2Right(msg);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() != 22 || GetInterfaceTools.getUICreator().isViewVisible(getMenuView()) || ((!isLoadingData() && !isShowingCacheData() && (isRightFragmentHasData() || getNoResultPanel().isShowButton())) || this.mInfoModel == null || this.mInfoModel.isNoLeftFragment())) {
            return super.dispatchKeyEvent(event);
        }
        return true;
    }

    public void requestLeftPanelFocus() {
    }
}
