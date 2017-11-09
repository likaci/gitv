package com.gala.video.app.epg.ui.albumlist.fragment.left;

import android.graphics.drawable.Drawable;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.ListView;
import com.gala.video.albumlist4.widget.ListView.ItemDivider;
import com.gala.video.albumlist4.widget.RecyclerView;
import com.gala.video.albumlist4.widget.RecyclerView.ItemDecoration;
import com.gala.video.albumlist4.widget.RecyclerView.OnFocusLostListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarAnimaitonUtils;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnLabelFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.api.SearchResultApi;
import com.gala.video.app.epg.ui.albumlist.factory.AlbumFragmentFactory;
import com.gala.video.app.epg.ui.albumlist.fragment.right.AlbumBaseRightFragment;
import com.gala.video.app.epg.ui.albumlist.fragment.right.cardview.ChannelCardBaseFragment;
import com.gala.video.app.epg.ui.albumlist.fragment.right.gridview.ChannelGridBaseFragment;
import com.gala.video.app.epg.ui.albumlist.fragment.right.recommend.ChannelRecommendBaseFragment;
import com.gala.video.app.epg.ui.albumlist.multimenu.MultiMenuPanel;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.app.epg.ui.albumlist.widget.SelectView;
import com.gala.video.app.epg.ui.albumlist.widget.SelectView.OnItemSelectListener;
import com.gala.video.app.epg.ui.albumlist.widget.adapter.LabelAlbumAdapter;
import com.gala.video.app.epg.utils.KeyEventUtils;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.player.CarouselPlayParamBuilder;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import org.xbill.DNS.WKSRecord.Service;

public class ChannelLeftFragment extends AlbumBaseLeftFragment implements OnItemRecycledListener, OnItemClickListener, OnItemFocusChangedListener, OnFocusLostListener {
    private static final int NO_DATA_INDEX = -3;
    private static final int REFRESH_DATA_WHAT = 111;
    private static final int REFRESH_INTER_DURING = 350;
    private static final int SELECT_INDEX = -1;
    private static final String TAG = "EPG/ChannelLeftFragment";
    private int mAnimationDuration = 100;
    private float mAnimationScale = 1.05f;
    private String mBuySourceSrc;
    private boolean mClickSelectViewBeforeLabelAdapter;
    private Drawable mDivider;
    private int mFristLabelLocation;
    private boolean mIsReloadAfterNetChange;
    private LabelAlbumAdapter mLabelAdapter;
    private List<Tag> mLabelTagList = new LinkedList();
    private int mLastDataPosition = -3;
    private int mLastKeyCode;
    private AlbumBaseRightFragment mLastRightFragment;
    private ListView mLeftLabel;
    private boolean mNeedReloadAfterNetChange;
    private boolean mPermitRefreshDataByTag;
    private SelectView mSelectLayout;
    private int mSelectType;
    private SelectView.OnItemClickListener mSelectViewClickListener = new C08222();
    private OnItemSelectListener mSelectViewSelectListener = new C08211();
    private long mStartLoadingTime;

    class C08211 implements OnItemSelectListener {
        C08211() {
        }

        public boolean onItemSelected(View v, int tagType, boolean isSelected) {
            int i = 2;
            ChannelLeftFragment.this.setNextFocusUpId(v);
            if (ChannelLeftFragment.this.mLastKeyCode != 21 || ChannelLeftFragment.this.mInfoModel.isMultiHasData() || ChannelLeftFragment.this.mLabelAdapter == null) {
                if (isSelected) {
                    ChannelLeftFragment.this.setGlobalLastFocusView(v);
                }
                SelectView access$500;
                switch (tagType) {
                    case 11:
                        if (!isSelected && ChannelLeftFragment.this.mInfoModel.isMultiHasData()) {
                            ChannelLeftFragment.this.mSelectLayout.setSelectViewColorStatus(1);
                            break;
                        }
                        access$500 = ChannelLeftFragment.this.mSelectLayout;
                        if (isSelected) {
                            i = 0;
                        }
                        access$500.setSelectViewColorStatus(i);
                        break;
                    case 12:
                        access$500 = ChannelLeftFragment.this.mSelectLayout;
                        if (isSelected) {
                            i = 0;
                        }
                        access$500.setCarrouselViewColorStatus(i);
                        break;
                }
                AnimationUtil.zoomLeftAnimation(v, isSelected, ChannelLeftFragment.this.mAnimationScale, ChannelLeftFragment.this.mAnimationDuration);
                return false;
            }
            if (!(ChannelLeftFragment.this.mLeftLabel == null || ChannelLeftFragment.this.mLeftLabel.isFocused())) {
                ChannelLeftFragment.this.mLeftLabel.requestFocus();
            }
            return true;
        }
    }

