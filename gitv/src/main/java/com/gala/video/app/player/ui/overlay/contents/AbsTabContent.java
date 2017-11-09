package com.gala.video.app.player.ui.overlay.contents;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.config.style.IPlayerMenuPanelUIStyle;
import com.gala.video.app.player.utils.UiUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.widget.MyRadioGroup;
import com.gala.video.widget.util.AnimationUtils;

public abstract class AbsTabContent<DataType, ItemType> implements IContent<DataType, ItemType> {
    protected static final boolean IS_ZOOM_ENABLED = Project.getInstance().getControl().isOpenAnimation();
    private final String TAG = ("Player/Ui/AbsTabContent@" + Integer.toHexString(hashCode()));
    protected View mContentView;
    protected Context mContext;
    protected Handler mHandler = new Handler(Looper.myLooper());
    protected boolean mIsPhoneControl = false;
    protected IPlayerMenuPanelUIStyle mUiStyle;

    public AbsTabContent(Context context, IPlayerMenuPanelUIStyle uiStyle) {
        this.mContext = context;
        this.mUiStyle = uiStyle;
    }

    protected void setupMyRadioGroupCommon(MyRadioGroup radioGroup) {
        radioGroup.setTextSize(ResourceUtil.getDimen(R.dimen.dimen_20dp));
        radioGroup.setTextColors(ResourceUtil.getColor(R.color.player_ui_text_color_default), ResourceUtil.getColor(R.color.player_ui_text_color_selected), ResourceUtil.getColor(R.color.player_ui_text_color_focused), ResourceUtil.getColor(R.color.player_ui_rg_text_color_disabled));
        radioGroup.setItemBackground(R.drawable.player_episode_item_bg);
        radioGroup.setDimens(new int[]{ResourceUtil.getDimen(R.dimen.dimen_120dp), ResourceUtil.getDimen(R.dimen.dimen_60dp)});
        radioGroup.setZoomEnabled(IS_ZOOM_ENABLED);
        radioGroup.setChildAutoAlignTop(false);
        radioGroup.setContentSpacing(ResourceUtil.getDimen(R.dimen.dimen_8dp));
        radioGroup.setDividerPadding(this.mContext.getResources().getDimensionPixelSize(R.dimen.player_definition_widget_divider_padding));
        radioGroup.setShowDivider(((0 | 1) | 2) | 4);
        radioGroup.setDividerDrawable(R.drawable.player_radio_item_divider_transparent);
    }

    public void show() {
    }

    public void hide() {
    }

    protected View getControlFocusableChild(View control) {
        if (control instanceof MyRadioGroup) {
            return ((MyRadioGroup) control).getFirstFocusableChild();
        }
        return control;
    }

    protected void setupContentLayout4HorizontalScrollView(MyRadioGroup radioGroup, LinearLayout ll, HorizontalScrollView scrollView) {
        int itemWidth = ResourceUtil.getDimen(R.dimen.dimen_120dp);
        int height = radioGroup.getItemPaddedHeight();
        int zoomWidth = (int) (((float) itemWidth) * (AnimationUtils.getDefaultZoomRatio() - 1.0f));
        int paddingTop = radioGroup.getBgDrawablePaddings().top;
        MarginLayoutParams params = (MarginLayoutParams) ll.getLayoutParams();
        MarginLayoutParams groupParams = (MarginLayoutParams) radioGroup.getLayoutParams();
        params.height = height;
        if (this.mUiStyle.getTabContentBgResId() != 0) {
            Rect tabContentBgPadding = new Rect();
            this.mContext.getResources().getDrawable(this.mUiStyle.getTabContentBgResId()).getPadding(tabContentBgPadding);
            params.topMargin -= tabContentBgPadding.top + paddingTop;
        } else {
            params.topMargin -= paddingTop;
        }
        int itemBgResId = R.drawable.player_episode_item_bg;
        Rect itemBgResIdPadding = new Rect();
        this.mContext.getResources().getDrawable(itemBgResId).getPadding(itemBgResIdPadding);
        int paddingLeft = itemBgResIdPadding.left;
        MarginLayoutParams scrollViewParams = (MarginLayoutParams) scrollView.getLayoutParams();
        scrollViewParams.leftMargin = this.mUiStyle.getTabContentMarginPx() - (zoomWidth + paddingLeft);
        groupParams.leftMargin = zoomWidth + paddingLeft;
        params.topMargin = UiUtils.getDimensionInPx(this.mContext, R.dimen.dimen_35dp);
        groupParams.rightMargin = zoomWidth;
        radioGroup.setLayoutParams(groupParams);
        ll.setLayoutParams(params);
        scrollView.setLayoutParams(scrollViewParams);
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setupHorizontalScrollView: params.height=" + params.height + ", params.topMargin=" + params.topMargin);
        }
    }
}
