package com.gala.video.app.epg.home.widget.tabmanager;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.utils.AnimationUtil;

public class TabSortedItemView extends RelativeLayout {
    private static final String TAG = "tabmanager/TabSortedItemView";
    private boolean isLocked = false;
    private Context mContext;
    private TabSortedState mCurTabSortedState = TabSortedState.FOCUS_GUIDE;
    private ImageView mLockLabelImageView;
    private ImageView mTabCoverImageView;
    private TabModel mTabModel;
    private TextView mTabNameView;

    public TabSortedItemView(Context context) {
        super(context);
        init(context);
    }

    public TabSortedItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabSortedItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.epg_tab_manager_tab_moving_item_view, this, true);
        this.mTabNameView = (TextView) findViewById(R.id.epg_tab_moving_item_name_tab);
        this.mTabNameView.setBackgroundResource(R.drawable.share_item_title_uncover_unfocus_bg);
        this.mTabCoverImageView = (ImageView) findViewById(R.id.epg_tab_moving_item_cover_img);
        this.mLockLabelImageView = (ImageView) findViewById(R.id.epg_tab_moving_item_lock);
        setFocusable(true);
        setDescendantFocusability(393216);
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        AnimationUtil.zoomAnimation(this.mTabNameView, gainFocus, 1.1f, 200, false);
        if (gainFocus) {
            this.mTabNameView.setBackgroundResource(R.drawable.epg_tab_manager_tab_moving_green_shape);
            this.mTabNameView.setTextColor(getResources().getColor(R.color.albumview_focus_color));
            this.mTabCoverImageView.setVisibility(0);
            this.mTabCoverImageView.startAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.epg_tab_manager_tab_moving_scale_animation));
            bringToFront();
            return;
        }
        this.mTabNameView.setTextColor(getResources().getColor(R.color.albumview_normal_color));
        this.mTabNameView.setBackgroundResource(R.drawable.share_item_title_uncover_unfocus_bg);
        this.mTabCoverImageView.clearAnimation();
        this.mTabCoverImageView.setVisibility(8);
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

    public void setLocked(boolean isLock) {
        this.isLocked = isLock;
        if (isLock) {
            this.mLockLabelImageView.setVisibility(0);
            this.mTabNameView.setTextColor(getResources().getColor(R.color.tab_bar_manager_text_lock));
            return;
        }
        this.mLockLabelImageView.setVisibility(8);
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public void updateStatus(TabSortedState state) {
        switch (state) {
            case NORMAL:
                this.mTabCoverImageView.setImageDrawable(null);
                this.mCurTabSortedState = TabSortedState.NORMAL;
                return;
            case FOCUS:
                this.mCurTabSortedState = TabSortedState.FOCUS;
                this.mTabCoverImageView.setImageDrawable(null);
                return;
            case FOCUS_GUIDE:
                this.mTabCoverImageView.setImageResource(R.drawable.epg_tab_manager_tab_moving_focus_guide);
                this.mCurTabSortedState = TabSortedState.FOCUS_GUIDE;
                return;
            case FOCUS_ACTIVATED_ARROW_LEFT:
                this.mTabCoverImageView.setImageResource(R.drawable.epg_tab_manager_tab_moving_focus_arrow_left);
                this.mCurTabSortedState = TabSortedState.FOCUS_ACTIVATED_ARROW_LEFT;
                return;
            case FOCUS_ACTIVATED_ARROW_RIGHT:
                this.mTabCoverImageView.setImageResource(R.drawable.epg_tab_manager_tab_moving_focus_arrow_right);
                this.mCurTabSortedState = TabSortedState.FOCUS_ACTIVATED_ARROW_RIGHT;
                return;
            case FOCUS_ACTIVATED_ARROW_LEFT_RIGHT:
                this.mTabCoverImageView.setImageResource(R.drawable.epg_tab_manager_tab_moving_focus_arrow_left_right);
                this.mCurTabSortedState = TabSortedState.FOCUS_ACTIVATED_ARROW_LEFT_RIGHT;
                return;
            default:
                return;
        }
    }

    public TabSortedState getCurTabState() {
        return this.mCurTabSortedState;
    }
}
