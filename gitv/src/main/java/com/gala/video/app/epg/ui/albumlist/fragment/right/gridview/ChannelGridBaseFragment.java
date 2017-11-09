package com.gala.video.app.epg.ui.albumlist.fragment.right.gridview;

import android.graphics.Bitmap;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.sdk.player.IMediaPlayer;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnScrollListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.adapter.BaseGridAdapter;
import com.gala.video.app.epg.ui.albumlist.adapter.ChannelHorizontalAdapter;
import com.gala.video.app.epg.ui.albumlist.adapter.GridAdapter;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.fragment.AlbumBaseFragment;
import com.gala.video.app.epg.ui.albumlist.fragment.right.AlbumBaseRightFragment;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class ChannelGridBaseFragment extends AlbumBaseRightFragment {
    private static final int GRIDVIEW_TOP = ResourceUtil.getDimen(R.dimen.dimen_2dp);
    private static final int GRIDVIEW_VERTICALMARGIN = ResourceUtil.getDimen(R.dimen.dimen_0dp);
    protected BaseGridAdapter<IData> mAdapter;
    private final Runnable mCacheViewRunnable = new Runnable() {
        public void run() {
            ChannelGridBaseFragment.this.showCacheView();
        }
    };
    protected List<IData> mConvertList = new ArrayList(1);
    protected int mGridAnimationDuration = 200;
    protected float mGridAnimationScale;
    protected VerticalGridView mGridView;
    protected int mGridViewFocusedPosition;
    private int mGridViewNum = 0;
    private QLayoutKind mLastLayoutKind;
    private final Runnable mLoadAsyncRunnable = new Runnable() {
        public void run() {
            ChannelGridBaseFragment.this.loadDataAsync();
        }
    };
    protected int mNeedRequestIndex = -1;
    private boolean mNetState = true;
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        public void onItemClick(ViewGroup parent, ViewHolder holder) {
            ChannelGridBaseFragment.this.onGridItemClick(parent, holder.itemView, holder.getLayoutPosition());
        }
    };
    private OnItemFocusChangedListener mOnItemFocusChangedListener = new OnItemFocusChangedListener() {
        public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
            int position = holder.getLayoutPosition();
            if (hasFocus) {
                ChannelGridBaseFragment.this.setNextFocusUpId(ChannelGridBaseFragment.this.mGridView);
                ChannelGridBaseFragment.this.mGridViewFocusedPosition = position;
                ChannelGridBaseFragment.this.mCurrentFocusedView = holder.itemView;
                ChannelGridBaseFragment.this.setGlobalLastFocusView(holder.itemView);
            }
            ChannelGridBaseFragment.this.onGridItemSelected(parent, holder.itemView, position, hasFocus);
            ChannelGridBaseFragment.this.mGridView.setLayerType(0, null);
        }
    };
    private OnItemRecycledListener mOnItemRecycledListener = new OnItemRecycledListener() {
        public void onItemRecycled(ViewGroup parent, ViewHolder holder) {
            if (ChannelGridBaseFragment.this.mAdapter != null) {
                ChannelGridBaseFragment.this.mAdapter.recycleBitmap(holder.itemView);
                ChannelGridBaseFragment.this.mAdapter.releaseData(holder.itemView);
            }
        }
    };
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        public void onScrollStart() {
            ChannelGridBaseFragment.this.mBaseHandler.removeCallbacks(ChannelGridBaseFragment.this.mScrollStopRunnable);
            if (ChannelGridBaseFragment.this.mAdapter != null) {
                ChannelGridBaseFragment.this.mAdapter.onCancelAllTasks();
            }
        }

        public void onScrollStop() {
            ChannelGridBaseFragment.this.onReloadTasks();
            onGridRowSelected();
        }

        public void onScroll(ViewParent parent, int firstAttachedItem, int lastAttachedItem, int totalItemCount) {
            if (ChannelGridBaseFragment.this.mGridView.getScrollType() == 19) {
                onGridRowSelected();
            }
        }

        private void onGridRowSelected() {
            int selectedRow = ChannelGridBaseFragment.this.mGridView.getFocusPosition() / ChannelGridBaseFragment.this.mGridView.getNumRows();
            if ((ChannelGridBaseFragment.this.mGridView.getCount() / ChannelGridBaseFragment.this.mGridView.getNumRows()) - selectedRow < 7 && selectedRow > 0 && selectedRow > ChannelGridBaseFragment.this.mSelectedRow) {
                ChannelGridBaseFragment.this.mBaseHandler.removeCallbacks(ChannelGridBaseFragment.this.mLoadAsyncRunnable);
                ChannelGridBaseFragment.this.mBaseHandler.postDelayed(ChannelGridBaseFragment.this.mLoadAsyncRunnable, ChannelGridBaseFragment.this.mGridView.getScrollType() == 19 ? 0 : 800);
            }
            ChannelGridBaseFragment.this.mSelectedRow = selectedRow;
        }

        public void onScrollBefore(int nextPos) {
        }
    };
    private final Runnable mScrollStopRunnable = new Runnable() {
        public void run() {
            if (ChannelGridBaseFragment.this.mGridView != null && ChannelGridBaseFragment.this.mAdapter != null) {
                int first = ChannelGridBaseFragment.this.mGridView.getFirstAttachedPosition();
                int last = ChannelGridBaseFragment.this.mGridView.getLastAttachedPosition();
                int i = first;
                while (i <= last) {
                    View v = ChannelGridBaseFragment.this.mGridView.getViewByPosition(i);
                    if (v != null) {
                        if (HomeDataConfig.LOW_PERFORMANCE_DEVICE) {
                            int topY = (v.getTop() - ChannelGridBaseFragment.this.mGridView.getScrollY()) + 16;
                            int bottomY = (v.getBottom() - ChannelGridBaseFragment.this.mGridView.getScrollY()) - 16;
                            int height = ChannelGridBaseFragment.this.mGridView.getBottom() - ChannelGridBaseFragment.this.mGridView.getTop();
                            if ((topY <= 0 || topY >= height) && (bottomY <= 0 || bottomY >= height)) {
                                ChannelGridBaseFragment.this.mAdapter.recycleBitmap(v);
                            } else {
                                ChannelGridBaseFragment.this.mAdapter.onReloadTasks(v);
                            }
                        } else {
                            ChannelGridBaseFragment.this.mAdapter.onReloadTasks(v);
                        }
                        i++;
                    } else {
                        return;
                    }
                }
            }
        }
    };
    protected int mSelectedRow;
    protected long mStartLoadingTime;

    private static class GridAlbumFetchListenerImpl implements OnAlbumFetchedListener {
        WeakReference<ChannelGridBaseFragment> mOuter;

        public GridAlbumFetchListenerImpl(ChannelGridBaseFragment outer) {
            this.mOuter = new WeakReference(outer);
        }

        public synchronized void onFetchAlbumSuccess(final List<IData> list) {
            final ChannelGridBaseFragment outer = (ChannelGridBaseFragment) this.mOuter.get();
            if (outer != null) {
                outer.runOnUiThread(new Runnable() {
                    public void run() {
                        boolean z = false;
                        outer.mAdapter.hideLoading();
                        long consumeTime = System.currentTimeMillis() - outer.mStartLoadingTime;
                        if (outer.mDataApi != null && !outer.isRemoving() && outer.mConvertList != null && outer.mGridView != null && outer.mIAlbumBaseEvent != null) {
                            int curPage = outer.mDataApi.getCurPage();
                            int listCount = ListUtils.getCount(list);
                            outer.log(ChannelGridBaseFragment.NOLOG ? null : "loadDataAsync success curPage=" + curPage + "--list.size=" + listCount + "---visitNet timeToken=" + consumeTime);
                            outer.onFetchDataSucceed(list);
                            QAPingback.sendAlbumPageShowPingback(curPage, listCount, outer.mInfoModel, consumeTime, outer.isNeedSendPageShowPingback(), 0);
                            BaseGridAdapter baseGridAdapter = outer.mAdapter;
                            if (list.size() < outer.mDataApi.getTotalCount()) {
                                z = true;
                            }
                            baseGridAdapter.showLoading(z);
                            if (list.size() < outer.mAdapter.getDefalutCount()) {
                                outer.mAdapter.hideLoading();
                            }
                        }
                    }
                });
            }
        }

        public void onFetchAlbumFail(final ApiException e) {
            final ChannelGridBaseFragment outer = (ChannelGridBaseFragment) this.mOuter.get();
            if (outer != null) {
                outer.runOnUiThread(new Runnable() {
                    public void run() {
                        String str = null;
                        if (outer.mDataApi == null || outer.isRemoving() || outer.mIAlbumBaseEvent == null) {
                            ChannelGridBaseFragment channelGridBaseFragment = outer;
                            if (!ChannelGridBaseFragment.NOLOG) {
                                str = "---loadDataAsync---fail---mDataApi=" + outer.mDataApi + "--isRemoving()=" + outer.isRemoving() + "---visitNet timeToken=" + (System.currentTimeMillis() - outer.mStartLoadingTime);
                            }
                            channelGridBaseFragment.log(str);
                            return;
                        }
                        String str2;
                        String code = e != null ? e.getCode() : "";
                        String failLog = "---loadDataAsync---fail--e=" + e + "---code=" + code + "---timeToken=" + (System.currentTimeMillis() - outer.mStartLoadingTime);
                        ChannelGridBaseFragment channelGridBaseFragment2 = outer;
                        if (ChannelGridBaseFragment.NOLOG) {
                            str2 = null;
                        } else {
                            str2 = failLog;
                        }
                        channelGridBaseFragment2.log(str2);
                        channelGridBaseFragment = outer;
                        if (!ChannelGridBaseFragment.NOLOG) {
                            str = failLog;
                        }
                        channelGridBaseFragment.logRecord(str);
                        outer.onFetchDataFailure(e, code);
                    }
                });
            }
        }
    }

    protected abstract BaseDataApi getNewDataApi();

    protected abstract BaseGridAdapter<IData> getVerticalGridAdapter();

    protected abstract void onFetchDataFailure(ApiException apiException, String str);

    protected abstract void onFetchDataSucceed(List<IData> list);

    protected int getLayoutResId() {
        return R.layout.epg_q_album_right5;
    }

    protected void initView() {
        initGridView();
    }

    protected void loadData() {
        String str = null;
        if (this.mDataApi == null) {
            showNoResultPanel(ErrorKind.NET_ERROR, null);
            if (!AlbumBaseFragment.NOLOG) {
                str = "---loadData---mDataApi = null";
            }
            log(str);
            return;
        }
        showProgress();
        resetTempValues();
        this.mBaseHandler.removeCallbacks(this.mCacheViewRunnable);
        if (this.mShowCacheWithoutLoadData) {
            this.mBaseHandler.postDelayed(this.mCacheViewRunnable, 0);
            this.mShowCacheWithoutLoadData = false;
            return;
        }
        this.mBaseHandler.postDelayed(this.mCacheViewRunnable, 250);
        loadDataAsync();
    }

    private void showCacheView() {
        if (!AlbumInfoFactory.needShowLoadingView(this.mInfoModel.getPageType())) {
            log(AlbumBaseFragment.NOLOG ? null : "---show cache view--right");
            showHasCachePanel();
        }
    }

    protected void resetTempValues() {
        this.mTotalItemCount = 0;
        this.mDisplayTotal = 0;
        this.mSelectedRow = 0;
        this.mGridViewFocusedPosition = 0;
        this.mGridAnimationDuration = 200;
        setRightFragmentHasData(false);
        setShowingCacheData(false);
        this.mCurrentFocusedView = null;
        if (this.mConvertList != null) {
            this.mConvertList.clear();
        } else {
            this.mConvertList = new ArrayList(1);
        }
        prepareAdapter();
        this.mGridView.setTotalSize(0);
        this.mGridView.setFocusPosition(0);
        this.mGridView.setExtraPadding(IMediaPlayer.AD_INFO_OVERLAY_LOGIN_SUCCESS);
    }

    private void prepareAdapter() {
        String str = null;
        BaseDataApi fetchNewDataApi = getNewDataApi();
        if (fetchNewDataApi != null) {
            setDataApi(fetchNewDataApi);
        }
        QLayoutKind layout = this.mDataApi.getLayoutKind();
        if (this.mLastLayoutKind == null || this.mLastLayoutKind != layout) {
            String str2;
            String kindLog = "---prepareAdapter---LastKind=" + this.mLastLayoutKind + "---NewTag().getLayout=" + layout;
            if (AlbumBaseFragment.NOLOG) {
                str2 = null;
            } else {
                str2 = kindLog;
            }
            log(str2);
            if (!AlbumBaseFragment.NOLOG) {
                str = kindLog;
            }
            logRecord(str);
            this.mLastLayoutKind = layout;
            this.mAdapter = getVerticalGridAdapter();
            setGridParams();
        }
    }

    protected void setGridParams() {
        int high;
        if (this.mDataApi == null || !QLayoutKind.LANDSCAPE.equals(this.mDataApi.getLayoutKind())) {
            this.mGridView.setNumRows(5);
            int low = GRIDVIEW_TOP + (GridAdapter.HEIGHT / 2);
            high = (GridAdapter.HEIGHT + low) + GRIDVIEW_VERTICALMARGIN;
            this.mGridView.setFocusPlace(low, low);
        } else {
            this.mGridView.setNumRows(4);
            high = GRIDVIEW_TOP + (ChannelHorizontalAdapter.HEIGHT / 2);
            this.mGridView.setFocusPlace(high, high);
        }
        this.mGridView.setFocusable(false);
        this.mGridView.setFocusLoop(true);
        this.mGridView.setFocusMode(1);
        this.mGridView.setScrollRoteScale(0.8f, 1.0f, 2.5f);
        this.mGridView.setLayerType(2, null);
        this.mGridView.setExtraPadding(IMediaPlayer.AD_INFO_OVERLAY_LOGIN_SUCCESS);
        this.mGridView.setPadding(ResourceUtil.getDimen(R.dimen.dimen_24dp), ResourceUtil.getDimen(R.dimen.dimen_2dp), ResourceUtil.getDimen(R.dimen.dimen_10dp), ResourceUtil.getDimen(R.dimen.dimen_15dp));
        this.mGridView.setVerticalMargin(ResourceUtil.getPx(-7));
        this.mGridView.setHorizontalMargin(ResourceUtil.getDimen(R.dimen.dimen_4dp));
        this.mGridView.setFocusLeaveForbidden(194);
        this.mGridView.setOnItemFocusChangedListener(this.mOnItemFocusChangedListener);
        this.mGridView.setOnItemClickListener(this.mOnItemClickListener);
        this.mGridView.setOnScrollListener(this.mOnScrollListener);
        this.mGridView.setOnItemRecycledListener(this.mOnItemRecycledListener);
        this.mGridView.setScrollBarDrawable(R.drawable.epg_thumb);
        this.mGridView.setAdapter(this.mAdapter);
        this.mGridViewNum = this.mGridView.getNumRows();
    }

    private void initGridView() {
        this.mGridView = (VerticalGridView) this.mMainView.findViewById(R.id.epg_qalbum_gridview);
        prepareAdapter();
    }

    protected void onGridItemClick(ViewGroup parent, View view, int position) {
        String str = null;
        log(AlbumBaseFragment.NOLOG ? null : "onGridItemClick position=" + position);
        logRecord(AlbumBaseFragment.NOLOG ? null : "--- onGridItemClick Called....position=" + position);
        if (position < 0 || position >= ListUtils.getCount(this.mConvertList)) {
            if (!AlbumBaseFragment.NOLOG) {
                str = "--- onGridItemClick return....";
            }
            log(str);
            return;
        }
        int selectedColumn = position % this.mGridViewNum;
        int selectedRow = position / this.mGridViewNum;
        this.mInfoModel.setFocusPosition(this.mGridViewFocusedPosition);
        this.mInfoModel.setSelectColumn(selectedColumn);
        this.mInfoModel.setSelectRow(selectedRow);
        ((IData) this.mConvertList.get(position)).click(this.mContext, this.mInfoModel);
    }

    protected void onGridItemSelected(ViewGroup parent, View view, int position, boolean hasFocus) {
        AnimationUtil.zoomAnimation(view, hasFocus, this.mGridAnimationScale, 200, true);
        if (!parent.hasFocus()) {
            this.mGridAnimationDuration = 200;
        }
    }

    protected void loadDataAsync() {
        String str;
        if (AlbumBaseFragment.NOLOG) {
            str = null;
        } else {
            str = "---loadDataAsync---next log should be callback--curPage=" + this.mDataApi.getCurPage();
        }
        log(str);
        this.mStartLoadingTime = System.currentTimeMillis();
        this.mDataApi.loadAlbumData(new GridAlbumFetchListenerImpl(this));
    }

    protected void onDownloadCompleted(List<IData> list) {
        String str;
        String str2 = null;
        this.mTotalItemCount = this.mDataApi.getTotalCount();
        this.mDisplayTotal = this.mDataApi.getDisplayCount();
        this.mConvertList = list;
        String completedLog = "---mTotalItemCount=" + this.mTotalItemCount + "---mDisplayTotal=" + this.mDisplayTotal + "---mConvertList.size=" + ListUtils.getCount(this.mConvertList);
        if (AlbumBaseFragment.NOLOG) {
            str = null;
        } else {
            str = completedLog;
        }
        log(str);
        if (AlbumBaseFragment.NOLOG) {
            completedLog = null;
        }
        logRecord(completedLog);
        if (this.mTotalItemCount == 0) {
            int count = ListUtils.getCount((List) list);
            this.mTotalItemCount = count;
            this.mDisplayTotal = count;
            if (list != null) {
                this.mConvertList.addAll(list);
            }
            if (!AlbumBaseFragment.NOLOG) {
                str2 = "---onDownloadCompleted--mTotalItemCount==0 ， 虽做了兼容，但后台必须修复 ！！！";
            }
            log(str2);
        }
        dataNotified();
        QAPingback.multiMenuAfterLoad(this.mContext, this.mInfoModel, this.mDataApi, getMenuView(), this.mStartLoadingTime);
    }

    private void dataNotified() {
        String str = null;
        if (isRemoving() || this.mIAlbumBaseEvent == null) {
            if (!AlbumBaseFragment.NOLOG) {
                str = "---dataNotified---isRemoving() or mGridParams is null--";
            }
            log(str);
            return;
        }
        if (this.mTotalItemCount <= this.mAdapter.getScrollCount() || ListUtils.getCount(this.mConvertList) <= this.mAdapter.getScrollCount()) {
            this.mGridView.setTotalSize(0);
        } else {
            this.mGridView.setTotalSize(this.mTotalItemCount);
        }
        if (this.mDataApi == null || this.mDataApi.getCurPage() > 1) {
            if (!AlbumBaseFragment.NOLOG) {
                str = "---dataNotified---notifyDataSetChanged--";
            }
            log(str);
            this.mAdapter.updateData(this.mConvertList);
        } else {
            if (!AlbumBaseFragment.NOLOG) {
                str = "---dataNotified---notifyDataSetInvalidated--";
            }
            log(str);
            this.mAdapter.onResume();
            this.mAdapter.updateData(this.mConvertList);
            showHasResultPanel();
        }
        this.mGridView.setFocusable(true);
        this.mGridView.setExtraPadding(100);
        if (this.mInfoModel.isMultiHasData()) {
            setGlobalLastFocusView(this.mGridView);
            if (!StringUtils.isEmpty(getInfoModel().getFirstMultiLocationTagId()) && getInfoModel().isFirstContentUpdate()) {
                this.mGridView.requestFocus();
                getInfoModel().setFirstContentUpdate(false);
            }
        }
    }

    private void showHasCachePanel() {
        this.mBaseHandler.removeCallbacks(this.mCacheViewRunnable);
        setShowingCacheData(true);
        if (this.mDataApi != null && this.mDataApi.getCurPage() <= 1) {
            setTopTagTextAfterLoad(this.mTotalItemCount);
        }
        super.showHasResultPanel();
    }

    public void showHasResultPanel() {
        this.mBaseHandler.removeCallbacks(this.mCacheViewRunnable);
        if (this.mDataApi != null && this.mDataApi.getCurPage() <= 1) {
            setTopTagTextAfterLoad(this.mTotalItemCount);
        }
        super.showHasResultPanel();
        setShowingCacheData(false);
    }

    public Bitmap showNoResultPanel(ErrorKind kind, ApiException e) {
        this.mBaseHandler.removeCallbacks(this.mCacheViewRunnable);
        setShowingCacheData(false);
        return super.showNoResultPanel(kind, e);
    }

    protected void setNetworkState(boolean state) {
        this.mNetState = state;
    }

    protected void onNetChanged() {
        String str = null;
        if (isShowingCacheData() && !isLeftFragmentHasData()) {
            return;
        }
        if (this.mDataApi == null || this.mDataApi.getCurPage() > 1 || !ListUtils.isEmpty(this.mConvertList)) {
            if (!this.mNetState) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e("onNetChanged:" + this.mNetState);
                }
                onReloadTasks();
            }
            loadDataAsync();
            if (!AlbumBaseFragment.NOLOG) {
                str = "---onNetChanged----loadDataAsync";
            }
            log(str);
            return;
        }
        loadData();
        if (!AlbumBaseFragment.NOLOG) {
            str = "---onNetChanged----loadData";
        }
        log(str);
    }

    private void onReloadTasks() {
        this.mBaseHandler.removeCallbacks(this.mScrollStopRunnable);
        this.mBaseHandler.post(this.mScrollStopRunnable);
    }

    protected void reloadBitmap() {
        onReloadTasks();
    }

    protected void recyleBitmap() {
    }

    public void onDestroy() {
        if (this.mGridView != null) {
            this.mGridView.setLayerType(0, null);
        }
        this.mBaseHandler.removeCallbacks(this.mScrollStopRunnable);
        this.mBaseHandler.removeCallbacks(this.mCacheViewRunnable);
        this.mBaseHandler.removeCallbacks(this.mLoadAsyncRunnable);
        this.mDataApi = null;
        super.onDestroy();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == 82) {
            setMenu2Activity();
        }
        return super.dispatchKeyEvent(event);
    }

    public void onStop() {
        if (this.mAdapter != null) {
            this.mAdapter.onPause();
        }
        super.onStop();
    }

    public void onStart() {
        if (this.mAdapter != null) {
            this.mAdapter.onResume();
        }
        super.onStart();
    }

    public void handlerMessage2Right(Message msg) {
        super.handlerMessage2Right(msg);
        if (msg != null && msg.what == 51) {
            log(AlbumBaseFragment.NOLOG ? null : "---handlerMessage2Right---refresh GridViewFragment");
            ImageProviderApi.getImageProvider().stopAllTasks();
            if (this.mAdapter != null) {
                this.mAdapter.onPause();
                this.mAdapter.resetList();
            }
            if (getInfoModel() != null) {
                getDataApi();
                resetTopInfoAfterChangeTag();
                loadData();
            }
        }
    }

    protected void resetTopInfoAfterChangeTag() {
    }

    protected boolean isNeedSendPageShowPingback() {
        return true;
    }
}