    class C08222 implements SelectView.OnItemClickListener {
        C08222() {
        }

        public void onItemClick(View view, int tagType) {
            switch (tagType) {
                case 11:
                    ChannelLeftFragment.this.setMenu2Activity();
                    if (ChannelLeftFragment.this.getMenuView() != null) {
                        ChannelLeftFragment.this.mRefreshImmediately = true;
                        ChannelLeftFragment.this.mPermitRefreshDataByTag = true;
                        ChannelLeftFragment.this.mClickSelectViewBeforeLabelAdapter = true;
                        ChannelLeftFragment.this.prepareRefresh(-1);
                        break;
                    }
                    return;
                case 12:
                    CarouselPlayParamBuilder carouselPlayParamBuilder = new CarouselPlayParamBuilder();
                    carouselPlayParamBuilder.setChannel(null);
                    carouselPlayParamBuilder.setFrom(ChannelLeftFragment.this.getCarrouselFrom(PingBackUtils.getTabSrc()));
                    carouselPlayParamBuilder.setTabSource(PingBackUtils.getTabSrc());
                    GetInterfaceTools.getPlayerPageProvider().startCarouselPlayerPage(ChannelLeftFragment.this.mContext, carouselPlayParamBuilder);
                    break;
            }
            QAPingback.labelTagClickPingback(ChannelLeftFragment.this.mSelectLayout.getTagName(tagType), ChannelLeftFragment.this.mInfoModel);
        }
    }

    public class GridItemDecoration extends ItemDecoration {
        public int getItemOffsets(int position, RecyclerView parent) {
            return ChannelLeftFragment.this.isLevel(position) ? ChannelLeftFragment.this.getDimen(C0508R.dimen.dimen_1dp) : ChannelLeftFragment.this.getDimen(C0508R.dimen.dimen_8dp);
        }
    }

    public class GridItemDivider extends ItemDivider {
        public Drawable getItemDivider(int position, RecyclerView parent) {
            return ChannelLeftFragment.this.isLevel(position) ? null : ChannelLeftFragment.this.getDividerDrawable();
        }
    }

    static class LabelFetchedListener implements OnLabelFetchedListener {
        WeakReference<ChannelLeftFragment> mOuter;

        public LabelFetchedListener(ChannelLeftFragment outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onFetchLabelSuccess(final List<Tag> srcList) {
            String str = null;
            final ChannelLeftFragment outer = (ChannelLeftFragment) this.mOuter.get();
            if (outer != null) {
                if (outer.mDataApi == null || outer.isRemoving() || outer.mLabelTagList == null || outer.mLabelTagList.size() > 0) {
                    if (!ChannelLeftFragment.NOLOG) {
                        str = "---loadDataAsync---callback---success---mDataApi=" + outer.mDataApi + "--isRemoving()=" + outer.isRemoving() + "---mLabelTagList=" + outer.mLabelTagList + "---visitNet timeToken=" + (System.currentTimeMillis() - outer.mStartLoadingTime);
                    }
                    outer.log(str);
                    return;
                }
                String str2;
                String labelLog = "--loadDataAsync---callback--success--list.size=" + ListUtils.getCount((List) srcList) + "---visitNet timeToken=" + (System.currentTimeMillis() - outer.mStartLoadingTime);
                if (ChannelLeftFragment.NOLOG) {
                    str2 = null;
                } else {
                    str2 = labelLog;
                }
                outer.log(str2);
                if (!ChannelLeftFragment.NOLOG) {
                    str = labelLog;
                }
                outer.logRecord(str);
                outer.runOnUiThread(new Runnable() {
                    public void run() {
                        outer.mLabelTagList = srcList;
                        if (ListUtils.isEmpty(outer.mLabelTagList)) {
                            outer.handleWhenNoCallbackList(new ApiException("fetch label data, list is empty !"));
                            return;
                        }
                        outer.mPermitRefreshDataByTag = true;
                        outer.setLabelAdapter();
                    }
                });
            }
        }

        public void onFetchLabelFail(ApiException exception) {
            String str = null;
            ChannelLeftFragment outer = (ChannelLeftFragment) this.mOuter.get();
            if (outer != null) {
                String str2;
                String labelLog = "--loadDataAsync---callback--fail---e=" + exception + "---visitNet timeToken=" + (System.currentTimeMillis() - outer.mStartLoadingTime);
                if (ChannelLeftFragment.NOLOG) {
                    str2 = null;
                } else {
                    str2 = labelLog;
                }
                outer.log(str2);
                if (ChannelLeftFragment.NOLOG) {
                    labelLog = null;
                }
                outer.logRecord(labelLog);
                if (outer.mDataApi == null || outer.isRemoving() || outer.mLabelTagList == null) {
                    if (!ChannelLeftFragment.NOLOG) {
                        str = "---loadDataAsync---callback---fail---mDataApi=" + outer.mDataApi + "--isRemoving()=" + outer.isRemoving();
                    }
                    outer.log(str);
                    return;
                }
                outer.runTaskOnUiThread(exception);
            }
        }
    }

