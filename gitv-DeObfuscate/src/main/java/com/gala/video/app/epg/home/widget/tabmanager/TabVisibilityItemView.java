package com.gala.video.app.epg.home.widget.tabmanager;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.utils.AnimationUtil;

public class TabVisibilityItemView extends RelativeLayout {
    private static final String TAG = "tabmanager/TabVisibilityItemView";
    private Context mContext;
    private TabVisibilityState mCurTabSortedState = TabVisibilityState.NORMAL_NOT_ADDED;
    private boolean mIsAtFirstLine = false;
    private boolean mIsAtLastLine = false;
    private boolean mIsFirstOne = false;
    private boolean mIsLastOne = false;
    private TabModel mTabModel;
    private TextView mTabNameView;
    private ImageView mTagImageView;

    public TabVisibilityItemView(Context context) {
        super(context);
        init(context);
    }

    public TabVisibilityItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabVisibilityItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(C0508R.layout.epg_tab_manager_tab_visibilty_item_view, this, true);
        this.mTabNameView = (TextView) findViewById(C0508R.id.epg_tab_visibility_item_name_tab);
        this.mTabNameView.setBackgroundResource(C0508R.drawable.share_item_title_uncover_unfocus_bg);
        this.mTagImageView = (ImageView) findViewById(C0508R.id.epg_tab_visibility_item_tag);
        setFocusable(true);
        setClipChildren(false);
        setDescendantFocusability(393216);
    }

    public void setText(String name) {
        if (name != null && name.length() > 5) {
            name = name.substring(0, 5);
        }
        this.mTabNameView.setText(name);
    }

    public void setData(TabModel tabModel) {
        this.mTabModel = tabModel;
    }

    public TabModel getData() {
        return this.mTabModel;
    }

    public boolean isAtLastLine() {
        return this.mIsAtLastLine;
    }

    public void setIsAtLastLine(boolean isAtLastLine) {
        this.mIsAtLastLine = isAtLastLine;
    }

    public boolean isAtFirstLine() {
        return this.mIsAtFirstLine;
    }

    public void setIsAtFirstLine(boolean mIsAtFirstLine) {
        this.mIsAtFirstLine = mIsAtFirstLine;
    }

    public boolean isFirstOne() {
        return this.mIsFirstOne;
    }

    public void setIsFirstOne(boolean mIsFirstOne) {
        this.mIsFirstOne = mIsFirstOne;
    }

    public boolean isLastOne() {
        return this.mIsLastOne;
    }

    public void setIsLastOne(boolean mIsLastOne) {
        this.mIsLastOne = mIsLastOne;
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        AnimationUtil.zoomAnimation(this, gainFocus, 1.1f, 200, false);
        if (gainFocus) {
            this.mTabNameView.setBackgroundResource(C0508R.drawable.epg_tab_manager_tab_moving_green_shape);
            this.mTabNameView.setTextColor(getResources().getColor(C0508R.color.albumview_focus_color));
            if (getData().isShown()) {
                setStatus(TabVisibilityState.FOCUS_REMOVABLE);
                return;
            } else {
                setStatus(TabVisibilityState.FOCUS_ADDIBLE);
                return;
            }
        }
        this.mTabNameView.setTextColor(getResources().getColor(C0508R.color.albumview_normal_color));
        this.mTabNameView.setBackgroundResource(C0508R.drawable.share_item_title_uncover_unfocus_bg);
        if (getData().isShown()) {
            setStatus(TabVisibilityState.NORMAL_ADDED);
        } else {
            setStatus(TabVisibilityState.NORMAL_NOT_ADDED);
        }
    }

    public void setStatus(TabVisibilityState state) {
        switch (state) {
            case NORMAL_NOT_ADDED:
                this.mTagImageView.setVisibility(4);
                this.mCurTabSortedState = TabVisibilityState.NORMAL_NOT_ADDED;
                return;
            case NORMAL_ADDED:
                this.mTagImageView.setVisibility(0);
                this.mCurTabSortedState = TabVisibilityState.NORMAL_NOT_ADDED;
                return;
            case FOCUS_ADDIBLE:
                this.mTagImageView.setVisibility(4);
                this.mCurTabSortedState = TabVisibilityState.FOCUS_ADDIBLE;
                return;
            case FOCUS_REMOVABLE:
                this.mTagImageView.setVisibility(0);
                this.mCurTabSortedState = TabVisibilityState.FOCUS_REMOVABLE;
                return;
            default:
                return;
        }
    }

    public TabVisibilityState getCurTabSortedState() {
        return this.mCurTabSortedState;
    }

    public void updateState() {
        if (this.mCurTabSortedState != null) {
            switch (this.mCurTabSortedState) {
                case FOCUS_ADDIBLE:
                    setStatus(TabVisibilityState.FOCUS_REMOVABLE);
                    return;
                case FOCUS_REMOVABLE:
                    setStatus(TabVisibilityState.FOCUS_ADDIBLE);
                    return;
                default:
                    return;
            }
        }
    }
}
