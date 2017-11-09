package com.gala.video.app.epg.ui.ucenter.record;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.constant.IFootConstant;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.enums.IFootEnum.FootLeftRefreshPage;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.app.epg.ui.albumlist.utils.UserUtil;
import com.gala.video.app.epg.ui.ucenter.record.contract.RecordFavouriteContentContract.Presenter;
import com.gala.video.app.epg.ui.ucenter.record.contract.RecordFavouriteContentContract.View;
import com.gala.video.app.epg.ui.ucenter.record.model.AlbumDataImpl;
import com.gala.video.app.epg.ui.ucenter.record.model.AlbumDataSource;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import java.lang.ref.WeakReference;
import java.util.List;

public class RecordFavouriteContentPresenter implements Presenter {
    private static final String TAG = "RecordFavouriteContentPresenter";
    private Context mContext;
    private String mCookie;
    private List<IData> mDataList;
    private AlbumDataSource mDataSource;
    private boolean mDefaultLoading = true;
    private FootLeftRefreshPage mDefaultPage;
    private IVrsCallback<ApiResultCode> mDeleteCallback = new C12161();
    private boolean mFirstLoad = true;
    private MyOnAlbumFetchedListener mGridAlbumFetchListener;
    private AlbumInfoModel mInfoModel;
    private boolean mIsDataLoading;
    private boolean mIsLogin;
    private OnStatusListener mOnStatusListener;
    private FootLeftRefreshPage mPage;
    private long mStartLoadingTime;
    private int mTotalCount;
    private View mView;

    public interface OnStatusListener {
        void onDataChanged(FootLeftRefreshPage footLeftRefreshPage, int i, int i2);

        void onError(FootLeftRefreshPage footLeftRefreshPage);
    }

    class C12161 implements IVrsCallback<ApiResultCode> {
        C12161() {
        }

        public void onSuccess(ApiResultCode result) {
            Log.e(RecordFavouriteContentPresenter.TAG, "cancelCollect, mDeleteCallback, onSuccess");
        }

        public void onException(ApiException e) {
            Log.e(RecordFavouriteContentPresenter.TAG, "cancelCollect, mDeleteCallback, onException=" + (e != null ? e.getDetailMessage() + "," + e.getCode() : "null"));
        }
    }

    private static class ClearCallbackImpl implements IVrsCallback<ApiResultCode> {
        WeakReference<RecordFavouriteContentPresenter> mOuter;

        public ClearCallbackImpl(RecordFavouriteContentPresenter outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onSuccess(ApiResultCode result) {
            final RecordFavouriteContentPresenter outer = (RecordFavouriteContentPresenter) this.mOuter.get();
            if (outer != null && outer.mContext != null) {
                Log.e(RecordFavouriteContentPresenter.TAG, "mClearCallback, onSuccess");
                ((Activity) outer.mContext).runOnUiThread(new Runnable() {
                    public void run() {
                        outer.clearSuccess();
                    }
                });
            }
        }

        public void onException(final ApiException e) {
            final RecordFavouriteContentPresenter outer = (RecordFavouriteContentPresenter) this.mOuter.get();
            if (outer != null && outer.mContext != null) {
                Log.e(RecordFavouriteContentPresenter.TAG, "mClearCallback, onException=" + e);
                ((Activity) outer.mContext).runOnUiThread(new Runnable() {
                    public void run() {
                        outer.doOnError(e);
                    }
                });
            }
        }
    }

    private class MyOnAlbumFetchedListener implements OnAlbumFetchedListener {
        private boolean mDropped;

        private MyOnAlbumFetchedListener() {
            this.mDropped = false;
        }

        public synchronized void dropped() {
            this.mDropped = true;
        }

