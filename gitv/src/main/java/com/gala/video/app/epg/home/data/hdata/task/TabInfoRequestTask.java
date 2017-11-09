package com.gala.video.app.epg.home.data.hdata.task;

import android.os.Process;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.TabInfo;
import com.gala.tvapi.tv2.model.Tconts;
import com.gala.tvapi.tv2.result.ApiResultTabInfo;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.home.data.bus.HomeDataObservable;
import com.gala.video.app.epg.home.data.hdata.HomeDataType;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.provider.TabProvider;
import com.gala.video.app.epg.home.data.tool.TabModelManager;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.cache.UikitDataCache;
import com.gala.video.lib.share.utils.Precondition;
import java.util.ArrayList;
import java.util.List;
import org.cybergarage.soap.SOAP;

public class TabInfoRequestTask extends BaseRequestTask {
    private static final int TAB_MAX_STATIC = 4;
    private static final String TAG = "home/TabInfoTask";
    private WidgetChangeStatus mStatus;

    public TabInfoRequestTask(int taskId) {
        this.mTaskId = taskId;
    }

    public void invoke() {
        LogUtils.d(TAG, "invoke TabInfoRequestTask");
        Process.setThreadPriority(0);
        this.mStatus = WidgetChangeStatus.NoChange;
        buildTabInfo();
    }

    private void buildTabInfo() {
        TVApi.tabInfo.callSync(new IApiCallback<ApiResultTabInfo>() {
            public void onSuccess(ApiResultTabInfo apiResultTabInfo) {
                TabInfoRequestTask.this.buildTabPageInfoList(apiResultTabInfo);
                TabInfoRequestTask.this.sendTaskFinishedPingBack();
            }

            public void onException(ApiException e) {
                String str;
                LogUtils.e(TabInfoRequestTask.TAG, "Get tab info failed");
                IHomePingback addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008");
                String str2 = "pfec";
                if (e == null) {
                    str = "";
                } else {
                    str = e.getCode();
                }
                addItem = addItem.addItem(str2, str);
                str2 = Keys.ERRURL;
                if (e == null) {
                    str = "";
                } else {
                    str = e.getUrl();
                }
                addItem = addItem.addItem(str2, str).addItem(Keys.APINAME, "tabInfo");
                str2 = Keys.ERRDETAIL;
                if (e == null) {
                    str = "";
                } else {
                    str = e.getMessage();
                }
                addItem.addItem(str2, str).addItem("activity", "HomeActivity").addItem(Keys.T, "0").setOthersNull().post();
            }
        }, new String[0]);
    }