    protected int getLayoutResId() {
        return C0508R.layout.epg_q_album_left;
    }

    protected void initView() {
        this.mRefreshImmediately = true;
        this.mSelectLayout = (SelectView) this.mMainView.findViewById(C0508R.id.epg_search_select_view);
        this.mLeftLabel = (ListView) this.mMainView.findViewById(C0508R.id.epg_left_scrollview);
        initSelectView();
        initLabelView();
        this.mBuySourceSrc = this.mInfoModel.getBuySource();
        preparePingbackFrom();
    }

    private void initSelectView() {
        this.mSelectType = this.mDataApi.getSelectType();
        this.mSelectLayout.setViewParams(this.mSelectType);
        if (this.mSelectType == 0) {
            this.mSelectLayout.setVisibility(8);
            this.mSelectLayout.setFocusable(false);
            return;
        }
        this.mSelectLayout.setOnItemSelectListener(this.mSelectViewSelectListener);
        this.mSelectLayout.setOnItemClickListener(this.mSelectViewClickListener);
        View lastItem = this.mSelectLayout.getLastItem();
        if (lastItem != null) {
            lastItem.requestFocus();
            lastItem.setNextFocusLeftId(lastItem.getId());
            lastItem.setNextFocusDownId(lastItem.getId());
        }
    }

    private void zoomAnimation(View v, boolean isSelected) {
        AnimationUtil.zoomLeftAnimation(v, isSelected, this.mAnimationScale, this.mAnimationDuration);
        if (this.mAnimationDuration == 0) {
            this.mAnimationDuration = 100;
        }
    }

