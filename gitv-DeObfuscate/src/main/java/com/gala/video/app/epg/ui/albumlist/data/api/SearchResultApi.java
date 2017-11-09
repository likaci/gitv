package com.gala.video.app.epg.ui.albumlist.data.api;

import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.base.IAlbumSet;
import com.gala.albumprovider.base.IAlbumSource;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.logic.source.search.AlbumSearchSource;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnLabelFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.factory.AlbumDataMakeupFactory;
import com.gala.video.app.epg.ui.albumlist.data.loader.BaseDataLoader;
import com.gala.video.app.epg.ui.albumlist.data.loader.ChannelAlbumLoader;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.DebugUtils;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SearchResultApi extends BaseDataApi {
    protected BaseDataLoader mDataApi;
    private List<OnDataFetchedImpl> mDataFetchedImpls = new ArrayList();

    private static class IAlbumCallbackImpl implements IAlbumCallback {
        private final OnLabelFetchedListener fetchingLabel;
        private WeakReference<SearchResultApi> mOuter;
        private final long time1 = System.currentTimeMillis();

        public IAlbumCallbackImpl(SearchResultApi outer, OnLabelFetchedListener fetchingLabel) {
            this.fetchingLabel = fetchingLabel;
            this.mOuter = new WeakReference(outer);
        }

        public void onSuccess(int arg0, List<Album> list) {
            SearchResultApi outer = (SearchResultApi) this.mOuter.get();
            if (outer != null) {
                DebugUtils.limitNetSpeed();
                outer.logAndRecord(SearchResultApi.NOLOG ? null : "loadLabelData--- success , time consume = " + (System.currentTimeMillis() - this.time1));
                outer.mLoadingTag = outer.getDefaultTag();
                outer.mPrePageIndex = outer.getOriginalPage();
                outer.mOriginalList = list;
                List tagList = outer.mAlbumSet.getTagList();
                int count = ListUtils.getCount(tagList);
                outer.logAndRecord(SearchResultApi.NOLOG ? null : "loadLabelData---success--label.size = " + count + "---they are:");
                for (int i = 0; i < count; i++) {
                    outer.logAndRecord(SearchResultApi.NOLOG ? null : "loadLabelData---success--tag name = " + ((Tag) tagList.get(i)).getName());
                }
                if (tagList != null) {
                    outer.mTagLabelList.addAll(tagList);
                }
                this.fetchingLabel.onFetchLabelSuccess(outer.mTagLabelList);
            }
        }

        public void onFailure(int arg0, ApiException e) {
            final SearchResultApi outer = (SearchResultApi) this.mOuter.get();
            if (outer != null) {
                DebugUtils.limitNetSpeed();
                final long time2 = System.currentTimeMillis();
                outer.logAndRecord(SearchResultApi.NOLOG ? null : "loadLabelData--- fail , e=" + e + "-- start net work check. time consume = " + (time2 - this.time1));
                final ApiException apiException = e;
                NetWorkManager.getInstance().checkNetWork(new StateCallback() {
                    public void getStateResult(int state) {
                        String str;
                        SearchResultApi searchResultApi = outer;
                        if (SearchResultApi.NOLOG) {
                            str = null;
                        } else {
                            str = "loadLabelData---fail, end  net work check consume = " + (System.currentTimeMillis() - time2);
                        }
                        searchResultApi.logAndRecord(str);
                        IAlbumCallbackImpl.this.fetchingLabel.onFetchLabelFail(apiException);
                    }
                });
            }
        }
    }

    private static class OnDataFetchedImpl implements OnAlbumFetchedListener {
        private OnAlbumFetchedListener localDataListener;
        private long localLogTime1 = System.currentTimeMillis();
        private Tag localTag;
        private WeakReference<SearchResultApi> mOuter;

        public OnDataFetchedImpl(SearchResultApi outer, OnAlbumFetchedListener fetchingData, Tag tag) {
            this.mOuter = new WeakReference(outer);
            this.localDataListener = fetchingData;
            this.localTag = tag;
        }

        public void closeCallback() {
            this.localDataListener = null;
        }

        public void onFetchAlbumSuccess(List<IData> list) {
            String str = null;
            SearchResultApi outer = (SearchResultApi) this.mOuter.get();
            if (outer != null) {
                DebugUtils.limitNetSpeed();
                if (this.localDataListener == null) {
                    if (!SearchResultApi.NOLOG) {
                        str = "OnDataFetchedImpl---success localDataListener=null--return--tag.name=" + (this.localTag != null ? this.localTag.getName() : "null");
                    }
                    outer.log(str);
                    return;
                }
                String tagMsg;
                if (SearchResultApi.NOLOG) {
                    tagMsg = null;
                } else {
                    tagMsg = "OnDataFetchedImpl---success---tag.name=" + (this.localTag != null ? this.localTag.getName() : "null") + "--timeToken = " + (System.currentTimeMillis() - this.localLogTime1);
                }
                outer.logAndRecord(tagMsg);
                outer.mOriginalList = outer.mDataApi.getOriginalList();
                outer.handleOnDataSuccess(list, this.localDataListener);
            }
        }

        public void onFetchAlbumFail(ApiException e) {
            String str = null;
            SearchResultApi outer = (SearchResultApi) this.mOuter.get();
            if (outer != null) {
                DebugUtils.limitNetSpeed();
                if (this.localDataListener == null) {
                    if (!SearchResultApi.NOLOG) {
                        str = "OnDataFetchedImpl---fail localDataListener=null--return--tag.name=" + (this.localTag != null ? this.localTag.getName() : "null");
                    }
                    outer.log(str);
                    return;
                }
                String tagMsg;
                if (SearchResultApi.NOLOG) {
                    tagMsg = null;
                } else {
                    tagMsg = "OnDataFetchedImpl---fail---tag.name=" + (this.localTag != null ? this.localTag.getName() : "null") + "--timeToken = " + (System.currentTimeMillis() - this.localLogTime1);
                }
                outer.logAndRecord(tagMsg);
                outer.handleDataApiOnDataFail(e, this.localDataListener);
            }
        }
    }

    public SearchResultApi(AlbumInfoModel model) {
        super(model);
        initChildrenDataApi();
    }

    private void initChildrenDataApi() {
        clearImplList();
        log(NOLOG ? null : "initChildrenDataApi---tagType = " + (this.mNewTag != null ? this.mNewTag.getType() : "-100"));
        this.mDataApi = new ChannelAlbumLoader(this.mAlbumSource, this.mAlbumSet, this.mInfoModel);
    }

    protected void resetChildrenApi() {
        initChildrenDataApi();
    }

    private void clearImplList() {
        if (this.mDataFetchedImpls == null) {
            this.mDataFetchedImpls = new ArrayList();
            return;
        }
        for (OnDataFetchedImpl impl : this.mDataFetchedImpls) {
            impl.closeCallback();
        }
        this.mDataFetchedImpls.clear();
    }

    public void loadAlbumData(OnAlbumFetchedListener fetchingDataListener) {
        if (!isNeedLoad()) {
            return;
        }
        if (isSameTag(this.mLoadingTag, this.mNewTag) && this.mPrePageIndex == this.mCurPageIndex) {
            printLogMessage();
            handleOnDataSuccess(AlbumDataMakeupFactory.get().dataListMakeup(this.mOriginalList, this.mNewTag.getLayout(), this.mCurPageIndex, this.mInfoModel), fetchingDataListener);
            return;
        }
        this.mIsLoading = true;
        this.mLoadingTag = this.mNewTag;
        printLogMessage();
        OnDataFetchedImpl fetchingData = new OnDataFetchedImpl(this, fetchingDataListener, this.mLoadingTag);
        this.mDataFetchedImpls.add(fetchingData);
        this.mDataApi.fetchAlbumData(getEachPageCount(), this.mCurPageIndex, fetchingData, this.mLoadingTag);
    }

    private void printLogMessage() {
        String tagMsg;
        if (NOLOG) {
            tagMsg = null;
        } else {
            String str;
            StringBuilder append = new StringBuilder().append("loadAlbumData---CurPageIndex = ").append(this.mCurPageIndex).append("--LoadingTag=[");
            if (this.mLoadingTag == null) {
                str = "null";
            } else {
                str = "id=" + this.mLoadingTag.getID() + "--name=" + this.mLoadingTag.getName();
            }
            tagMsg = append.append(str).toString();
        }
        logAndRecord(tagMsg);
    }

    protected IAlbumSource getAlbumSource() {
        return this.mAlbumProvider.getSearchSourceByChinese(this.mInfoModel.getSearchModel().getKeyWord());
    }

    public IAlbumSet getAlbumSet() {
        if (SourceTool.PEOPLE_TAG.equals(this.mNewTag.getType())) {
            return ((AlbumSearchSource) this.mAlbumSource).getSearchPeopleSet(this.mInfoModel.getSearchModel().getQpId(), this.mNewTag);
        }
        return this.mAlbumSource.getSearchAlbumSet(this.mNewTag);
    }

    public void loadLabelData(OnLabelFetchedListener fetchingLabel) {
        String str = null;
        if (this.mAlbumSet == null) {
            if (!NOLOG) {
                str = "loadLabelData---albumset= null ,return;";
            }
            logAndRecord(str);
            return;
        }
        this.mTagLabelList.clear();
        if (!NOLOG) {
            str = "loadLabelData-- eachPageCount = " + getEachPageCount() + "--index = " + getOriginalPage() + "--AlbumSet = " + this.mAlbumSet;
        }
        logAndRecord(str);
        long time1 = System.currentTimeMillis();
        this.mAlbumSet.loadDataAsync(getOriginalPage(), getEachPageCount(), new IAlbumCallbackImpl(this, fetchingLabel));
    }

    protected int getOriginalPage() {
        return 1;
    }

    protected int getEachPageCount() {
        return 60;
    }

    public int getSelectType() {
        return 0;
    }

    public int getRecommendType() {
        return 0;
    }

    public Tag getDefaultTag() {
        if (StringUtils.isEmpty(this.mInfoModel.getSearchModel().getQpId())) {
            return this.mAlbumSource.getDefaultTag();
        }
        return ((AlbumSearchSource) this.mAlbumSource).getPeopleDefaultTag();
    }

    protected void setTotalCount() {
        int searchCount = this.mAlbumSet.getSearchCount();
        this.mDisplayCount = searchCount;
        this.mTotalItemCount = searchCount;
    }

    protected String getLogCatTag() {
        return "SearchResultApi";
    }
}
