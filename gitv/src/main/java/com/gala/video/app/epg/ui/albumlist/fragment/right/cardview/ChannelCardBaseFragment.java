package com.gala.video.app.epg.ui.albumlist.fragment.right.cardview;

import android.graphics.Bitmap;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.report.msghandler.MsgHanderEnum.HOSTMODULE;
import com.gala.report.msghandler.MsgHanderEnum.HOSTSTATUS;
import com.gala.sdk.player.TipType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.albumlist4.widget.RecyclerView;
import com.gala.video.albumlist4.widget.RecyclerView.ItemDecoration;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnScrollListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.adapter.MultiGridAdapter;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.factory.DataInfoProvider;
import com.gala.video.app.epg.ui.albumlist.fragment.right.AlbumBaseRightFragment;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ChannelCardBaseFragment extends AlbumBaseRightFragment {
    private static final int GRIDVIEW_TOP = ResourceUtil.getDimen(R.dimen.dimen_2dp);
    private static final int GRIDVIEW_VERTICALMARGIN_ALBUM = ResourceUtil.getPx(-7);
    private static final int GRIDVIEW_VERTICALMARGIN_CARD = ResourceUtil.getPx(-7);
    private static final int HALF_ALBUM = (ResourceUtil.getPx(TipType.CONCRETE_TYPE_HISTORY) / 2);
    private static final int HALF_CARD = ResourceUtil.getDimen(R.dimen.dimen_130dp);
    protected int firstVisble = -1;
    protected int lastVisble = -1;
    protected int listCount;
    protected List<IData> mAlbumConvertList;
    private final Runnable mCacheViewRunnable = new Runnable() {
        public void run() {
            ChannelCardBaseFragment.this.showCacheView();
        }
    };
    protected List<IData> mCardConvertList;
    protected int mChannelId;
    protected String mChannelName;
    protected boolean mFetchDataSucceed = false;
    private int mFocusPosition = 0;
    private int mGridAnimationDuration;
    private float mGridAnimationScale;
    protected VerticalGridView mGridView;
    private boolean mHasUnCardType;
    protected boolean mIsInitSend = false;
    private ItemDecoration mItemDecoration = new ItemDecoration() {
        public int getItemOffsets(int itemPosition, RecyclerView parent) {
            if (itemPosition < ChannelCardBaseFragment.this.mMultiGridAdapter.getCardDataListSize()) {
                return ResourceUtil.getDimen(R.dimen.dimen_12dp);
            }
            return ResourceUtil.getDimen(R.dimen.dimen_04dp);
        }
    };
    protected MultiGridAdapter mMultiGridAdapter;
    protected int mNeedRequestIndex = -1;
    private OnItemFocusChangedListener mOnItemFocusChangedListener = new OnItemFocusChangedListener() {
        public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
            int position = holder.getLayoutPosition();
            if (hasFocus) {
                ChannelCardBaseFragment.this.setNextFocusUpId(ChannelCardBaseFragment.this.mGridView);
                ChannelCardBaseFragment.this.mCurrentFocusedView = holder.itemView;
                ChannelCardBaseFragment.this.setGlobalLastFocusView(holder.itemView);
            }
            ChannelCardBaseFragment.this.onGridItemSelected(parent, holder.itemView, position, hasFocus);
            ChannelCardBaseFragment.this.mGridView.setLayerType(0, null);
        }
    };
    private OnItemRecycledListener mOnItemRecycledListener = new OnItemRecycledListener() {
        public void onItemRecycled(ViewGroup parent, ViewHolder holder) {
            if (ChannelCardBaseFragment.this.mMultiGridAdapter != null) {
                ChannelCardBaseFragment.this.mMultiGridAdapter.recycleBitmap(holder.itemView);
                ChannelCardBaseFragment.this.mMultiGridAdapter.releaseData(holder.itemView);
            }
        }
    };
    private OnItemClickListener mOnItemSelectedListener = new OnItemClickListener() {
        public void onItemClick(ViewGroup parent, ViewHolder holder) {
            ChannelCardBaseFragment.this.onGridItemClick(holder.getLayoutPosition());
        }
    };
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        public void onScrollStart() {
            ChannelCardBaseFragment.this.mBaseHandler.removeCallbacks(ChannelCardBaseFragment.this.mScrollStopRunnable);
            if (ChannelCardBaseFragment.this.mMultiGridAdapter != null) {
                ChannelCardBaseFragment.this.mMultiGridAdapter.onCancelAllTasks();
            }
        }

        public void onScrollStop() {
            ChannelCardBaseFragment.this.mBaseHandler.removeCallbacks(ChannelCardBaseFragment.this.mScrollStopRunnable);
            ChannelCardBaseFragment.this.mBaseHandler.postDelayed(ChannelCardBaseFragment.this.mScrollStopRunnable, 100);
        }

        public void onScroll(ViewParent parent, int firstAttachedItem, int lastAttachedItem, int totalItemCount) {
            ChannelCardBaseFragment.this.onGridRowSelected(ChannelCardBaseFragment.this.mGridView.getFocusPosition() / ChannelCardBaseFragment.this.mGridView.getNumRows(), totalItemCount / ChannelCardBaseFragment.this.mGridView.getNumRows());
        }

        public void onScrollBefore(int position) {
            int cardLow = ChannelCardBaseFragment.GRIDVIEW_TOP + ChannelCardBaseFragment.HALF_CARD;
            int cardHigh = (ChannelCardBaseFragment.GRIDVIEW_TOP + (ChannelCardBaseFragment.HALF_CARD * 3)) + ChannelCardBaseFragment.GRIDVIEW_VERTICALMARGIN_CARD;
            int albumLow = ChannelCardBaseFragment.GRIDVIEW_TOP + ChannelCardBaseFragment.HALF_ALBUM;
            int albumHigh = (ChannelCardBaseFragment.GRIDVIEW_TOP + (ChannelCardBaseFragment.HALF_ALBUM * 3)) + ChannelCardBaseFragment.GRIDVIEW_VERTICALMARGIN_ALBUM;
            if (position < ChannelCardBaseFragment.this.mMultiGridAdapter.getCardDataListSize()) {
                ChannelCardBaseFragment.this.mGridView.setFocusPlace(cardLow, cardLow);
            } else {
                ChannelCardBaseFragment.this.mGridView.setFocusPlace(albumLow, albumLow);
            }
        }
    };
    private final Runnable mScrollStopRunnable = new Runnable() {
        public void run() {
            if (ChannelCardBaseFragment.this.mGridView != null && ChannelCardBaseFragment.this.mMultiGridAdapter != null) {
                int first = ChannelCardBaseFragment.this.mGridView.getFirstAttachedPosition();
                int last = ChannelCardBaseFragment.this.mGridView.getLastAttachedPosition();
                for (int index = first; index <= last; index++) {
                    ChannelCardBaseFragment.this.mMultiGridAdapter.onReloadTasks(ChannelCardBaseFragment.this.mGridView.getViewByPosition(index));
                }
                int curPosition = ChannelCardBaseFragment.this.mGridView.getFocusPosition();
                ChannelCardBaseFragment.this.oldPositionList.add(Integer.valueOf(0));
                int maxOldPosition = ((Integer) Collections.max(ChannelCardBaseFragment.this.oldPositionList)).intValue();
                if (curPosition > ChannelCardBaseFragment.this.mFocusPosition && curPosition > maxOldPosition) {
                    ChannelCardBaseFragment.this.oldPositionList.add(Integer.valueOf(curPosition));
                    ChannelCardBaseFragment.this.sendRquestPingback(true);
                }
                ChannelCardBaseFragment.this.mFocusPosition = ChannelCardBaseFragment.this.mGridView.getFocusPosition();
            } else if (LogUtils.mIsDebug) {
                LogUtils.d("", ">> onScrollStop mMultiGridAdapter" + ChannelCardBaseFragment.this.mMultiGridAdapter + ",mGridView=" + ChannelCardBaseFragment.this.mGridView);
            }
        }
    };
    private int mSelectedRow;
    protected long mStartLoadingTime;
    private ArrayList<Integer> oldPositionList = new ArrayList();

    private static class OnAlbumFetchedListenerImpl implements OnAlbumFetchedListener {
        WeakReference<ChannelCardBaseFragment> mOuter;

        public OnAlbumFetchedListenerImpl(ChannelCardBaseFragment outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onFetchAlbumSuccess(final List<IData> list) {
            final ChannelCardBaseFragment outer = (ChannelCardBaseFragment) this.mOuter.get();
            if (outer != null) {
                outer.runOnUiThread(new Runnable() {
                    public void run() {
                        String str = null;
                        outer.mMultiGridAdapter.hideLoading();
                        long consumeTime = System.currentTimeMillis() - outer.mStartLoadingTime;
                        if (outer.mDataApi == null || outer.isRemoving() || outer.mCardConvertList == null || outer.mAlbumConvertList == null || outer.mGridView == null) {
                            ChannelCardBaseFragment channelCardBaseFragment = outer;
                            if (!ChannelCardBaseFragment.NOLOG) {
                                str = "---loadDataAsync---callback---success---mDataApi=" + outer.mDataApi + "--isRemoving()=" + outer.isRemoving() + "---visitNet timeToken=" + consumeTime + "----callback return!!!!!!";
                            }
                            channelCardBaseFragment.log(str);
                            if (GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore() != null) {
                                GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore().sendHostStatus(HOSTMODULE.EPG, HOSTSTATUS.FAIL);
                                return;
                            }
                            return;
                        }
                        int listCount = ListUtils.getCount(list);
                        int curPage = outer.mDataApi.getCurPage();
                        outer.log(ChannelCardBaseFragment.NOLOG ? null : "---loadDataAsync---callback---success--curPage=" + curPage + "--list.size=" + listCount + "---visitNet timeToken=" + consumeTime);
                        channelCardBaseFragment = outer;
                        if (!ChannelCardBaseFragment.NOLOG) {
                            str = "---loadDataAsync---callback---success--curPage=" + curPage + "--list.size=" + listCount + "---visitNet timeToken=" + consumeTime;
                        }
                        channelCardBaseFragment.logRecord(str);
                        outer.onFetchDataSucceed(list);
                        QAPingback.sendAlbumPageShowPingback(curPage, listCount, outer.mInfoModel, consumeTime, outer.isNeedSendPageShowPingback(), ListUtils.getCount(outer.mCardConvertList));
                        outer.mMultiGridAdapter.showLoading(list.size() < outer.mDataApi.getTotalCount());
                        if (GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore() != null) {
                            GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore().sendHostStatus(HOSTMODULE.EPG, HOSTSTATUS.SUCCESS);
                        }
                    }
                });
            }
        }

        public void onFetchAlbumFail(final ApiException e) {
            final ChannelCardBaseFragment outer = (ChannelCardBaseFragment) this.mOuter.get();
            if (outer != null) {
                outer.runOnUiThread(new Runnable() {
                    public void run() {
                        String str = null;
                        if (outer.mDataApi == null || outer.isRemoving()) {
                            ChannelCardBaseFragment channelCardBaseFragment = outer;
                            if (!ChannelCardBaseFragment.NOLOG) {
                                str = "---loadDataAsync---fail---mDataApi=" + outer.mDataApi + "--isRemoving()=" + outer.isRemoving() + "---visitNet timeToken=" + (System.currentTimeMillis() - outer.mStartLoadingTime);
                            }
                            channelCardBaseFragment.log(str);
                            if (GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore() != null) {
                                GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore().sendHostStatus(HOSTMODULE.EPG, HOSTSTATUS.FAIL);
                                return;
                            }
                            return;
                        }
                        String str2;
                        String code = e != null ? e.getCode() : "";
                        ChannelCardBaseFragment channelCardBaseFragment2 = outer;
                        if (ChannelCardBaseFragment.NOLOG) {
                            str2 = null;
                        } else {
                            str2 = "---loadDataAsync---fail--e=" + e + "---code=" + code + "---timeToken=" + (System.currentTimeMillis() - outer.mStartLoadingTime);
                        }
                        channelCardBaseFragment2.log(str2);
                        channelCardBaseFragment = outer;
                        if (!ChannelCardBaseFragment.NOLOG) {
                            str = "---loadDataAsync---fail--e=" + e + "---code=" + code + "---timeToken=" + (System.currentTimeMillis() - outer.mStartLoadingTime);
                        }
                        channelCardBaseFragment.logRecord(str);
                        outer.onFetchDataFailure(e, code);
                        if (GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore() != null) {
                            GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore().sendHostStatus(HOSTMODULE.EPG, HOSTSTATUS.FAIL);
                        }
                    }
                });
            }
        }
    }

    protected int getLayoutResId() {
        return R.layout.epg_q_album_grid_right;
    }

    private void initUIValues() {
        this.mGridAnimationDuration = 200;
        this.mGridAnimationScale = 1.04f;
    }

    protected void initView() {
        resetTopInfoAfterChangeTag();
        initUIValues();
        initGridView();
        initListener();
    }

    private void initListener() {
        this.mMainView.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && GetInterfaceTools.getUICreator().isViewVisible(ChannelCardBaseFragment.this.mCurrentFocusedView)) {
                    ChannelCardBaseFragment.this.mCurrentFocusedView.requestFocus();
                }
            }
        });
    }

    protected void resetTopInfoAfterChangeTag() {
        String str = null;
        this.mTopCountExpandTxt = IAlbumConfig.STR_TOP_COUNT_BU;
        this.mTopTagDesTxt = null;
        if (this.mInfoModel.isNoLeftFragment()) {
            this.mTopTagNameTxt = null;
            this.mTopMenuDesTxt = null;
            return;
        }
        this.mTopTagNameTxt = this.mInfoModel.getDataTagName();
        if (!ListUtils.isEmpty(this.mDataApi.getMultiTags())) {
            str = IAlbumConfig.STR_FILTER;
        }
        this.mTopMenuDesTxt = str;
    }

    private void initGridView() {
        BaseDataApi fetchNewDataApi = getNewDataApi();
        if (fetchNewDataApi != null) {
            setDataApi(fetchNewDataApi);
        }
        this.mGridView = (VerticalGridView) this.mMainView.findViewById(R.id.epg_qalbum_multigridview);
        this.mMultiGridAdapter = new MultiGridAdapter(this.mContext);
        this.mGridView.setFocusable(false);
        this.mGridView.setFocusMode(1);
        this.mGridView.setScrollRoteScale(0.8f, 1.0f, 2.5f);
        this.mGridView.setLayerType(2, null);
        this.mGridView.setFocusLeaveForbidden(194);
        this.mGridView.setPadding(ResourceUtil.getDimen(R.dimen.dimen_24dp), ResourceUtil.getDimen(R.dimen.dimen_2dp), ResourceUtil.getDimen(R.dimen.dimen_10dp), ResourceUtil.getDimen(R.dimen.dimen_15dp));
        this.mGridView.setHorizontalMargin(ResourceUtil.getDimen(R.dimen.dimen_4dp));
        this.mGridView.setOnItemFocusChangedListener(this.mOnItemFocusChangedListener);
        this.mGridView.setOnItemClickListener(this.mOnItemSelectedListener);
        this.mGridView.setOnScrollListener(this.mOnScrollListener);
        this.mGridView.setItemDecoration(this.mItemDecoration);
        this.mGridView.setFocusLoop(true);
        this.mGridView.setOnItemRecycledListener(this.mOnItemRecycledListener);
        this.mGridView.setScrollBarDrawable(R.drawable.epg_thumb);
        this.mGridView.setAdapter(this.mMultiGridAdapter);
        this.mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (ChannelCardBaseFragment.this.mGridView.getWidth() != 0 && ChannelCardBaseFragment.this.mGridView.getHeight() != 0 && ChannelCardBaseFragment.this.mFetchDataSucceed && !ChannelCardBaseFragment.this.mIsInitSend && ChannelCardBaseFragment.this.mGridView.getLastAttachedPosition() >= 0) {
                    ChannelCardBaseFragment.this.sendRquestPingback(false);
                    ChannelCardBaseFragment.this.mIsInitSend = true;
                }
            }
        });
    }

    protected void onGridItemClick(int position) {
        String str = null;
        log(NOLOG ? null : "onAlbumItemClicked position=" + position);
        logRecord(NOLOG ? null : "onAlbumItemClicked position=" + position);
        if (position < 0) {
            if (!NOLOG) {
                str = "onAlbumItemClicked return";
            }
            log(str);
            return;
        }
        int[] array = getRowAndColumn(position);
        int selectedRow = array[1];
        int selectedColumn = array[0] + 1;
        int cardSize = this.mMultiGridAdapter.getCardDataList();
        if (position < cardSize) {
            this.mInfoModel.setFocusPosition(position);
        } else if (cardSize % 2 == 0) {
            this.mInfoModel.setFocusPosition(position);
        } else {
            this.mInfoModel.setFocusPosition(position - 1);
        }
        IData info = this.mMultiGridAdapter.getIData(position);
        if (info != null) {
            Album album = info.getAlbum();
            if (album != null) {
                this.mChannelId = album.chnId;
                this.mChannelName = album.chnName;
            }
            this.mInfoModel.setSelectColumn(selectedColumn - 1);
            this.mInfoModel.setSelectRow(selectedRow - 1);
            info.click(this.mContext, this.mInfoModel);
        }
    }

    private int[] getRowAndColumn(int position) {
        int rowIndex = 0;
        int numRows = 0;
        int totalRow = 0;
        for (int i = 0; i <= position; i++) {
            int mGridViewNum = this.mGridView.getNumRows(i);
            if (numRows != mGridViewNum) {
                numRows = mGridViewNum;
                rowIndex = 0;
                totalRow++;
            } else {
                rowIndex++;
                if (rowIndex == numRows) {
                    rowIndex = 0;
                    totalRow++;
                }
            }
        }
        return new int[]{rowIndex, totalRow};
    }

    protected void onGridItemSelected(ViewGroup parent, View view, int position, boolean hasFocus) {
        if (position < this.mMultiGridAdapter.getCardDataListSize()) {
            this.mGridAnimationScale = 1.04f;
        } else {
            this.mGridAnimationScale = 1.09f;
        }
        AnimationUtil.zoomAnimation(view, hasFocus ? this.mGridAnimationScale : 1.0f, this.mGridAnimationDuration);
    }

    protected void onGridRowSelected(int selectedRow, int rowCount) {
        if (rowCount - selectedRow < 10 && selectedRow > 0 && selectedRow > this.mSelectedRow) {
            loadDataAsync();
        }
        this.mSelectedRow = selectedRow;
    }

    protected void loadData() {
        String str = null;
        if (this.mDataApi == null) {
            showNoResultPanel(ErrorKind.NET_ERROR, null);
            if (!NOLOG) {
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
        String str = null;
        if (!AlbumInfoFactory.needShowLoadingView(this.mInfoModel.getPageType())) {
            log(NOLOG ? null : "---show cache view--right");
            if (!NOLOG) {
                str = "---show cache view--right";
            }
            logRecord(str);
            showHasCachePanel();
        }
    }

    private void resetTempValues() {
        this.mTotalItemCount = 0;
        this.mDisplayTotal = 0;
        this.mSelectedRow = 0;
        this.mHasUnCardType = false;
        setRightFragmentHasData(false);
        setShowingCacheData(false);
        this.mCurrentFocusedView = null;
        if (this.mCardConvertList != null) {
            this.mCardConvertList.clear();
        } else {
            this.mCardConvertList = new ArrayList(1);
        }
        if (this.mAlbumConvertList != null) {
            this.mAlbumConvertList.clear();
        } else {
            this.mAlbumConvertList = new ArrayList(1);
        }
        this.mGridView.setFocusPosition(0);
        this.mGridView.setTotalSize(0);
    }

    private void loadDataAsync() {
        String str = null;
        log(NOLOG ? null : "---loadDataAsync---next log should be callback--curPage=" + this.mDataApi.getCurPage());
        if (!NOLOG) {
            str = "---loadDataAsync---next log should be callback--curPage=" + this.mDataApi.getCurPage();
        }
        logRecord(str);
        if (this.mDataApi.getCurPage() <= 0) {
            showProgress();
        }
        this.mStartLoadingTime = System.currentTimeMillis();
        this.mDataApi.loadAlbumData(new OnAlbumFetchedListenerImpl(this));
    }

    protected void onDownloadCompleted(List<IData> list) {
        String str;
        String str2 = null;
        this.mTotalItemCount = this.mDataApi.getTotalCount();
        this.mDisplayTotal = this.mDataApi.getDisplayCount();
        if (NOLOG) {
            str = null;
        } else {
            str = "-onDownloadCompleted 1 --mTotalItemCount=" + this.mTotalItemCount + "---mDisplayTotal=" + this.mDisplayTotal + "--list.size=" + ListUtils.getCount((List) list);
        }
        log(str);
        if (NOLOG) {
            str = null;
        } else {
            str = "-onDownloadCompleted 1 --mTotalItemCount=" + this.mTotalItemCount + "---mDisplayTotal=" + this.mDisplayTotal + "--list.size=" + ListUtils.getCount((List) list);
        }
        logRecord(str);
        if (this.mTotalItemCount == 0) {
            int count = ListUtils.getCount((List) list);
            this.mTotalItemCount = count;
            this.mDisplayTotal = count;
            log(NOLOG ? null : "---onDownloadCompleted--mTotalItemCount==0 ， 虽做了兼容，但后台必须修复 ！！！");
        }
        if (NOLOG) {
            str = null;
        } else {
            str = "-onDownloadCompleted 2 --mTotalItemCount=" + this.mTotalItemCount + "---mDisplayTotal=" + this.mDisplayTotal + "--list.size=" + ListUtils.getCount((List) list);
        }
        log(str);
        if (!NOLOG) {
            str2 = "-onDownloadCompleted 2 --mTotalItemCount=" + this.mTotalItemCount + "---mDisplayTotal=" + this.mDisplayTotal + "--list.size=" + ListUtils.getCount((List) list);
        }
        logRecord(str2);
        dataNotified(list);
        focusSetAfterLoad();
        QAPingback.multiMenuAfterLoad(this.mContext, this.mInfoModel, this.mDataApi, getMenuView(), this.mStartLoadingTime);
    }

    private void focusSetAfterLoad() {
        this.mGridView.setFocusable(true);
        int count = this.listCount;
        if (this.mGridView != null && count > 0 && this.mNeedRequestIndex >= 0 && this.mNeedRequestIndex >= count) {
            this.mNeedRequestIndex = count - 1;
        }
    }

    private void dataNotified(List<IData> list) {
        String str = null;
        if (this.mDataApi != null && this.mGridView != null && this.mMultiGridAdapter != null && this.mCardConvertList != null && this.mAlbumConvertList != null) {
            String str2;
            this.mCardConvertList.clear();
            this.mAlbumConvertList.clear();
            int ignoreCount = 0;
            for (IData albumData : list) {
                if (!DataInfoProvider.isCardData(albumData)) {
                    this.mHasUnCardType = true;
                    this.mAlbumConvertList.add(albumData);
                } else if (this.mHasUnCardType) {
                    ignoreCount++;
                } else {
                    albumData.setShowingCard(true);
                    this.mCardConvertList.add(albumData);
                }
                this.listCount = ListUtils.getCount(this.mCardConvertList) + ListUtils.getCount(this.mAlbumConvertList);
            }
            if (NOLOG) {
                str2 = null;
            } else {
                str2 = "---dataNotified--page = " + this.mDataApi.getCurPage() + " -- mCardConvertList.size=" + this.mCardConvertList.size() + ",mAlbumConvertList.size=" + this.mAlbumConvertList.size();
            }
            log(str2);
            if (!NOLOG) {
                str = "---dataNotified--page = " + this.mDataApi.getCurPage() + " -- mCardConvertList.size=" + this.mCardConvertList.size() + ",mAlbumConvertList.size=" + this.mAlbumConvertList.size();
            }
            logRecord(str);
            if (this.mDataApi.getCurPage() <= 1) {
                this.mMultiGridAdapter.onResume();
                this.mMultiGridAdapter.updateCardData(this.mCardConvertList);
                this.mMultiGridAdapter.updateAlbumData(this.mAlbumConvertList);
                showHasResultPanel();
            } else {
                this.mMultiGridAdapter.updateCardData(this.mCardConvertList);
                this.mMultiGridAdapter.updateAlbumData(this.mAlbumConvertList);
            }
            this.mGridView.setTotalSize(this.mTotalItemCount);
            int cardRow = this.mMultiGridAdapter.getCardDataListSize() / 2;
            int albumRow = (int) Math.ceil((double) (((float) ((this.mTotalItemCount - this.mCardConvertList.size()) - ignoreCount)) / 4.0f));
            int cardHeight = ResourceUtil.getDimen(R.dimen.dimen_279dp);
            int cardMargin = ResourceUtil.getDimen(R.dimen.dimen_20dp);
            this.mGridView.setScrollRange((((((cardRow * cardHeight) + (albumRow * ResourceUtil.getDimen(R.dimen.dimen_214dp))) + ((cardRow - 1) * cardMargin)) + (ResourceUtil.getDimen(R.dimen.dimen_4dp) * albumRow)) + this.mGridView.getPaddingTop()) + this.mGridView.getPaddingBottom());
        }
    }

    public void showHasResultPanel() {
        this.mBaseHandler.removeCallbacks(this.mCacheViewRunnable);
        if (this.mDataApi != null && this.mDataApi.getCurPage() <= 1) {
            setTopTagTextAfterLoad(this.mTotalItemCount);
        }
        super.showHasResultPanel();
        if (!StringUtils.equals(this.mTopMenuDesTxt, IAlbumConfig.STR_FILTER)) {
            setTopMenuLayoutVisible(4);
        }
        setShowingCacheData(false);
    }

    public Bitmap showNoResultPanel(ErrorKind kind, ApiException e) {
        this.mBaseHandler.removeCallbacks(this.mCacheViewRunnable);
        setShowingCacheData(false);
        super.showNoResultPanel(kind, e);
        if (StringUtils.equals(this.mTopMenuDesTxt, IAlbumConfig.STR_FILTER)) {
            setTopMenuLayoutVisible(0);
        }
        return null;
    }

    private void showHasCachePanel() {
        this.mBaseHandler.removeCallbacks(this.mCacheViewRunnable);
        setShowingCacheData(true);
        if (this.mDataApi != null && this.mDataApi.getCurPage() <= 1) {
            setTopTagTextAfterLoad(this.mTotalItemCount);
        }
        super.showHasResultPanel();
    }

    protected void onFetchDataSucceed(List<IData> list) {
        if (ListUtils.isEmpty((List) list)) {
            showNoResultPanel(ListUtils.isEmpty(this.mDataApi.getMultiTags()) ? ErrorKind.NO_RESULT_AND_NO_MENU : ErrorKind.NO_RESULT, null);
            sendRquestPingback(false);
        } else {
            onDownloadCompleted(list);
            if (this.mIsInitSend) {
                sendRquestPingback(false);
            }
        }
        this.mFetchDataSucceed = true;
    }

    protected void onFetchDataFailure(ApiException e, String code) {
        if (ListUtils.isEmpty(this.mCardConvertList) && ListUtils.isEmpty(this.mAlbumConvertList)) {
            showNoResultPanel(ErrorKind.NET_ERROR, e);
        } else {
            showHasResultPanel();
        }
    }

    protected void onNetChanged() {
        String str = null;
        if (isShowingCacheData() && !isLeftFragmentHasData()) {
            return;
        }
        if (this.mDataApi == null || this.mDataApi.getCurPage() > 1 || !ListUtils.isEmpty(this.mCardConvertList) || !ListUtils.isEmpty(this.mAlbumConvertList)) {
            log(NOLOG ? null : "---onNetChanged----loadDataAsync");
            if (!NOLOG) {
                str = "---onNetChanged----loadDataAsync";
            }
            logRecord(str);
            loadDataAsync();
            return;
        }
        log(NOLOG ? null : "---onNetChanged----loadData");
        if (!NOLOG) {
            str = "---onNetChanged----loadData";
        }
        logRecord(str);
        loadData();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == 82) {
            setMenu2Activity();
        }
        return super.dispatchKeyEvent(event);
    }

    public void onDestroy() {
        if (this.mGridView != null) {
            this.mGridView.setLayerType(0, null);
        }
        this.mBaseHandler.removeCallbacks(this.mScrollStopRunnable);
        this.mBaseHandler.removeCallbacks(this.mCacheViewRunnable);
        this.mDataApi = null;
        super.onDestroy();
    }

    public void handlerMessage2Right(Message msg) {
        String str = null;
        super.handlerMessage2Right(msg);
        if (msg != null && msg.what == 51) {
            log(NOLOG ? null : "---handlerMessage2Right---refresh ChannelSearchResultCardFragment");
            if (!NOLOG) {
                str = "---handlerMessage2Right---refresh ChannelSearchResultCardFragment";
            }
            logRecord(str);
            ImageProviderApi.getImageProvider().stopAllTasks();
            if (this.mMultiGridAdapter != null) {
                this.mMultiGridAdapter.onPause();
                this.mMultiGridAdapter.resetList();
                this.mMultiGridAdapter.showLoading(false);
                this.mIsInitSend = false;
                this.mFetchDataSucceed = false;
            }
            if (getInfoModel() != null) {
                getDataApi();
                resetTopInfoAfterChangeTag();
                loadData();
            }
        }
    }

    private BaseDataApi getNewDataApi() {
        return null;
    }

    protected boolean isNeedSendPageShowPingback() {
        return true;
    }

    protected void sendRquestPingback(boolean fromScrollEnd) {
    }
}