    private String getCarrouselFrom(String from) {
        String str = null;
        if (StringUtils.isEmpty((CharSequence) from)) {
            LogUtils.m1571e(TAG, "getCarrouselFrom from  is null");
        } else {
            try {
                str = from.substring(4) + "_" + this.mInfoModel.getChannelName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    private void initLabelView() {
        this.mLeftLabel.setVisibility(8);
        this.mLeftLabel.setFocusPlace(FocusPlace.FOCUS_CENTER);
        this.mLeftLabel.setFocusMode(1);
        this.mLeftLabel.setScrollRoteScale(1.0f, 1.0f, 2.0f);
        this.mLeftLabel.setOnItemFocusChangedListener(this);
        this.mLeftLabel.setOnItemRecycledListener(this);
        this.mLeftLabel.setOnItemClickListener(this);
        this.mLeftLabel.setOnFocusLostListener(this);
        this.mLeftLabel.setFocusLeaveForbidden(Service.CISCO_FNA);
        this.mLeftLabel.setItemDivider(new GridItemDivider());
        this.mLeftLabel.setItemDecoration(new GridItemDecoration());
        this.mLeftLabel.setDividerWidth(getDimen(C0508R.dimen.dimen_169dp));
        this.mLeftLabel.setBackgroundWidth(getDimen(C0508R.dimen.dimen_218dp));
        this.mLeftLabel.setShakeForbidden(17);
        if (this.mSelectType == 0) {
            MarginLayoutParams lp = (MarginLayoutParams) this.mLeftLabel.getLayoutParams();
            this.mLeftLabel.setClipToPadding(false);
            lp.topMargin = getDimen(C0508R.dimen.dimen_50dp);
            this.mLeftLabel.setPadding(0, getDimen(C0508R.dimen.dimen_6dp), 0, 0);
        }
    }

    private boolean isLevel(int position) {
        if (this.mLabelTagList == null) {
            return true;
        }
        if (!AlbumInfoFactory.isNewVipChannel(this.mInfoModel.getChannelId()) && !AlbumInfoFactory.isLiveChannel(this.mInfoModel.getChannelId(), this.mInfoModel.getPageType())) {
            return false;
        }
        Tag tag = (Tag) this.mLabelTagList.get(position);
        Tag tagNext = (Tag) this.mLabelTagList.get(position + 1);
        if (tag == null || tagNext == null || tag.getLevel() != 2 || tagNext.getLevel() != 2) {
            return true;
        }
        return false;
    }

    private Drawable getDividerDrawable() {
        if (this.mDivider == null) {
            this.mDivider = ResourceUtil.getDrawable(C0508R.drawable.epg_album_label_line);
        }
        return this.mDivider;
    }

    private void setLabelAdapter() {
        LogUtils.m1571e(TAG, "setLabelAdapter");
        this.mLabelAdapter = new LabelAlbumAdapter(this.mContext, this.mLabelTagList, this.mInfoModel);
        this.mLeftLabel.setAdapter(this.mLabelAdapter);
        this.mLeftLabel.setVisibility(0);
        initSelectFocus();
        initLeftFirstLocation();
        setLeftFragmentHasData(true);
        setLoadingData(false);
    }

    private void initSelectFocus() {
        View lastItem = this.mSelectLayout.getLastItem();
        if (lastItem != null) {
            lastItem.setNextFocusDownId(this.mLeftLabel.getId());
        }
        if (this.mSelectType == 0) {
            setNextFocusUpId(this.mLeftLabel);
        }
    }

    private void initLeftFirstLocation() {
        String str = null;
        if (this.mDataApi != null && !this.mClickSelectViewBeforeLabelAdapter) {
            if (StringUtils.isEmpty(this.mInfoModel.getFirstMultiLocationTagId()) || this.mSelectLayout.getSelectItem() == null) {
                this.mFristLabelLocation = this.mDataApi.getLabelFirstLocation();
                if (!NOLOG) {
                    str = "-- initLeftFirstLocation =" + this.mFristLabelLocation;
                }
                log(str);
                setLabelLocationWithDataRefresh(this.mFristLabelLocation);
            } else {
                log(NOLOG ? null : "--initLeftFirstLocation,with multimenu tagId ");
                setMenu2Activity();
                final MultiMenuPanel menuView = (MultiMenuPanel) getMenuView();
                if (menuView == null) {
                    if (!NOLOG) {
                        str = "--initLeftFirstLocation,fail create MultiMenuPanel,return";
                    }
                    log(str);
                    return;
                }
                setMultiMenuDataInfo(true);
                this.mRefreshImmediately = false;
                this.mLastDataPosition = -1;
                this.mSelectLayout.getSelectItem().requestFocus();
                if (menuView.isInitCompleted()) {
                    refreshMultiLocationData(menuView);
                } else {
                    if (!NOLOG) {
                        str = "---MultiMenuPanel is not complete , need try again";
                    }
                    log(str);
                    this.mBaseHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (menuView != null && menuView.isInitCompleted()) {
                                ChannelLeftFragment.this.refreshMultiLocationData(menuView);
                            }
                        }
                    }, 500);
                }
            }
            hideMenu();
        }
    }

    private void refreshMultiLocationData(MultiMenuPanel menuView) {
        log(NOLOG ? null : "---MultiMenuPanel is completed ,go to page by tagId");
        menuView.selectTargetTag(this.mInfoModel.getFirstMultiLocationTagId(), true);
    }

    private void setLabelLocationWithDataRefresh(int pos) {
        if (ListUtils.isLegal(this.mLabelTagList, pos)) {
            this.mRefreshImmediately = true;
            if (this.mIsReloadAfterNetChange) {
                this.mRefreshImmediately = false;
                pos = 0;
            }
            this.mAnimationDuration = 0;
            ActionBarAnimaitonUtils.setDelay(0);
            this.mLeftLabel.requestFocus();
            this.mLeftLabel.setFocusPosition(pos);
        }
    }

