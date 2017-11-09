package com.gala.video.app.epg.home.widget.menufloatlayer;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.widget.menufloatlayer.adapter.MenuFloatLayerSettingListAdapter;
import com.gala.video.app.epg.home.widget.menufloatlayer.data.MenuFloatLayerDataProvider;
import com.gala.video.cloudui.CloudUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.network.NetworkStatePresenter;
import com.gala.video.lib.share.utils.ResourceUtil;

public class MenuFloatLayerSettingActivity extends QMultiScreenActivity implements OnItemClickListener, OnItemFocusChangedListener {
    private static final String TAG = "/home/widget/MenuFloatLayerSettingActivity";
    protected MenuFloatLayerSettingListAdapter mAdapter;
    private TextView mCountTextView;
    private VerticalGridView mSettingGridView;

    class C07161 implements OnGlobalLayoutListener {
        C07161() {
        }

        public void onGlobalLayout() {
            if (MenuFloatLayerSettingActivity.this.mSettingGridView.getWidth() != 0 && MenuFloatLayerSettingActivity.this.mSettingGridView.getHeight() != 0) {
                int low = (ResourceUtil.getDimen(C0508R.dimen.dimen_188dp) / 2) + ResourceUtil.getDimen(C0508R.dimen.dimen_67dp);
                MenuFloatLayerSettingActivity.this.mSettingGridView.setFocusPlace(low, MenuFloatLayerSettingActivity.this.mSettingGridView.getHeight() - low);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0508R.layout.epg_home_menu_float_layer_setting_activity);
        initView();
        initAdapter();
        initSettingGridView();
        setData();
    }

    protected void onStart() {
        super.onStart();
        NetworkStatePresenter.getInstance().setContext(this);
    }

    protected void onResume() {
        super.onResume();
        HomePingbackFactory.instance().createPingback(ShowPingback.MENU_FLOAT_LAYER_SETTING_PAGE_SHOW_PINGBACK).addItem("bstp", "1").addItem("qtcurl", "设置").addItem("block", "设置").post();
        this.mAdapter.updateDataList(MenuFloatLayerDataProvider.getSettingListData());
    }

    private void initView() {
        this.mSettingGridView = (VerticalGridView) findViewById(C0508R.id.epg_setting_gridview_layout);
        this.mCountTextView = (TextView) findViewById(C0508R.id.epg_setting_sum_text);
    }

    private void initAdapter() {
        this.mAdapter = new MenuFloatLayerSettingListAdapter(this);
        this.mAdapter.setDataList(MenuFloatLayerDataProvider.getSettingListData());
    }

    private void initSettingGridView() {
        this.mSettingGridView.setNumRows(6);
        this.mSettingGridView.setFocusLoop(true);
        this.mSettingGridView.setFocusLeaveForbidden(115);
        this.mSettingGridView.setFocusMode(1);
        this.mSettingGridView.setScrollRoteScale(0.8f, 1.0f, 2.5f);
        this.mSettingGridView.setPadding(ResourceUtil.getDimen(C0508R.dimen.dimen_22dp), 0, ResourceUtil.getDimen(C0508R.dimen.dimen_4dp), 0);
        int ninePatchBorder = CloudUtils.calcNinePatchBorder(ResourceUtil.getDrawable(C0508R.drawable.share_item_rect_btn_selector));
        this.mSettingGridView.setVerticalMargin(ResourceUtil.getDimen(C0508R.dimen.dimen_16dp) - (ninePatchBorder * 2));
        this.mSettingGridView.setHorizontalMargin(ResourceUtil.getDimen(C0508R.dimen.dimen_16dp) - (ninePatchBorder * 2));
        LogUtils.m1576i(TAG, "ninePatchBorder = ", Integer.valueOf(ninePatchBorder));
        this.mSettingGridView.setContentWidth(ResourceUtil.getDimen(C0508R.dimen.dimen_188dp) + (ninePatchBorder * 2));
        this.mSettingGridView.setContentHeight(ResourceUtil.getDimen(C0508R.dimen.dimen_188dp) + (ninePatchBorder * 2));
        this.mSettingGridView.setVerticalScrollBarEnabled(true);
        this.mSettingGridView.setOnItemFocusChangedListener(this);
        this.mSettingGridView.setOnItemClickListener(this);
        this.mSettingGridView.getViewTreeObserver().addOnGlobalLayoutListener(new C07161());
    }

    private void setData() {
        this.mSettingGridView.setAdapter(this.mAdapter);
        this.mCountTextView.setText(this.mAdapter.getCount() + "个");
    }

    public void onItemClick(ViewGroup viewGroup, ViewHolder viewHolder) {
        this.mAdapter.onClick(viewGroup, viewHolder);
    }

    public void onItemFocusChanged(ViewGroup viewGroup, ViewHolder viewHolder, boolean hasFocus) {
        this.mAdapter.onItemFocusChange(viewGroup, viewHolder, hasFocus);
    }

    protected View getBackgroundContainer() {
        return findViewById(C0508R.id.epg_setting_container);
    }
}
