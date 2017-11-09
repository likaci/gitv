package com.gala.video.app.epg.home.widget.tabhost;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import com.gala.tvapi.tv2.constants.ChannelId;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.data.TabData;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.pingback.HomePingbackSender;
import com.gala.video.app.epg.home.data.provider.TabProvider;
import com.gala.video.app.epg.home.view.ViewDebug;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.utils.KeyEventUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.player.CarouselPlayParamBuilder;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.network.NetworkStatePresenter;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.lib.share.utils.TraceEx;
import java.util.ArrayList;
import java.util.List;

public class TabBarAdapter {
    private static final boolean DBG = ViewDebug.DBG;
    private static final int MAX_TOAST_COUNT = 3;
    private static final int RETRY_DELAY = 1000;
    private static final int TAB_HOLDER = R.id.epg_tab_host;
    private static final String TAG = "tabhost/TabBarAdapter";
    private static final int TOAST_DELAY = 500;
    private static int mToastCount = -1;
    private Context mContext;
    private Handler mHandler = new Handler();
    private boolean mHasShow = false;
    private ImageView mIndicatorVew;
    private Drawable mNormalIndicator;
    private TabNameView mSelectedTab;
    private List<TabData> mTabBarItems;
    private List<TabNameView> mTabViews = new ArrayList();
    private QToast mToast;
    private Drawable mVipIndicator;

    private class ToastRunnable implements Runnable {
        private ToastRunnable() {
        }

        public void run() {
            TraceEx.beginSection("TabBarAdapter.toast");
            LogRecordUtils.logd(TabBarAdapter.TAG, "run: mToastCount -> " + TabBarAdapter.mToastCount);
            TabBarAdapter.this.mToast = QToast.makeText(TabBarAdapter.this.mContext, TabBarAdapter.this.mContext.getResources().getString(R.string.tab_click_guide_toast), (int) QToast.LENGTH_4000);
            SystemConfigPreference.saveTabToastCount(TabBarAdapter.this.mContext, TabBarAdapter.access$104());
            TabBarAdapter.this.mHasShow = true;
            TabBarAdapter.this.mToast.show();
            TraceEx.endSection();
        }
    }

    static /* synthetic */ int access$104() {
        int i = mToastCount + 1;
        mToastCount = i;
        return i;
    }

    public TabBarAdapter(Context context, List<TabData> tabBarItems, ImageView indicator) {
        this.mContext = context;
        this.mTabBarItems = tabBarItems;
        this.mIndicatorVew = indicator;
        this.mNormalIndicator = context.getResources().getDrawable(R.drawable.epg_tab_bar_decorated_line);
        this.mVipIndicator = context.getResources().getDrawable(R.drawable.epg_tab_bar_decorated_vip_line);
        if (mToastCount < 0) {
            mToastCount = SystemConfigPreference.getTabToastCount(context);
        }
    }

    public void updateTabName(String name, int index) {
        if (DBG) {
            Log.d(TAG, "update tab name = " + name + ",index = " + index);
        }
        if (index < this.mTabViews.size() && index >= 0) {
            ((TabNameView) this.mTabViews.get(index)).setText(name);
        }
    }

    public void onChildFocusChanged(View v, boolean hasFocus) {
        TabNameView tabNameView = (TabNameView) v;
        Object result = tabNameView.getTag(TAB_HOLDER);
        if (result instanceof TabData) {
            boolean isVip = ((TabData) result).isIsVipTab();
            if (this.mIndicatorVew != null) {
                if (isVip) {
                    this.mIndicatorVew.setImageDrawable(this.mVipIndicator);
                } else {
                    this.mIndicatorVew.setImageDrawable(this.mNormalIndicator);
                }
            }
            LogRecordUtils.logd(TAG, "onChildFocusChanged: mToastCount -> " + mToastCount + ", MAX_TOAST_COUNT -> " + 3);
            if (mToastCount <= 3 && hasFocus) {
                handleTabGuide((TabData) result);
            }
            if (hasFocus) {
                String tabName = ((TabData) result).getTitle();
                PingBackCollectionFieldUtils.setTabName(tabName);
                PingBackCollectionFieldUtils.setTabIndex(getTabIndex(tabName));
            }
        }
        tabNameView.onFocusChange(v, hasFocus);
    }

    private String getTabIndex(String tabName) {
        String index = "0";
        List tabModelList = TabProvider.getInstance().getTabInfo();
        if (ListUtils.isEmpty(tabModelList) || StringUtils.isEmpty((CharSequence) tabName)) {
            return index;
        }
        for (int i = 0; i < tabModelList.size(); i++) {
            if (tabName.equals(((TabModel) tabModelList.get(i)).getTitle())) {
                index = String.valueOf(i + 1);
            }
        }
        return index;
    }

    @TargetApi(16)
    private void setBackground(View v, Drawable drawable) {
        if (VERSION.SDK_INT > 16) {
            v.setBackground(drawable);
        } else {
            v.setBackgroundDrawable(drawable);
        }
    }

    public void onChildSelectChanged(View v, boolean selected) {
        TabNameView tabNameView = (TabNameView) v;
        tabNameView.setSelected(selected);
        if (selected) {
            this.mSelectedTab = tabNameView;
        }
    }

    private void handleTabGuide(TabData tab) {
        this.mHandler.removeCallbacksAndMessages(null);
        if (this.mToast != null && this.mHasShow) {
            this.mToast.hide();
            this.mToast = null;
            mToastCount = 4;
        } else if (mToastCount < 3 && tab != null && tab.isIsChannelTab()) {
            this.mHandler.postDelayed(new ToastRunnable(), 500);
        }
    }