    protected void runTaskOnUiThread(final ApiException exception) {
        runOnUiThread(new Runnable() {
            public void run() {
                ChannelLeftFragment.this.mNeedReloadAfterNetChange = true;
                ChannelLeftFragment.this.handleWhenNoCallbackList(exception);
            }
        });
    }

    private void handleWhenNoCallbackList(ApiException e) {
        if (IAlbumConfig.UNIQUE_CHANNEL_SEARCH_RESULT_CARD.equals(this.mInfoModel.getPageType())) {
            this.mLabelTagList.clear();
            this.mLabelTagList.add(new Tag("0", IAlbumConfig.STR_ALL, QLayoutKind.PORTRAIT));
            setLabelAdapter();
        }
        showNoResultPanel(ErrorKind.NET_ERROR, e);
        setShowingCacheData(false);
    }

    protected void loadData() {
        String str = null;
        log(NOLOG ? null : "--loadData--mDataApi=" + this.mDataApi + "---next log should be callback");
        if (!NOLOG) {
            str = "--loadData--mDataApi=" + this.mDataApi + "---next log should be callback";
        }
        logRecord(str);
        showProgress();
        showCacheView();
        this.mStartLoadingTime = System.currentTimeMillis();
        this.mDataApi.loadLabelData(new LabelFetchedListener(this));
    }

    private void showCacheView() {
        if (!AlbumInfoFactory.needShowLoadingView(this.mInfoModel.getPageType())) {
            prepareSwitchWay(AlbumFragmentFactory.createChannelRightFragment(this.mInfoModel, this));
        }
    }

    private void prepareRefresh(int pos) {
        if ((this.mLastDataPosition != pos || this.mLastDataPosition == -1) && this.mPermitRefreshDataByTag) {
            Message msg = this.mBaseHandler.obtainMessage(111);
            msg.obj = Integer.valueOf(pos);
            this.mBaseHandler.sendMessageDelayed(msg, this.mRefreshImmediately ? 0 : 350);
            this.mRefreshImmediately = false;
            return;
        }
        this.mBaseHandler.removeMessages(111);
    }

    protected void handlerMessage(Message msg) {
        int curPos = ((Integer) msg.obj).intValue();
        switch (this.mLastDataPosition) {
            case -1:
                this.mSelectLayout.setSelectViewColorStatus(2);
                break;
            default:
                if (this.mLabelAdapter != null) {
                    this.mLabelAdapter.setSelectDefault();
                    break;
                }
                break;
        }
        this.mLastDataPosition = curPos;
        if (curPos == this.mLeftLabel.getFocusPosition() || curPos == -1) {
            initNewChangeTagTask(curPos);
        }
    }

    private void initNewChangeTagTask(int curPage) {
        Tag tag;
        switch (curPage) {
            case -1:
                if (getMenuView() != null && (getMenuView() instanceof MultiMenuPanel)) {
                    showMenu();
                    setMultiMenuDataInfo(true);
                    tag = ((MultiMenuPanel) getMenuView()).getCheckedTag();
                    this.mSelectLayout.setSelectViewColorStatus(1);
                    setGlobalLastFocusView(this.mSelectLayout.getSelectItem());
                    if (!(tag == null || tag.getID() == null || !tag.getID().equals(this.mInfoModel.getDataTagId()))) {
                        return;
                    }
                }
                return;
                break;
            default:
                setMultiMenuDataInfo(false);
                tag = (Tag) this.mLabelTagList.get(curPage);
                QAPingback.labelTagClickPingback(tag != null ? tag.getName() : "", this.mInfoModel);
                break;
        }
        replaceNewFragment(tag);
    }

    public void handlerMessage2Left(Message msg) {
        super.handlerMessage2Left(msg);
        if (msg.what == 50) {
            setMultiMenuDataInfo(true);
            this.mRefreshImmediately = false;
            this.mClickSelectViewBeforeLabelAdapter = true;
            switch (this.mLastDataPosition) {
                case -1:
                    break;
                default:
                    if (this.mLabelAdapter != null) {
                        this.mLabelAdapter.setSelectDefault();
                    }
                    this.mSelectLayout.setSelectViewColorStatus(1);
                    break;
            }
            setGlobalLastFocusView(this.mSelectLayout.getSelectItem());
            this.mLastDataPosition = -1;
            replaceNewFragment(msg.obj);
        } else if (msg.what == 55) {
            setLabelLocationWithDataRefresh(this.mLastDataPosition - 1);
        } else if (msg.what == 54) {
            setLabelLocationWithDataRefresh(this.mLastDataPosition + 1);
        }
    }

