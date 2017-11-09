package com.gala.video.app.player.ui.carousel;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import com.gala.pingback.IPingbackContext;
import com.gala.tvapi.tv2.model.TVChannelCarouselTag;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.player.C1291R;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.AnimationUtil;
import java.util.ArrayList;
import java.util.List;

public class CarouselLabelPanel {
    private static final String TAG_S = "Player/CarouselLabelPanel";
    private String TAG = ("Player/CarouselLabelPanel@" + Integer.toHexString(hashCode()));
    private CarouselLabelAdapter mAdapter;
    private PlayerListContent mContent;
    private Context mContext;
    private TVChannelCarouselTag mDefaultLabel;
    private boolean mIsFullScreen;
    private boolean mIsInvalidList = false;
    private List<TVChannelCarouselTag> mLabelList = new ArrayList();
    private PlayerListListener mOuterPlayerListListener;
    private IPingbackContext mPingbackContext;
    private PlayerListListener mPlayerListListener = new C14551();
    private View mRootView;
    private int mSelectedPosition = -1;
    private int mSpreadPosition = -1;
    private int mState;

    class C14551 implements PlayerListListener {
        C14551() {
        }

        public void onItemFocusChanged(ViewHolder holder, boolean isSelected, TVChannelCarouselTag label, int tag) {
            if (holder != null) {
                int position = holder.getLayoutPosition();
                CarouseLabelListViewItem itemView = holder.itemView;
                TextView labelView = itemView.getLabelView();
                int itemFocusedBgResId = C1291R.drawable.player_carousel_label_focus;
                int itemNormalBgResId = C1291R.drawable.player_carousel_btn_transparent;
                int itemSpreadBgResId = C1291R.drawable.player_carousel_label_spread;
                int normalColor = CarouselLabelPanel.this.mContext.getResources().getColor(C1291R.color.player_ui_text_color_default);
                int spreadColor = CarouselLabelPanel.this.mContext.getResources().getColor(C1291R.color.player_ui_carousel_item_channel_selected);
                int focusColor = CarouselLabelPanel.this.mContext.getResources().getColor(C1291R.color.player_ui_text_color_focused);
                if (isSelected) {
                    itemView.setBackgroundResource(itemFocusedBgResId);
                    labelView.setTextColor(focusColor);
                    boolean isValidLabel = CarouselLabelPanel.this.mSpreadPosition != position && position >= 0 && !ListUtils.isEmpty(CarouselLabelPanel.this.mLabelList) && CarouselLabelPanel.this.mLabelList.size() > position;
                    if (CarouselLabelPanel.this.mOuterPlayerListListener != null && isValidLabel) {
                        CarouselLabelPanel.this.mOuterPlayerListListener.onItemFocusChanged(holder, isSelected, (TVChannelCarouselTag) CarouselLabelPanel.this.mLabelList.get(position), 1);
                    }
                    CarouselLabelPanel.this.mSpreadPosition = position;
                } else {
                    itemView.setBackgroundResource(itemNormalBgResId);
                    labelView.setTextColor(normalColor);
                    if (CarouselLabelPanel.this.mSelectedPosition > -1 && CarouselLabelPanel.this.mSelectedPosition == position) {
                        labelView.setTextColor(spreadColor);
                        itemView.setBackgroundResource(itemSpreadBgResId);
                    }
                }
                AnimationUtil.zoomAnimation(itemView, isSelected, 1.05f, 200, false);
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(CarouselLabelPanel.this.TAG, "onItemFocusChanged isSelected=" + isSelected + ", position=" + position + ", mSelectedPosition=" + CarouselLabelPanel.this.mSelectedPosition);
                }
            }
        }

