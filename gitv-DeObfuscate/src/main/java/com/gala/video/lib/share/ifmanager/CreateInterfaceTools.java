package com.gala.video.lib.share.ifmanager;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.IEpgEntry;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.IEpgPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.IAppDownloadManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.IPromotionCache;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.banner.IBannerAdProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.IChannelProviderProxy;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.IModelHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.devcecheck.IDeviceCheckProxy;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackDialogController;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackFactory;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.livecorner.ILiveCornerFactory;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.IMultiSubjectInfoModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.IMultiSubjectUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.netdiagnose.IGetAlbumProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.build.IBuildInterface;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.config.IConfigInterface;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.control.IControlInterface;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.INetworkProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.INetworkProvider.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.IUpdateManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.IWebRoleEntry;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.IOpenBroadcastActionHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IMultiEventHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerExitHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.voice.IVoiceCommon;

public class CreateInterfaceTools {
    public static INetworkProvider createNetworkProvider() {
        INetworkProvider ret = Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_NP));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create CustomSettingProvider fail!");
        }
        return ret;
    }

    public static IFeedbackDialogController createFeedbackDialogController() {
        IFeedbackDialogController ret = IFeedbackDialogController.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_FBDC));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create FeedbackDialogController fail!");
        }
        return ret;
    }

    public static IDeviceCheckProxy createDeviceCheckProxy() {
        IDeviceCheckProxy ret = IDeviceCheckProxy.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_DCP));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create DeviceCheckProxy fail!");
        }
        return ret;
    }

    public static IGetAlbumProvider createGetAlbumProvider() {
        IGetAlbumProvider ret = IGetAlbumProvider.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_GAP));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create GetAlbumProvider fail!");
        }
        return ret;
    }

    public static IEpgPingback createEpgPingback() {
        IEpgPingback ret = IEpgPingback.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_PB));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create GetAlbumProvider fail!");
        }
        return ret;
    }

    public static IEpgEntry createEpgEntry() {
        IEpgEntry ret = IEpgEntry.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_E));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create EpgEntry fail!");
        }
        return ret;
    }

    public static ILiveCornerFactory createLiveCornerFactory() {
        ILiveCornerFactory ret = ILiveCornerFactory.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_LCF));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create LiveCornerFactory fail!");
        }
        return ret;
    }

    public static IMultiSubjectInfoModel createMultiSubjectInfoModel() {
        IMultiSubjectInfoModel ret = IMultiSubjectInfoModel.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_MSM));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create IMultiSubjectInfoModel fail!");
        }
        return ret;
    }

    public static IFeedbackResultCallback createFeedbackResultListener() {
        IFeedbackResultCallback ret = IFeedbackResultCallback.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_FRC));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create IFeedbackResultCallback fail!");
        }
        return ret;
    }

    public static IVoiceCommon createVoiceCommon() {
        IVoiceCommon ret = IVoiceCommon.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.SHARE_VC));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create VoiceCommon fail!");
        }
        return ret;
    }

    public static IFeedbackFactory createFeedbackFactory() {
        IFeedbackFactory ret = IFeedbackFactory.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_FF));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create FeedbackFactory fail!");
        }
        return ret;
    }

    public static IWebRoleEntry createWebRoleFactory() {
        IWebRoleEntry ret = IWebRoleEntry.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_WEB_ROLE));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create IWebRoleEntry fail!");
        }
        return ret;
    }

    public static IModelHelper createModelHelper() {
        IModelHelper ret = IModelHelper.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_MH));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create ModelHelper fail!");
        }
        return ret;
    }

    public static IChannelProviderProxy createChannelProviderProxy() {
        IChannelProviderProxy ret = IChannelProviderProxy.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_CHNPP));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create CustomSettingProvider fail!");
        }
        return ret;
    }

    public static IBannerAdProvider createBannerAdProvider() {
        IBannerAdProvider ret = IBannerAdProvider.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_BAP));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create BannerAdProvider fail!");
        }
        return ret;
    }

    public static IMultiSubjectUtils createMultiSubjectUtils() {
        IMultiSubjectUtils ret = IMultiSubjectUtils.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_MSU));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create MultiSubjectUtils fail!");
        }
        return ret;
    }

    public static IOpenApiCommandHolder createEpgOpenApiHolder() {
        IOpenApiCommandHolder ret = IOpenApiCommandHolder.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_OPC));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create EpgOpenApiHolder fail!");
        }
        return ret;
    }

    public static IOpenApiCommandHolder createPlayerOpenApiHolder() {
        IOpenApiCommandHolder ret = IOpenApiCommandHolder.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.PLAYER_OPC));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create PlayerOpenApiHolder fail!");
        }
        return ret;
    }

    public static IOpenBroadcastActionHolder createEpgBroadcastHolder() {
        IOpenBroadcastActionHolder ret = IOpenBroadcastActionHolder.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_OPA));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create PlayerOpenApiHolder fail!");
        }
        return ret;
    }

    public static IOpenBroadcastActionHolder createPlayerBroadcastHolder() {
        IOpenBroadcastActionHolder ret = IOpenBroadcastActionHolder.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.PLAYER_OPA));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create PlayerOpenApiHolder fail!");
        }
        return ret;
    }

    public static IMultiEventHelper createMultiEventHelper() {
        IMultiEventHelper ret = IMultiEventHelper.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.PLAYER_MEH));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create MultiEventHelper fail!");
        }
        return ret;
    }

    public static IBuildInterface createBuildInterface() {
        IBuildInterface ret = IBuildInterface.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_BUILD_IF));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create IBuildInterface fail!");
        }
        return ret;
    }

    public static IConfigInterface createConfigInterface() {
        IConfigInterface ret = IConfigInterface.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_CFG_IF));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create IConfigInterface fail!");
        }
        return ret;
    }

    public static IControlInterface createControlInterface() {
        IControlInterface ret = IControlInterface.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_CTRL_IF));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create IControlInterface fail!");
        }
        return ret;
    }

    public static IPlayerExitHelper createPlayerExitHelper() {
        IPlayerExitHelper ret = IPlayerExitHelper.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.PLAYER_EH));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create IPlayerExitHelper fail!");
        }
        return ret;
    }

    public static IUpdateManager createUpdateManager() {
        IUpdateManager ret = IUpdateManager.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_UM));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create IUpdateManager fail!");
        }
        return ret;
    }

    public static IAppDownloadManager createAppDownloadManager() {
        IAppDownloadManager ret = IAppDownloadManager.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_ADM));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create IAppDownloadManager fail!");
        }
        return ret;
    }

    public static IPromotionCache createPromotionCache() {
        IPromotionCache ret = IPromotionCache.Wrapper.asInterface(InterfaceFactory.createWithoutRegister(InterfaceKey.EPG_PC));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create IPromotionCache fail!");
        }
        return ret;
    }
}