    private void replaceNewFragment(Tag tag) {
        if (tag != null && this.mInfoModel != null) {
            setTagId(tag.getID());
            setTagName(tag.getName());
            setTagType(tag.getType());
            setTagResourceType(tag.getResourceType());
            resetDataApi(tag);
            AlbumBaseRightFragment fragment = AlbumFragmentFactory.createChannelRightFragment(this.mInfoModel);
            preparePingbackBuySource();
            preparePingbackFrom();
            prepareMultiMenuLocation(tag);
            prepareSwitchWay(fragment);
        }
    }

    private void preparePingbackBuySource() {
        String buySource = this.mBuySourceSrc;
        String tagName = this.mInfoModel.getDataTagName();
        if (IAlbumConfig.PROJECT_NAME_OPEN_API.equals(this.mInfoModel.getProjectName())) {
            setBuySource("openAPI");
        } else if (!TextUtils.isEmpty(buySource) && !TextUtils.isEmpty(tagName) && buySource.contains(IAlbumConfig.BUY_SOURCE_NEED_REPLACE) && IAlbumConfig.PROJECT_NAME_BASE_LINE.equals(this.mInfoModel.getProjectName())) {
            if (AlbumInfoFactory.isNewVipChannel(this.mInfoModel.getChannelId()) || AlbumInfoFactory.isLiveChannel(this.mInfoModel.getChannelId(), this.mInfoModel.getPageType())) {
                if (SourceTool.getRecommendTagName().equals(tagName)) {
                    buySource = buySource.replace(IAlbumConfig.BUY_SOURCE_NEED_REPLACE, "rec");
                } else {
                    buySource = buySource.replace(IAlbumConfig.BUY_SOURCE_NEED_REPLACE, this.mInfoModel.getDataTagId());
                }
            } else if (this.mInfoModel.isMultiHasData()) {
                buySource = buySource.replace(IAlbumConfig.BUY_SOURCE_NEED_REPLACE, IAlbumConfig.BUY_SOURCE_FLITER);
            } else if (SourceTool.getChannelPlayListTagName().equals(tagName)) {
                buySource = buySource.replace(IAlbumConfig.BUY_SOURCE_NEED_REPLACE, "topic");
            } else if (SourceTool.getHotTagName().equals(tagName)) {
                buySource = buySource.replace(IAlbumConfig.BUY_SOURCE_NEED_REPLACE, IAlbumConfig.BUY_SOURCE_HOT);
            } else if (SourceTool.getNewestTagName().equals(tagName)) {
                buySource = buySource.replace(IAlbumConfig.BUY_SOURCE_NEED_REPLACE, IAlbumConfig.BUY_SOURCE_NEW);
            } else if (SourceTool.getRecommendTagName().equals(tagName)) {
                buySource = buySource.replace(IAlbumConfig.BUY_SOURCE_NEED_REPLACE, "rec");
            } else {
                buySource = buySource.replace(IAlbumConfig.BUY_SOURCE_NEED_REPLACE, this.mInfoModel.getDataTagId());
            }
            setBuySource(buySource);
        }
    }

    private void preparePingbackFrom() {
        if (10009 == this.mInfoModel.getChannelId()) {
            setChannelFromInfo("hotlist");
        }
    }

    private void prepareMultiMenuLocation(Tag tag) {
        String tagName = tag.getName();
        if ((IAlbumConfig.STR_RECENT_UPDATE.equals(tagName) || IAlbumConfig.STR_RECENT_HOT.equals(tagName)) && getMenuView() != null) {
            ((MultiMenuPanel) getMenuView()).selectTargetTag(IAlbumConfig.STR_RECENT_UPDATE.equals(tagName) ? "4;sort" : "11;sort", false);
        }
    }

