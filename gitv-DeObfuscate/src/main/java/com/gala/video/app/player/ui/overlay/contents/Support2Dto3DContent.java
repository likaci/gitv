package com.gala.video.app.player.ui.overlay.contents;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout.LayoutParams;
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

public class Support2Dto3DContent extends AbsTabContent<List<Boolean>, Boolean> {
    private final String TAG = ("Player/Ui/Support2Dto3DContent@" + Integer.toHexString(hashCode()));
    private LinearLayout m2dTo3dPanel;
    private Context mContext;
    private IItemListener<Boolean> mItemListener;
    private MyRadioGroup mRG2Dto3D;
    private String mTitle;

    class C15021 implements OnCheckedChangeListener {
        C15021() {
        }

        public void onCheckedChanged(int index) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(Support2Dto3DContent.this.TAG, "onCheckedChanged(2dto3d): index=" + index);
            }
        }

        public void onItemChecked(int index) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(Support2Dto3DContent.this.TAG, "onItemChecked(2dto3d): " + index);
            }
            Support2Dto3DContent.this.mItemListener.onItemClicked(null, index);
        }
    }

    public Support2Dto3DContent(Context context, IPlayerMenuPanelUIStyle uiStyle, String title) {
        super(context, uiStyle);
        this.mTitle = title;
        this.mContext = context;
    }

    private void initViews() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initContentView => inflate");
        }
        this.mContentView = LayoutInflater.from(this.mContext).inflate(C1291R.layout.player_tabpanel_2dto3d, null);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initContentView <= inflate: result=" + this.mContentView);
        }
        initDefinitionWidget(this.mContentView);
    }

    private void initDefinitionWidget(View contentView) {
        this.mRG2Dto3D = (MyRadioGroup) contentView.findViewById(C1291R.id.rg_2dto3d);
        this.m2dTo3dPanel = (LinearLayout) contentView.findViewById(C1291R.id.ll_2d_to_3d);
        setupMyRadioGroupCommon(this.mRG2Dto3D);
        this.mRG2Dto3D.setCornerIconResId(this.mUiStyle.getDefCornerIconResId());
        LayoutParams cornerImgParams = this.mUiStyle.getDefCornerImgLayoutParams();
        if (cornerImgParams != null) {
            this.mRG2Dto3D.setCornerImageParams(cornerImgParams);
        }
        this.mRG2Dto3D.setAutoFocusOnSelection(true);
        if (!IS_ZOOM_ENABLED) {
            Rect contentPadding = this.mRG2Dto3D.getContentPadding();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "initDefinitionWidget: content padding=" + contentPadding);
            }
            MarginLayoutParams params = (MarginLayoutParams) this.mRG2Dto3D.getLayoutParams();
            if (params != null) {
                params.leftMargin -= contentPadding.left;
                this.mRG2Dto3D.setLayoutParams(params);
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
        return this.mRG2Dto3D;
    }

    public List<Boolean> getContentData() {
        return null;
    }

    public void setData(List<Boolean> list) {
    }

    public void setSelection(Boolean curData) {
    }

    public void show() {
        int i = 0;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "onShow");
        }
        super.show();
        Resources resources = this.mContext.getResources();
        List<String> list = Arrays.asList(new String[]{resources.getString(C1291R.string.open_jump_header), resources.getString(C1291R.string.close_jump_header)});
        boolean is2Dto3DEnabled = PlayerAppConfig.is2DTo3DModel();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "init2Dto3DWidget: is2Dto3DEnabled=" + is2Dto3DEnabled);
        }
        MyRadioGroup myRadioGroup = this.mRG2Dto3D;
        if (!is2Dto3DEnabled) {
            i = 1;
        }
        myRadioGroup.setDataSource(list, i);
        this.mRG2Dto3D.setOnCheckedChangedListener(new C15021());
        if (Project.getInstance().getConfig().isEnableHardwareAccelerated()) {
            int size = this.mRG2Dto3D.getChildCount();
            for (int i2 = 0; i2 < size; i2++) {
                this.mRG2Dto3D.getChildAt(i2).setLayerType(2, null);
            }
        }
    }

    public void hide() {
        super.hide();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "onHide()");
        }
        this.m2dTo3dPanel.setVisibility(8);
    }

    public void setItemListener(IItemListener<Boolean> listener) {
        this.mItemListener = listener;
    }
}