        public synchronized void onFetchAlbumSuccess(final List<IData> list) {
            if (!this.mDropped && RecordFavouriteContentPresenter.this.mView.isActive()) {
                RecordFavouriteContentPresenter.this.mIsDataLoading = false;
                ((Activity) RecordFavouriteContentPresenter.this.mContext).runOnUiThread(new Runnable() {
                    public void run() {
                        RecordFavouriteContentPresenter.this.mView.hideLoading();
                        if (UserUtil.isLogin() || list == null || list.size() < 5) {
                            RecordFavouriteContentPresenter.this.mDataList = list;
                            RecordFavouriteContentPresenter.this.mView.showLogin(false);
                            RecordFavouriteContentPresenter.this.mView.showLoading(RecordFavouriteContentPresenter.this.mDataList.size() < RecordFavouriteContentPresenter.this.mDataSource.getTotalCount());
                        } else {
                            RecordFavouriteContentPresenter.this.mDataList = list.subList(0, 5);
                            RecordFavouriteContentPresenter.this.mView.showLogin(true);
                            RecordFavouriteContentPresenter.this.mView.showLoading(false);
                        }
                        if (!RecordFavouriteContentPresenter.this.mDefaultLoading || ListUtils.isEmpty(RecordFavouriteContentPresenter.this.mDataList) || RecordFavouriteContentPresenter.this.mDataList.size() != 0 || RecordFavouriteContentPresenter.this.mDefaultPage == FootLeftRefreshPage.FAVOURITE) {
                            RecordFavouriteContentPresenter.this.mView.updateData(RecordFavouriteContentPresenter.this.mDataList, RecordFavouriteContentPresenter.this.mFirstLoad);
                            RecordFavouriteContentPresenter.this.mFirstLoad = false;
                            if (RecordFavouriteContentPresenter.this.mTotalCount == 0) {
                                RecordFavouriteContentPresenter.this.mTotalCount = RecordFavouriteContentPresenter.this.mDataSource.getTotalCount();
                            }
                            RecordFavouriteContentPresenter.this.mView.setTotalSize(RecordFavouriteContentPresenter.this.mTotalCount);
                            RecordFavouriteContentPresenter.this.doDataChanged();
                            QAPingback.sendAlbumPageShowPingback(RecordFavouriteContentPresenter.this.mDataSource.getCurPage(), ListUtils.getCount(RecordFavouriteContentPresenter.this.mDataList), RecordFavouriteContentPresenter.this.mInfoModel, System.currentTimeMillis() - RecordFavouriteContentPresenter.this.mStartLoadingTime, true, 0);
                        } else {
                            RecordFavouriteContentPresenter.this.doDataChanged();
                        }
                        RecordFavouriteContentPresenter.this.mDefaultLoading = false;
                    }
                });
            }
        }

        public void onFetchAlbumFail(final ApiException e) {
            if (!this.mDropped && RecordFavouriteContentPresenter.this.mView.isActive()) {
                RecordFavouriteContentPresenter.this.mIsDataLoading = false;
                ((Activity) RecordFavouriteContentPresenter.this.mContext).runOnUiThread(new Runnable() {
                    public void run() {
                        if (RecordFavouriteContentPresenter.this.mDataSource.getCurPage() <= 1 && ListUtils.isEmpty(RecordFavouriteContentPresenter.this.mView.getList())) {
                            RecordFavouriteContentPresenter.this.doOnError(e);
                        }
                    }
                });
                QAPingback.error(RecordFavouriteContentPresenter.TAG, String.valueOf(RecordFavouriteContentPresenter.this.mInfoModel.getChannelId()), RecordFavouriteContentPresenter.this.mInfoModel.getDataTagName(), e);
            }
        }
    }

    public RecordFavouriteContentPresenter(Context context, View view, AlbumInfoModel infoModel) {
        this.mContext = context;
        this.mView = view;
        this.mView.setPresenter(this);
        this.mInfoModel = infoModel;
        this.mDataSource = new AlbumDataImpl();
        this.mDataSource.setAlbumInfoModel(infoModel);
        this.mIsLogin = UserUtil.isLogin();
        this.mCookie = UserUtil.getCookie();
    }

    public void loadDefaultPage(FootLeftRefreshPage defaultPage) {
        this.mDefaultPage = defaultPage;
        setPage(defaultPage);
        loadData();
    }

    public void start(FootLeftRefreshPage page) {
        if (this.mPage == null || this.mPage != page) {
            setPage(page);
            this.mFirstLoad = true;
            this.mIsDataLoading = false;
            loadData();
            QAPingback.labelTagClickPingback(this.mInfoModel.getDataTagName(), this.mInfoModel);
        }
    }

    private void setPage(FootLeftRefreshPage page) {
        this.mTotalCount = 0;
        this.mPage = page;
        this.mView.setPage(page);
        setTagInfo();
        this.mDataSource.resetDataApi(new Tag(this.mInfoModel.getDataTagId(), this.mInfoModel.getDataTagName(), SourceTool.HISTORY_TAG, QLayoutKind.PORTRAIT));
    }

    public void start() {
    }

    private void setTagInfo() {
        switch (this.mPage) {
            case PLAY_HISTORY_ALL:
                this.mInfoModel.setFrom("8");
                this.mInfoModel.setDataTagId(String.valueOf(0));
                this.mInfoModel.setDataTagName(IFootConstant.STR_PLAYHISTORY_ALL);
                this.mInfoModel.setPageType(IAlbumConfig.UNIQUE_FOOT_PLAYHISTORY);
                break;
            case PLAY_HISTORY_LONG:
                this.mInfoModel.setFrom("8");
                this.mInfoModel.setDataTagId(String.valueOf(1));
                this.mInfoModel.setDataTagName(IFootConstant.STR_PLAYHISTORY_LONG);
                this.mInfoModel.setPageType(IAlbumConfig.UNIQUE_FOOT_PLAYHISTORY);
                break;
            case FAVOURITE:
                this.mInfoModel.setFrom("favorite");
                this.mInfoModel.setDataTagName(IFootConstant.STR_FAV);
                this.mInfoModel.setPageType(IAlbumConfig.UNIQUE_FOOT_FAVOURITE);
                break;
            case SUBSCRIBE:
                this.mInfoModel.setFrom("order");
                this.mInfoModel.setDataTagName(IFootConstant.STR_SUBSCRIBLE);
                this.mInfoModel.setPageType(IAlbumConfig.UNIQUE_FOOT_SUBSCRIBLE);
                break;
        }
        this.mDataSource.notifyPageType();
    }

