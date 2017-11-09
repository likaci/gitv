package com.gala.video.app.player.ui.overlay.contents;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.alibaba.fastjson.asm.Opcodes;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackStore.BLOCK;
import com.gala.pingback.PingbackStore.PAGE_SHOW.BTSPTYPE;
import com.gala.pingback.PingbackStore.QTCURL;
import com.gala.sdk.player.BaseAdData;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.albumlist4.widget.HorizontalGridView;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.LayoutManager.Orientation;
import com.gala.video.albumlist4.widget.RecyclerView;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnScrollListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.data.VideoData;
import com.gala.video.app.player.data.VideoDataMakeupFactory;
import com.gala.video.app.player.ui.config.style.common.IGalleryUIStyle;
import com.gala.video.app.player.ui.overlay.OnAdStateListener;
import com.gala.video.app.player.ui.overlay.contents.IContent.IItemListener;
import com.gala.video.app.player.ui.widget.CommonScrollAdapter;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.common.widget.AlbumView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GalleryListContent implements IContent<List<IVideo>, IVideo> {
    private static final Integer[] DEFAULT_LAND_RECOMMEND_ITEM_LIST = new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)};
    private static final Integer[] DEFAULT_PORT_RECOMMEND_ITEM_LIST = new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7)};
    private static final float LAND_SELECTED_SCALEANIMRATIO = 1.07f;
    private static final int MSG_DATA_REFRESHED = 1;
    private static final int MSG_SELECTION_REFRESHED = 2;
    private static final float NORMAL_SCALEANIMRATIO = 1.0f;
    private static final float PORT_EXIT_SCALEANIMRATIO = 1.1f;
    private static final float PORT_SELECTED_SCALEANIMRATIO = 1.05f;
    public final String TAG;
    private BaseAdData mAdData;
    private OnAdStateListener mAdStateListener;
    private RelativeLayout mAdView;
    private View mContentView;
    private Context mContext;
    private List<Integer> mCurShownItems = new ArrayList();
    private IVideo mCurVideo;
    private List<View> mCurVisibleViews = new ArrayList();
    private List<VideoData> mDataList = new ArrayList();
    private boolean mEnableTvWindow;
    private boolean mFirstSetData = false;
    private HorizontalGridView mHorizontalGridView;
    private CommonScrollAdapter mHorizontalScrollAdapter;
    private OnHorizontalScrollListener mHorizontalScrollListener;
    private boolean mIsAutoFocus = false;
    private boolean mIsDetail;
    private boolean mIsPort = true;
    private boolean mIsShowExclusive = false;
    private boolean mIsShown = false;
    private OnItemFocusChangedListener mItemFocusChangedListener;
    private IItemListener<IVideo> mItemListener;
    private OnItemRecycledListener mItemRecycledListener;
    private OnItemClickListener mItemSelectedListener;
    private Handler mMainHandler;
    private boolean mNeedPlayingIcon = false;
    private IPingbackContext mPingbackContext;
    private boolean mPlayIconErased = false;
    private OnScrollListener mScrollListener;
    private String mTitle;
    private IGalleryUIStyle mUiStyle;

    class C14922 implements OnItemClickListener {
        C14922() {
        }

        public void onItemClick(ViewGroup viewGroup, ViewHolder viewHolder) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GalleryListContent.this.TAG, "onItemClick ");
            }
            int index = viewHolder.getLayoutPosition();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GalleryListContent.this.TAG, "onItemClick, clicked position=" + index);
            }
            IVideo clickedVideo = null;
            if (!(GalleryListContent.this.mDataList == null || ListUtils.isEmpty(GalleryListContent.this.mDataList))) {
                clickedVideo = ((VideoData) GalleryListContent.this.mDataList.get(index)).getData();
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GalleryListContent.this.TAG, "onItemClick clickVideo " + clickedVideo);
            }
            if (clickedVideo == null) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1572e(GalleryListContent.this.TAG, "onItemClick: pos=" + index + ", null video!! backtrace=", new Throwable().fillInStackTrace());
                }
            } else if (GalleryListContent.this.mItemListener != null) {
                GalleryListContent.this.mItemListener.onItemClicked(clickedVideo, index);
            }
        }
    }

    class C14933 implements OnItemFocusChangedListener {
        C14933() {
        }

        public void onItemFocusChanged(ViewGroup viewGroup, ViewHolder viewHolder, boolean hasFocus) {
            View v = viewHolder.itemView;
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GalleryListContent.this.TAG, "onItemFocusChanged, hasFocus=" + hasFocus);
            }
            if (ListUtils.isEmpty(GalleryListContent.this.mDataList)) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(GalleryListContent.this.TAG, "onItemFocusChanged, mDataList is empty.");
                }
                AnimationUtil.zoomOutAnimation(v, 1.0f);
                if (GalleryListContent.this.mItemListener != null) {
                    GalleryListContent.this.mItemListener.onItemSelected(null, -1);
                }
            } else if (hasFocus) {
                int index = ((RecyclerView) viewGroup).getFocusPosition();
                if (index >= 0 && index <= GalleryListContent.this.mDataList.size() - 1) {
                    IVideo focusedVideo = ((VideoData) GalleryListContent.this.mDataList.get(index)).getData();
                    v.bringToFront();
                    v.getParent().requestLayout();
                    if (!GalleryListContent.this.mIsPort) {
                        AnimationUtil.zoomInAnimation(v, GalleryListContent.LAND_SELECTED_SCALEANIMRATIO);
                    } else if (GalleryListContent.this.mUiStyle.isExitDialog()) {
                        AnimationUtil.zoomInAnimation(v, 1.1f);
                    } else {
                        AnimationUtil.zoomInAnimation(v, GalleryListContent.PORT_SELECTED_SCALEANIMRATIO);
                    }
                    if (GalleryListContent.this.mItemListener != null) {
                        GalleryListContent.this.mItemListener.onItemSelected(focusedVideo, index);
                    }
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(GalleryListContent.this.TAG, "onItemFocusChanged, index=" + index + ", focusedVideo=" + focusedVideo);
                    }
                } else if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(GalleryListContent.this.TAG, "onItemFocusChanged, invalid index, index=" + index + ", mDataList.size()=" + GalleryListContent.this.mDataList.size());
                }
            } else {
                AnimationUtil.zoomOutAnimation(v, 1.0f);
            }
        }
    }

    class C14944 implements OnItemRecycledListener {
        C14944() {
        }

        public void onItemRecycled(ViewGroup viewGroup, ViewHolder viewHolder) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GalleryListContent.this.TAG, ">> onItemRecycled");
            }
            View v = viewHolder.itemView;
            int index = viewHolder.getLayoutPosition();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GalleryListContent.this.TAG, "onItemRecycled, index=" + index + ", v=" + v);
            }
            if (v instanceof AlbumView) {
                GalleryListContent.this.mHorizontalScrollAdapter.showDefaultBitmap((AlbumView) v);
                GalleryListContent.this.mHorizontalScrollAdapter.clearData((AlbumView) v);
            }
        }
    }

    class C14955 extends OnScrollListener {
        C14955() {
        }

        public void onScrollStart() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GalleryListContent.this.TAG, ">> onScrollStart");
            }
            GalleryListContent.this.mHorizontalScrollAdapter.onCancelAllTasks();
        }

        public void onScrollStop() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GalleryListContent.this.TAG, ">> onScrollStop");
            }
            int first = GalleryListContent.this.mHorizontalGridView.getFirstAttachedPosition();
            int last = GalleryListContent.this.mHorizontalGridView.getLastAttachedPosition();
            GalleryListContent.this.resetVisibleViewWithCache(first, last);
            GalleryListContent.this.mHorizontalScrollAdapter.onReloadTasks(GalleryListContent.this.mCurVisibleViews);
            GalleryListContent.this.checkVisibleItems(first, last);
            if (GalleryListContent.this.mHorizontalScrollListener != null) {
                GalleryListContent.this.mHorizontalScrollListener.onScrollStopped(GalleryListContent.this.mCurShownItems);
            }
        }

        public void onScroll(ViewParent arg0, int arg1, int arg2, int arg3) {
        }

        public void onScrollBefore(int arg0) {
        }
    }

    public interface OnHorizontalScrollListener {
        void onScrollStarted();

        void onScrollStopped(List<Integer> list);
    }

    private void handleDataRefreshed(List<VideoData> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> handleDataRefreshed, list size = " + list.size());
        }
        boolean oldDataEmpty = ListUtils.isEmpty(this.mDataList);
        int oldDataSize = this.mDataList.size();
        this.mDataList.clear();
        this.mDataList.addAll(list);
        if (!ListUtils.isEmpty(this.mDataList)) {
            int position = updatePlayingSelection(this.mCurVideo);
            if (oldDataEmpty || oldDataSize > this.mDataList.size()) {
                setDataSource(position);
            } else {
                updateDataSource();
            }
        }
    }

    private void handleSelectionRefreshed(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> handleSelectionRefreshed");
        }
        if (video != null) {
            this.mPlayIconErased = false;
            this.mCurVideo = video;
            updateSelection(updatePlayingSelection(video));
            return;
        }
        this.mPlayIconErased = true;
        erasePlayingIcon();
    }

    public GalleryListContent(Context context, IGalleryUIStyle uiStyle, String title, boolean showPlaying, boolean autoFocus, boolean showExclusive) {
        String str;
        boolean z = true;
        if (!(Project.getInstance().getBuild().isSupportAlbumDetailWindowPlay() && Project.getInstance().getBuild().isSupportSmallWindowPlay() && PlayerDebugUtils.isAlbumDetailPageShowPlay())) {
            z = false;
        }
        this.mEnableTvWindow = z;
        this.mMainHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        GalleryListContent.this.handleDataRefreshed(msg.obj);
                        return;
                    case 2:
                        GalleryListContent.this.handleSelectionRefreshed(msg.obj);
                        return;
                    default:
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d(GalleryListContent.this.TAG, "unhandled msg, what=" + msg.what);
                            return;
                        }
                        return;
                }
            }
        };
        this.mItemSelectedListener = new C14922();
        this.mItemFocusChangedListener = new C14933();
        this.mItemRecycledListener = new C14944();
        this.mScrollListener = new C14955();
        this.mContext = context;
        this.mPingbackContext = (IPingbackContext) context;
        if (title == null) {
            str = "";
        } else {
            str = title;
        }
        this.mTitle = str;
        this.mNeedPlayingIcon = showPlaying;
        this.mIsShowExclusive = showExclusive;
        this.mIsPort = uiStyle.isPort();
        this.mUiStyle = uiStyle;
        this.mIsDetail = uiStyle.isDetailGallery();
        this.mIsAutoFocus = autoFocus;
        this.TAG = "/Player/ui/layout/GalleryListContent[" + this.mTitle + "][@" + hashCode() + AlbumEnterFactory.SIGN_STR;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "GalleryListContent, title=" + title + ", mTitle=" + this.mTitle + ", mNeedPlayingIcon=" + this.mNeedPlayingIcon + "mIsShowExclusive=" + this.mIsShowExclusive + "mIsPort=" + this.mIsPort + ", mIsDetail=" + this.mIsDetail + ", mIsAutoFocus=" + this.mIsAutoFocus);
        }
    }

    private void initViews() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> initViews");
        }
        initContentView();
        initGalleryPager();
    }

    private void initContentView() {
        this.mContentView = LayoutInflater.from(this.mContext).inflate(C1291R.layout.player_gallery_content_common, null);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initContentView() inflate: result=" + this.mContentView);
        }
    }

    private void initGalleryPager() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> initGalleryPager ");
        }
        this.mHorizontalGridView = (HorizontalGridView) this.mContentView.findViewById(C1291R.id.horizontalgirdview);
        setupHorizontalGridView();
        if (this.mHorizontalScrollAdapter == null) {
            initAdapter();
            this.mHorizontalGridView.setAdapter(this.mHorizontalScrollAdapter);
            updatePlayingSelection(this.mCurVideo);
        }
        setMargins(this.mUiStyle.getGalleryMarginLeft(), this.mUiStyle.getGalleryMarginTop(), this.mUiStyle.getGalleryMarginRight(), this.mUiStyle.getGalleryMarginBottom());
    }

    private void setupHorizontalGridView() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setupHorizontalGridView");
        }
        setLayoutProperties();
        setFocusForbidden();
        setShakeAnimForbidden();
        setupListeners();
        initDefaultShownItems();
    }

    private void setLayoutProperties() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setLayoutProperties");
        }
        this.mHorizontalGridView.setOrientation(Orientation.HORIZONTAL);
        this.mHorizontalGridView.setNumRows(1);
        this.mHorizontalGridView.setFocusPlace(FocusPlace.FOCUS_CENTER);
        this.mHorizontalGridView.setFocusMode(1);
        this.mHorizontalGridView.setScrollRoteScale(1.0f, 1.0f, 2.0f);
        this.mHorizontalGridView.setHorizontalMargin(ResourceUtil.getDimen(C1291R.dimen.dimen_4dp));
        if (ListUtils.isEmpty(this.mDataList)) {
            this.mHorizontalGridView.setFocusable(false);
        }
        this.mHorizontalGridView.setQuickFocusLeaveForbidden(false);
    }

    private void setFocusForbidden() {
        if (this.mIsDetail) {
            this.mHorizontalGridView.setFocusLeaveForbidden(83);
        } else {
            this.mHorizontalGridView.setFocusLeaveForbidden(211);
        }
    }

    private void setShakeAnimForbidden() {
        if (!this.mIsDetail) {
            this.mHorizontalGridView.setShakeForbidden(Opcodes.IF_ICMPGT);
        }
    }

    private void setupListeners() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setupListeners");
        }
        this.mHorizontalGridView.setOnItemClickListener(this.mItemSelectedListener);
        this.mHorizontalGridView.setOnItemFocusChangedListener(this.mItemFocusChangedListener);
        this.mHorizontalGridView.setOnScrollListener(this.mScrollListener);
        this.mHorizontalGridView.setOnItemRecycledListener(this.mItemRecycledListener);
    }

    private void initDefaultShownItems() {
        if (!this.mIsDetail) {
            return;
        }
        if (this.mIsPort) {
            Collections.addAll(this.mCurShownItems, DEFAULT_PORT_RECOMMEND_ITEM_LIST);
        } else {
            Collections.addAll(this.mCurShownItems, DEFAULT_LAND_RECOMMEND_ITEM_LIST);
        }
    }

    private void checkVisibleItems(int first, int last) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> checkVisibleItems, first=" + first + ", last=" + last);
        }
        if (first >= 0 && last >= 0) {
            int i;
            int screenWidth = ((Activity) this.mContext).getWindowManager().getDefaultDisplay().getWidth();
            List<Integer> list = new ArrayList();
            for (i = first; i <= last; i++) {
                list.add(Integer.valueOf(i));
            }
            int[] location = new int[2];
            int size = list.size();
            for (i = 0; i < size; i++) {
                int index = ((Integer) list.get(i)).intValue();
                View view = this.mHorizontalGridView.getViewByPosition(index);
                view.getLocationOnScreen(location);
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(this.TAG, "checkVisibleItems, index=" + index + ", x=" + location[0] + ", y=" + location[1] + ", view.measuredWidth=" + view.getMeasuredWidth() + ", view=" + view);
                }
                if (location[0] + view.getMeasuredWidth() < 0) {
                    list.set(i, Integer.valueOf(-1));
                }
                if (location[0] > screenWidth) {
                    list.set(i, Integer.valueOf(-1));
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "checkVisibleItems, list=" + list);
            }
            for (i = list.size() - 1; i >= 0; i--) {
                if (((Integer) list.get(i)).intValue() == -1) {
                    list.remove(i);
                }
            }
            if (!ListUtils.isEmpty(this.mCurShownItems)) {
                this.mCurShownItems.clear();
            }
            this.mCurShownItems.addAll(list);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "checkVisibleItems, mCurShownItems=" + this.mCurShownItems);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "checkVisibleItems, invalid index, return default list, mCurShownItems=" + this.mCurShownItems);
        }
    }

    private List<View> resetVisibleViewWithCache(int first, int last) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> resetVisibleViewWithCache, first=" + first + ", last=" + last);
        }
        this.mCurVisibleViews.clear();
        if (first < 0 || last < 0) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "resetVisibleViewWithCache, invalid index, first=" + first + ", last=" + last);
            }
            return this.mCurVisibleViews;
        }
        for (int index = first; index <= last; index++) {
            this.mCurVisibleViews.add(this.mHorizontalGridView.getViewByPosition(index));
        }
        return this.mCurVisibleViews;
    }

    public void setItemListener(IItemListener<IVideo> listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setItemListener[@" + listener + AlbumEnterFactory.SIGN_STR);
        }
        this.mItemListener = listener;
    }

    public void setOnHorizontalScrollListener(OnHorizontalScrollListener listener) {
        this.mHorizontalScrollListener = listener;
    }

    public void show() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1574i(this.TAG, ">> show()");
        }
        if (!this.mIsShown) {
            this.mIsShown = true;
            if (this.mContentView == null) {
                initViews();
            }
            if (this.mContentView.getVisibility() != 0) {
                this.mContentView.setVisibility(0);
            }
            if (this.mHorizontalGridView.getVisibility() != 0) {
                this.mHorizontalGridView.setVisibility(0);
            }
            if (!this.mIsDetail) {
                updateSelection(updatePlayingSelection(this.mCurVideo));
            }
            if (this.mAdView != null) {
                this.mAdView.setVisibility(0);
                sendAdPingback();
                notifyAdCallback();
            } else if (this.mAdStateListener != null) {
                this.mAdStateListener.onRequest(3);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "show, mIsShown=" + this.mIsShown);
        }
    }

    private void initAdapter() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initAdapter: mDataList size=" + this.mDataList.size());
        }
        AlbumViewType type = this.mIsDetail ? this.mIsPort ? AlbumViewType.DETAIL_VERTICAL : AlbumViewType.DETAIL_HORIZONAL : this.mUiStyle.isExitDialog() ? AlbumViewType.EXITDIALOG_VERTICAL : AlbumViewType.PLAYER_HORIZONAL;
        this.mHorizontalScrollAdapter = new CommonScrollAdapter(this.mContext, this.mIsPort, type);
    }

    private void setMargins(int left, int top, int right, int bottom) {
        if (this.mHorizontalGridView != null) {
            LayoutParams params = (LayoutParams) this.mHorizontalGridView.getLayoutParams();
            params.leftMargin = left;
            params.topMargin = top;
            params.rightMargin = right;
            params.bottomMargin = bottom;
            this.mHorizontalGridView.setLayoutParams(params);
        }
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> hide() ");
        }
        if (this.mIsShown) {
            this.mIsShown = false;
            if (this.mContentView.getVisibility() == 0) {
                this.mContentView.setVisibility(4);
            }
            if (this.mHorizontalGridView.getVisibility() == 0) {
                this.mHorizontalGridView.setVisibility(4);
            }
        }
    }

    public View getView() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> getView");
        }
        if (this.mContentView == null) {
            initViews();
        }
        return this.mContentView;
    }

    public View getFocusableView() {
        return this.mHorizontalGridView;
    }

    public List<IVideo> getContentData() {
        return extract(this.mDataList);
    }

    public List<Integer> getCurShownItems() {
        checkVisibleItems(this.mHorizontalGridView.getFirstAttachedPosition(), this.mHorizontalGridView.getLastAttachedPosition());
        return this.mCurShownItems;
    }

    public void setData(List<IVideo> data) {
        List<VideoData> list = wrap(VideoDataMakeupFactory.get().dataListMakeup(data, this.mIsPort ? QLayoutKind.PORTRAIT : QLayoutKind.LANDSCAPE, 1, null));
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setData, list.size=" + list.size());
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            handleDataRefreshed(list);
        } else {
            this.mMainHandler.sendMessage(this.mMainHandler.obtainMessage(1, list));
        }
    }

    private List<IVideo> extract(List<VideoData> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> extract ");
        }
        int size = list.size();
        List<IVideo> videos = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            videos.add(((VideoData) list.get(i)).getData());
        }
        return videos;
    }

    private List<VideoData> wrap(List<IData> list) {
        List<VideoData> newList = new ArrayList();
        if (list != null) {
            for (IData data : list) {
                ((VideoData) data).setIsDetail(this.mIsDetail);
                newList.add((VideoData) data);
            }
        }
        return newList;
    }

    public void setSelection(IVideo item) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setSelection, item=" + item);
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            handleSelectionRefreshed(item);
        } else {
            this.mMainHandler.sendMessage(this.mMainHandler.obtainMessage(2, item));
        }
    }

    private int updatePlayingSelection(IVideo item) {
        boolean shouldShowPlaying;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> updatePlayingSelection, video=" + item);
        }
        if (!shouldShowPlayingIcon() || this.mPlayIconErased) {
            shouldShowPlaying = false;
        } else {
            shouldShowPlaying = true;
        }
        int position = findPosition(this.mDataList, item);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "updatePlayingSelection, shouldShowPlaying=" + shouldShowPlaying + ", position=" + position);
        }
        int size = this.mDataList.size();
        for (int i = 0; i < size; i++) {
            boolean z;
            VideoData data = (VideoData) this.mDataList.get(i);
            data.setDuboCornerStatus(this.mIsShowExclusive);
            if (i == position && shouldShowPlaying) {
                z = true;
            } else {
                z = false;
            }
            data.setPlaying(z);
        }
        return (position < 0 || !this.mNeedPlayingIcon) ? 0 : position;
    }

    private void setDataSource(int position) {
        updateSelection(position);
    }

    private void updateDataSource() {
        if (this.mHorizontalGridView != null && this.mHorizontalScrollAdapter != null) {
            this.mHorizontalScrollAdapter.updateDataSet(this.mDataList);
        }
    }

    private boolean shouldShowPlayingIcon() {
        boolean ret = false;
        if (this.mIsDetail) {
            if (this.mNeedPlayingIcon && this.mEnableTvWindow) {
                ret = true;
            }
        } else if (this.mNeedPlayingIcon) {
            ret = true;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "shouldShowPlayingIcon, ret=" + ret + ", mIsDetail=" + this.mIsDetail + ", mNeedPlayingIcon=" + this.mNeedPlayingIcon + ", mEnableTvWindow=" + this.mEnableTvWindow);
        }
        return ret;
    }

    private void erasePlayingIcon() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> erasePlayingIcon");
        }
        int position = findPosition(this.mDataList, this.mCurVideo);
        if (position >= 0) {
            ((VideoData) this.mDataList.get(position)).setPlaying(false);
            this.mHorizontalScrollAdapter.updateDataSet(this.mDataList);
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "erasePlayingIcon, invalid current position !!!");
        }
    }

    private void updateSelection(int position) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> updateSelection, position=" + position);
        }
        if (this.mHorizontalGridView != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "updateSelection, mHorizontalGridView.hasFocus()=" + this.mHorizontalGridView.hasFocus());
            }
            if (ListUtils.isEmpty(this.mDataList)) {
                this.mHorizontalGridView.setFocusable(false);
                return;
            }
            this.mHorizontalGridView.setFocusable(true);
            if ((this.mIsAutoFocus && this.mIsShown) || this.mHorizontalGridView.hasFocus()) {
                this.mHorizontalGridView.requestFocus();
            }
            this.mHorizontalGridView.setFocusPosition(position, true);
            if (this.mHorizontalScrollAdapter != null) {
                this.mHorizontalScrollAdapter.changeDataSet(this.mDataList);
                if (!this.mFirstSetData) {
                    this.mFirstSetData = true;
                    if (this.mItemListener != null) {
                        this.mItemListener.onItemFilled();
                    }
                }
            }
        }
    }

    private int findPosition(List<VideoData> list, IVideo cur) {
        int find = -1;
        if (cur != null && !ListUtils.isEmpty((List) list)) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (((VideoData) list.get(i)).getData().getTvId().equals(cur.getTvId())) {
                    find = i;
                    break;
                }
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "findPosition() find=" + find);
        }
        return find;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setOnAdStateListener(OnAdStateListener l) {
        this.mAdStateListener = l;
    }

    public void setPercisionAdData(BaseAdData data) {
        addAd(data);
    }

    private void addAd(BaseAdData data) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "addAd()");
        }
        if (data != null) {
            this.mAdData = data;
            if (this.mContentView != null && this.mAdView == null) {
                this.mAdView = data.getAdView();
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(this.TAG, "addAd() mAdView=" + this.mAdView);
                }
                if (this.mAdView != null) {
                    LayoutParams params = new LayoutParams(this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_256dp), this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_36dp));
                    params.topMargin = this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_8dp);
                    params.rightMargin = this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_20dp);
                    params.gravity = 5;
                    ((ViewGroup) this.mContentView).addView(this.mAdView, params);
                    if (this.mIsShown) {
                        this.mAdView.setVisibility(0);
                        sendAdPingback();
                        notifyAdCallback();
                        return;
                    }
                    this.mAdView.setVisibility(8);
                }
            }
        }
    }

    public void clearAd() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "clearAd()");
        }
        if (this.mAdView != null) {
            if (this.mContentView != null) {
                ((ViewGroup) this.mContentView).removeView(this.mAdView);
            }
            this.mAdView = null;
            this.mAdData = null;
        }
    }

    private void notifyAdCallback() {
        if (this.mAdStateListener != null && this.mAdData != null) {
            this.mAdStateListener.onShow(102, Integer.valueOf(this.mAdData.getID()));
        }
    }

    private void sendAdPingback() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "<< sendAdPingback()");
        }
        if (this.mPingbackContext != null) {
            PingbackFactory.instance().createPingback(53).addItem(BTSPTYPE.BSTP_1).addItem(QTCURL.QTCURL_TYPE("ad_chgep")).addItem(this.mPingbackContext.getItem("e")).addItem(BLOCK.BLOCK_TYPE("ad_chgep")).post();
        }
    }
}
