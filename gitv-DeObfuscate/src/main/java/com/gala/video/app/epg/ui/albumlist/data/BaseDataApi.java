package com.gala.video.app.epg.ui.albumlist.data;

import android.util.Log;
import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IAlbumProvider;
import com.gala.albumprovider.base.IAlbumSet;
import com.gala.albumprovider.base.IAlbumSource;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.tv2.model.TwoLevelTag;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.DebugUtils;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDataApi {
    protected static String LOG_TAG = "EPG/album4/BaseDataApi";
    protected static boolean NOLOG = (!DebugUtils.ALBUM4_NEEDLOG);
    protected final IAlbumProvider mAlbumProvider = AlbumProviderApi.getAlbumProvider();
    protected IAlbumSet mAlbumSet;
    protected IAlbumSource mAlbumSource;
    protected int mCurPageIndex;
    protected List<IData> mDataList = null;
    protected int mDisplayCount;
    protected AlbumInfoModel mInfoModel;
    protected boolean mIsLoading;
    protected int mLabelLimitSize;
    protected Tag mLoadingTag;
    protected Tag mNewTag;
    private OnLoadStatusListener mOnLoadStatusListener;
    protected List<?> mOriginalList;
    protected int mPrePageIndex;
    protected List<Tag> mTagLabelList = new ArrayList();
    protected int mTotalItemCount;

    private static class INetWorkManagerImpl implements StateCallback {
        private final ApiException f1928e;
        private final OnAlbumFetchedListener listener;
        private WeakReference<BaseDataApi> mOuter;
        final long time1 = System.currentTimeMillis();

        public INetWorkManagerImpl(BaseDataApi outer, OnAlbumFetchedListener listener, ApiException e) {
            this.mOuter = new WeakReference(outer);
            this.listener = listener;
            this.f1928e = e;
        }

        public void getStateResult(int state) {
            BaseDataApi outer = (BaseDataApi) this.mOuter.get();
            if (outer != null) {
                outer.log(BaseDataApi.NOLOG ? null : "BaseDataApi---handleDataApiOnDataFail---end netcheck timeToken=" + (System.currentTimeMillis() - this.time1));
                this.listener.onFetchAlbumFail(this.f1928e);
            }
        }
    }

    public interface OnAlbumFetchedListener {
        void onFetchAlbumFail(ApiException apiException);

        void onFetchAlbumSuccess(List<IData> list);
    }

    public interface OnLabelFetchedListener {
        void onFetchLabelFail(ApiException apiException);

        void onFetchLabelSuccess(List<Tag> list);
    }

    public interface OnLoadStatusListener {
        void onComplete();
    }

    protected abstract IAlbumSource getAlbumSource();

    protected abstract int getEachPageCount();

    protected abstract String getLogCatTag();

    protected abstract int getOriginalPage();

    public abstract int getRecommendType();

    public abstract int getSelectType();

    public abstract void loadAlbumData(OnAlbumFetchedListener onAlbumFetchedListener);

    public abstract void loadLabelData(OnLabelFetchedListener onLabelFetchedListener);

    protected abstract void resetChildrenApi();

    protected abstract void setTotalCount();

    public BaseDataApi(AlbumInfoModel infoModel) {
        LOG_TAG = "EPG/album4/" + getLogCatTag();
        this.mInfoModel = infoModel;
        this.mAlbumSource = getAlbumSource();
        this.mNewTag = getDefaultTag();
        this.mAlbumSet = getAlbumSet();
        this.mLabelLimitSize = this.mInfoModel.getLoadLimitSize();
        this.mTotalItemCount = 0;
        this.mCurPageIndex = getOriginalPage();
        this.mDataList = new ArrayList();
        this.mLoadingTag = null;
        this.mPrePageIndex = -1;
    }

    protected void handleOnDataSuccess(List<IData> list, OnAlbumFetchedListener listener) {
        String str;
        String str2 = null;
        this.mIsLoading = false;
        this.mLoadingTag = this.mNewTag;
        this.mPrePageIndex = this.mCurPageIndex;
        log(NOLOG ? null : "handleOnDataSuccess---list.size=" + ListUtils.getCount((List) list) + "---curPageIndex=" + this.mCurPageIndex);
        if (ListUtils.isEmpty((List) list)) {
            log(NOLOG ? null : "handleOnDataSuccess---list is empty,return. curPageIndex=" + this.mCurPageIndex);
            this.mCurPageIndex++;
            if (this.mCurPageIndex < getLoadMaxTimes()) {
                if (!NOLOG) {
                    str2 = "handleOnDataSuccess---list is empty,主动load";
                }
                log(str2);
                loadAlbumData(listener);
                return;
            }
            if (this.mOnLoadStatusListener != null) {
                log(NOLOG ? null : "handleOnDataSuccess---list is empty,回调加载结束");
                this.mOnLoadStatusListener.onComplete();
            }
            this.mCurPageIndex--;
        }
        setTotalCount();
        log(NOLOG ? null : "handleOnDataSuccess---TotalCount=" + this.mTotalItemCount + "---DisplayCount=" + this.mDisplayCount);
        if (NOLOG) {
            str = null;
        } else {
            str = "handleOnDataSuccess---TotalCount=" + this.mTotalItemCount + "---DisplayCount=" + this.mDisplayCount;
        }
        logRecord(str);
        if (this.mCurPageIndex <= getOriginalPage() && this.mLabelLimitSize > 0 && this.mLabelLimitSize < getEachPageCount() && this.mLabelLimitSize < ListUtils.getCount((List) list)) {
            list = list.subList(0, this.mLabelLimitSize);
        }
        if (list != null) {
            this.mDataList.addAll(list);
        }
        this.mCurPageIndex++;
        if (this.mCurPageIndex > getLoadMaxTimes()) {
            this.mTotalItemCount = this.mDataList.size();
        }
        if (this.mLabelLimitSize > 0 && this.mLabelLimitSize < ListUtils.getCount(this.mDataList)) {
            this.mDataList = this.mDataList.subList(0, this.mLabelLimitSize);
            this.mTotalItemCount = this.mLabelLimitSize;
        }
        if (this.mDisplayCount <= 0) {
            this.mDisplayCount = this.mTotalItemCount;
        }
        if (listener != null) {
            listener.onFetchAlbumSuccess(this.mDataList);
            return;
        }
        if (!NOLOG) {
            str2 = "handleOnDataSuccess---listener=null";
        }
        log(str2);
    }

    protected void handleDataApiOnDataFail(ApiException e, OnAlbumFetchedListener listener) {
        String str = null;
        log(NOLOG ? null : "BaseDataApi---handleDataApiOnDataFail---start netcheck---e = " + e);
        if (!NOLOG) {
            str = "BaseDataApi---handleDataApiOnDataFail---start netcheck---e = " + e;
        }
        logRecord(str);
        this.mIsLoading = false;
        this.mLoadingTag = this.mNewTag;
        this.mPrePageIndex = this.mCurPageIndex;
        NetWorkManager.getInstance().checkNetWork(new INetWorkManagerImpl(this, listener, e));
        QAPingback.error(LOG_TAG, String.valueOf(this.mInfoModel.getChannelId()), this.mInfoModel.getDataTagName(), e);
    }

    protected boolean isNeedLoad() {
        boolean b = true;
        String str = null;
        if (this.mLoadingTag != null && !this.mLoadingTag.equals(this.mNewTag)) {
            if (!NOLOG) {
                str = "isNeedLoad---新tag与旧tag不同，需load";
            }
            log(str);
            return true;
        } else if (this.mIsLoading) {
            if (!NOLOG) {
                str = "isNeedLoad---mIsLoading=true，不load";
            }
            log(str);
            return false;
        } else if (this.mTotalItemCount == 0) {
            if (!NOLOG) {
                str = "isNeedLoad---mTotalItemCount == 0 ， 需load";
            }
            log(str);
            return true;
        } else if (ListUtils.getCount(this.mDataList) < this.mLabelLimitSize) {
            if (!NOLOG) {
                str = "isNeedLoad---mDataList.size < mLabelLimitSize ， 需load";
            }
            log(str);
            return true;
        } else {
            int loadMaxTimes = getLoadMaxTimes();
            log(NOLOG ? null : "isNeedLoad---mCurPageIndex = " + this.mCurPageIndex + ", loadMaxTimes = " + loadMaxTimes + "---mTotalItemCount=" + this.mTotalItemCount);
            if (this.mCurPageIndex > loadMaxTimes) {
                log(NOLOG ? null : "isNeedLoad---mCurPageIndex > loadMaxTimes, 不load");
                if (this.mOnLoadStatusListener != null) {
                    if (!NOLOG) {
                        str = "isNeedLoad---dismiss small loading--1";
                    }
                    log(str);
                    this.mOnLoadStatusListener.onComplete();
                }
                return false;
            }
            log(NOLOG ? null : "isNeedLoad---datalist.size = " + ListUtils.getCount(this.mDataList));
            if (ListUtils.getCount(this.mDataList) >= this.mTotalItemCount) {
                b = false;
            }
            if (b || this.mOnLoadStatusListener == null) {
                return b;
            }
            if (!NOLOG) {
                str = "isNeedLoad---dismiss small loading--2";
            }
            log(str);
            this.mOnLoadStatusListener.onComplete();
            return b;
        }
    }

    protected int getLoadMaxTimes() {
        int total = (this.mLabelLimitSize <= 0 || this.mLabelLimitSize > this.mTotalItemCount) ? this.mTotalItemCount : this.mLabelLimitSize;
        return ((getEachPageCount() + total) - 1) / getEachPageCount();
    }

    public void resetApi(Tag tag) {
        String str = null;
        if (tag == null) {
            if (!NOLOG) {
                str = "resetApi---tag = null, return";
            }
            log(str);
            return;
        }
        if (this.mDataList == null) {
            this.mDataList = new ArrayList();
        } else {
            this.mDataList.clear();
        }
        this.mTotalItemCount = 0;
        this.mNewTag = tag;
        if (!isSameTag(tag, this.mLoadingTag)) {
            this.mAlbumSet = getAlbumSet();
        }
        this.mCurPageIndex = getOriginalPage();
        this.mInfoModel.setDataTagId(this.mNewTag.getID());
        this.mInfoModel.setDataTagName(this.mNewTag.getName());
        this.mInfoModel.setDataTagType(this.mNewTag.getType());
        resetChildrenApi();
        if (!NOLOG) {
            str = "resetApi---NewTag[id=" + this.mNewTag.getID() + ",name=" + this.mNewTag.getName();
        }
        log(str);
    }

    protected boolean isSameTag(Tag tag, Tag newTag) {
        if (tag == null || newTag == null || !StringUtils.equals(tag.getID(), newTag.getID()) || !StringUtils.equals(tag.getName(), newTag.getName())) {
            return false;
        }
        return true;
    }

    public int getCurPage() {
        return this.mCurPageIndex - 1;
    }

    public int getTotalCount() {
        if (this.mLabelLimitSize <= 0 || this.mLabelLimitSize > this.mTotalItemCount) {
            return this.mTotalItemCount;
        }
        return this.mLabelLimitSize;
    }

    public int getDisplayCount() {
        if (this.mLabelLimitSize <= 0 || this.mLabelLimitSize > this.mDisplayCount) {
            return this.mDisplayCount;
        }
        return this.mLabelLimitSize;
    }

    public QLayoutKind getLayoutKind() {
        QLayoutKind l = this.mNewTag != null ? this.mNewTag.getLayout() : null;
        if (l != QLayoutKind.LANDSCAPE) {
            return QLayoutKind.PORTRAIT;
        }
        return l;
    }

    public IAlbumSource getIAlbumSource() {
        return this.mAlbumSource;
    }

    public List<TwoLevelTag> getMultiTags() {
        return this.mAlbumSource.getMultiTags();
    }

    public Tag getNewTag() {
        return this.mNewTag;
    }

    public IAlbumSet getAlbumSet() {
        return this.mAlbumSource.getAlbumSet(this.mNewTag);
    }

    protected Tag getDefaultTag() {
        return this.mAlbumSource.getDefaultTag();
    }

    public int getLabelFirstLocation() {
        return 0;
    }

    public List<?> getOriginalDataList() {
        return this.mOriginalList;
    }

    public void setOnLoadStatusListener(OnLoadStatusListener onLoadStatusListener) {
        this.mOnLoadStatusListener = onLoadStatusListener;
    }

    protected void log(String str) {
        if (str != null) {
            Log.e(LOG_TAG, "qdata//" + str);
        }
    }

    private void logRecord(String str) {
        if (str != null) {
        }
    }

    protected void logAndRecord(String str) {
        if (str != null) {
            Log.e(LOG_TAG, "qdata//" + str);
        }
    }
}
