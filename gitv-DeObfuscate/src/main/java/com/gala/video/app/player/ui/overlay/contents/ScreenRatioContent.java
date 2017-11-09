package com.gala.video.app.player.ui.overlay.contents;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Pair;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScreenRatioContent extends AbsTabContent<List<Pair<Integer, Integer>>, Integer> {
    private final String TAG = ("Player/Ui/ScreenRatioContent@" + Integer.toHexString(hashCode()));
    private int mCheckedIndex = -1;
    private Context mContext;
    private Integer mCurVideoRatio;
    private HorizontalScrollView mHScrollViewScreenRatio;
    private IItemListener<Integer> mItemListener;
    private MyRadioGroup mRGScreenRatio;
    private LinearLayout mScreenRatioPanel;
    private String mTitle;
    private List<Pair<Integer, Integer>> mVideoRatioList = new CopyOnWriteArrayList();

    class C14961 implements OnCheckedChangeListener {
        C14961() {
        }

        public void onCheckedChanged(int index) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(ScreenRatioContent.this.TAG, "onCheckedChanged(screen): index=" + index);
            }
            ScreenRatioContent.this.mItemListener.onItemSelected(((Pair) ScreenRatioContent.this.mVideoRatioList.get(index)).first, index);
        }

        public void onItemChecked(int index) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(ScreenRatioContent.this.TAG, "onItemChecked(screen): " + index);
            }
            ScreenRatioContent.this.mItemListener.onItemClicked(((Pair) ScreenRatioContent.this.mVideoRatioList.get(index)).first, index);
            ScreenRatioContent.this.mCheckedIndex = index;
        }
    }

    public ScreenRatioContent(Context context, IPlayerMenuPanelUIStyle uiStyle, String title) {
        super(context, uiStyle);
        this.mTitle = title;
        this.mContext = context;
    }

    private void initViews() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initContentView => inflate");
        }
        this.mContentView = LayoutInflater.from(this.mContext).inflate(C1291R.layout.player_tabpanel_screenratio, null);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initContentView <= inflate: result=" + this.mContentView);
        }
        initDefinitionWidget(this.mContentView);
    }

    private void initDefinitionWidget(View contentView) {
        this.mHScrollViewScreenRatio = (HorizontalScrollView) contentView.findViewById(C1291R.id.rg);
        this.mRGScreenRatio = (MyRadioGroup) contentView.findViewById(C1291R.id.rg_screenratio);
        this.mScreenRatioPanel = (LinearLayout) contentView.findViewById(C1291R.id.ll_screenratio);
        setupMyRadioGroupCommon(this.mRGScreenRatio);
        this.mRGScreenRatio.setCornerIconResId(this.mUiStyle.getDefCornerIconResId());
        LayoutParams cornerImgParams = this.mUiStyle.getDefCornerImgLayoutParams();
        if (cornerImgParams != null) {
            this.mRGScreenRatio.setCornerImageParams(cornerImgParams);
        }
        this.mRGScreenRatio.setAutoFocusOnSelection(true);
        if (!IS_ZOOM_ENABLED) {
            Rect contentPadding = this.mRGScreenRatio.getContentPadding();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "initDefinitionWidget: content padding=" + contentPadding);
            }
            MarginLayoutParams params = (MarginLayoutParams) this.mRGScreenRatio.getLayoutParams();
            if (params != null) {
                params.leftMargin -= contentPadding.left;
                this.mRGScreenRatio.setLayoutParams(params);
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
        return this.mRGScreenRatio;
    }

    public List<Pair<Integer, Integer>> getContentData() {
        return this.mVideoRatioList;
    }

    public void setData(List<Pair<Integer, Integer>> data) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setData");
        }
        if (!this.mVideoRatioList.isEmpty()) {
            this.mVideoRatioList.clear();
        }
        this.mVideoRatioList.addAll(data);
    }

    public void setSelection(Integer curData) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setSelection");
        }
        this.mCurVideoRatio = curData;
    }

    public void show() {
        int i;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "onShow");
        }
        super.show();
        Resources resources = this.mContext.getResources();
        List<String> list = new ArrayList();
        for (Pair<Integer, Integer> p : this.mVideoRatioList) {
            list.add(resources.getString(((Integer) p.second).intValue()));
        }
        if (PlayerAppConfig.getStretchPlaybackToFullScreen()) {
            i = 1;
        } else {
            i = 0;
        }
        this.mCheckedIndex = i;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "onShow: list=" + list + ", checkedIndex=" + this.mCheckedIndex);
        }
        this.mRGScreenRatio.setDataSource(list, this.mCheckedIndex);
        this.mRGScreenRatio.setOnCheckedChangedListener(new C14961());
        if (Project.getInstance().getConfig().isEnableHardwareAccelerated()) {
            int size = this.mRGScreenRatio.getChildCount();
            for (int i2 = 0; i2 < size; i2++) {
                this.mRGScreenRatio.getChildAt(i2).setLayerType(2, null);
            }
        }
        this.mScreenRatioPanel.setVisibility(0);
        this.mRGScreenRatio.setVisibility(0);
    }

    public void hide() {
        super.hide();
        this.mScreenRatioPanel.setVisibility(8);
    }

    public void setItemListener(IItemListener<Integer> listener) {
        this.mItemListener = listener;
    }
}
