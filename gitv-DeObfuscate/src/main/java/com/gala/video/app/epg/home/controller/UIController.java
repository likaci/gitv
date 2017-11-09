package com.gala.video.app.epg.home.controller;

import android.content.Context;
import android.text.TextUtils;
import com.gala.video.app.epg.home.component.PageManage;
import com.gala.video.app.epg.home.component.Widget;
import com.gala.video.app.epg.home.data.TabData;
import com.gala.video.app.epg.home.data.provider.TabProvider;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UIController {
    private static final String[] DEFAULT_TAB_NAMES = new String[]{"轮播", "首页", "VIP会员", "电视剧", "电影", "综艺", "动漫", "分类"};
    private static final int MAX_BUILD_THREAD;
    public static final int MAX_TAB_NUM = 16;
    private static final String TAG = "UIController";
    public static final boolean dumpWidgetTreeflag = true;
    private HomeController mHomeController;
    public PageManage[] mPageTabs;
    private int mPriorityPageIndex = 0;
    public int mTotalTabCount = 16;
    private final List<PageManage> removeList = new CopyOnWriteArrayList();

    public interface BuildCallback {
        void onComplete();
    }

    static {
        int i;
        if (HomeDataConfig.LOW_PERFORMANCE_DEVICE) {
            i = 1;
        } else {
            i = 2;
        }
        MAX_BUILD_THREAD = i;
    }

    public UIController(HomeController homeController) {
        this.mHomeController = homeController;
    }

    public int findTabResIdByIndex(String resid) {
        if (TextUtils.isEmpty(resid)) {
            return -1;
        }
        int index = 0;
        while (index < this.mTotalTabCount && index < 16) {
            if (this.mPageTabs[index] != null && this.mPageTabs[index].mTabdata != null && resid.equals(this.mPageTabs[index].mTabdata.getResourceId())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public void createALLTab(Context context) {
        List<TabModel> tabList = TabProvider.getInstance().getTabInfo();
        if (tabList == null) {
            LogUtils.m1571e(TAG, "create tab failed, no tab list!!!");
            return;
        }
        this.mTotalTabCount = tabList.size();
        int pageIndex = 0;
        for (TabModel tab : tabList) {
            createTab(pageIndex, tab, context);
            pageIndex++;
        }
    }

    public void loadData() {
        List<TabModel> tabList = TabProvider.getInstance().getTabInfo();
        if (tabList == null) {
            LogUtils.m1571e(TAG, "create tab failed, no tab list!!!");
            return;
        }
        this.mTotalTabCount = tabList.size();
        int pageIndex = 0;
        TabModel firstLoad = calcPriorityLoadPage(tabList);
        this.mPageTabs[this.mPriorityPageIndex].loadData(firstLoad);
        for (TabModel tab : tabList) {
            if (tab == firstLoad) {
                pageIndex++;
            } else {
                this.mPageTabs[pageIndex].loadData(tab);
                pageIndex++;
            }
        }
    }

    private void createTab(int pageIndex, TabModel tab, Context context) {
        if (pageIndex >= this.mPageTabs.length || pageIndex < 0) {
            LogUtils.m1571e(TAG, "invalid tab index = " + pageIndex + ",tabmodel = " + tab);
            return;
        }
        LogUtils.m1568d(TAG, "create tab info = " + tab);
        this.mPageTabs[pageIndex].mBuilded = false;
        this.mPageTabs[pageIndex].mNoData = false;
        this.mPageTabs[pageIndex].mTabdata = (TabData) CreateInterfaceTools.createModelHelper().convertToDataSource(tab);
        this.mPageTabs[pageIndex].misVip = tab.isVipTab();
        this.mPageTabs[pageIndex].mIsDefault = tab.isFocusTab();
        this.mPageTabs[pageIndex].init(context);
        this.mPageTabs[pageIndex].setActionPolicy(new HomeActionPolicy(this.mHomeController, pageIndex));
    }

    private TabModel calcPriorityLoadPage(List<TabModel> tabList) {
        TabModel tabModel = null;
        int index = 0;
        if (ListUtils.isEmpty((List) tabList)) {
            return null;
        }
        for (TabModel model : tabList) {
            if (model.isFocusTab()) {
                tabModel = model;
                this.mPriorityPageIndex = index;
                break;
            }
            index++;
        }
        if (tabModel == null) {
            tabModel = (TabModel) tabList.get(0);
        }
        LogUtils.m1568d(TAG, "priority page index = " + this.mPriorityPageIndex);
        return tabModel;
    }

    public void updateALLTab(Context context) {
        LogUtils.m1568d(TAG, "updateALLTab");
        updateALLTab(context, TabProvider.getInstance().getTabInfo());
    }

    public void updateALLTab(Context context, List<TabModel> tabList) {
        PageManage[] newTab = new PageManage[16];
        if (tabList == null) {
            LogUtils.m1571e(TAG, "create Tab failed tabList is null");
            return;
        }
        int newIndex;
        for (int pageIndex = 0; pageIndex < this.mTotalTabCount; pageIndex++) {
            PageManage oldTab = this.mPageTabs[pageIndex];
            if (oldTab.mTabdata != null) {
                boolean find = false;
                newIndex = 0;
                for (TabModel tabmodel : tabList) {
                    if (oldTab.mTabdata.getResourceId().equals(tabmodel.getResourceGroupId())) {
                        newTab[newIndex] = oldTab;
                        find = true;
                        break;
                    }
                    newIndex++;
                }
                if (find) {
                    continue;
                } else {
                    LogUtils.m1571e(TAG, "find oldtab " + oldTab.mTabdata.getTitle() + "remove");
                    synchronized (this.removeList) {
                        this.removeList.add(oldTab);
                    }
                }
            }
        }
        newIndex = 0;
        for (TabModel tabmodel2 : tabList) {
            if (newTab[newIndex] != null && newTab[newIndex].mBuilded) {
                this.mPageTabs[newIndex] = newTab[newIndex];
                this.mPageTabs[newIndex].mIsDefault = false;
                ((HomeActionPolicy) this.mPageTabs[newIndex].mActionPolicy).updatePageindex(newIndex);
                LogUtils.m1568d(TAG, " reused old tab: " + tabmodel2.getTitle());
            } else if (tabmodel2.getWidgetChangeStatus() == WidgetChangeStatus.InitChange || tabmodel2.getWidgetChangeStatus() == WidgetChangeStatus.TabLayoutChange) {
                LogUtils.m1568d(TAG, "update create new tab: " + tabmodel2.getTitle());
                this.mPageTabs[newIndex] = new PageManage(newIndex);
                this.mPageTabs[newIndex].init(context);
                this.mPageTabs[newIndex].setActionPolicy(new HomeActionPolicy(this.mHomeController, newIndex));
                this.mPageTabs[newIndex].mIsDefault = tabmodel2.isFocusTab();
                this.mPageTabs[newIndex].misVip = tabmodel2.isVipTab();
                this.mPageTabs[newIndex].mIsDefault = tabmodel2.isFocusTab();
                this.mPageTabs[newIndex].loadData(tabmodel2);
                this.mPageTabs[newIndex].mBuilded = false;
                this.mPageTabs[newIndex].mNoData = false;
                this.mPageTabs[newIndex].mTabdata = (TabData) CreateInterfaceTools.createModelHelper().convertToDataSource(tabmodel2);
            }
            if (!(this.mPageTabs[newIndex].mTabdata == null || this.mPageTabs[newIndex].mTabdata.getTitle() == null)) {
                LogUtils.m1568d(TAG, "index:" + newIndex + "title:" + this.mPageTabs[newIndex].mTabdata.getTitle());
            }
            newIndex++;
        }
        this.mTotalTabCount = tabList.size();
        LogUtils.m1568d(TAG, "mTotalTabCount" + this.mTotalTabCount);
    }

    public void destroyAllTab() {
        for (PageManage tab : this.removeList) {
            destroyTab(tab);
        }
    }

    public void destroyTab(PageManage tab) {
        tab.mTabdata = null;
        tab.mBuilded = false;
    }

    public void createDefaultALLTab(Context context) {
        this.mTotalTabCount = DEFAULT_TAB_NAMES.length;
        for (int pageIndex = 0; pageIndex < DEFAULT_TAB_NAMES.length; pageIndex++) {
            TabData tabdata = new TabData();
            tabdata.setTitle(DEFAULT_TAB_NAMES[pageIndex]);
            if (pageIndex == 1) {
                tabdata.setIsFocusTab(true);
                this.mPageTabs[pageIndex].mIsDefault = true;
            }
            if (pageIndex == 2) {
                tabdata.setIsVipTab(true);
                this.mPageTabs[pageIndex].misVip = true;
            }
            this.mPageTabs[pageIndex].mBuilded = false;
            this.mPageTabs[pageIndex].mNoData = false;
            tabdata.setWidgetChangeStatus(WidgetChangeStatus.InitChange);
            this.mPageTabs[pageIndex].mTabdata = tabdata;
            if (this.mPageTabs[pageIndex].mChild == null) {
                this.mPageTabs[pageIndex].init(context);
                this.mPageTabs[pageIndex].setActionPolicy(new HomeActionPolicy(this.mHomeController, pageIndex));
                if (pageIndex == 1) {
                    this.mPageTabs[pageIndex].createDefault(pageIndex);
                } else {
                    this.mPageTabs[pageIndex].initLoading();
                }
            }
        }
    }

    public void cleanDefault() {
        for (int pageIndex = 0; pageIndex < DEFAULT_TAB_NAMES.length; pageIndex++) {
            PageManage tab = this.mPageTabs[pageIndex];
            tab.mBuilded = false;
            tab.mNoData = false;
            tab.mTabdata = null;
            tab.mIsDefault = false;
        }
    }

    public boolean isStand(Widget item) {
        if (item == null) {
            return false;
        }
        return item.getStandardType();
    }

    public void destroyWidgetTreeIndex(int index) {
        this.mPageTabs[index].destroy();
    }

    public boolean isPageBuildCompleted() {
        for (int i = 0; i < this.mTotalTabCount; i++) {
            if (!this.mPageTabs[i].mBuilded) {
                return false;
            }
        }
        return true;
    }

    public void initPageTabs() {
        this.mPageTabs = new PageManage[16];
        for (int i = 0; i < 16; i++) {
            this.mPageTabs[i] = new PageManage(i);
        }
    }
}
