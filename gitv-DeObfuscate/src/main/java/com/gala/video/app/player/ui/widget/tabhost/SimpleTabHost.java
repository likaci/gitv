package com.gala.video.app.player.ui.widget.tabhost;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.ui.overlay.UiHelper;
import com.gala.video.app.player.ui.widget.tabhost.ISimpleTabHost.OnTabChangedListener;
import com.gala.video.app.player.ui.widget.tabhost.ISimpleTabHost.OnTabFocusChangedListener;
import com.gala.video.app.player.ui.widget.tabhost.ISimpleTabHostAdapter.OnDataChangedListener;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.widget.util.AnimationUtils;
import com.mcto.ads.internal.net.SendFlag;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.xbill.DNS.WKSRecord.Service;

public class SimpleTabHost extends LinearLayout implements ISimpleTabHost {
    private static final AtomicInteger CONTENT_ID = new AtomicInteger(125125409);
    private static final AtomicInteger INDICATOR_ID = new AtomicInteger(124076833);
    private static final int INITIAL_COUNT = -1;
    private static final int TAB_STYLE_DEFAULT = 1;
    private static final int TAB_STYLE_FOCUSED = 4;
    private static final int TAB_STYLE_SELECTED = 2;
    private static final int TEXT_COLOR_DEFAULT = ResourceUtil.getColor(C1291R.color.player_ui_text_color_default);
    private static final int TEXT_COLOR_FOCUSED = ResourceUtil.getColor(C1291R.color.player_ui_text_color_focused);
    private static final int TEXT_COLOR_SELECTED = ResourceUtil.getColor(C1291R.color.player_ui_text_color_selected);
    private final String TAG;
    private SimpleTabHostAdapter mAdapter;
    private FrameLayout mContentContainer;
    private Context mContext;
    private View mCurContentView;
    private int mCurrentIndex;
    private OnDataChangedListener mDataChangedListener;
    private LinearLayout mIndicatorContainer;
    private List<IndicatorView> mIndicators;
    private OnTabChangedListener mTabChangedListener;
    private OnTabFocusChangedListener mTabFocusChangedListener;
    private int mTabsCount;

    class C15421 implements OnDataChangedListener {
        C15421() {
        }

        public void onDataChanged() {
            SimpleTabHost.this.updateTabs();
        }
    }

