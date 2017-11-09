package com.gala.video.app.epg.ui.albumlist.fragment.right.recommend;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.imageprovider.base.ImageRequest.ScaleType;
import com.gala.tvapi.type.ResourceType;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.adapter.BaseGridAdapter;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.fragment.right.AlbumBaseRightFragment;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridView;
import com.gala.video.app.epg.ui.albumlist.widget.RecommendView;
import com.gala.video.app.epg.ui.albumlist.widget.RecommendView.OnSelectedListener;
import com.gala.video.app.epg.utils.EpgImageCache;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class ChannelRecommendBaseFragment extends AlbumBaseRightFragment {
    private static final int ALBUMLIST_POS = 1;
    private static final int BIGVIEW_POS = 0;
    protected static final int MAX_DISPLAY_SIZE = 5;
    private OnClickListener bigViewClickListener = new OnClickListener() {
        public void onClick(View v) {
            ChannelRecommendBaseFragment.this.onClickItem(0);
        }
    };
    private OnSelectedListener bigViewSelectedListener = new OnSelectedListener() {
        public void onSelected(View view, boolean hasSelect) {
            if (hasSelect) {
                ChannelRecommendBaseFragment.this.setNextFocusUpId(view);
                ChannelRecommendBaseFragment.this.mCurrentFocusedView = view;
                ChannelRecommendBaseFragment.this.setGlobalLastFocusView(view);
            }
        }
    };
    private List<IData> mAlbumDataList;
    protected RecommendView mBigView;
    protected IData mBigViewData;
    protected int mBigViewHeight = getBigViewHeight();
    private int mBigViewWidth = getBigViewWidth();
    protected String mBuySourceSrc;
    private final Runnable mCacheViewRunnable = new Runnable() {
        public void run() {
            ChannelRecommendBaseFragment.this.showCacheView();
        }
    };
    private VerticalGridView mGridView;
    protected int mGridViewFocusedPosition;
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        public void onItemClick(ViewGroup parent, ViewHolder holder) {
            ChannelRecommendBaseFragment.this.onClickItem(holder.getLayoutPosition() + 1);
        }
    };
    private OnItemFocusChangedListener mOnItemFocusChangedListener = new OnItemFocusChangedListener() {
        public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
            int position = holder.getLayoutPosition();
            holder.itemView.bringToFront();
            AnimationUtil.zoomAnimation(holder.itemView, hasFocus ? ChannelRecommendBaseFragment.this.getGridAnimationScale() : 1.0f, 200);
            if (hasFocus) {
                ChannelRecommendBaseFragment.this.mGridViewFocusedPosition = position;
                ChannelRecommendBaseFragment.this.mCurrentFocusedView = holder.itemView;
                ChannelRecommendBaseFragment.this.setGlobalLastFocusView(holder.itemView);
            }
        }
    };
    private OnItemRecycledListener mOnItemRecycledListener = new OnItemRecycledListener() {
        public void onItemRecycled(ViewGroup parent, ViewHolder holder) {
            if (ChannelRecommendBaseFragment.this.mRecmmendAdapter != null) {
                ChannelRecommendBaseFragment.this.mRecmmendAdapter.recycleBitmap(holder.itemView);
            }
        }
    };
    protected PhotoGridView mPhotoView;
    protected BaseGridAdapter<IData> mRecmmendAdapter;
    private long mStartLoadingTime;

    private static class BigViewImageCallback implements IImageCallback {
        WeakReference<ChannelRecommendBaseFragment> mOuter;

        public BigViewImageCallback(ChannelRecommendBaseFragment outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onSuccess(final ImageRequest request, final Bitmap bitmap) {
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    ChannelRecommendBaseFragment outer = (ChannelRecommendBaseFragment) BigViewImageCallback.this.mOuter.get();
                    if (outer != null && request != null && bitmap != null) {
                        outer.onImageSuccess(outer, request, bitmap);
                    }
                }
            });
        }

        public void onFailure(ImageRequest request, Exception e) {
            String str = null;
            ChannelRecommendBaseFragment outer = (ChannelRecommendBaseFragment) this.mOuter.get();
            if (outer != null) {
                outer.log(ChannelRecommendBaseFragment.NOLOG ? null : "------initView--onFailure------- e = " + e + "--url=" + request.getUrl());
                if (!ChannelRecommendBaseFragment.NOLOG) {
                    str = "------initView--onFailure------- e = " + e + "--url=" + request.getUrl();
                }
                outer.logRecord(str);
            }
        }
    }

    private static class OnAlbumFetchedListenerImpl implements OnAlbumFetchedListener {
        WeakReference<ChannelRecommendBaseFragment> mOuter;

        public OnAlbumFetchedListenerImpl(ChannelRecommendBaseFragment outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onFetchAlbumSuccess(final List<IData> list) {
            final ChannelRecommendBaseFragment outer = (ChannelRecommendBaseFragment) this.mOuter.get();
            if (outer != null) {
                outer.runOnUiThread(new Runnable() {
                    public void run() {
                        String str;
                        String str2 = null;
                        ChannelRecommendBaseFragment channelRecommendBaseFragment = outer;
                        if (ChannelRecommendBaseFragment.NOLOG) {
                            str = null;
                        } else {
                            str = "---loadDataAsync---callback---success---visitNet timeToken=" + (System.currentTimeMillis() - outer.mStartLoadingTime);
                        }
                        channelRecommendBaseFragment.log(str);
                        channelRecommendBaseFragment = outer;
                        if (ChannelRecommendBaseFragment.NOLOG) {
                            str = null;
                        } else {
                            str = "---loadDataAsync---callback---success---visitNet timeToken=" + (System.currentTimeMillis() - outer.mStartLoadingTime);
                        }
                        channelRecommendBaseFragment.logRecord(str);
                        if (outer.mDataApi == null || outer.isRemoving() || outer.mAlbumDataList == null) {
                            ChannelRecommendBaseFragment channelRecommendBaseFragment2 = outer;
                            if (!ChannelRecommendBaseFragment.NOLOG) {
                                str2 = "---loadDataAsync---callback---success---mDataApi=" + outer.mDataApi + "--isRemoving()=" + outer.isRemoving() + "---mAlbumDataList=" + outer.mAlbumDataList + "----callback return!!!!!!";
                            }
                            channelRecommendBaseFragment2.log(str2);
                        } else if (ListUtils.isEmpty(list)) {
                            outer.showNoResultPanel(ErrorKind.NET_ERROR, null);
                        } else {
                            outer.onFetchDataSucceed(list);
                        }
                    }
                });
            }
        }

        public void onFetchAlbumFail(final ApiException e) {
            final ChannelRecommendBaseFragment outer = (ChannelRecommendBaseFragment) this.mOuter.get();
            if (outer != null) {
                outer.runOnUiThread(new Runnable() {
                    public void run() {
                        String str = null;
                        if (outer.mDataApi == null || outer.isRemoving()) {
                            ChannelRecommendBaseFragment channelRecommendBaseFragment = outer;
                            if (!ChannelRecommendBaseFragment.NOLOG) {
                                str = "---loadDataAsync---fail---mDataApi=" + outer.mDataApi + "--isRemoving()=" + outer.isRemoving() + "---visitNet timeToken=" + (System.currentTimeMillis() - outer.mStartLoadingTime);
                            }
                            channelRecommendBaseFragment.log(str);
                            return;
                        }
                        String str2;
                        String code = e != null ? e.getCode() : "";
                        ChannelRecommendBaseFragment channelRecommendBaseFragment2 = outer;
                        if (ChannelRecommendBaseFragment.NOLOG) {
                            str2 = null;
                        } else {
                            str2 = "---loadDataAsync---fail--e=" + e + "---code=" + code + "---visitNet timeToken=" + (System.currentTimeMillis() - outer.mStartLoadingTime);
                        }
                        channelRecommendBaseFragment2.log(str2);
                        channelRecommendBaseFragment = outer;
                        if (!ChannelRecommendBaseFragment.NOLOG) {
                            str = "---loadDataAsync---fail--e=" + e + "---code=" + code + "---visitNet timeToken=" + (System.currentTimeMillis() - outer.mStartLoadingTime);
                        }
                        channelRecommendBaseFragment.logRecord(str);
                        outer.showNoResultPanel(ErrorKind.NET_ERROR, e);
                    }
                });
            }
        }
    }

    protected abstract BaseGridAdapter<IData> getAdapter();

    protected abstract int getBigViewHeight();

    protected abstract int getBigViewWidth();

    protected abstract int getMaxDisplaySize();

    protected abstract List<IData> getRealDataList(List<IData> list);

    protected abstract void initCartoonViews();

    protected abstract void setBigViewData();

    protected abstract void setCartoonData();

    protected abstract void showCache4CartoonView();

    protected void initView() {
        this.mTopMenuDesTxt = !ListUtils.isEmpty(this.mDataApi.getMultiTags()) ? IAlbumConfig.STR_FILTER : "";
        initBigView();
        initGridView();
        initCartoonViews();
    }

    protected float getGridAnimationScale() {
        return 1.093f;
    }

    private void initBigView() {
        this.mBigView = (RecommendView) this.mMainView.findViewById(R.id.epg_recommend_big_item);
        this.mBigView.setViewParams(this.mDataApi.getRecommendType(), this.mBigViewWidth, this.mBigViewHeight);
        this.mBigView.setOnClickListener(this.bigViewClickListener);
        this.mBigView.setOnSelectedListener(this.bigViewSelectedListener);
        this.mBigView.setTextBgDrawable(EpgImageCache.COVER_COLOR_UNFOCUS_DRAWABLE_FOR_RECOMMENDVIEW);
        this.mCurrentFocusedView = this.mBigView;
        this.mBigView.setNextFocusDownId(this.mBigView.getId());
        this.mBigView.setNextFocusRightId(R.id.epg_recommend_gridview_item);
    }

    private void initGridView() {
        this.mGridView = (VerticalGridView) this.mMainView.findViewById(R.id.epg_recommend_gridview_item);
        this.mRecmmendAdapter = getAdapter();
        this.mGridView.setFocusable(false);
        this.mGridView.setNumRows(5);
        this.mGridView.setFocusPlace(FocusPlace.FOCUS_EDGE);
        this.mGridView.setFocusMode(1);
        this.mGridView.setScrollRoteScale(0.8f, 1.0f, 2.5f);
        this.mGridView.setPadding(ResourceUtil.getDimen(R.dimen.dimen_24dp), ResourceUtil.getDimen(R.dimen.dimen_7dp), 0, 0);
        this.mGridView.setVerticalMargin(ResourceUtil.getPx(-7));
        this.mGridView.setHorizontalMargin(ResourceUtil.getDimen(R.dimen.dimen_4dp));
        this.mGridView.setOnItemRecycledListener(this.mOnItemRecycledListener);
        this.mGridView.setOnItemFocusChangedListener(this.mOnItemFocusChangedListener);
        this.mGridView.setOnItemClickListener(this.mOnItemClickListener);
        this.mGridView.setAdapter(this.mRecmmendAdapter);
        this.mGridView.setClipToPadding(false);
        this.mGridView.setFocusMemorable(false);
    }

    private void onClickItem(int pos) {
        IData data;
        String str = null;
        if (pos == 0) {
            if (this.mBigViewData == null) {
                if (!NOLOG) {
                    str = "------itemClickAction()------mBigViewInfo is null";
                }
                log(str);
                return;
            }
            data = this.mBigViewData;
        } else if (ListUtils.isEmpty(this.mAlbumDataList) || pos - 1 > ListUtils.getCount(this.mAlbumDataList)) {
            if (!NOLOG) {
                str = "------itemClickAction()------mAlbumList.size=" + ListUtils.getCount(this.mAlbumDataList);
            }
            log(str);
            return;
        } else {
            data = (IData) this.mAlbumDataList.get(pos - 1);
        }
        if (pos == 0) {
            this.mInfoModel.setRseat("1_1");
        } else {
            this.mInfoModel.setRseat((this.mPhotoView == null ? 2 : 3) + "_" + pos);
        }
        String buySource = this.mBuySourceSrc;
        if (IAlbumConfig.PROJECT_NAME_BASE_LINE.equals(this.mInfoModel.getProjectName()) && !TextUtils.isEmpty(buySource) && buySource.contains("rec")) {
            setBuySource(buySource + "[" + this.mInfoModel.getRseat() + AlbumEnterFactory.SIGN_STR);
        } else if (IAlbumConfig.PROJECT_NAME_OPEN_API.equals(this.mInfoModel.getProjectName())) {
            setBuySource("openAPI");
        }
        data.click(this.mContext, this.mInfoModel);
    }

    protected void loadData() {
        String str = null;
        showProgress();
        resetTempValues();
        if (this.mDataApi != null) {
            this.mBaseHandler.removeCallbacks(this.mCacheViewRunnable);
            if (this.mShowCacheWithoutLoadData) {
                this.mBaseHandler.postDelayed(this.mCacheViewRunnable, 0);
                this.mShowCacheWithoutLoadData = false;
                return;
            }
            this.mBaseHandler.postDelayed(this.mCacheViewRunnable, 250);
            loadDataAsync();
            return;
        }
        showNoResultPanel(ErrorKind.NET_ERROR, null);
        if (!NOLOG) {
            str = "---mDataApi = null";
        }
        log(str);
    }

    private void showCacheView() {
        if (!AlbumInfoFactory.needShowLoadingView(this.mInfoModel.getPageType())) {
            log(NOLOG ? null : "---show cache view");
            showCache4CartoonView();
            showHasResultPanel();
            setShowingCacheData(true);
        }
    }

    private void resetTempValues() {
        this.mTotalItemCount = 0;
        this.mDisplayTotal = 0;
        this.mGridViewFocusedPosition = 0;
        setRightFragmentHasData(false);
        this.mCurrentFocusedView = null;
        setShowingCacheData(false);
        this.mBigViewData = null;
        this.mBuySourceSrc = this.mInfoModel.getBuySource();
        if (this.mAlbumDataList != null) {
            this.mAlbumDataList.clear();
        } else {
            this.mAlbumDataList = new ArrayList(1);
        }
    }

    public void showHasResultPanel() {
        this.mBaseHandler.removeCallbacks(this.mCacheViewRunnable);
        setShowingCacheData(false);
        super.showHasResultPanel();
    }

    public Bitmap showNoResultPanel(ErrorKind kind, ApiException e) {
        this.mBaseHandler.removeCallbacks(this.mCacheViewRunnable);
        setShowingCacheData(false);
        return super.showNoResultPanel(kind, e);
    }

    public void onDestroy() {
        this.mBaseHandler.removeCallbacks(this.mCacheViewRunnable);
        super.onDestroy();
    }

    private void loadDataAsync() {
        String str = null;
        log(NOLOG ? null : "---loadDataAsync---next log should be callback");
        if (!NOLOG) {
            str = "---loadDataAsync---next log should be callback";
        }
        logRecord(str);
        this.mStartLoadingTime = System.currentTimeMillis();
        this.mDataApi.loadAlbumData(new OnAlbumFetchedListenerImpl(this));
    }

    private void onFetchDataSucceed(List<IData> srclist) {
        List<IData> list = getRealDataList(srclist);
        int albumMaxCount = 0;
        int len = list.size();
        for (int i = 0; i < len && albumMaxCount <= getMaxDisplaySize(); i++) {
            IData iAlbumData = (IData) list.get(i);
            ResourceType type = iAlbumData.getResourceType();
            int channelId = this.mInfoModel.getChannelId();
            if (channelId == 1 || channelId == 1000002) {
                if (!ResourceType.DEFAULT.equals(type)) {
                    if (albumMaxCount == 0) {
                        this.mBigViewData = iAlbumData;
                        setBigViewData();
                    } else {
                        this.mAlbumDataList.add(iAlbumData);
                    }
                    albumMaxCount++;
                }
            } else if (!ResourceType.DIY.equals(type)) {
                if (albumMaxCount == 0) {
                    this.mBigViewData = iAlbumData;
                    setBigViewData();
                } else {
                    this.mAlbumDataList.add(iAlbumData);
                }
                albumMaxCount++;
            }
        }
        if (this.mBigViewData == null) {
            log(NOLOG ? null : "---loadDataAsync---mBigViewData == null---showNoResultPanel");
            logRecord(NOLOG ? null : "---loadDataAsync---mBigViewData == null---showNoResultPanel");
            showNoResultPanel(ErrorKind.NO_RESULT, null);
            return;
        }
        showHasResultPanel();
        setCartoonData();
        if (!ListUtils.isEmpty(this.mAlbumDataList)) {
            this.mBigView.setNextFocusDownId(-1);
            this.mGridView.setFocusable(true);
            this.mRecmmendAdapter.updateData(this.mAlbumDataList);
        }
        QAPingback.sendAlbumPageShowPingback(0, Integer.MAX_VALUE, this.mInfoModel, System.currentTimeMillis() - this.mStartLoadingTime, true, 0);
    }

    public void setTopMenuLayoutVisible(int visible) {
        if (ListUtils.isEmpty(this.mDataApi.getMultiTags())) {
            super.setTopMenuLayoutVisible(4);
        } else {
            super.setTopMenuLayoutVisible(visible);
        }
    }

    protected void reloadBitmap() {
    }

    protected void recyleBitmap() {
    }

    protected void loadBigView(int width) {
        String str = null;
        String url = this.mBigViewData.getImageUrl(1);
        log(NOLOG ? null : "---loadBigView----url=" + url);
        if (!NOLOG) {
            str = "---loadBigView----url=" + url;
        }
        logRecord(str);
        ImageRequest request = new ImageRequest(url, this.mBigView);
        request.setTargetWidth(width);
        request.setTargetHeight(getBigViewHeight());
        request.setScaleType(ScaleType.NO_CROP);
        ImageProviderApi.getImageProvider().loadImage(request, new BigViewImageCallback(this));
    }

    protected void onNetChanged() {
        if (!isRightFragmentHasData()) {
            loadData();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == 82) {
            setMenu2Activity();
        }
        return super.dispatchKeyEvent(event);
    }

    public void onImageSuccess(ChannelRecommendBaseFragment outer, final ImageRequest request, final Bitmap bitmap) {
        outer.runOnUiThread(new Runnable() {
            public void run() {
                RecommendView view = (RecommendView) request.getCookie();
                if (view != null) {
                    view.setImage(bitmap);
                    view.setCornerImage(ChannelRecommendBaseFragment.this.mBigViewData);
                }
            }
        });
    }
}
