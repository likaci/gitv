package com.gala.video.app.epg.init;

import com.gala.video.app.epg.EpgEntry;
import com.gala.video.app.epg.SOpenApiEpgCommandHolder;
import com.gala.video.app.epg.apkupgrade.UpdateManager;
import com.gala.video.app.epg.appdownload.AppDownloadManager;
import com.gala.video.app.epg.carousel.CarouselHistoryCacheManagerCreater;
import com.gala.video.app.epg.feedback.FeedBackFactory;
import com.gala.video.app.epg.feedback.FeedbackCreater;
import com.gala.video.app.epg.feedback.FeedbackResultListener;
import com.gala.video.app.epg.home.ads.controller.HomeFocusImageAdProvider;
import com.gala.video.app.epg.home.ads.task.HomeFocusImageAdRequestTask;
import com.gala.video.app.epg.home.component.item.corner.LiveCornerFactory;
import com.gala.video.app.epg.home.data.DeviceCheckProxy;
import com.gala.video.app.epg.home.data.ModelHelper;
import com.gala.video.app.epg.home.data.constants.HomeConstants;
import com.gala.video.app.epg.home.data.pingback.HomePingback;
import com.gala.video.app.epg.home.data.provider.BannerAdProvider;
import com.gala.video.app.epg.home.data.provider.ChannelProviderProxy;
import com.gala.video.app.epg.home.promotion.local.PromotionCache;
import com.gala.video.app.epg.home.utils.ActionJump;
import com.gala.video.app.epg.netdiagnose.GetAlbumProvider;
import com.gala.video.app.epg.network.NetworkProvider;
import com.gala.video.app.epg.openBroadcast.OpenBroadcastEpgActionHolder;
import com.gala.video.app.epg.pingback.EpgPingback;
import com.gala.video.app.epg.project.build.BuildProvider;
import com.gala.video.app.epg.project.config.ConfigCreator;
import com.gala.video.app.epg.project.control.ControlProvider;
import com.gala.video.app.epg.screensaver.ScreenSaverCreater;
import com.gala.video.app.epg.startup.StartupDataLoader;
import com.gala.video.app.epg.ui.albumlist.AlbumInfoHelper;
import com.gala.video.app.epg.ui.albumlist.EPGCornerProvider;
import com.gala.video.app.epg.ui.albumlist.utils.UICreator;
import com.gala.video.app.epg.ui.multisubject.model.MultiSubjectInfoModel;
import com.gala.video.app.epg.ui.multisubject.util.MultiSubjectUtils;
import com.gala.video.app.epg.ui.multisubject.util.MultiSubjectViewFactory;
import com.gala.video.app.epg.web.WebEntry;
import com.gala.video.app.epg.web.WebRoleEntry;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.ifimpl.web.provider.WebJsonParmsProviderCreator;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.epgIfFactory.IEpgInterfaceFactory.Wrapper;

public class EpgInterfaceFactory extends Wrapper {
    public IInterfaceWrapper getEpgInterface(String key) {
        if (InterfaceKey.EPG_FB.equals(key)) {
            return FeedbackCreater.createKeyProcessor();
        }
        if (InterfaceKey.EPG_CHCM.equals(key)) {
            return CarouselHistoryCacheManagerCreater.create();
        }
        if (InterfaceKey.EPG_SUDL.equals(key)) {
            return new StartupDataLoader();
        }
        if (InterfaceKey.EPG_FBDC.equals(key)) {
            return FeedbackCreater.createDialogController();
        }
        if (InterfaceKey.EPG_DCP.equals(key)) {
            return new DeviceCheckProxy();
        }
        if (InterfaceKey.EPG_NP.equals(key)) {
            return new NetworkProvider();
        }
        if (InterfaceKey.EPG_GAP.equals(key)) {
            return new GetAlbumProvider();
        }
        if (InterfaceKey.EPG_PB.equals(key)) {
            return new EpgPingback();
        }
        if (InterfaceKey.EPG_E.equals(key)) {
            return new EpgEntry();
        }
        if (InterfaceKey.EPG_AIH.equals(key)) {
            return new AlbumInfoHelper();
        }
        if (InterfaceKey.EPG_CP.equals(key)) {
            return new EPGCornerProvider();
        }
        if (InterfaceKey.EPG_WE.equals(key)) {
            return new WebEntry();
        }
        if (InterfaceKey.EPG_LCF.equals(key)) {
            return new LiveCornerFactory();
        }
        if (InterfaceKey.EPG_MSM.equals(key)) {
            return new MultiSubjectInfoModel();
        }
        if (InterfaceKey.EPG_FRC.equals(key)) {
            return new FeedbackResultListener();
        }
        if (InterfaceKey.EPG_MSF.equals(key)) {
            return new MultiSubjectViewFactory();
        }
        if (InterfaceKey.EPG_MSU.equals(key)) {
            return new MultiSubjectUtils();
        }
        if (InterfaceKey.EPG_UIC.equals(key)) {
            return new UICreator();
        }
        if (InterfaceKey.EPG_FF.equals(key)) {
            return new FeedBackFactory();
        }
        if (InterfaceKey.EPG_WEB_DATA.equals(key)) {
            return WebJsonParmsProviderCreator.create();
        }
        if (InterfaceKey.EPG_WEB_ROLE.equals(key)) {
            return new WebRoleEntry();
        }
        if (InterfaceKey.EPG_MH.equals(key)) {
            return new ModelHelper();
        }
        if (InterfaceKey.EPG_CHNPP.equals(key)) {
            return new ChannelProviderProxy();
        }
        if (InterfaceKey.EPG_BAP.equals(key)) {
            return BannerAdProvider.getInstance();
        }
        if (InterfaceKey.EPG_OPC.equals(key)) {
            return new SOpenApiEpgCommandHolder();
        }
        if (InterfaceKey.EPG_OPA.equals(key)) {
            return new OpenBroadcastEpgActionHolder();
        }
        if (InterfaceKey.EPG_BUILD_IF.equals(key)) {
            return new BuildProvider();
        }
        if (InterfaceKey.EPG_CFG_IF.equals(key)) {
            return new ConfigCreator().config(AppRuntimeEnv.get().getApplicationContext());
        }
        if (InterfaceKey.EPG_CTRL_IF.equals(key)) {
            return new ControlProvider();
        }
        if (InterfaceKey.EPG_UM.equals(key)) {
            return UpdateManager.getInstance();
        }
        if (InterfaceKey.EPG_ADM.equals(key)) {
            return AppDownloadManager.getInstance();
        }
        if (InterfaceKey.EPG_PC.equals(key)) {
            return PromotionCache.instance();
        }
        if ("screensaver".equals(key)) {
            return ScreenSaverCreater.create();
        }
        if (InterfaceKey.EPG_AJ.equals(key)) {
            return new ActionJump();
        }
        if (InterfaceKey.EPG_HPB.equals(key)) {
            return new HomePingback();
        }
        if (InterfaceKey.EPG_HFIADT.equals(key)) {
            return HomeFocusImageAdRequestTask.getInstance();
        }
        if (InterfaceKey.EPG_HFIADP.equals(key)) {
            return HomeFocusImageAdProvider.getInstance();
        }
        if (InterfaceKey.EPG_HC.equals(key)) {
            return HomeConstants.getInstance();
        }
        return null;
    }
}