    private void buildTabPageInfoList(ApiResultTabInfo apiResultTabInfo) {
        if (apiResultTabInfo != null && apiResultTabInfo.getData() != null) {
            List<TabInfo> tabList = apiResultTabInfo.getData();
            UikitDataCache cache = UikitDataCache.getInstance();
            if (tabList.size() > 0) {
                List tabInfoList = new ArrayList();
                List<TabModel> tabHideInfoList = new ArrayList();
                TabInfo info = (TabInfo) tabList.get(0);
                if (!(info == null || Precondition.isEmpty(info.tconts))) {
                    TabModel tabInfo;
                    String[] ids;
                    boolean isHasFocus = false;
                    for (Tconts cont : info.tconts) {
                        if (cont.type1 == 7) {
                            if (Project.getInstance().getControl().isOpenCarousel() || cont.chnId != 1000005) {
                                if ("轮播".equals(cont.name)) {
                                    LogUtils.d(TAG, "buildTabPageInfoList, get tab info, carousel tab channel id: " + cont.chnId);
                                }
                                tabInfo = new TabModel();
                                tabInfo.setTitle(cont.name);
                                tabInfo.setChannelId(cont.chnId);
                                tabInfo.setId(cont.id);
                                tabInfo.setIsVipTab(cont.isVipTab);
                                tabInfo.setIsSupportSort(cont.isSupportSort == 1);
                                if (!Precondition.isEmpty(cont.type2)) {
                                    ids = cont.type2.split(",");
                                    if (!Precondition.isEmpty(ids)) {
                                        tabInfo.setResourceGroupId(ids[0]);
                                    }
                                }
                                if (cont.isFocusTab != 1 || isHasFocus) {
                                    tabInfo.setIsFocusTab(false);
                                } else {
                                    isHasFocus = true;
                                    tabInfo.setIsFocusTab(true);
                                }
                                tabInfoList.add(tabInfo);
                                cache.addHomeSourceId(tabInfo.getResourceGroupId());
                            } else {
                                LogUtils.d(TAG, "buildTabPageInfoList, not support carousel , remove carousel tab");
                            }
                        }
                    }
                    if (!Precondition.isEmpty(tabInfoList)) {
                        boolean isFind = false;
                        int i = tabInfoList.size() - 1;
                        while (i >= 0) {
                            TabModel t = (TabModel) tabInfoList.get(i);
                            if (isFind) {
                                t.setIsSupportSort(false);
                                if (i >= 4) {
                                    tabInfoList.remove(i);
                                }
                            } else {
                                isFind = !t.isSupportSort();
                                if (isFind && i >= 4) {
                                    tabInfoList.remove(i);
                                }
                            }
                            i--;
                        }
                    }
                    if (!(isHasFocus || Precondition.isEmpty(tabInfoList))) {
                        if (tabInfoList.size() >= 1) {
                            ((TabModel) tabInfoList.get(1)).setIsFocusTab(true);
                        } else {
                            ((TabModel) tabInfoList.get(0)).setIsFocusTab(true);
                        }
                    }
                    if (!Precondition.isEmpty(info.tconts2)) {
                        for (Tconts cont2 : info.tconts2) {
                            if (cont2.type1 == 7) {
                                if (Project.getInstance().getControl().isOpenCarousel() || cont2.chnId != 1000005) {
                                    if ("轮播".equals(cont2.name)) {
                                        LogUtils.d(TAG, "buildTabPageInfoList, get hide tab info, carousel tab channel id: " + cont2.chnId);
                                    }
                                    tabInfo = new TabModel();
                                    tabInfo.setTitle(cont2.name);
                                    tabInfo.setChannelId(cont2.chnId);
                                    tabInfo.setId(cont2.id);
                                    tabInfo.setIsVipTab(cont2.isVipTab);
                                    tabInfo.setIsSupportSort(true);
                                    tabInfo.setIsAlternative(true);
                                    if (!Precondition.isEmpty(cont2.type2)) {
                                        ids = cont2.type2.split(",");
                                        if (!Precondition.isEmpty(ids)) {
                                            tabInfo.setResourceGroupId(ids[0]);
                                        }
                                    }
                                    tabHideInfoList.add(tabInfo);
                                    cache.addHomeSourceId(tabInfo.getResourceGroupId());
                                } else {
                                    LogUtils.d(TAG, "buildTabPageInfoList, get hide tab info, not support carousel , remove carousel tab");
                                }
                            }
                        }
                    }
                }
                TabModelManager.processRaw(tabInfoList, tabHideInfoList);
                checkTabInfoChange(TabProvider.getInstance().readTabInfoFromCache(), tabInfoList);
                TabProvider.getInstance().setTabInfo(tabInfoList);
                TabProvider.getInstance().setTabHideInfo(tabHideInfoList);
            }
        }
    }

