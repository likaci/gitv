package com.gala.video.lib.share.uikit.view.widget.record;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ViewUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.uikit.contract.RecordItemContract.Presenter;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.utils.LogUtils;
import com.gala.video.lib.share.uikit.view.IViewLifecycle;
import com.gala.video.lib.share.uikit.view.widget.record.LongRecordView.LongHistoryItemModel;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.mcto.ads.internal.net.SendFlag;
import java.util.ArrayList;

public class RecordItemView extends LinearLayout implements IViewLifecycle<Presenter> {
    protected static final int DEFAULT_DURATION = 200;
    protected static final float DEFAULT_SCALE = 1.1f;
    public static final int FIRST_LONG_RECORD_CLICK_TYPE = 10;
    private static final int LONG_HISTORY_VISIBLE_MARGIN = 0;
    public static final int LONG_LONG_RECORD = 3;
    public static final int LONG_RECORD = 2;
    public static final int NO_TAG = -1;
    public static final int ONLY_RECORD = 1;
    public static final int RECORD_ITEM_CLICK_TYPE = 12;
    public static final int SECOND_LONG_RECORD_CLICK_TYPE = 11;
    private String LOG_TAG = "RecordItemView";
    private Context mContext;
    private LongRecordView mFirstLongRecordView;
    protected int mFoucTag = -1;
    private int mHeight;
    private MyObserver mHistoryChangedObserver = new C18484();
    private boolean mIsAlreadySetStyle = false;
    private OnFocusChangeListener mItemFocusChangeListener = new C18441();
    private int mLongHistoryItemHeight;
    private int mMarginLongHistroy;
    private int mNineBorderMargin = 0;
    private OnClickListener mOnClickListener = new C18452();
    private Presenter mPresenter;
    private int mSearchHistoryItemHeight;
    private SearchHistoryView mSearchHistoryView;
    private LongRecordView mSecondLongRecordView;
    private int mViewType;
    private int mWidth;

    class C18441 implements OnFocusChangeListener {
        C18441() {
        }

        public void onFocusChange(View view, boolean hasFocus) {
            if (view == null) {
                LogUtils.m1590e("mItemFocusChangeListener---view== null.return.");
                return;
            }
            LogUtils.m1593i(RecordItemView.this.LOG_TAG, "mItemFocusChangeListener >view.getTag() =  " + view.getTag(), "hasFocus = " + hasFocus);
            AnimationUtil.zoomAnimation(view, hasFocus, 1.1f, 200, true);
            CardFocusHelper mgr = CardFocusHelper.getMgr(RecordItemView.this.getContext());
            if (hasFocus) {
                if (mgr != null) {
                    mgr.viewGotFocus(view);
                }
            } else if (mgr != null) {
                mgr.viewLostFocus(view);
            }
        }
    }

    class C18452 implements OnClickListener {
        C18452() {
        }

        public void onClick(View v) {
            RecordItemView.this.mPresenter.onClick(RecordItemView.this.mViewType, ((Integer) v.getTag()).intValue());
        }
    }

    class C18463 implements Runnable {
        C18463() {
        }

        public void run() {
            RecordItemView.this.requestFocusAgainIfNeeded();
        }
    }

    class C18484 implements MyObserver {

        class C18471 implements Runnable {
            C18471() {
            }

            public void run() {
                LogUtils.m1585d(RecordItemView.this.LOG_TAG, "on receive history change event");
                RecordItemView.this.onResumeUpdate();
            }
        }

        C18484() {
        }

        public void update(String event) {
            RecordItemView.this.post(new C18471());
        }
    }

