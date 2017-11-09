package com.gala.video.app.epg.home.widget.menufloatlayer.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import com.gala.video.app.epg.home.widget.menufloatlayer.adapter.MenuFloatLayerAdapter;
import com.gala.video.app.epg.home.widget.menufloatlayer.data.MenuFloatLayerDataProvider;
import com.gala.video.app.epg.home.widget.menufloatlayer.data.MenuFloatLayerItemModel;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.List;

public class MenuFloatLayerWindow extends PopupWindow {
    private static final String TAG = "home/widget/MenuFloatLayerWindow";
    private MenuFloatLayerOnClickCallBack mClickCallBack = new MenuFloatLayerOnClickCallBack() {
        public void onClick(View v) {
            MenuFloatLayerWindow.this.dismiss();
        }
    };
    private MenuFloatLayerLayout mContentView = null;
    private Context mContext = null;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private MenuFloatLayerOnKeyEventCallBack mKeyEventCallBack = new MenuFloatLayerOnKeyEventCallBack() {
        public void onKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == 82 && event.getAction() == 0) {
                MenuFloatLayerWindow.this.dismiss();
            } else if (event.getKeyCode() == 4 && event.getAction() == 0) {
                MenuFloatLayerWindow.this.dismiss();
            } else {
                LogUtils.w(MenuFloatLayerWindow.TAG, "not menu key press down, not back key press down");
            }
        }
    };

    public MenuFloatLayerWindow(Context context) {
        super(context);
        initContext(context);
        initWindow();
        initView();
    }

    private void fetchBottomPreviewData(final View parentView) {
        new Thread8K(new Runnable() {
            public void run() {
                final List<MenuFloatLayerItemModel> models = MenuFloatLayerWindow.this.getModels();
                MenuFloatLayerWindow.this.mHandler.postAtFrontOfQueue(new Runnable() {
                    public void run() {
                        MenuFloatLayerWindow.this.mContentView.setAdapter(new MenuFloatLayerAdapter(MenuFloatLayerWindow.this.mContext, models));
                        MenuFloatLayerWindow.this.showAtLocation(parentView, 8388611, 0, 0);
                    }
                });
            }
        }, "MenuFloatLayerWindow").start();
    }

    private List<MenuFloatLayerItemModel> getModels() {
        List models = MenuFloatLayerDataProvider.getOnLineData();
        if (ListUtils.getCount(models) >= 6) {
            models = models.subList(0, 6);
        }
        if (ListUtils.isEmpty(models)) {
            return MenuFloatLayerDataProvider.getLocalDefaultData();
        }
        return models;
    }

    private void initContext(Context context) {
        this.mContext = context;
    }

    private void initWindow() {
        setWindowLayoutMode(-1, -1);
        setBackgroundDrawable(null);
        setFocusable(true);
    }

    private void initView() {
        this.mContentView = new MenuFloatLayerLayout(this.mContext);
        LayoutParams layoutParams = this.mContentView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-1, -1);
        }
        layoutParams.height = -1;
        layoutParams.width = -1;
        this.mContentView.setLayoutParams(layoutParams);
        this.mContentView.setBackgroundColor(Color.parseColor("#00000000"));
        this.mContentView.setOnKeyEventCallBack(this.mKeyEventCallBack);
        this.mContentView.setOnClickEventCallBack(this.mClickCallBack);
        setContentView(this.mContentView);
    }

    public void dismiss() {
        super.dismiss();
    }

    public void show(View parentView) {
        fetchBottomPreviewData(parentView);
    }
}
