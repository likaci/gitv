package com.gala.video.app.epg.home.data.provider;

import com.gala.video.app.epg.home.data.bus.HomeDataObservable;
import com.gala.video.app.epg.home.data.hdata.HomeDataType;
import com.gala.video.app.epg.home.data.tool.TabModelManager;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.utils.Precondition;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TabProvider {
    private static final String TAG = "TabProvider";
    public static final int VIP_CHANNEL_ID = 1000002;
    private static TabProvider mInstance = new TabProvider();
    private List<TabModel> mTabHideInfo;
    private List<TabModel> mTabPageInfo;

    private TabProvider() {
    }

    public static TabProvider getInstance() {
        return mInstance;
    }

    public synchronized List<TabModel> getTabInfo() {
        List<TabModel> readTabInfoFromCache;
        if (Precondition.isEmpty(this.mTabPageInfo)) {
            readTabInfoFromCache = readTabInfoFromCache();
        } else {
            readTabInfoFromCache = new ArrayList(this.mTabPageInfo.size());
            for (TabModel info : this.mTabPageInfo) {
                readTabInfoFromCache.add(info);
            }
        }
        return readTabInfoFromCache;
    }

    public int getTabIndexByName(String name) {
        if (StringUtils.isEmpty((CharSequence) name)) {
            return -1;
        }
        List list = getTabInfo();
        if (ListUtils.isEmpty(list)) {
            return -1;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            TabModel tabModel = (TabModel) list.get(i);
            if (tabModel != null && name.equals(tabModel.getTitle())) {
                return i + 1;
            }
        }
        return -1;
    }

    public synchronized List<TabModel> getTabHideInfo() {
        List<TabModel> readTabHideInfoFromCache;
        if (Precondition.isEmpty(this.mTabHideInfo)) {
            readTabHideInfoFromCache = readTabHideInfoFromCache();
        } else {
            readTabHideInfoFromCache = new ArrayList(this.mTabHideInfo.size());
            for (TabModel info : this.mTabHideInfo) {
                readTabHideInfoFromCache.add(info);
            }
        }
        return readTabHideInfoFromCache;
    }

    public synchronized void setTabInfo(List<TabModel> mTabPageInfo) {
        this.mTabPageInfo = mTabPageInfo;
        writeTabInfoToCache(mTabPageInfo);
    }

    public synchronized void setTabHideInfo(List<TabModel> mTabHideInfo) {
        this.mTabHideInfo = mTabHideInfo;
        writeTabHideInfoToCache(mTabHideInfo);
    }

    public List<TabModel> readTabInfoFromCache() {
        List<TabModel> list = null;
        try {
            return (List) SerializableUtils.read(HomeDataConfig.HOME_TAB_INFO_DIR);
        } catch (Exception e) {
            LogUtils.m1572e(TAG, "Read Tab Info From Cache Failed", e);
            return list;
        }
    }

    public List<TabModel> readTabHideInfoFromCache() {
        List<TabModel> list = null;
        try {
            return (List) SerializableUtils.read(HomeDataConfig.HOME_TAB_HIDE_INFO_DIR);
        } catch (Exception e) {
            LogUtils.m1572e(TAG, "Read Tab Hide Info From Cache Failed", e);
            return list;
        }
    }

    public void writeTabInfoToCache(List<TabModel> tabPageInfo) {
        try {
            SerializableUtils.write(tabPageInfo, HomeDataConfig.HOME_TAB_INFO_DIR);
            LogUtils.m1568d(TAG, "Write Tab Info From Cache successful");
        } catch (Exception e) {
            LogUtils.m1571e(TAG, "Write Tab Info From Cache Failed");
        }
    }

    public void writeTabHideInfoToCache(List<TabModel> tabHideInfo) {
        try {
            SerializableUtils.write(tabHideInfo, HomeDataConfig.HOME_TAB_HIDE_INFO_DIR);
            LogUtils.m1568d(TAG, "Write Tab Hide Info From Cache successful");
        } catch (Exception e) {
            LogUtils.m1571e(TAG, "Write Tab Hide Info From Cache Failed");
        }
    }

    public void clearTabInfoCache() {
        String filePath = AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + HomeDataConfig.HOME_TAB_INFO_DIR;
        new File(filePath).delete();
        new File(filePath + File.separator + HomeDataConfig.HOME_TAB_HIDE_INFO_DIR).delete();
    }

    public void setWidgetChangeStatus(List<TabModel> list, WidgetChangeStatus status) {
        if (!Precondition.isEmpty((List) list)) {
            for (TabModel tab : list) {
                tab.setWidgetChangeStatus(status);
            }
        }
    }

    public void updateTabSetting(long delay) {
        final List<TabModel> addedlist = new LinkedList();
        final List<TabModel> removelist = new LinkedList();
        List list = getTabInfo();
        if (!Precondition.isEmpty(list)) {
            if (TabModelManager.process(list, getTabHideInfo())) {
                check(list, addedlist, removelist);
                setTabInfo(list);
                final long j = delay;
                ThreadUtils.execute(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(j);
                        } catch (InterruptedException e) {
                            LogUtils.m1578w(TabProvider.TAG, "updateTabSetting, thread delay interruptedException : e", e);
                        }
                        if (addedlist.isEmpty() && removelist.isEmpty()) {
                            HomeDataObservable.getInstance().post(HomeDataType.TAB_INFO, WidgetChangeStatus.TabOrderChangeManual, null);
                        } else {
                            HomeDataObservable.getInstance().post(HomeDataType.TAB_INFO, WidgetChangeStatus.TabLayoutChangeManual, null);
                        }
                    }
                });
                return;
            }
            HomeDataObservable.getInstance().post(HomeDataType.TAB_INFO, WidgetChangeStatus.TAB_FOCUS_RESET, null);
        }
    }

    private void check(List<TabModel> result, List<TabModel> addedList, List<TabModel> removeList) {
        List<TabModel> oldList = getTabInfo();
        for (TabModel model : result) {
            if (!contain(oldList, model)) {
                addedList.add(model);
                model.setWidgetChangeStatus(WidgetChangeStatus.InitChange);
                LogUtils.m1568d(TAG, "added tab:" + model);
            }
        }
        for (TabModel model2 : oldList) {
            if (!contain(result, model2)) {
                removeList.add(model2);
                model2.setWidgetChangeStatus(WidgetChangeStatus.NoChange);
                LogUtils.m1568d(TAG, "remove tab:" + model2);
            }
        }
    }

    private boolean contain(List<TabModel> arrays, TabModel model) {
        if (!(model == null || arrays == null)) {
            for (TabModel item : arrays) {
                if (item.getId() == model.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public TabModel getTabModel(int channelId) {
        List<TabModel> tabModelList = getInstance().getTabInfo();
        List<TabModel> tabModelHideList = getInstance().getTabHideInfo();
        LogUtils.m1568d(TAG, "getTabModel, tab show list size : " + ListUtils.getCount((List) tabModelList));
        LogUtils.m1568d(TAG, "getTabModel, tab hide list size : " + ListUtils.getCount((List) tabModelHideList));
        if (!ListUtils.isEmpty((List) tabModelList)) {
            for (TabModel tabShowModel : tabModelList) {
                if (tabShowModel != null && channelId == tabShowModel.getChannelId()) {
                    return tabShowModel;
                }
            }
        }
        if (!ListUtils.isEmpty((List) tabModelHideList)) {
            for (TabModel tabHideModel : tabModelHideList) {
                if (tabHideModel != null && channelId == tabHideModel.getChannelId()) {
                    return tabHideModel;
                }
            }
        }
        return null;
    }

    public boolean hasVipTab() {
        List<TabModel> tabModelList = getInstance().getTabInfo();
        List<TabModel> tabModelHideList = getInstance().getTabHideInfo();
        LogUtils.m1568d(TAG, "hasVipTab, tab show list size : " + ListUtils.getCount((List) tabModelList));
        LogUtils.m1568d(TAG, "hasVipTab, tab hide list size : " + ListUtils.getCount((List) tabModelHideList));
        if (!ListUtils.isEmpty((List) tabModelList)) {
            for (TabModel tabModel : tabModelList) {
                if (tabModel != null && 1000002 == tabModel.getChannelId()) {
                    return true;
                }
            }
        }
        if (!ListUtils.isEmpty((List) tabModelHideList)) {
            for (TabModel tabModel2 : tabModelHideList) {
                if (tabModel2 != null && 1000002 == tabModel2.getChannelId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
