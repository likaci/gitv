package com.gala.video.app.epg.ui.albumlist.data.api;

import com.gala.albumprovider.base.IAlbumSet;
import com.gala.albumprovider.base.IAlbumSource;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnLabelFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.factory.AlbumDataMakeupFactory;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.DebugUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.IHistoryResultCallBack;
import java.lang.ref.WeakReference;
import java.util.List;

public class PlayHistoryApi extends BaseDataApi {
    private int mTotalSize = 0;

    private static class HistoryCallBack implements IHistoryResultCallBack {
        private OnAlbumFetchedListener localDataListener;
        private WeakReference<PlayHistoryApi> mOuter;
        private long time1 = System.currentTimeMillis();

        public HistoryCallBack(PlayHistoryApi outer, OnAlbumFetchedListener dataListener) {
            this.mOuter = new WeakReference(outer);
            this.localDataListener = dataListener;
        }

        public void onSuccess(List<Album> list, int total) {
            PlayHistoryApi outer = (PlayHistoryApi) this.mOuter.get();
            if (outer != null) {
                DebugUtils.limitNetSpeed();
                outer.logAndRecord(PlayHistoryApi.NOLOG ? null : "HistoryCallBack---success-- index = " + outer.mCurPageIndex + "--total = " + total + "--timeToken = " + (System.currentTimeMillis() - this.time1));
                outer.mTotalSize = total;
                outer.mOriginalList = list;
                outer.handleOnDataSuccess(AlbumDataMakeupFactory.get().dataListMakeup(list, outer.getLayoutKind(), outer.mCurPageIndex, outer.mInfoModel), this.localDataListener);
            }
        }
    }

    public PlayHistoryApi(AlbumInfoModel model) {
        super(model);
    }

    public void loadAlbumData(OnAlbumFetchedListener dataListener) {
        if (isNeedLoad()) {
            this.mIsLoading = true;
            this.mLoadingTag = this.mNewTag;
            int historyType = StringUtils.parse(this.mLoadingTag.getID(), 0);
            logAndRecord(NOLOG ? null : "loadAlbumData---CurPageIndex = " + this.mCurPageIndex + "--LoadingTag.name=" + this.mLoadingTag.getName() + "---historyType=" + historyType);
            GetInterfaceTools.getIHistoryCacheManager().synchronizeHistoryListFromCloud();
            GetInterfaceTools.getIHistoryCacheManager().loadHistoryList(this.mCurPageIndex, getEachPageCount(), historyType, new HistoryCallBack(this, dataListener));
        }
    }

    protected IAlbumSource getAlbumSource() {
        return null;
    }

    public IAlbumSet getAlbumSet() {
        return null;
    }

    protected Tag getDefaultTag() {
        return null;
    }

    public void loadLabelData(OnLabelFetchedListener fetchingLabel) {
    }

    protected int getOriginalPage() {
        return 1;
    }

    protected int getEachPageCount() {
        return 200;
    }

    public int getSelectType() {
        return 1;
    }

    public int getRecommendType() {
        return 0;
    }

    protected void resetChildrenApi() {
    }

    protected void setTotalCount() {
        this.mTotalItemCount = this.mTotalSize;
        this.mDisplayCount = this.mTotalSize;
    }

    protected String getLogCatTag() {
        return "PlayHistoryApi";
    }
}
