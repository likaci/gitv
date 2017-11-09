package com.gala.video.app.epg.ui.albumlist.fragment.right;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.enums.IAlbumEnum.AlbumFragmentLocation;
import com.gala.video.app.epg.ui.albumlist.fragment.AlbumBaseFragment;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;

public abstract class AlbumBaseRightFragment extends AlbumBaseFragment {
    protected View mCurrentFocusedView;
    protected int mDisplayTotal;
    protected boolean mIsRecycledBitmap;
    protected boolean mShowCacheWithoutLoadData;
    protected int mTotalItemCount;

    protected abstract int getLayoutResId();

    protected abstract void initView();

    protected abstract void loadData();

    protected abstract void recyleBitmap();

    protected abstract void reloadBitmap();

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
        this.mInfoModel.setIdentification(getLogCatTag());
        if (this.mArguments != null) {
            this.mShowCacheWithoutLoadData = this.mArguments.getBoolean(IAlbumConfig.INTENT_SHOW_CACHE_WITHOUT_DATA, false);
        }
        initView();
        loadData();
        return this.mMainView;
    }

    public Bitmap showNoResultPanel(ErrorKind kind, ApiException e) {
        String str = null;
        log(NOLOG ? null : "---showNoResultPanel---");
        if (!NOLOG) {
            str = "---showNoResultPanel---";
        }
        logRecord(str);
        setTopTagTextWithZero();
        setRightFragmentHasData(false);
        setTopMenuLayoutVisible(4);
        return super.showNoResultPanel(kind, e);
    }

    public void showHasResultPanel() {
        String str = null;
        log(NOLOG ? null : "---showHasResultPanel---");
        if (!NOLOG) {
            str = "---showHasResultPanel---";
        }
        logRecord(str);
        setTopMenuLayoutVisible(0);
        super.showHasResultPanel();
        setRightFragmentHasData(true);
    }

    public void showProgress() {
        log(NOLOG ? null : "---showProgress---");
        setTopTagTextWithZero();
        setRightFragmentHasData(false);
        setTopMenuDesTxt(this.mTopMenuDesTxt);
        setTopMenuLayoutVisible(StringUtils.equals(this.mTopMenuDesTxt, IAlbumConfig.STR_FILTER) ? 0 : 4);
        super.showProgress();
    }

    public void showProgressWithoutDelay() {
        log(NOLOG ? null : "---showProgressWithoutDelay---");
        setTopTagTextWithZero();
        setRightFragmentHasData(false);
        setTopMenuDesTxt(this.mTopMenuDesTxt);
        setTopMenuLayoutVisible(StringUtils.equals(this.mTopMenuDesTxt, IAlbumConfig.STR_FILTER) ? 0 : 4);
        super.showProgressWithoutDelay();
    }

    protected void setTopTagTextWithZero() {
        setTopTagTxt(this.mTopTagDesTxt, this.mTopTagNameTxt, null);
    }

    protected void setTopTagTextAfterLoad(int totalItems) {
        String tagDesTxt = this.mTopTagDesTxt;
        String tagNameTxt = this.mTopTagNameTxt;
        String tagCountTxt = null;
        if (totalItems > 0 && this.mDisplayTotal != 0) {
            tagCountTxt = this.mDisplayTotal + this.mTopCountExpandTxt;
        }
        setTopTagTxt(tagDesTxt, tagNameTxt, tagCountTxt);
    }

    public void onStart() {
        super.onStart();
        this.mBaseHandler.postDelayed(new Runnable() {
            public void run() {
                if (AlbumBaseRightFragment.this.mIsRecycledBitmap) {
                    AlbumBaseRightFragment.this.reloadBitmap();
                    AlbumBaseRightFragment.this.mIsRecycledBitmap = false;
                }
            }
        }, 300);
    }

    public void onPause() {
        super.onPause();
        ImageProviderApi.getImageProvider().stopAllTasks();
    }

    public void onStop() {
        super.onStop();
        if (!this.mIsRecycledBitmap) {
            recyleBitmap();
            this.mIsRecycledBitmap = true;
        }
    }

    public AlbumFragmentLocation getLocationType() {
        return AlbumFragmentLocation.RIGHT;
    }

    public void handlerMessage2Right(Message msg) {
    }

    public void handlerMessage2Left(Message msg) {
        String str = null;
        if (this.mIAlbumBaseEvent == null || msg == null) {
            if (!NOLOG) {
                str = "--handlerMessage2Left---error---mIAlbumBaseEvent=" + this.mIAlbumBaseEvent + "--msg=" + msg;
            }
            log(str);
            return;
        }
        if (!NOLOG) {
            str = "--handlerMessage2Left---success";
        }
        log(str);
        this.mIAlbumBaseEvent.handlerMessage2Left(msg);
    }

    public void requestLeftPanelFocus() {
        if (this.mIAlbumBaseEvent != null) {
            log(NOLOG ? null : "--requestLeftPanelFocus-----");
            this.mIAlbumBaseEvent.requestLeftPanelFocus();
        }
    }
}
