package com.gala.video.app.epg.home.data.hdata.task;

import com.alibaba.fastjson.JSON;
import com.gala.video.api.ApiFactory;
import com.gala.video.api.ICommonApiCallback;
import com.gala.video.app.epg.home.data.bus.HomeDataObservable;
import com.gala.video.app.epg.home.data.hdata.HomeDataType;
import com.gala.video.app.epg.home.data.model.AppDataInfo;
import com.gala.video.app.epg.home.data.model.AppDataModel;
import com.gala.video.app.epg.home.data.model.AppDataSet;
import com.gala.video.app.epg.home.data.model.AppModel;
import com.gala.video.app.epg.home.data.model.CollectAppData;
import com.gala.video.app.epg.home.data.model.FocusAppData;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.provider.AppsProvider;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppOperateRequestTask extends BaseRequestTask {
    private static final int DATA_COLLECTION_FLAG = 2;
    private static final int DATA_DETAIL_FLAG = 1;
    private static final int DATA_FOCUS_FLAG = 3;
    private static final String TAG = "home/AppOperateRequestTask";
    private boolean mFromOpenApi;
    private ICommonApiCallback mICommonApiCallback;
    private String mUrl;

    class C06241 implements ICommonApiCallback {
        C06241() {
        }

        public void onSuccess(String resJson) {
            if (StringUtils.isEmpty((CharSequence) resJson)) {
                LogUtils.m1571e(AppOperateRequestTask.TAG, "mICommonApiCallback() -> resJson is null");
                return;
            }
            LogUtils.m1568d(AppOperateRequestTask.TAG, "mICommonApiCallback() ->  onSuccess = " + resJson);
            try {
                AppModel model = (AppModel) JSON.parseObject(resJson, AppModel.class);
                if (model == null) {
                    LogUtils.m1571e(AppOperateRequestTask.TAG, "mICommonApiCallback() -> onSuccess model is null");
                    return;
                }
                AppDataSet appDataSet = model.data;
                if (appDataSet == null) {
                    LogUtils.m1571e(AppOperateRequestTask.TAG, "mICommonApiCallback() -> appDataSet is empty!");
                    return;
                }
                AppsProvider.getInstance().clearAppsList();
                AppOperateRequestTask.this.onLoadSuccess(appDataSet);
            } catch (Exception e) {
                LogUtils.m1572e(AppOperateRequestTask.TAG, "mICommonApiCallback() -> AppModel json e:", e);
            }
        }

        public void onException(Exception e, String code) {
            String str;
            LogUtils.m1573e(AppOperateRequestTask.TAG, "mICommonApiCallback() -> onException() -> e:", e, "code:", code);
            IHomePingback addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008").addItem("pfec", code).addItem(Keys.ERRURL, AppOperateRequestTask.this.mUrl).addItem(Keys.APINAME, "AppOperateRequest");
            String str2 = Keys.ERRDETAIL;
            if (e == null) {
                str = "";
            } else {
                str = e.getMessage();
            }
            addItem.addItem(str2, str).addItem("activity", "HomeActivity").addItem(Keys.CRASHTYPE, "").addItem(Keys.f2035T, "0").setOthersNull().post();
        }
    }

    public AppOperateRequestTask() {
        this.mFromOpenApi = false;
        this.mUrl = "";
        this.mICommonApiCallback = new C06241();
        this.mFromOpenApi = false;
    }

    public AppOperateRequestTask(boolean fromOpenApi) {
        this.mFromOpenApi = false;
        this.mUrl = "";
        this.mICommonApiCallback = new C06241();
        this.mFromOpenApi = fromOpenApi;
    }

    private void onLoadSuccess(AppDataSet data) {
        onLoadApp(data.getAppstore());
        onLoadData(data.getFocusApplist(), 3);
        onLoadData(data.getCollectionAppList(), 2);
        onLoadData(data.getAppDataInfolist(), 1);
        try {
            SerializableUtils.write(AppsProvider.getInstance().getAppsList(), HomeDataConfig.HOME_APP_OPERATOR_LIST_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onLoadData(List<?> list, int flag) {
        if (ListUtils.isEmpty((List) list)) {
            LogUtils.m1571e(TAG, "onLoadData() ->list is null");
            return;
        }
        AppsProvider provider = AppsProvider.getInstance();
        for (AppDataModel appModel : getOperateDataModelList(list, flag)) {
            provider.addApp(appModel);
        }
    }

    private void onLoadApp(AppDataInfo appDataInfo) {
        AppsProvider.getInstance().addApp(getAppStore(appDataInfo));
    }

    private List<AppDataModel> getOperateDataModelList(List<?> info, int flag) {
        if (ListUtils.isEmpty((List) info)) {
            LogUtils.m1571e(TAG, "getOperateDataModelList() -> list == null ,size:" + info.size());
            return null;
        }
        List<AppDataModel> list = new ArrayList();
        int size = info.size();
        for (int i = 0; i < size; i++) {
            if (getChange(info.get(i), flag) != null) {
                list.add(getChange(info.get(i), flag));
            }
        }
        return list;
    }

    private AppDataModel getAppStore(AppDataInfo appstore) {
        AppDataModel app = new AppDataModel();
        app.setDownloadUrl(appstore.app_download_url);
        app.setId(String.valueOf(appstore.app_id));
        app.setPackageName(appstore.app_package_name);
        return app;
    }

    private AppDataModel getChange(Object info, int i) {
        if (info == null) {
            return null;
        }
        AppDataModel app = new AppDataModel();
        String id = null;
        String name = null;
        String imageUrl = null;
        String downloadUrl = null;
        String packageName = null;
        boolean isNull = true;
        switch (i) {
            case 1:
                if (info instanceof AppDataInfo) {
                    id = String.valueOf(((AppDataInfo) info).app_id);
                    name = ((AppDataInfo) info).app_name;
                    imageUrl = ((AppDataInfo) info).app_logo;
                    downloadUrl = ((AppDataInfo) info).app_download_url;
                    packageName = ((AppDataInfo) info).app_package_name;
                    isNull = false;
                    break;
                }
                break;
            case 2:
                if (info instanceof CollectAppData) {
                    List list = ((CollectAppData) info).appList;
                    if (!ListUtils.isEmpty(list)) {
                        imageUrl = true == this.mFromOpenApi ? ((CollectAppData) info).collection_icon : ((AppDataInfo) list.get(0)).app_logo;
                        downloadUrl = ((AppDataInfo) list.get(0)).app_download_url;
                        id = String.valueOf(((AppDataInfo) list.get(0)).app_id);
                        packageName = ((AppDataInfo) list.get(0)).app_package_name;
                        name = ((AppDataInfo) list.get(0)).app_name;
                        isNull = false;
                        break;
                    }
                }
                break;
            case 3:
                if (info instanceof FocusAppData) {
                    id = String.valueOf(((FocusAppData) info).app_id);
                    name = ((FocusAppData) info).appTitle;
                    if (true == this.mFromOpenApi) {
                        imageUrl = ((FocusAppData) info).img_url;
                    } else {
                        imageUrl = ((FocusAppData) info).logoUrl;
                    }
                    downloadUrl = ((FocusAppData) info).downloadUrl;
                    packageName = ((FocusAppData) info).packageName;
                    isNull = false;
                    break;
                }
                break;
        }
        if (isNull) {
            return null;
        }
        app.setFlag(i);
        app.setId(id);
        app.setName(name);
        app.setImageUrl(imageUrl);
        app.setDownloadUrl(downloadUrl);
        app.setPackageName(packageName);
        return app;
    }

    public void invoke() {
        LogUtils.m1568d(TAG, "invoke app operate request task");
        String url = "http://store." + Project.getInstance().getBuild().getDomainName() + "/apis/tv/launcher/app_index.action?agent_type=5202";
        this.mUrl = url;
        LogUtils.m1568d(TAG, "requestDataInThread() ->  url = " + url);
        ApiFactory.getCommonApi().callSync(url, this.mICommonApiCallback, false, "appStore");
    }

    public void onOneTaskFinished() {
        LogUtils.m1568d(TAG, "App size = " + AppsProvider.getInstance().getAppsList().size());
        HomeDataObservable.getInstance().post(HomeDataType.APP_OPERATOR, WidgetChangeStatus.DataChange, null);
    }
}
