package com.gala.video.app.epg.home.data.core;

import com.gala.video.app.epg.home.data.hdata.task.AppOperateRequestTask;
import com.gala.video.app.epg.home.data.hdata.task.AppStoreRequestTask;
import com.gala.video.app.epg.home.data.hdata.task.AppUpgradeCheckNewTask;
import com.gala.video.app.epg.home.data.hdata.task.AppUpgradeCheckTask;
import com.gala.video.app.epg.home.data.hdata.task.BackgroundRequestTask;
import com.gala.video.app.epg.home.data.hdata.task.CarouselRequestTask;
import com.gala.video.app.epg.home.data.hdata.task.ChannelRequestTask;
import com.gala.video.app.epg.home.data.hdata.task.DailyNewsRequestTask;
import com.gala.video.app.epg.home.data.hdata.task.DataRequestRefreshTask;
import com.gala.video.app.epg.home.data.hdata.task.DeviceCheckTask;
import com.gala.video.app.epg.home.data.hdata.task.DynamicRequestTask;
import com.gala.video.app.epg.home.data.hdata.task.ErrorCodeDownLoadTask;
import com.gala.video.app.epg.home.data.hdata.task.ExitOperateImageRequestTask;
import com.gala.video.app.epg.home.data.hdata.task.HomeMenuTask;
import com.gala.video.app.epg.home.data.hdata.task.HomeNetWorkCheckTask;
import com.gala.video.app.epg.home.data.hdata.task.IntelligentSearchDownLoadTask;
import com.gala.video.app.epg.home.data.hdata.task.NetWorkCheckTask;
import com.gala.video.app.epg.home.data.hdata.task.PluginApkUpgradeTask;
import com.gala.video.app.epg.home.data.hdata.task.PromotionAppRequestTask;
import com.gala.video.app.epg.home.data.hdata.task.RecommendQuitApkTask;
import com.gala.video.app.epg.home.data.hdata.task.ScreenSaverOperateImageRequestTask;
import com.gala.video.app.epg.home.data.hdata.task.ServerTimeRequest;
import com.gala.video.app.epg.home.data.hdata.task.StartOperateImageRequestTask;
import com.gala.video.app.epg.home.data.hdata.task.TabInfoRequestTask;
import com.gala.video.app.epg.home.data.hdata.task.TaskAction;
import com.gala.video.app.epg.home.data.hdata.task.TaskAction.Builder;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeDataTaskFactory {
    private static Map<String, TaskAction> TabResourceTasks = new HashMap();

    public static TaskAction makeUpDeviceCheckTaskAction(int taskId) {
        return new Builder(new DeviceCheckTask(taskId), 1, -2).fixedInterval(86400000).build();
    }

    public static TaskAction makeUpDynamicTaskAction() {
        return new Builder(new DynamicRequestTask(), 1, -2).fixedInterval(86400000).build();
    }

    public static TaskAction makeUpNetWorkCheckTaskAction(int taskId) {
        return new Builder(new NetWorkCheckTask(taskId), 1, -1).build();
    }

    public static TaskAction makeUpHomeNetWorkCheckTaskAction() {
        return new Builder(new HomeNetWorkCheckTask(), 1, -1).build();
    }

    public static TaskAction makeUpAppUpgradeTaskAction() {
        return new Builder(new AppUpgradeCheckTask(), 0, -1).build();
    }

    public static TaskAction makeUpNewAppUpgradeTaskAction() {
        return new Builder(new AppUpgradeCheckNewTask(), 0, -1).build();
    }

    public static TaskAction makeUpServerTimeRequestTaskAction() {
        return new Builder(new ServerTimeRequest(), 1, -1).build();
    }

    public static TaskAction makeUpChannelRequestTaskAction() {
        return new Builder(new ChannelRequestTask(null), 0, 3).build();
    }

    public static TaskAction makeUpErrorCodeDownLoadTaskAction() {
        return new Builder(new ErrorCodeDownLoadTask(), 0, -1).build();
    }

    public static TaskAction makeUpAppStoreTaskAction() {
        return new Builder(new AppStoreRequestTask(), 0, 3).build();
    }

    public static TaskAction makeUpAppOperateTaskAction() {
        return new Builder(new AppOperateRequestTask(), 0, 3).build();
    }

    public static TaskAction makeUpRefreshTimeTaskAction(int taskId) {
        return new Builder(new DataRequestRefreshTask(taskId), 1, -1).build();
    }

    public static TaskAction makeUpTabInfoTaskAction(int nextTaskId) {
        return new Builder(new TabInfoRequestTask(nextTaskId), 1, -2).fixedInterval(86400000).build();
    }

    private TabModel findPriorityTab(List<TabModel> tabList) {
        int index = 0;
        TabModel tabModel = null;
        if (ListUtils.isEmpty((List) tabList)) {
            return null;
        }
        for (TabModel model : tabList) {
            if (model.isFocusTab()) {
                tabModel = model;
                break;
            }
            index++;
        }
        if (tabModel == null) {
            tabModel = (TabModel) tabList.get(0);
        }
        return tabModel;
    }

    public static TaskAction makeUpDailyNewsRequestTaskAction() {
        return new Builder(new DailyNewsRequestTask(), 0, 2).build();
    }

    public static TaskAction makeUpScreenSaverOperateImageRequestTaskAction() {
        return new Builder(new ScreenSaverOperateImageRequestTask(1), 0, 2).build();
    }

    public static TaskAction makeUpExitOperateImageRequestTaskAction() {
        return new Builder(new ExitOperateImageRequestTask(1), 0, 2).build();
    }

    public static TaskAction makeUpStartOperateImageRequestTaskAction() {
        return new Builder(new StartOperateImageRequestTask(1), 0, 2).build();
    }

    public static TaskAction makeUpIntelligentSearchTaskAction() {
        return new Builder(new IntelligentSearchDownLoadTask(), 0, -2).fixedInterval(86400000).build();
    }

    public static TaskAction makeUpCarouselChannelTaskAction() {
        return new Builder(new CarouselRequestTask(), 0, 3).build();
    }

    public static TaskAction makeUpBackgroundTaskAction() {
        return new Builder(new BackgroundRequestTask(), 0, 3).delay(HomeDataConfig.THEME_REQUEST_DELAY).build();
    }

    public static TaskAction makeUpHomeMenuTaskAction() {
        return new Builder(new HomeMenuTask(), 0, -2).delay(180000).build();
    }

    public static Map<String, TaskAction> getTabResourceTaskActions() {
        return TabResourceTasks;
    }

    public static TaskAction makeUpRecommendQuitApkAction() {
        return new Builder(new RecommendQuitApkTask(), 0, -2).build();
    }

    public static TaskAction makeUpPromotionAppRequestTaskAction() {
        return new Builder(new PromotionAppRequestTask(), 0, -1).build();
    }

    public static TaskAction makeUpPluginApkUpgradeTaskAction() {
        return new Builder(new PluginApkUpgradeTask(), 0, -1).delay(180000).build();
    }
}
