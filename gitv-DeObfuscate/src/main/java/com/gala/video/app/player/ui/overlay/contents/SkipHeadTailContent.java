package com.gala.video.app.player.ui.overlay.contents;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.ui.config.style.IPlayerMenuPanelUIStyle;
import com.gala.video.app.player.ui.overlay.contents.IContent.IItemListener;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.widget.MyRadioGroup;
import com.gala.video.widget.MyRadioGroup.OnCheckedChangeListener;
import java.util.Arrays;
import java.util.List;

public class SkipHeadTailContent extends AbsTabContent<List<Boolean>, Boolean> {
    private final String TAG = ("Player/Ui/SkipHeadTailContent@" + Integer.toHexString(hashCode()));
    private int mCheckedDefIndex = -1;
    private Context mContext;
    private HorizontalScrollView mHScrollViewSkipHeadTail;
    private IItemListener mItemListener;
    private MyRadioGroup mRGSkipHeadTail;
    private LinearLayout mSkipHeadTailPanel;
    private String mTitle;

    class C14971 implements OnCheckedChangeListener {
        C14971() {
        }

        public void onCheckedChanged(int index) {
            boolean z;
            boolean z2 = true;
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(SkipHeadTailContent.this.TAG, "onCheckedChanged(skip): index=" + index);
            }
            if (index == 0) {
                z = true;
            } else {
                z = false;
            }
            PlayerAppConfig.setSkipVideoHeaderAndTail(z);
            IItemListener access$100 = SkipHeadTailContent.this.mItemListener;
            if (index != 0) {
                z2 = false;
            }
            access$100.onItemSelected(Boolean.valueOf(z2), index);
        }

        public void onItemChecked(int index) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(SkipHeadTailContent.this.TAG, "onItemChecked(skip): " + index);
            }
            SkipHeadTailContent.this.mItemListener.onItemClicked(Boolean.valueOf(index == 0), index);
            SkipHeadTailContent.this.mCheckedDefIndex = index;
        }
    }

    public SkipHeadTailContent(Context context, IPlayerMenuPanelUIStyle uiStyle, String title) {
        super(context, uiStyle);
        this.mTitle = title;
        this.mContext = context;
    }

    private void initViews() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initContentView => inflate");
        }
        this.mContentView = LayoutInflater.from(this.mContext).inflate(C1291R.layout.player_tabpanel_skipheadtail, null);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initContentView <= inflate: result=" + this.mContentView);
        }
        initDefinitionWidget(this.mContentView);
    }

    private void initDefinitionWidget(View contentView) {
        this.mHScrollViewSkipHeadTail = (HorizontalScrollView) contentView.findViewById(C1291R.id.rg);
        this.mRGSkipHeadTail = (MyRadioGroup) contentView.findViewById(C1291R.id.rg_skipheadtail);
        this.mSkipHeadTailPanel = (LinearLayout) contentView.findViewById(C1291R.id.ll_skipheadtail);
        setupMyRadioGroupCommon(this.mRGSkipHeadTail);
        this.mRGSkipHeadTail.setCornerIconResId(this.mUiStyle.getDefCornerIconResId());
        LayoutParams cornerImgParams = this.mUiStyle.getDefCornerImgLayoutParams();
        if (cornerImgParams != null) {
            this.mRGSkipHeadTail.setCornerImageParams(cornerImgParams);
        }
        this.mRGSkipHeadTail.setAutoFocusOnSelection(true);
        if (!IS_ZOOM_ENABLED) {
            Rect contentPadding = this.mRGSkipHeadTail.getContentPadding();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "initDefinitionWidget: content padding=" + contentPadding);
            }
            MarginLayoutParams params = (MarginLayoutParams) this.mRGSkipHeadTail.getLayoutParams();
            if (params != null) {
                params.leftMargin -= contentPadding.left;
                this.mRGSkipHeadTail.setLayoutParams(params);
            }
        }
    }

    public String getTitle() {
        return this.mTitle;
    }

    public View getView() {
        if (this.mContentView == null) {
            initViews();
        }
        return this.mContentView;
    }

    public View getFocusableView() {
        return this.mRGSkipHeadTail;
    }

    public List<Boolean> getContentData() {
        return null;
    }

    public void setData(List<Boolean> list) {
    }

    public void setSelection(Boolean item) {
        updateSkipHeadAndTail(item.booleanValue());
    }

    public void show() {
        int i = 0;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "onShow");
        }
        super.show();
        Resources resources = this.mContext.getResources();
        List<String> list = Arrays.asList(new String[]{resources.getString(C1291R.string.open_jump_header), resources.getString(C1291R.string.close_jump_header)});
        boolean skipHeaderTail = PlayerAppConfig.shouldSkipVideoHeaderAndTail();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initSkipHeaderTailChoices: " + skipHeaderTail);
        }
        MyRadioGroup myRadioGroup = this.mRGSkipHeadTail;
        if (!skipHeaderTail) {
            i = 1;
        }
        myRadioGroup.setDataSource(list, i);
        this.mRGSkipHeadTail.setOnCheckedChangedListener(new C14971());
        if (Project.getInstance().getConfig().isEnableHardwareAccelerated()) {
            int size = this.mRGSkipHeadTail.getChildCount();
            for (int i2 = 0; i2 < size; i2++) {
                this.mRGSkipHeadTail.getChildAt(i2).setLayerType(2, null);
            }
        }
    }

    public void hide() {
        super.hide();
        LogUtils.m1568d(this.TAG, "onHide()");
        this.mSkipHeadTailPanel.setVisibility(8);
    }

    public void setItemListener(IItemListener listener) {
        this.mItemListener = listener;
    }

    private void updateSkipHeadAndTail(boolean isSkipTail) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "updateSkipHeadAndTail( " + isSkipTail + " )");
        }
        if (this.mRGSkipHeadTail != null) {
            this.mRGSkipHeadTail.setSelection(isSkipTail ? 0 : 1);
        }
    }
}
