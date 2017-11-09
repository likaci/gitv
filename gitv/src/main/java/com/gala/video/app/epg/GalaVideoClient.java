package com.gala.video.app.epg;

import android.content.Context;
import com.gala.albumprovider.AlbumProviderApi;
import com.gala.cloudui.utils.CloudUtilsGala;
import com.gala.video.app.epg.config.EpgAppConfig;
import com.gala.video.app.epg.dependency.Dependencies;
import com.gala.video.app.epg.init.EpgInterfaceFactory;
import com.gala.video.app.epg.init.InitFactory;
import com.gala.video.cloudui.CloudUtils;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.BroadcastConfigEPG;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.gala.video.lib.share.ifmanager.InterfaceManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiInitHelper;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.data.flatbuffers.ItemStyleConfig;
import com.tvos.appdetailpage.AppDetailPageConfig;

public class GalaVideoClient {
    private static final GalaVideoClient gInstance = new GalaVideoClient();

    public static final GalaVideoClient get() {
        return gInstance;
    }

    public void setupWithContext(Context appContext) {
        AppRuntimeEnv.get().init(appContext);
        AppRuntimeEnv.get().setSessionId("");
        AppRuntimeEnv.get().setTotalMemory(DeviceUtils.getTotalMemory());
        AppRuntimeEnv.get().setCpuCores(DeviceUtils.getCpuCoreNums());
        InterfaceManager.get().register(InterfaceKey.EPG_IFF, new EpgInterfaceFactory());
        String pkg = Dependencies.getPackageName();
        CloudUtils.setPackageName(pkg);
        CloudUtilsGala.setPackageName(pkg);
        CloudUtilsGala.setPackageContext(appContext);
        ItemStyleConfig.initItemStyle();
        AppDetailPageConfig.setResourcePkgName(pkg);
        new IOpenApiInitHelper().getEPGHelper().init(appContext, StringUtils.parseStringtoList(Project.getInstance().getBuild().getOpenapiFeatureList()));
        BroadcastConfigEPG.initialize(appContext, StringUtils.parseStringtoList(Project.getInstance().getBuild().getBroadcastActions())).initBroadcastFeatures();
        GetInterfaceTools.getIInit().execute(InitFactory.makeUpStartAdRequestTask());
        AlbumProviderApi.getAlbumProvider().isNeedChannelCache(EpgAppConfig.isUseAlbumListCache());
        GetInterfaceTools.getIInit().execute(InitFactory.makeUpLoadFondTask());
        GetInterfaceTools.getIInit().execute(InitFactory.makeUpCommonInitTask());
        GetInterfaceTools.getIInit().execute(InitFactory.makeUpAppendPingbackParamsTask());
        GetInterfaceTools.getIInit().execute(InitFactory.makeUpLogRecordInitTask());
        GetInterfaceTools.getIInit().execute(InitFactory.makeUpOnlyMainProcessInitTask());
        GetInterfaceTools.getIInit().execute(InitFactory.makeUpLayoutCacheInitTask());
        GetInterfaceTools.getIInit().execute(InitFactory.makeUpMultiScreenInitTask());
        GetInterfaceTools.getIInit().execute(InitFactory.makeUpAppstoreInitTask());
        GetInterfaceTools.getIInit().execute(InitFactory.makeUpJSConfigInitTask());
        GetInterfaceTools.getIInit().execute(InitFactory.makeUpDeleteCollectionTask());
        GetInterfaceTools.getIInit().execute(InitFactory.makeUpInitFingerPrintTask());
        if (Project.getInstance().getBuild().isOpenMessageCenter()) {
            GetInterfaceTools.getIInit().execute(InitFactory.makeUpPushServiceInitTask());
        }
    }
}