    public RecordItemView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setChildrenDrawingOrderEnabled(true);
        setClipChildren(false);
        setFocusable(true);
        setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
        this.mContext = context;
        this.LOG_TAG = "item/SearchHistory" + getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
        setOrientation(1);
        this.mMarginLongHistroy = -(this.mNineBorderMargin + 0);
        createViews();
        setViewClickTypeTags();
        registerListeners();
    }

    private void createViews() {
        this.mFirstLongRecordView = createLongHistoryView();
        this.mSecondLongRecordView = createLongHistoryView();
        this.mSearchHistoryView = createSearchHistoryView();
    }

    private LongRecordView createLongHistoryView() {
        LongRecordView longRecordView = new LongRecordView(this.mContext);
        longRecordView.setId(ViewUtils.generateViewId());
        longRecordView.setLayoutParams(new LayoutParams(this.mWidth, this.mLongHistoryItemHeight));
        return longRecordView;
    }

    private SearchHistoryView createSearchHistoryView() {
        SearchHistoryView searchHistoryView = new SearchHistoryView(this.mContext);
        searchHistoryView.setId(ViewUtils.generateViewId());
        searchHistoryView.setLayoutParams(new LayoutParams(this.mWidth, this.mSearchHistoryItemHeight));
        return searchHistoryView;
    }

    private void setViewClickTypeTags() {
        this.mFirstLongRecordView.setTag(Integer.valueOf(10));
        this.mSecondLongRecordView.setTag(Integer.valueOf(11));
        this.mSearchHistoryView.setTag(Integer.valueOf(12));
    }

    private void registerListeners() {
        registerListenerForView(this.mFirstLongRecordView);
        registerListenerForView(this.mSecondLongRecordView);
        registerListenerForView(this.mSearchHistoryView);
    }

    private void registerListenerForView(View itemView) {
        if (itemView != null) {
            itemView.setOnFocusChangeListener(this.mItemFocusChangeListener);
            itemView.setOnClickListener(this.mOnClickListener);
        }
    }

    public void setViewType(int viewType, ItemInfoModel itemInfoModel) {
        com.gala.video.lib.framework.core.utils.LogUtils.m1576i(this.LOG_TAG, "setViewType mViewType = ", Integer.valueOf(this.mViewType), " viewType = ", Integer.valueOf(viewType));
        if (this.mViewType != viewType) {
            this.mViewType = viewType;
            this.mHeight = itemInfoModel.getHeight();
            this.mWidth = itemInfoModel.getWidth();
            setStylesAndTags(itemInfoModel);
            calcItemHeight();
            adjustSize();
            resetFocus();
            remainFocusTag();
            removeAllViews();
            setViewsPlaceAndFocus();
            addViews();
        }
    }

    private void remainFocusTag() {
        if (hasFocus()) {
            this.mFoucTag = ((Integer) findFocus().getTag()).intValue();
        } else {
            this.mFoucTag = -1;
        }
    }

    private void setStylesAndTags(ItemInfoModel itemInfoModel) {
        if (!this.mIsAlreadySetStyle) {
            this.mIsAlreadySetStyle = true;
            String skinEnd = itemInfoModel.getSkinEndsWith();
            String styleNameForLongRecordView = StringUtils.append("longrecordview", skinEnd);
            String styleNameForSearchHistoryView = StringUtils.append("searchrecordview", skinEnd);
            this.mFirstLongRecordView.setStyleByName(styleNameForLongRecordView);
            this.mSecondLongRecordView.setStyleByName(styleNameForLongRecordView);
            this.mSearchHistoryView.setStyleByName(styleNameForSearchHistoryView);
            this.mFirstLongRecordView.setTag(C1632R.id.focus_res_ends_with, skinEnd);
            this.mSecondLongRecordView.setTag(C1632R.id.focus_res_ends_with, skinEnd);
            this.mSearchHistoryView.setTag(C1632R.id.focus_res_ends_with, skinEnd);
        }
    }

    private void calcItemHeight() {
        if (this.mHeight <= 0) {
            com.gala.video.lib.framework.core.utils.LogUtils.m1571e(this.LOG_TAG, "calcHeight -- mHeight<=0--return. mHeight = " + this.mHeight);
        }
        this.mLongHistoryItemHeight = 0;
        this.mSearchHistoryItemHeight = 0;
        switch (this.mViewType) {
            case 1:
                this.mSearchHistoryItemHeight = this.mHeight;
                return;
            case 2:
                this.mLongHistoryItemHeight = ((this.mHeight + (this.mNineBorderMargin * 2)) + 0) / 3;
                this.mSearchHistoryItemHeight = (((this.mHeight * 2) + this.mNineBorderMargin) + 0) / 3;
                return;
            case 3:
                this.mLongHistoryItemHeight = ((this.mHeight + (this.mNineBorderMargin * 2)) + 0) / 3;
                this.mSearchHistoryItemHeight = this.mLongHistoryItemHeight;
                return;
            default:
                return;
        }
    }

    private void adjustSize() {
        LayoutParams firstLongHistoryLayoutParams = (LayoutParams) this.mFirstLongRecordView.getLayoutParams();
        LayoutParams secondLongHistoryLayoutParams = (LayoutParams) this.mSecondLongRecordView.getLayoutParams();
        LayoutParams firstSearchHistorylLayoutParams = (LayoutParams) this.mSearchHistoryView.getLayoutParams();
        firstLongHistoryLayoutParams.width = this.mWidth;
        secondLongHistoryLayoutParams.width = this.mWidth;
        firstSearchHistorylLayoutParams.width = this.mWidth;
        firstLongHistoryLayoutParams.height = this.mLongHistoryItemHeight;
        secondLongHistoryLayoutParams.height = this.mLongHistoryItemHeight;
        firstSearchHistorylLayoutParams.height = this.mSearchHistoryItemHeight;
        this.mSearchHistoryView.adjustTitleViewPlaceAndIconSize(this.mViewType, this.mSearchHistoryItemHeight, this.mWidth);
    }

    private void resetFocus() {
        resetFocusForView(this.mFirstLongRecordView);
        resetFocusForView(this.mSecondLongRecordView);
        resetFocusForView(this.mSearchHistoryView);
    }

    private void setViewsPlaceAndFocus() {
        LayoutParams firstLongHistoryItemLayoutParams = (LayoutParams) this.mFirstLongRecordView.getLayoutParams();
        LayoutParams secondLongHistoryItemLayoutParams = (LayoutParams) this.mSecondLongRecordView.getLayoutParams();
        LayoutParams searchHistoryItemLayoutParams = (LayoutParams) this.mSearchHistoryView.getLayoutParams();
        switch (this.mViewType) {
            case 1:
                searchHistoryItemLayoutParams.setMargins(0, 0, 0, 0);
                return;
            case 2:
                firstLongHistoryItemLayoutParams.setMargins(0, 0, 0, 0);
                searchHistoryItemLayoutParams.setMargins(0, this.mMarginLongHistroy, 0, 0);
                setFocus(this.mFirstLongRecordView, this.mSearchHistoryView);
                return;
            case 3:
                firstLongHistoryItemLayoutParams.setMargins(0, 0, 0, 0);
                secondLongHistoryItemLayoutParams.setMargins(0, this.mMarginLongHistroy, 0, 0);
                searchHistoryItemLayoutParams.setMargins(0, this.mMarginLongHistroy, 0, 0);
                setFocus(this.mFirstLongRecordView, this.mSecondLongRecordView);
                setFocus(this.mSecondLongRecordView, this.mSearchHistoryView);
                return;
            default:
                return;
        }
    }

    private void setFocus(View upLayout, View downLayout) {
        if (upLayout != null && downLayout != null) {
            upLayout.setNextFocusDownId(downLayout.getId());
            downLayout.setNextFocusUpId(upLayout.getId());
        } else if (upLayout == null && downLayout != null) {
            downLayout.setNextFocusUpId(downLayout.getId());
        } else if (upLayout != null && downLayout == null) {
            upLayout.setNextFocusDownId(upLayout.getId());
        }
    }

    private void resetFocusForView(View view) {
        if (view != null) {
            view.setNextFocusDownId(-1);
            view.setNextFocusUpId(-1);
        }
    }

    private void addViews() {
        switch (this.mViewType) {
            case 1:
                addView(this.mSearchHistoryView);
                return;
            case 2:
                addView(this.mFirstLongRecordView);
                addView(this.mSearchHistoryView);
                return;
            case 3:
                addView(this.mFirstLongRecordView);
                addView(this.mSecondLongRecordView);
                addView(this.mSearchHistoryView);
                return;
            default:
                return;
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        post(new C18463());
    }

    private void requestFocusAgainIfNeeded() {
        LogUtils.m1591i(this.LOG_TAG, "requestFocusAgainIfNeeded --mFoucTag = " + this.mFoucTag + " ;mViewType = " + this.mViewType);
        if (this.mFoucTag != -1) {
            switch (this.mViewType) {
                case 1:
                    if (this.mSearchHistoryView != null) {
                        this.mSearchHistoryView.requestFocus();
                        break;
                    }
                    break;
                case 2:
                    if (this.mFoucTag != 12) {
                        if (this.mFirstLongRecordView != null) {
                            this.mFirstLongRecordView.requestFocus();
                            break;
                        }
                    } else if (this.mSearchHistoryView != null) {
                        this.mSearchHistoryView.requestFocus();
                        break;
                    }
                    break;
                case 3:
                    if (this.mFoucTag != 12) {
                        if (this.mFoucTag != 11) {
                            if (this.mFirstLongRecordView != null) {
                                this.mFirstLongRecordView.requestFocus();
                                break;
                            }
                        } else if (this.mSecondLongRecordView != null) {
                            this.mSecondLongRecordView.requestFocus();
                            break;
                        }
                    } else if (this.mSearchHistoryView != null) {
                        this.mSearchHistoryView.requestFocus();
                        break;
                    }
                    break;
            }
            this.mFoucTag = -1;
        }
    }

    public void onBind(Presenter object) {
        object.updateUI(this);
        this.mPresenter = object;
        GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.HISTORY_CLOUD_SYNC_COMPLETED, this.mHistoryChangedObserver);
        GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.HISTORY_DELETE, this.mHistoryChangedObserver);
        GetInterfaceTools.getDataBus().registerSubscriber(IDataBus.HISTORY_CLOUD_SYNC_COMPLETED, this.mHistoryChangedObserver);
        GetInterfaceTools.getDataBus().registerSubscriber(IDataBus.HISTORY_DELETE, this.mHistoryChangedObserver);
    }

    private void onResumeUpdate() {
        if (this.mPresenter != null) {
            this.mPresenter.updateUI(this);
        }
    }

    public void onUnbind(Presenter object) {
        GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.HISTORY_CLOUD_SYNC_COMPLETED, this.mHistoryChangedObserver);
        GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.HISTORY_DELETE, this.mHistoryChangedObserver);
    }

    public void onShow(Presenter object) {
        this.mPresenter = object;
        object.updateUI(this);
    }

    public void onHide(Presenter object) {
    }

    public void setFirstLongHistoryContent(LongHistoryItemModel longHistoryItemModel) {
        if (this.mFirstLongRecordView == null) {
            com.gala.video.lib.framework.core.utils.LogUtils.m1571e(this.LOG_TAG, "setFirstLongHistoryContent --- mFirstLongHistoryItem is null");
        } else if (1 == this.mViewType) {
            com.gala.video.lib.framework.core.utils.LogUtils.m1573e(this.LOG_TAG, "setFirstLongHistoryContent --- current ViewType do not support this method, mViewType =", Integer.valueOf(this.mViewType));
        } else {
            this.mFirstLongRecordView.setData(longHistoryItemModel);
        }
    }

    public void setSecondLongHistoryContent(LongHistoryItemModel longHistoryItemModel) {
        if (this.mSecondLongRecordView == null) {
            com.gala.video.lib.framework.core.utils.LogUtils.m1571e(this.LOG_TAG, "setSecondLongHistoryContent --- mSecondLongHistoryItem is null");
        } else if (this.mViewType != 3) {
            com.gala.video.lib.framework.core.utils.LogUtils.m1573e(this.LOG_TAG, "setSecondLongHistoryContent --- current ViewType do not support this method, mViewType =", Integer.valueOf(this.mViewType));
        } else {
            this.mSecondLongRecordView.setData(longHistoryItemModel);
        }
    }

    public void bringChildToFront(View child) {
        invalidate();
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        if (hasFocus() && !isFocused()) {
            int top = indexOfChild(getFocusedChild());
            if (i == childCount - 1) {
                return top;
            }
            if (i >= top) {
                return i + 1;
            }
        }
        return i;
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (hasFocus()) {
            switch (this.mViewType) {
                case 1:
                    views.add(this.mSearchHistoryView);
                    return;
                case 2:
                    views.add(this.mFirstLongRecordView);
                    views.add(this.mSearchHistoryView);
                    return;
                case 3:
                    views.add(this.mFirstLongRecordView);
                    views.add(this.mSecondLongRecordView);
                    views.add(this.mSearchHistoryView);
                    return;
                default:
                    return;
            }
        }
        views.add(this);
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (17 == direction || 66 == direction) {
            return getDefaultFocusView().requestFocus(direction, previouslyFocusedRect);
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    private View getDefaultFocusView() {
        View view = this.mSearchHistoryView;
        switch (this.mViewType) {
            case 1:
                return this.mSearchHistoryView;
            case 2:
            case 3:
                return this.mFirstLongRecordView;
            default:
                return view;
        }
    }
}