    private void prepareSwitchWay(AlbumBaseRightFragment fragment) {
        Message m;
        if (this.mLastRightFragment != null && (this.mLastRightFragment instanceof ChannelGridBaseFragment) && (fragment instanceof ChannelGridBaseFragment)) {
            m = this.mBaseHandler.obtainMessage();
            m.what = 51;
            handlerMessage2Right(m);
        } else if (this.mLastRightFragment != null && (this.mLastRightFragment instanceof ChannelCardBaseFragment) && (fragment instanceof ChannelCardBaseFragment)) {
            m = this.mBaseHandler.obtainMessage();
            m.what = 51;
            handlerMessage2Right(m);
        } else if (this.mLastRightFragment != null && (this.mLastRightFragment instanceof ChannelRecommendBaseFragment) && (fragment instanceof ChannelRecommendBaseFragment)) {
            m = this.mBaseHandler.obtainMessage();
            m.what = 51;
            handlerMessage2Right(m);
        } else {
            this.mLastRightFragment = fragment;
            replaceFragment(fragment);
        }
    }

    protected void onNetChanged() {
        log(NOLOG ? null : "--onNetChanged---mNeedReloadAfterNetChange=" + this.mNeedReloadAfterNetChange);
        if (this.mNeedReloadAfterNetChange) {
            this.mNeedReloadAfterNetChange = false;
            this.mIsReloadAfterNetChange = true;
            if (AlbumInfoFactory.isSearchResultPage(this.mInfoModel.getPageType())) {
                setDataApi(new SearchResultApi(this.mInfoModel));
            }
            this.mLabelTagList.clear();
            loadData();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        this.mLastKeyCode = event.getKeyCode();
        if ((this.mLastKeyCode == 19 || this.mLastKeyCode == 20 || this.mLastKeyCode == 21 || this.mLastKeyCode == 22) && isShowingCacheData() && AlbumInfoFactory.isSearchResultPage(this.mInfoModel.getPageType()) && !isLeftFragmentHasData()) {
            return true;
        }
        if (!(this.mLastKeyCode != 22 || isLeftFragmentHasData() || this.mSelectLayout == null || this.mSelectLayout.getSelectItem() == null || this.mSelectLayout.getSelectItem().getVisibility() != 0)) {
            setFeedbackPanelFocus(this.mSelectLayout.getSelectItem());
        }
        if (this.mLastKeyCode == 82) {
            setMenu2Activity();
        }
        return super.dispatchKeyEvent(event);
    }

    protected String getLogCatTag() {
        return IAlbumConfig.UNIQUE_CHANNEL_LEFT;
    }

    public void onFocusLost(ViewGroup v, ViewHolder holder) {
        int first = this.mLeftLabel.getFirstAttachedPosition();
        int last = this.mLeftLabel.getLastAttachedPosition();
        this.mLeftLabel.setFocusPosition(this.mLastDataPosition, true);
        if (this.mLastDataPosition < first || this.mLastDataPosition > last) {
            this.mLabelAdapter.notifyDataSetChanged();
        }
    }

    public void onItemFocusChanged(ViewGroup v, ViewHolder holder, boolean isSelected) {
        if (this.mLastKeyCode != 21 || !this.mInfoModel.isMultiHasData()) {
            int position = holder.getLayoutPosition();
            View itemView = holder.itemView;
            if (itemView != null) {
                zoomAnimation(itemView, isSelected);
                if (position == 0 && this.mSelectType == 0 && isSelected) {
                    setNextFocusUpId(this.mLeftLabel);
                }
                if (isSelected) {
                    if (this.mLabelAdapter.getSelect() == position) {
                        this.mLabelAdapter.setSelectDefault();
                    }
                    setGlobalLastFocusView(itemView);
                    prepareRefresh(position);
                    return;
                }
                this.mLabelAdapter.setSelect(this.mLastDataPosition);
                this.mLabelAdapter.notifyDataSetUpdate();
                this.mBaseHandler.removeMessages(111);
            }
        } else if (this.mSelectLayout != null && this.mSelectLayout.getSelectItem() != null) {
            this.mSelectLayout.getSelectItem().requestFocus();
        }
    }

    public void onItemClick(ViewGroup v, ViewHolder holder) {
        if (this.mLastDataPosition != holder.getLayoutPosition()) {
            this.mRefreshImmediately = true;
            this.mBaseHandler.removeMessages(111);
            prepareRefresh(holder.getLayoutPosition());
        } else if (isRightFragmentHasData()) {
            KeyEventUtils.simulateKeyEvent(22);
        }
    }

    public void onItemRecycled(ViewGroup v, ViewHolder holder) {
    }
}