    class C15432 implements OnFocusChangeListener {
        C15432() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                ((IndicatorView) SimpleTabHost.this.mIndicators.get(SimpleTabHost.this.mCurrentIndex)).requestFocus();
            }
        }
    }

    class C15443 implements OnKeyListener {
        C15443() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() != 0 || 20 != event.getKeyCode() || event.getRepeatCount() != 0) {
                return false;
            }
            SimpleTabHost.this.mContentContainer.requestFocus(Service.CISCO_FNA);
            return true;
        }
    }

    public class IndicatorView extends FrameLayout {
        Rect bgRect = UiHelper.getBgDrawablePaddings(ResourceUtil.getDrawable(C1291R.drawable.player_episode_item_bg));
        int layoutHeight = ResourceUtil.getDimen(C1291R.dimen.dimen_53dp);
        int layoutWidth;
        int mIndex;
        TextView mText;
        String mTitle;

        public IndicatorView(Context context, int index, String title) {
            super(context);
            this.mIndex = index;
            this.mTitle = title;
            initViews();
            setup();
        }

        public IndicatorView(Context context, AttributeSet attrs, int index, String title) {
            super(context, attrs);
            this.mIndex = index;
            this.mTitle = title;
            initViews();
            setup();
        }

        public IndicatorView(Context context, AttributeSet attrs, int defStyle, int index, String title) {
            super(context, attrs, defStyle);
            this.mIndex = index;
            this.mTitle = title;
            initViews();
            setup();
        }

        private void initViews() {
            setClipChildren(false);
            setClipToPadding(false);
            int textSize = ResourceUtil.getDimen(C1291R.dimen.dimen_20dp);
            if (this.mTitle.length() > 9) {
                this.mTitle = this.mTitle.substring(0, 8) + "...";
                this.layoutWidth = textSize * 15;
            } else {
                this.layoutWidth = (this.mTitle.length() + 6) * textSize;
            }
            LayoutParams layoutLP = new LayoutParams(this.layoutWidth, this.layoutHeight);
            layoutLP.gravity = 17;
            setLayoutParams(layoutLP);
            this.mText = new TextView(SimpleTabHost.this.mContext);
            LayoutParams textLP = new LayoutParams((this.layoutWidth + this.bgRect.left) + this.bgRect.right, (this.layoutHeight + this.bgRect.top) + this.bgRect.bottom);
            textLP.gravity = 17;
            addView(this.mText, textLP);
        }

        private void setup() {
            setFocusable(true);
            setClickable(true);
            setId(SimpleTabHost.INDICATOR_ID.getAndIncrement());
            setDescendantFocusability(393216);
            this.mText.setText(this.mTitle);
            this.mText.setTextSize(0, (float) ResourceUtil.getDimensionPixelSize(C1291R.dimen.dimen_20dp));
            this.mText.setGravity(17);
            this.mText.setTextColor(SimpleTabHost.TEXT_COLOR_DEFAULT);
        }

        public TextView getTextView() {
            return this.mText;
        }

        public boolean dispatchKeyEvent(KeyEvent event) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(SimpleTabHost.this.TAG, "IndicatorView dispatchKeyEvent =" + event);
            }
            boolean isFirstDownEvent = event.getAction() == 0 && event.getRepeatCount() == 0;
            if (!SimpleTabHost.this.mIndicatorContainer.hasFocus() || event.getKeyCode() != 19 || !isFirstDownEvent) {
                return super.dispatchKeyEvent(event);
            }
            if (!LogUtils.mIsDebug) {
                return true;
            }
            LogUtils.m1568d(SimpleTabHost.this.TAG, "IndicatorView dispatchKeyEvent return true");
            return true;
        }
    }

    class TabClickListener implements OnClickListener {
        private final int mTabIndex;

        public TabClickListener(int index) {
            this.mTabIndex = index;
        }

        public void onClick(View v) {
            SimpleTabHost.this.mContentContainer.requestFocus(Service.CISCO_FNA);
        }
    }

    class TabOnFocusChangeListener implements OnFocusChangeListener {
        private final int mTabIndex;

        public TabOnFocusChangeListener(int index) {
            this.mTabIndex = index;
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                SimpleTabHost.this.setCurrentTab(this.mTabIndex);
                SimpleTabHost.this.setIndicatorStyle(this.mTabIndex, 4);
            } else {
                int count = SimpleTabHost.this.mTabsCount;
                boolean focusInnerIndicators = false;
                for (int i = 0; i < count; i++) {
                    if (((IndicatorView) SimpleTabHost.this.mIndicators.get(i)).hasFocus()) {
                        focusInnerIndicators = true;
                    }
                }
                if (focusInnerIndicators) {
                    SimpleTabHost.this.setIndicatorStyle(this.mTabIndex, 1);
                } else {
                    SimpleTabHost.this.setIndicatorStyle(this.mTabIndex, 2);
                }
            }
            if (SimpleTabHost.this.mTabFocusChangedListener != null) {
                SimpleTabHost.this.mTabFocusChangedListener.onTabFocusChanged(v, hasFocus);
            }
        }
    }

    public SimpleTabHost(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleTabHost(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIndicators = new ArrayList();
        this.mTabsCount = -1;
        this.mCurrentIndex = -1;
        this.mDataChangedListener = new C15421();
        this.TAG = "SimpleTabHost@" + Integer.toHexString(hashCode());
        this.mContext = context;
        initContainers();
        initDivider();
    }

    public SimpleTabHost(Context context) {
        this(context, null);
    }

    private void updateTabs() {
        int newSize = this.mAdapter.getCount();
        if (this.mTabsCount < newSize) {
            for (int i = this.mTabsCount; i < newSize; i++) {
                addTab(i);
            }
            if (-1 == this.mCurrentIndex) {
                initTabAndStyle(0);
                setCurrentTab(0);
            }
        }
        this.mTabsCount = newSize;
    }

    private void initContainers() {
        setOrientation(1);
        setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        View.inflate(this.mContext, C1291R.layout.player_layout_simple_tabhost, this);
        this.mIndicatorContainer = (LinearLayout) findViewById(C1291R.id.indicator_container);
        this.mIndicatorContainer.setId(16908307);
        this.mIndicatorContainer.setFocusable(true);
        this.mIndicatorContainer.setClipChildren(false);
        this.mIndicatorContainer.setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_1Q);
        this.mIndicatorContainer.setOnFocusChangeListener(new C15432());
        this.mContentContainer = (FrameLayout) findViewById(C1291R.id.content_container);
        this.mContentContainer.setClipChildren(false);
        this.mContentContainer.setId(16908305);
        setClipChildren(false);
    }

    @TargetApi(14)
    private void initDivider() {
        this.mIndicatorContainer.setDividerDrawable(ResourceUtil.getDrawable(C1291R.drawable.player_tab_divider_drawable));
        this.mIndicatorContainer.setShowDividers(7);
        this.mIndicatorContainer.setDividerPadding(0);
    }

    public void setCurrentTab(int index) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setCurrentTab, index=" + index);
        }
        if (index < 0 || index >= this.mTabsCount) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1571e(this.TAG, ">> setCurrentTab, invalid index=" + index);
            }
        } else if (this.mCurrentIndex != index) {
            initTabAndStyle(index);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "setCurrentTab, mCurrentIndex=" + this.mCurrentIndex);
            }
            if (-1 != this.mCurrentIndex) {
                this.mAdapter.getView(this.mCurrentIndex).setVisibility(8);
                if (!((IndicatorView) this.mIndicators.get(index)).hasFocus()) {
                    ((IndicatorView) this.mIndicators.get(this.mCurrentIndex)).clearFocus();
                    clearAllIndicatorStyle();
                    setIndicatorStyle(index, 2);
                }
            }
            this.mCurrentIndex = index;
            this.mCurContentView = this.mAdapter.getView(this.mCurrentIndex);
            if (this.mCurContentView == null) {
                this.mCurContentView = this.mAdapter.getView(this.mCurrentIndex);
                this.mCurContentView.setId(CONTENT_ID.getAndIncrement());
            }
            if (this.mCurContentView.getParent() == null) {
                this.mContentContainer.addView(this.mCurContentView);
            }
            this.mCurContentView.setVisibility(0);
            if (this.mTabChangedListener != null) {
                this.mTabChangedListener.onTabChanged(this.mCurrentIndex);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "set the same current tab, index=" + index);
        }
    }

    private void clearAllIndicatorStyle() {
        int count = this.mIndicators.size();
        for (int i = 0; i < count; i++) {
            ((IndicatorView) this.mIndicators.get(i)).getTextView().setTextColor(TEXT_COLOR_DEFAULT);
            ((IndicatorView) this.mIndicators.get(i)).setBackgroundResource(0);
        }
    }

    public void setAdapter(SimpleTabHostAdapter adapter) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setAdapter, adapter=" + adapter);
        }
        if (adapter != null) {
            if (this.mAdapter != null) {
                reset();
            }
            this.mAdapter = adapter;
            this.mTabsCount = adapter.getCount();
            adapter.setOnDataChangedListener(this.mDataChangedListener);
            for (int i = 0; i < this.mTabsCount; i++) {
                addTab(i);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1571e(this.TAG, "setAdapter, adapter is null");
        }
    }

    private void reset() {
        clearFocus();
        this.mIndicatorContainer.removeAllViews();
        this.mContentContainer.removeAllViews();
        if (!ListUtils.isEmpty(this.mIndicators)) {
            this.mIndicators.clear();
        }
        this.mTabsCount = -1;
        this.mCurrentIndex = -1;
    }

    private void addTab(int index) {
        addIndicator(index);
        constraintTabWidgetFocuses(index);
    }

    private void addIndicator(int index) {
        IndicatorView indicator = new IndicatorView(this.mContext, index, this.mAdapter.getTitle(index));
        this.mIndicatorContainer.addView(indicator);
        indicator.setOnFocusChangeListener(new TabOnFocusChangeListener(index));
        indicator.setOnClickListener(new TabClickListener(index));
        indicator.setOnKeyListener(new C15443());
        this.mIndicators.add(indicator);
    }

    private void initTabAndStyle(int index) {
        ((IndicatorView) this.mIndicators.get(index)).setBackgroundResource(0);
        ((IndicatorView) this.mIndicators.get(index)).getTextView().setTextColor(TEXT_COLOR_SELECTED);
    }

    private void constraintTabWidgetFocuses(int index) {
        int count = this.mIndicators.size();
        for (int i = 0; i < count; i++) {
            int id;
            View indicator = (View) this.mIndicators.get(i);
            if (i == 0) {
                id = indicator.getId();
            } else {
                id = -1;
            }
            indicator.setNextFocusLeftId(id);
            if (i == count - 1) {
                id = indicator.getId();
            } else {
                id = -1;
            }
            indicator.setNextFocusRightId(id);
            indicator.setNextFocusUpId(getNextFocusUpId());
        }
    }

    public void setOnTabChangedListener(OnTabChangedListener listener) {
        this.mTabChangedListener = listener;
    }

    public void setOnTabFocusChangedListener(OnTabFocusChangedListener listener) {
        this.mTabFocusChangedListener = listener;
    }

    public IndicatorView getIndicatorView() {
        if (ListUtils.isEmpty(this.mIndicators) || this.mCurrentIndex >= this.mIndicators.size()) {
            return null;
        }
        return (IndicatorView) this.mIndicators.get(this.mCurrentIndex);
    }

    private void setIndicatorStyle(int index, int style) {
        TextView indicator_text = ((IndicatorView) this.mIndicators.get(index)).getTextView();
        switch (style) {
            case 1:
                AnimationUtils.zoomOut(indicator_text);
                indicator_text.setTextColor(TEXT_COLOR_DEFAULT);
                indicator_text.setBackgroundResource(0);
                return;
            case 2:
                AnimationUtils.zoomOut(indicator_text);
                indicator_text.setTextColor(TEXT_COLOR_SELECTED);
                indicator_text.setBackgroundResource(0);
                return;
            case 4:
                indicator_text.bringToFront();
                AnimationUtils.zoomIn(indicator_text);
                indicator_text.setTextColor(TEXT_COLOR_FOCUSED);
                indicator_text.setBackgroundResource(C1291R.drawable.share_btn_focus);
                return;
            default:
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(this.TAG, ">>setIndicatorStyle, unhandled style, index=" + index + ", style=" + style);
                    return;
                }
                return;
        }
    }
}