        public void onItemClick(ViewHolder holder, int tag) {
            if (holder != null) {
                int position = holder.getLayoutPosition();
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(CarouselLabelPanel.this.TAG, "onItemClick position = " + position);
                }
                if (CarouselLabelPanel.this.mOuterPlayerListListener != null) {
                    CarouselLabelPanel.this.mOuterPlayerListListener.onItemClick(holder, 1);
                }
            }
        }

        public void onItemRecycled(ViewHolder holder) {
        }

        public void onListShow(TVChannelCarouselTag label, int tag, boolean isShow) {
        }
    }

    public CarouselLabelPanel(View rootView) {
        this.mRootView = rootView;
        this.mContext = rootView.getContext();
        this.mPingbackContext = (IPingbackContext) this.mContext;
        initView();
    }

    private void initView() {
        this.mContent = (PlayerListContent) this.mRootView.findViewById(C1291R.id.label_content);
        this.mContent.initView();
        this.mContent.setListParams(this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_186dp), this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_100dp), this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_10dp), this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_700dp));
        this.mContent.setBackgroundColor(Color.parseColor("#e5000000"));
        this.mAdapter = new CarouselLabelAdapter(this.mContext);
        this.mContent.setListListener(this.mPlayerListListener);
    }

    public void show() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "show()");
        }
        if (!this.mIsFullScreen) {
            return;
        }
        if (this.mIsInvalidList) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "show() mIsInvalidList=" + this.mIsInvalidList);
            }
            this.mContent.showError(this.mContext.getResources().getString(C1291R.string.carousel_list_error));
        } else if (ListUtils.isEmpty(this.mLabelList)) {
            this.mState = 4;
            this.mContent.showLoading();
        } else {
            refreshLabelList();
            this.mContent.showList(false);
            showSelectedLabel();
            this.mContent.setFocusPosition(this.mSpreadPosition);
            this.mState = 6;
        }
    }

    private void showSelectedLabel() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setSelectedLabel()");
        }
        if (!ListUtils.isEmpty(this.mLabelList) && this.mOuterPlayerListListener != null) {
            if (this.mDefaultLabel == null) {
                TVChannelCarouselTag label = (TVChannelCarouselTag) this.mLabelList.get(0);
                this.mOuterPlayerListListener.onListShow(label, 1, true);
                this.mDefaultLabel = label;
                this.mSpreadPosition = 0;
            } else {
                this.mOuterPlayerListListener.onListShow(this.mDefaultLabel, 1, true);
                this.mSpreadPosition = this.mLabelList.indexOf(this.mDefaultLabel);
            }
            this.mSelectedPosition = this.mSpreadPosition;
            this.mAdapter.setSelectedPosition(this.mSelectedPosition);
        }
    }

    private void refreshLabelList() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "refreshLabelList()");
        }
        this.mAdapter.setAllLabelList(this.mLabelList);
        this.mContent.setAdapter(this.mAdapter);
    }

    public boolean isShow() {
        if (this.mContent != null) {
            return this.mContent.isShown();
        }
        return false;
    }

    public void updateSelectedPosition(boolean selected) {
        if (this.mContent == null || !selected) {
            this.mSelectedPosition = -1;
        } else {
            this.mSelectedPosition = this.mContent.getFocusPosition();
        }
    }

    public boolean isListShow() {
        if (this.mContent != null) {
            return this.mContent.isListShow();
        }
        return false;
    }

    public void updateDefaultLabel() {
        if (!ListUtils.isEmpty(this.mLabelList) && this.mSpreadPosition >= 0 && this.mLabelList.size() > this.mSpreadPosition) {
            this.mDefaultLabel = (TVChannelCarouselTag) this.mLabelList.get(this.mSpreadPosition);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "updateDefaultLabel() mDefaultLabel=" + this.mDefaultLabel + ", name=" + (this.mDefaultLabel == null ? null : this.mDefaultLabel.name));
        }
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "hide()");
        }
        if (this.mContent != null && this.mContent.isShown()) {
            this.mContent.hide();
        }
        this.mState = 5;
        this.mSpreadPosition = -1;
        this.mSelectedPosition = -1;
    }

    public void setAllTagList(List<TVChannelCarouselTag> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setAllTagList() mIsInvalidList=" + this.mIsInvalidList);
        }
        if (ListUtils.isEmpty(this.mLabelList)) {
            if (ListUtils.isEmpty((List) list)) {
                this.mIsInvalidList = true;
            } else {
                this.mLabelList.addAll(list);
            }
            if (this.mState == 4) {
                show();
                return;
            }
            return;
        }
        this.mLabelList.clear();
        this.mLabelList.addAll(list);
    }

    public void setPlayerListListener(PlayerListListener listener) {
        this.mOuterPlayerListListener = listener;
    }

    public void requestFocus() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "requestFocus()");
        }
        if (this.mContent != null) {
            if (this.mSpreadPosition >= 0) {
                this.mContent.setFocusPosition(this.mSpreadPosition);
            } else if (!(this.mDefaultLabel == null || ListUtils.isEmpty(this.mLabelList))) {
                int index = this.mLabelList.indexOf(this.mDefaultLabel);
                this.mContent.setFocusPosition(index);
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(this.TAG, "requestFocus() index=" + index);
                }
            }
            this.mContent.requestFocus();
        }
    }

    public boolean hasFocus() {
        if (this.mContent != null) {
            return this.mContent.hasFocus();
        }
        return false;
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        this.mIsFullScreen = isFullScreen;
    }
}