    private void checkTabInfoChange(List<TabModel> pageInfoListCache, List<TabModel> pageInfoListNew) {
        if (Precondition.isEmpty((List) pageInfoListNew)) {
            this.mStatus = WidgetChangeStatus.NoData;
            return;
        }
        int newLength = pageInfoListNew.size();
        LogUtils.d(TAG, "new tab info list size=" + newLength);
        if (Precondition.isEmpty((List) pageInfoListCache)) {
            this.mStatus = WidgetChangeStatus.InitChange;
            for (TabModel model : pageInfoListNew) {
                model.setWidgetChangeStatus(WidgetChangeStatus.InitChange);
            }
            return;
        }
        int cacheLength = pageInfoListCache.size();
        LogUtils.d(TAG, "cache tab info list size=" + cacheLength);
        List<String> checkFlags = new ArrayList();
        for (int i = 0; i < newLength; i++) {
            TabModel newTab = (TabModel) pageInfoListNew.get(i);
            boolean isNew = true;
            for (int j = 0; j < cacheLength; j++) {
                TabModel cacheTab = (TabModel) pageInfoListCache.get(j);
                if (isSameTabs(cacheTab, newTab)) {
                    isNew = false;
                    if (i != j) {
                        newTab.setIsPlaceChanged(true);
                        if (newTab.isVipTab() != cacheTab.isVipTab()) {
                            this.mStatus = WidgetChangeStatus.TabLayoutChange;
                            newTab.setWidgetChangeStatus(WidgetChangeStatus.TabLayoutChange);
                            LogUtils.d(TAG, "tab layout change: is same tab but i!=j");
                        } else {
                            newTab.setWidgetChangeStatus(WidgetChangeStatus.TabDataChange);
                            if (this.mStatus == WidgetChangeStatus.NoChange) {
                                this.mStatus = WidgetChangeStatus.TabDataChange;
                            }
                        }
                        if (!checkFlags.contains(cacheTab.getResourceGroupId())) {
                            checkFlags.add(cacheTab.getResourceGroupId());
                        }
                    } else {
                        if (newTab.isVipTab() != cacheTab.isVipTab()) {
                            this.mStatus = WidgetChangeStatus.TabLayoutChange;
                            newTab.setWidgetChangeStatus(WidgetChangeStatus.TabLayoutChange);
                            LogUtils.d(TAG, "tab layout change: is same tab but i==j");
                        }
                        if (!checkFlags.contains(cacheTab.getResourceGroupId())) {
                            checkFlags.add(cacheTab.getResourceGroupId());
                        }
                    }
                } else {
                    if (isLayoutChanged(cacheTab, newTab)) {
                        isNew = false;
                        newTab.setWidgetChangeStatus(WidgetChangeStatus.TabLayoutChange);
                        if (this.mStatus != WidgetChangeStatus.TabLayoutChange) {
                            this.mStatus = WidgetChangeStatus.TabLayoutChange;
                            LogUtils.d(TAG, "tab layout change: is layout change");
                        }
                        if (!checkFlags.contains(cacheTab.getResourceGroupId())) {
                            checkFlags.add(cacheTab.getResourceGroupId());
                        }
                    }
                    if (isDataChanged(cacheTab, newTab)) {
                        isNew = false;
                        if (newTab.isVipTab() != cacheTab.isVipTab()) {
                            this.mStatus = WidgetChangeStatus.TabLayoutChange;
                            newTab.setWidgetChangeStatus(WidgetChangeStatus.TabLayoutChange);
                            LogUtils.d(TAG, "tab layout change: is data change but vip is changed");
                        } else {
                            newTab.setWidgetChangeStatus(WidgetChangeStatus.TabDataChange);
                            if (this.mStatus == WidgetChangeStatus.NoChange) {
                                this.mStatus = WidgetChangeStatus.TabDataChange;
                            }
                        }
                        if (!checkFlags.contains(cacheTab.getResourceGroupId())) {
                            checkFlags.add(cacheTab.getResourceGroupId());
                        }
                    }
                }
            }
            if (isNew) {
                newTab.setWidgetChangeStatus(WidgetChangeStatus.TabLayoutChange);
                newTab.setIsNew(true);
                if (this.mStatus != WidgetChangeStatus.TabLayoutChange) {
                    this.mStatus = WidgetChangeStatus.TabLayoutChange;
                    LogUtils.d(TAG, "tab layout change: new tab");
                }
            }
        }
        if (checkFlags.size() < cacheLength) {
            this.mStatus = WidgetChangeStatus.TabLayoutChange;
            LogUtils.d(TAG, "tab layout change: delete tab");
        }
    }

    private boolean isDataChanged(TabModel tabCache, TabModel tabNew) {
        if (!tabCache.getResourceGroupId().equals(tabNew.getResourceGroupId()) || tabCache.getId() == tabNew.getId()) {
            return false;
        }
        return true;
    }

    private boolean isLayoutChanged(TabModel tabCache, TabModel tabNew) {
        if (tabCache.getId() != tabNew.getId() || tabCache.getResourceGroupId().equals(tabNew.getResourceGroupId())) {
            return false;
        }
        return true;
    }

    private boolean isSameTabs(TabModel tabCache, TabModel tabNew) {
        if (tabCache.getId() == tabNew.getId() && tabCache.getResourceGroupId().equals(tabNew.getResourceGroupId())) {
            return true;
        }
        return false;
    }

    public void onOneTaskFinished() {
        LogUtils.d(TAG, "send tab info change event,event = " + this.mStatus);
        HomeDataObservable.getInstance().post(HomeDataType.TAB_INFO, this.mStatus, null);
    }

    private void sendTaskFinishedPingBack() {
        List tabModelList = TabProvider.getInstance().getTabInfo();
        if (!ListUtils.isEmpty(tabModelList)) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < tabModelList.size(); i++) {
                if (i == tabModelList.size() - 1) {
                    result.append("tab_" + ((TabModel) tabModelList.get(i)).getTitle() + SOAP.DELIM + (i + 1));
                } else {
                    result.append("tab_" + ((TabModel) tabModelList.get(i)).getTitle() + SOAP.DELIM + (i + 1) + ",");
                }
            }
            HomePingbackFactory.instance().createPingback(ShowPingback.TAB_BAR_SHOW_PINGBACK).addItem("qtcurl", "tab栏").addItem("block", "tab栏").addItem("e", "").addItem("count", result.toString()).setOthersNull().post();
        }
    }
}
