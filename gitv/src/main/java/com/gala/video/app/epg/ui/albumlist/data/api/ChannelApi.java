package com.gala.video.app.epg.ui.albumlist.data.api;

import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.base.IAlbumProvider;
import com.gala.albumprovider.base.IAlbumSource;
import com.gala.albumprovider.base.ITagCallback;
import com.gala.albumprovider.logic.set.ChannelPlayListSet;
import com.gala.albumprovider.logic.set.ChannelResourceSet;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnLabelFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.factory.DataInfoProvider;
import com.gala.video.app.epg.ui.albumlist.data.loader.BaseDataLoader;
import com.gala.video.app.epg.ui.albumlist.data.loader.ChannelAlbumLoader;
import com.gala.video.app.epg.ui.albumlist.data.loader.ChannelLabelLoader;
import com.gala.video.app.epg.ui.albumlist.data.loader.ChannelPlayListLoader;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.albumlist.utils.DebugUtils;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.app.epg.ui.albumlist.utils.TagUtils;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.project.Project;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ChannelApi extends BaseDataApi {
    private int mChannelId;
    private List<OnDataFetchedImpl> mDataFetchedImpls = new ArrayList();
    private BaseDataLoader mFetchApi;
    private Tag recommendTag;

    private static class IAlbumCallbackImpl implements IAlbumCallback {
        private OnLabelFetchedListener mFetchingLabel;
        private WeakReference<ChannelApi> mOuter;
        private long time1 = System.currentTimeMillis();

        public IAlbumCallbackImpl(ChannelApi outer, OnLabelFetchedListener fetchingLabel) {
            this.mOuter = new WeakReference(outer);
            this.mFetchingLabel = fetchingLabel;
        }

        public void onSuccess(int arg0, List<Album> list) {
            ChannelApi outer = (ChannelApi) this.mOuter.get();
            if (outer != null) {
                DebugUtils.limitNetSpeed();
                outer.logAndRecord(ChannelApi.NOLOG ? null : "loadLabelDataAsync---虚拟频道 success , time consume = " + (System.currentTimeMillis() - this.time1));
                outer.generateLabelList(outer.mAlbumSet.getTagList());
                this.mFetchingLabel.onFetchLabelSuccess(outer.mTagLabelList);
            }
        }

        public void onFailure(int arg0, ApiException apiException) {
            final ChannelApi outer = (ChannelApi) this.mOuter.get();
            if (outer != null) {
                final long time2 = System.currentTimeMillis();
                outer.logAndRecord(ChannelApi.NOLOG ? null : "loadLabelDataAsync---虚拟频道 fail , e=" + apiException + " start net work check. time consume = " + (time2 - this.time1));
                final ApiException apiException2 = apiException;
                NetWorkManager.getInstance().checkNetWork(new StateCallback() {
                    public void getStateResult(int state) {
                        outer.log(ChannelApi.NOLOG ? null : "loadLabelDataAsync---虚拟频道 fail, end net work check consume = " + (System.currentTimeMillis() - time2));
                        IAlbumCallbackImpl.this.mFetchingLabel.onFetchLabelFail(apiException2);
                    }
                });
                QAPingback.error(ChannelApi.LOG_TAG, String.valueOf(outer.mInfoModel.getChannelId()), outer.mInfoModel.getDataTagName(), apiException);
            }
        }
    }

    private static class ITagCallbackImpl implements ITagCallback {
        private OnLabelFetchedListener mFetchingLabel;
        private WeakReference<ChannelApi> mOuter;
        private long time1 = System.currentTimeMillis();

        public ITagCallbackImpl(ChannelApi outer, OnLabelFetchedListener fetchingLabel) {
            this.mOuter = new WeakReference(outer);
            this.mFetchingLabel = fetchingLabel;
        }

        public void onSuccess(List<Tag> tags) {
            ChannelApi outer = (ChannelApi) this.mOuter.get();
            if (outer != null) {
                DebugUtils.limitNetSpeed();
                outer.logAndRecord(ChannelApi.NOLOG ? null : "loadLabelDataAsync---实体频道、会员频道、直播频道 success , time consume = " + (System.currentTimeMillis() - this.time1));
                outer.deleteH5Data(tags);
                outer.generateLabelList(tags);
                outer.handleRecommendTag();
                this.mFetchingLabel.onFetchLabelSuccess(outer.mTagLabelList);
            }
        }

        public void onFailure(ApiException exception) {
            final ChannelApi outer = (ChannelApi) this.mOuter.get();
            if (outer != null) {
                final long time2 = System.currentTimeMillis();
                outer.logAndRecord(ChannelApi.NOLOG ? null : "loadLabelDataAsync---实体频道、会员频道、直播频道 fail , e=" + exception + "-- start net work check. time consume = " + (time2 - this.time1));
                final ApiException apiException = exception;
                NetWorkManager.getInstance().checkNetWork(new StateCallback() {
                    public void getStateResult(int state) {
                        outer.log(ChannelApi.NOLOG ? null : "loadLabelDataAsync---实体频道 fail, end net work check consume = " + (System.currentTimeMillis() - time2));
                        ITagCallbackImpl.this.mFetchingLabel.onFetchLabelFail(apiException);
                    }
                });
                QAPingback.error(ChannelApi.LOG_TAG, String.valueOf(outer.mInfoModel.getChannelId()), outer.mInfoModel.getDataTagName(), exception);
            }
        }
    }

    private static class OnDataFetchedImpl implements OnAlbumFetchedListener {
        private OnAlbumFetchedListener localDataListener;
        private int localIndex;
        private long localLogTime1 = System.currentTimeMillis();
        private Tag localTag;
        private WeakReference<ChannelApi> mOuter;

        public OnDataFetchedImpl(ChannelApi outer, OnAlbumFetchedListener fetchingData, Tag tag, int index) {
            this.mOuter = new WeakReference(outer);
            this.localDataListener = fetchingData;
            this.localTag = tag;
            this.localIndex = index;
        }

        public void closeCallback() {
            this.localDataListener = null;
        }

        public void onFetchAlbumSuccess(List<IData> list) {
            String str = null;
            ChannelApi outer = (ChannelApi) this.mOuter.get();
            if (outer != null) {
                DebugUtils.limitNetSpeed();
                if (this.localDataListener == null) {
                    if (!ChannelApi.NOLOG) {
                        str = "OnDataFetchedImpl---success localDataListener=null--return--tag.name=" + (this.localTag != null ? this.localTag.getName() : "null");
                    }
                    outer.log(str);
                    return;
                }
                String tagMsg;
                if (ChannelApi.NOLOG) {
                    tagMsg = null;
                } else {
                    tagMsg = "OnDataFetchedImpl---success--pageIndex = " + this.localIndex + "---getRealList.size = " + ListUtils.getCount((List) list) + "--timeToken = " + (System.currentTimeMillis() - this.localLogTime1);
                }
                outer.logAndRecord(tagMsg);
                outer.mOriginalList = outer.mFetchApi.getOriginalList();
                outer.handleOnDataSuccess(list, this.localDataListener);
            }
        }

        public void onFetchAlbumFail(ApiException e) {
            String str = null;
            ChannelApi outer = (ChannelApi) this.mOuter.get();
            if (outer != null) {
                DebugUtils.limitNetSpeed();
                if (this.localDataListener == null) {
                    if (!ChannelApi.NOLOG) {
                        str = "OnDataFetchedImpl---fail localDataListener=null--return--tag.name=" + (this.localTag != null ? this.localTag.getName() : "null");
                    }
                    outer.log(str);
                    return;
                }
                outer.logAndRecord(ChannelApi.NOLOG ? null : "OnDataFetchedImpl---fail--timeToken = " + (System.currentTimeMillis() - this.localLogTime1));
                outer.handleDataApiOnDataFail(e, this.localDataListener);
            }
        }
    }

    public ChannelApi(AlbumInfoModel infoModel) {
        super(infoModel);
        this.mChannelId = infoModel.getChannelId();
        initChildrenDataApi();
    }

    private void initChildrenDataApi() {
        clearImplList();
        log(NOLOG ? null : "initChildrenDataApi---AlbumSet:" + this.mAlbumSet);
        if (this.mAlbumSet instanceof ChannelResourceSet) {
            this.mFetchApi = new ChannelLabelLoader(this.mAlbumSource, this.mAlbumSet, this.mInfoModel);
        } else if (this.mAlbumSet instanceof ChannelPlayListSet) {
            this.mFetchApi = new ChannelPlayListLoader(this.mAlbumSource, this.mAlbumSet, this.mInfoModel);
        } else {
            this.mFetchApi = new ChannelAlbumLoader(this.mAlbumSource, this.mAlbumSet, this.mInfoModel);
        }
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
        if (isNeedLoad()) {
            String str;
            this.mIsLoading = true;
            this.mLoadingTag = this.mNewTag;
            if (NOLOG) {
                str = null;
            } else {
                str = "loadAlbumData---CurPageIndex = " + this.mCurPageIndex + "--LoadingTag=[" + (this.mLoadingTag == null ? "null" : "tag.name=" + this.mLoadingTag.getName());
            }
            log(str);
            OnDataFetchedImpl onDataFetchedImpl = new OnDataFetchedImpl(this, fetchingDataListener, this.mLoadingTag, this.mCurPageIndex);
            this.mDataFetchedImpls.add(onDataFetchedImpl);
            if (this.mFetchApi instanceof ChannelLabelLoader) {
                ((ChannelLabelLoader) this.mFetchApi).setRecTag(this.recommendTag);
            }
            this.mFetchApi.fetchAlbumData(getEachPageCount(), this.mCurPageIndex, onDataFetchedImpl, this.mLoadingTag);
        }
    }

    protected IAlbumSource getAlbumSource() {
        boolean z;
        this.mChannelId = this.mInfoModel.getChannelId();
        String version = Project.getInstance().getBuild().getVersionString();
        IAlbumProvider iAlbumProvider = this.mAlbumProvider;
        String valueOf = String.valueOf(this.mChannelId);
        if (GetInterfaceTools.getIDynamicQDataProvider().isSupportVip()) {
            z = false;
        } else {
            z = true;
        }
        return iAlbumProvider.getChannelAlbumSource(valueOf, z, version, Project.getInstance().getBuild().isShowLive(), false);
    }

    public void loadLabelData(OnLabelFetchedListener fetchingLabel) {
        String str = null;
        if (!ListUtils.isEmpty(this.mTagLabelList)) {
            if (!NOLOG) {
                str = "loadLabelData---label.size = " + ListUtils.getCount(this.mTagLabelList);
            }
            log(str);
            fetchingLabel.onFetchLabelSuccess(this.mTagLabelList);
        } else if ((AlbumInfoFactory.isNewVipChannel(this.mInfoModel.getChannelId()) || AlbumInfoFactory.isLiveChannel(this.mInfoModel.getChannelId(), this.mInfoModel.getPageType()) || AlbumInfoFactory.isTrueChannel(this.mChannelId)) && this.mAlbumSource != null) {
            logAndRecord(NOLOG ? null : "loadLabelDataAsync---实体频道、会员频道、直播频道channelid=" + this.mChannelId);
            this.mAlbumSource.getTags(new ITagCallbackImpl(this, fetchingLabel));
        } else if (this.mAlbumSet != null) {
            this.mAlbumSet.loadDataAsync(getOriginalPage(), getEachPageCount(), new IAlbumCallbackImpl(this, fetchingLabel));
        } else {
            logAndRecord(NOLOG ? null : "loadLabelDataAsync---not found interface");
        }
    }

    private void generateLabelList(List<Tag> tags) {
        int count = ListUtils.getCount((List) tags);
        logAndRecord(NOLOG ? null : "loadLabelData---success--label.size = " + count + "---they are:");
        for (int i = 0; i < count; i++) {
            logAndRecord(NOLOG ? null : "loadLabelData---success--tag name = " + ((Tag) tags.get(i)).getName());
        }
        if (count > 0 && tags != null) {
            for (Tag tag : tags) {
                this.mTagLabelList.add(tag);
                List tagList2 = tag.getTagList();
                if (!ListUtils.isEmpty(tagList2)) {
                    this.mTagLabelList.addAll(tagList2);
                }
            }
        }
    }

    private void handleRecommendTag() {
        String str = null;
        if (this.mChannelId <= -1) {
            if (!NOLOG) {
                str = "handleRecommendTag---ChannelId not found";
            }
            log(str);
        } else if (DataInfoProvider.isRecommend1Type(this.mChannelId)) {
            this.recommendTag = null;
        } else if (DataInfoProvider.isRecommend2Type(this.mChannelId)) {
            for (Tag tag : this.mTagLabelList) {
                if (tag != null && SourceTool.REC_CHANNEL_TAG.equals(tag.getType())) {
                    this.mTagLabelList.remove(tag);
                    this.recommendTag = tag;
                    return;
                }
            }
        }
    }

    protected int getOriginalPage() {
        return 1;
    }

    protected int getEachPageCount() {
        return 60;
    }

    public int getSelectType() {
        return DataInfoProvider.getSelectViewType(getMultiTags(), this.mChannelId);
    }

    public int getRecommendType() {
        boolean isVipRecommend = AlbumInfoFactory.isNewVipChannel(this.mInfoModel.getChannelId());
        boolean isLiveRecommend = AlbumInfoFactory.isLiveChannel(this.mInfoModel.getChannelId(), this.mInfoModel.getPageType());
        if (isVipRecommend || isLiveRecommend) {
            return 1;
        }
        if (this.mChannelId <= -1) {
            return 0;
        }
        if (DataInfoProvider.isRecommend1Type(this.mChannelId)) {
            return 1;
        }
        if (DataInfoProvider.isRecommend2Type(this.mChannelId)) {
            return 2;
        }
        return 0;
    }

    protected void setTotalCount() {
        this.mTotalItemCount = this.mAlbumSet.getAlbumCount();
        this.mDisplayCount = this.mAlbumSet.getSearchCount();
    }

    public int getLabelFirstLocation() {
        if (this.mInfoModel.isJumpNextByRecTag()) {
            return TagUtils.getRecommendTagIndex(this.mTagLabelList) + 1;
        }
        return TagUtils.getIndex(this.mInfoModel.getFirstLabelLocationTagId(), this.mTagLabelList);
    }

    protected String getLogCatTag() {
        return "ChannelApi";
    }

    private void deleteH5Data(List<Tag> list) {
    }
}