    public void clearChildFocus(View v) {
        ((TabNameView) v).clearFocus();
    }

    public void onClick(View child, int index) {
        if (index < this.mTabBarItems.size()) {
            TabData tabData = (TabData) this.mTabBarItems.get(index);
            if (DBG) {
                Log.d(TAG, "tab clicked channel id = " + tabData.getChannelId() + ",tab name : " + tabData.getTitle());
            }
            if (tabData != null) {
                if (tabData.isIsChannelTab()) {
                    if (NetworkStatePresenter.getInstance().checkStateIllegal()) {
                        String tabIndex = PingBackCollectionFieldUtils.getTabIndex();
                        PingBackCollectionFieldUtils.setIncomeSrc(PingBackCollectionFieldUtils.getTabName() + "_" + tabIndex + "_b_" + tabIndex + "_t_" + tabIndex);
                        String from = tabData.getTitle() + "_tab栏";
                        if (tabData.getChannelId() == ChannelId.CHANNEL_ID_CAROUSEL) {
                            CarouselPlayParamBuilder carouselPlayParamBuilder = new CarouselPlayParamBuilder();
                            carouselPlayParamBuilder.setChannel(null);
                            carouselPlayParamBuilder.setFrom(from);
                            carouselPlayParamBuilder.setTabSource("tab_" + tabData.getTitle());
                            GetInterfaceTools.getPlayerPageProvider().startCarouselPlayerPage(this.mContext, carouselPlayParamBuilder);
                        } else {
                            AlbumUtils.startChannelPage(this.mContext, tabData.getChannelId(), from, "", true);
                        }
                    } else {
                        return;
                    }
                } else if (!StringUtils.isEmpty(tabData.getResourceId())) {
                    KeyEventUtils.simulateKeyEvent(20);
                }
            } else {
                return;
            }
        }
        onClickForPingback();
    }

    public int getCount() {
        return this.mTabBarItems.size();
    }

    private void initTabNameView(TabNameView tabNameView) {
        int left = (int) this.mContext.getResources().getDimension(R.dimen.dimen_20dp);
        int top = (int) this.mContext.getResources().getDimension(R.dimen.dimen_14dp);
        int right = (int) this.mContext.getResources().getDimension(R.dimen.dimen_20dp);
        int bottom = (int) this.mContext.getResources().getDimension(R.dimen.dimen_14dp);
        tabNameView.setGravity(16);
        tabNameView.setPadding(left, 0, right, 0);
        tabNameView.setHeight((int) this.mContext.getResources().getDimension(R.dimen.dimen_54dp));
        tabNameView.setTextSize(0, (float) ResourceUtil.getDimen(R.dimen.dimen_25dp));
        tabNameView.setTextColor(this.mContext.getResources().getColor(R.color.home_tab_name_text_normal));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TabNameView tabNameView = new TabNameView(this.mContext);
        initTabNameView(tabNameView);
        this.mTabViews.add(tabNameView);
        String name = "";
        if (this.mTabBarItems != null && position < this.mTabBarItems.size()) {
            name = ((TabData) this.mTabBarItems.get(position)).getTitle();
            boolean isVip = ((TabData) this.mTabBarItems.get(position)).isIsVipTab();
            tabNameView.setTag(TAB_HOLDER, this.mTabBarItems.get(position));
            if (isVip) {
                tabNameView.setTextShaderColor(this.mContext.getResources().getColor(R.color.home_vip_tab_name_text_focus_shader_start), this.mContext.getResources().getColor(R.color.home_vip_tab_name_text_focus_shader_end));
            } else {
                tabNameView.setTextShaderColor(this.mContext.getResources().getColor(R.color.home_tab_name_text_focus_shader_start), this.mContext.getResources().getColor(R.color.home_tab_name_text_focus_shader_end));
            }
        }
        Object result = tabNameView.getTag(TAB_HOLDER);
        if (result instanceof TabData) {
            if (((TabData) result).isIsVipTab()) {
                setBackground(tabNameView, this.mContext.getResources().getDrawable(R.drawable.epg_tab_bar_special_bg));
            } else {
                setBackground(tabNameView, this.mContext.getResources().getDrawable(R.drawable.epg_tab_bar_normal_bg));
            }
        }
        tabNameView.setText(name);
        MarginLayoutParams layoutParams = (LayoutParams) tabNameView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-2, this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_44dp));
        }
        layoutParams.topMargin = ResourceUtil.getDimen(R.dimen.dimen_7dp);
        layoutParams.bottomMargin = ResourceUtil.getDimen(R.dimen.dimen_6dp);
        MarginLayoutParams marginLayoutParams = layoutParams;
        if (position != 0) {
            layoutParams.leftMargin = ResourceUtil.getDimensionPixelSize(R.dimen.dimen_29dp);
        } else {
            layoutParams.leftMargin = 0;
        }
        tabNameView.setLayoutParams(marginLayoutParams);
        return tabNameView;
    }

    private void onClickForPingback() {
        String tabName = "tab_" + HomePingbackSender.getInstance().getTabName();
        HomePingbackFactory.instance().createPingback(ClickPingback.TAB_BAR_CLICK_PINGBACK).addItem("r", tabName).addItem("rpage", tabName).addItem("block", "tab").addItem("rt", "i").addItem("rseat", tabName).addItem("count", HomePingbackSender.getInstance().getTabIndex()).setOthersNull().post();
    }

    private boolean isVipTab(TabNameView view) {
        Object result = view.getTag(TAB_HOLDER);
        if (result instanceof TabData) {
            return ((TabData) result).isIsVipTab();
        }
        return false;
    }
}