    public void loadData() {
        if (!this.mIsDataLoading) {
            this.mIsDataLoading = true;
            if (this.mGridAlbumFetchListener != null) {
                this.mGridAlbumFetchListener.dropped();
                this.mGridAlbumFetchListener = null;
            }
            this.mGridAlbumFetchListener = new MyOnAlbumFetchedListener();
            this.mDataSource.loadAlbumData(this.mGridAlbumFetchListener);
            this.mStartLoadingTime = System.currentTimeMillis();
        }
    }

    public void setOnStatusListener(OnStatusListener l) {
        this.mOnStatusListener = l;
    }

    public void doOnItemClick(int position, IData iAlbumData) {
        if (this.mView.isDeleteMode()) {
            switch (this.mPage) {
                case PLAY_HISTORY_ALL:
                case PLAY_HISTORY_LONG:
                    removeItem(position);
                    GetInterfaceTools.getIHistoryCacheManager().deleteHistory(iAlbumData.getField(1), iAlbumData.getField(3));
                    GetInterfaceTools.getOpenapiReporterManager().onDeleteSinglePlayRecord(iAlbumData.getAlbum());
                    break;
                case FAVOURITE:
                    String subKey = iAlbumData.getField(6);
                    String subType = iAlbumData.getField(7);
                    Log.e(TAG, "doOnItemClick, FAVOURITE, mIsLogin=" + this.mIsLogin);
                    if (this.mIsLogin) {
                        UserHelper.cancelCollect.call(this.mDeleteCallback, subType, subKey, this.mCookie, iAlbumData.getField(2));
                    } else {
                        UserHelper.cancelCollectForAnonymity.call(this.mDeleteCallback, subType, subKey, this.mCookie, iAlbumData.getField(2));
                    }
                    removeItem(position);
                    GetInterfaceTools.getOpenapiReporterManager().onDeleteSingleFavRecord(iAlbumData.getAlbum());
                    break;
            }
            QAPingback.recordDeleteLayerPingback(0);
            return;
        }
        this.mInfoModel.setIdentification(this.mPage == FootLeftRefreshPage.SUBSCRIBE ? IAlbumConfig.UNIQUE_FOOT_SUBSCRIBLE : IAlbumConfig.UNIQUE_FOOT_PLAYHISTORY);
        iAlbumData.click(this.mContext, this.mInfoModel);
    }

    private void removeItem(int position) {
        this.mDataList.remove(position);
        this.mTotalCount--;
        this.mView.notifyItemRemoved(position);
        this.mView.setTotalSize(this.mTotalCount);
        doDataChanged();
    }

    private void doDataChanged() {
        int count = 0;
        if (this.mView.getList() != null) {
            count = this.mView.getList().size();
        }
        if (this.mOnStatusListener != null) {
            this.mOnStatusListener.onDataChanged(this.mPage, count, this.mTotalCount);
        }
    }

    public void clearAll() {
        switch (this.mPage) {
            case PLAY_HISTORY_ALL:
            case PLAY_HISTORY_LONG:
                GetInterfaceTools.getIHistoryCacheManager().clear();
                clearSuccess();
                return;
            case FAVOURITE:
                if (this.mIsLogin) {
                    UserHelper.clearCollect.call(new ClearCallbackImpl(this), this.mCookie);
                } else {
                    UserHelper.clearCollectForAnonymity.call(new ClearCallbackImpl(this), this.mCookie);
                }
                GetInterfaceTools.getOpenapiReporterManager().onDeleteAllFavRecord();
                return;
            default:
                return;
        }
    }

    private void clearSuccess() {
        this.mTotalCount = 0;
        this.mView.deleteAll();
        doDataChanged();
    }

    public void loadMore(int selectedRow, int totalRow) {
        if (totalRow - selectedRow < 7 && selectedRow >= 0) {
            loadData();
        }
    }

    public AlbumInfoModel getInfoModel() {
        return this.mInfoModel;
    }

    private void doOnError(ApiException e) {
        this.mView.showErrorView(ErrorKind.NET_ERROR, e);
        if (this.mOnStatusListener != null) {
            this.mOnStatusListener.onError(this.mPage);
        }
    }

    public FootLeftRefreshPage getPage() {
        return this.mPage;
    }

    public boolean isLogin() {
        return this.mIsLogin;
    }

    public boolean isLoginChanged() {
        boolean isLogin = UserUtil.isLogin();
        String cookie = UserUtil.getCookie();
        if (isLogin == this.mIsLogin && cookie == this.mCookie) {
            return false;
        }
        this.mIsLogin = UserUtil.isLogin();
        this.mCookie = UserUtil.getCookie();
        return true;
    }

    public void reloadData() {
        this.mFirstLoad = true;
        this.mIsDataLoading = false;
        setPage(this.mPage);
        loadData();
    }
}
