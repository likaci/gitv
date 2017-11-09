package com.gala.video.app.epg.home.data.hdata.task;

import android.os.Process;
import com.gala.video.app.epg.home.data.bus.HomeDataObservable;
import com.gala.video.app.epg.home.data.hdata.DataRequestRouter;
import com.gala.video.app.epg.home.data.hdata.HomeDataType;
import com.gala.video.app.epg.home.data.provider.TabProvider;
import com.gala.video.app.epg.home.data.tool.TabModelManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.uikit.cache.UikitDataCache;
import com.gala.video.lib.share.uikit.cache.UikitDataCacheSecurity;
import com.gala.video.lib.share.utils.Precondition;
import java.util.List;

public class HomePageInitTask extends BaseRequestTask {
    private static final String TAG = "home/HomePageInitTask";
    private static HomePageInitTask sInstance = new HomePageInitTask();
    private MyObserver DeviceResultObserver;
    private HomeDataObservable mHomeDataObservable;
    private int mLoadPage;
    private TabProvider mTabProvider;

    public static HomePageInitTask getInstance() {
        return sInstance;
    }

    public HomePageInitTask(int loadPage) {
        this();
        this.mLoadPage = loadPage;
    }

    public HomePageInitTask() {
        this.mLoadPage = -1;
        this.DeviceResultObserver = new MyObserver() {
            public void update(String event) {
                HomePageInitTask.this.startHomeDataRequest();
                GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.DEVICE_CHECK_FISNISHED_EVENT, HomePageInitTask.this.DeviceResultObserver);
            }
        };
        this.mTabProvider = TabProvider.getInstance();
        this.mHomeDataObservable = HomeDataObservable.getInstance();
    }

    public void invoke() {
        Process.setThreadPriority(0);
        LogUtils.d(TAG, "perform HomePageInitTask");
        List tabList = this.mTabProvider.getTabInfo();
        TabModelManager.loadCache();
        if (!UikitDataCacheSecurity.getInstance().isCacheSecurity()) {
            LogUtils.d(TAG, "HomePageInitTask- flag = INVALID");
            UikitDataCacheSecurity.getInstance().clearCache();
            clearCache();
            HomeDataObservable.getInstance().post(HomeDataType.TAB_INFO, WidgetChangeStatus.Default, null);
        } else if (Precondition.isEmpty(tabList)) {
            clearCache();
            UikitDataCacheSecurity.getInstance().setSecurity(false);
            UikitDataCacheSecurity.getInstance().clearCache();
            HomeDataObservable.getInstance().post(HomeDataType.TAB_INFO, WidgetChangeStatus.Default, null);
        } else {
            LogUtils.d(TAG, "HomePageInitTask- read tab info: size " + tabList.size());
            UikitDataCacheSecurity.getInstance().setSecurity(false);
            TabModel targetPageModel = calcPriorityLoadPage(tabList);
            LogUtils.d(TAG, "read and parse page data start target model@" + targetPageModel);
            if (targetPageModel != null) {
                postTabInfoMessage(targetPageModel.getResourceGroupId(), tabList);
            }
        }
    }

    private TabModel calcPriorityLoadPage(List<TabModel> tabList) {
        int index = 0;
        TabModel tabModel = null;
        for (TabModel model : tabList) {
            if ((model.isFocusTab() && this.mLoadPage == -1) || index == this.mLoadPage) {
                tabModel = model;
                break;
            }
            index++;
        }
        if (tabModel == null) {
            return (TabModel) tabList.get(0);
        }
        return tabModel;
    }

    private void postTabInfoMessage(String resId, List<TabModel> models) {
        if (UikitDataCache.getInstance().isPageCached(resId)) {
            this.mTabProvider.setWidgetChangeStatus(models, WidgetChangeStatus.InitChange);
            this.mTabProvider.setTabInfo(models);
            this.mHomeDataObservable.post(HomeDataType.TAB_INFO, WidgetChangeStatus.InitChange, null);
            return;
        }
        clearCache();
        UikitDataCacheSecurity.getInstance().setSecurity(false);
        UikitDataCacheSecurity.getInstance().clearCache();
        HomeDataObservable.getInstance().post(HomeDataType.TAB_INFO, WidgetChangeStatus.Default, null);
    }

    private void clearCache() {
        TabProvider.getInstance().clearTabInfoCache();
    }

    private void startHomeDataRequest() {
        DataRequestRouter.sInstance.startHomeDataRequest();
    }

    public void onOneTaskFinished() {
        LogUtils.d(TAG, "perform HomePageInitTask finished");
        GetInterfaceTools.getDataBus().registerStickySubscriber(IDataBus.DEVICE_CHECK_FISNISHED_EVENT, this.DeviceResultObserver);
    }
}
