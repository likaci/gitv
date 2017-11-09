package com.gala.video.app.player.ui.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.tvapi.tools.DateLocalThread;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.utils.DataHelper;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.ResourceUtil;

public class NewsDetaiListViewItem extends AbsDetailListViewItem {
    private int mColorNormalTimeTxt;
    private View mContainerView;
    private TextView mContenTextView;
    private View mDividerView;
    private boolean mIsLitchi = Project.getInstance().getBuild().isLitchi();
    private boolean mIsPlaying = false;
    private TextView mTimeTextView;
    private int mTxtColorFocus;
    private int mTxtColorNormal;
    private int mTxtColorSelect;
    private boolean mWasFocused;

    public NewsDetaiListViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    public NewsDetaiListViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public NewsDetaiListViewItem(Context context) {
        super(context);
        initViews();
    }

    private void adjustDivider() {
        LayoutParams lp = (LayoutParams) this.mDividerView.getLayoutParams();
        int bottomMargin = (this.mInnerPadding * 2) - this.mListItemDividerHeight;
        LogUtils.m1571e(this.TAG, "setItemDivider: bottomMargin" + bottomMargin + "/" + this.mListItemDividerHeight + "/" + this.mInnerPadding);
        lp.bottomMargin = bottomMargin / 2;
        this.mDividerView.setLayoutParams(lp);
        this.mDividerView.setBackgroundResource(this.mListItemDividerDbResId);
        this.mDividerView.setVisibility(0);
    }

    private void initUI() {
        this.mTxtColorFocus = ResourceUtil.getColor(C1291R.color.news_detail_list_text_color_focused);
        this.mTxtColorNormal = ResourceUtil.getColor(C1291R.color.news_detail_list_text_color_default);
        this.mTxtColorSelect = ResourceUtil.getColor(C1291R.color.news_detail_tab_indicator_selected);
        this.mItemFocusedBgResId = C1291R.drawable.share_btn_focus;
        this.mItemNormalBgResId = C1291R.drawable.share_btn_transparent;
        this.mItemHeight = getResources().getDimensionPixelSize(C1291R.dimen.dimen_84dp);
        this.mItemWidth = getResources().getDimensionPixelSize(C1291R.dimen.dimen_431dp);
        this.mListItemDividerHeight = getResources().getDimensionPixelSize(C1291R.dimen.dimen_0dp);
        this.mListItemDividerDbResId = C1291R.drawable.player_news_item_divider;
        this.mColorNormalTimeTxt = ResourceUtil.getColor(C1291R.color.news_detail_time_text_color_default);
    }

    private void initViews() {
        this.TAG = "NewsDetaiListViewItem@" + Integer.toHexString(hashCode());
        initUI();
        View view = LayoutInflater.from(this.mContext).inflate(C1291R.layout.player_news_listview_item, null);
        this.mContenTextView = (TextView) view.findViewById(C1291R.id.tv_content);
        this.mContenTextView.setTextColor(this.mTxtColorNormal);
        this.mTimeTextView = (TextView) view.findViewById(C1291R.id.tv_update_time);
        this.mTimeTextView.setTextColor(this.mColorNormalTimeTxt);
        this.mDividerView = view.findViewById(C1291R.id.view_divider);
        this.mContainerView = view.findViewById(C1291R.id.item_container);
        this.mContainerView.setBackgroundResource(this.mItemNormalBgResId);
        setOnFocusChangeListener(this);
        this.mInnerPadding = getDrawablePadding();
        int mItemWidthPadded = this.mItemWidth;
        int mItemHeightPadded = this.mItemHeight + (this.mInnerPadding * 2);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "mItemWidth/mItemHeight" + this.mItemWidth + "/" + this.mItemHeight);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "mItemWidthPadded/mItemHeightPadded" + mItemWidthPadded + "/" + mItemHeightPadded);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mItemWidthPadded, mItemHeightPadded);
        params.addRule(13);
        addView(view, params);
        int outerH = Math.round(this.mScaleAnimRatio * ((float) mItemHeightPadded));
        int outerW = mItemWidthPadded;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initViews: item w/h=" + mItemWidthPadded + "/" + outerH);
        }
        setLayoutParams(new AbsListView.LayoutParams(outerW, mItemHeightPadded));
        this.mContainerView.setLayoutParams((LayoutParams) this.mContainerView.getLayoutParams());
        adjustDivider();
    }

    public void setPlaying(boolean isPlaying) {
        LogUtils.m1568d(this.TAG, "setPlaying" + isPlaying);
        this.mIsPlaying = isPlaying;
        if (this.mWasFocused) {
            this.mContenTextView.setTextColor(this.mTxtColorFocus);
        } else if (isPlaying) {
            this.mContenTextView.setTextColor(this.mTxtColorSelect);
            this.mTimeTextView.setTextColor(this.mTxtColorSelect);
        } else {
            this.mContenTextView.setTextColor(this.mTxtColorNormal);
            this.mTimeTextView.setTextColor(this.mColorNormalTimeTxt);
        }
    }

    public void setAlbum(Album album) {
        this.mContenTextView.setText(album.tvName);
        this.mTimeTextView.setVisibility(0);
        String time = DataHelper.parseDailyNewsIssueTime(DateLocalThread.getTime(album.initIssueTime));
        LogUtils.m1568d(this.TAG, "setAlbum album.issumeTime=" + time + ",album" + album);
        this.mTimeTextView.setText(time);
    }

    public void onFocusChange(View itemView, boolean hasFocus) {
        Log.d(this.TAG, "onFocusChange" + hasFocus);
        if (hasFocus) {
            this.mContenTextView.setSingleLine(false);
            this.mContenTextView.setTextColor(this.mTxtColorFocus);
            this.mTimeTextView.setTextColor(this.mTxtColorFocus);
            this.mContainerView.setBackgroundResource(this.mItemFocusedBgResId);
            zoomIn(itemView);
        } else if (this.mWasFocused) {
            if (this.mIsPlaying) {
                this.mTimeTextView.setTextColor(this.mTxtColorSelect);
                this.mContenTextView.setTextColor(this.mTxtColorSelect);
            } else {
                this.mTimeTextView.setTextColor(this.mColorNormalTimeTxt);
                this.mContenTextView.setTextColor(this.mTxtColorNormal);
            }
            this.mContainerView.setBackgroundResource(this.mItemNormalBgResId);
            zoomOut(itemView);
        }
        this.mWasFocused = hasFocus;
    }
}
