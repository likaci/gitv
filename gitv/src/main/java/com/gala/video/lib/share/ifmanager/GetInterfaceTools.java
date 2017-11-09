package com.gala.video.lib.share.ifmanager;

import com.gala.video.lib.share.ifmanager.bussnessIF.activestatepolicy.IActiveStateDispatcher;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.IAdApi;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.IAdProcessingUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.background.IBackgroundManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicQDataProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.IActionJump;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.IStartupDataLoader;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.ICornerProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.IUICreator;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.carousel.ICarouselHistoryCacheManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackKeyProcess;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.homeconstants.IHomeConstants;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.homefocusimagead.IHomeFocusImageAdProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.homefocusimagead.IHomeFocusImageAdTask;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.IMultiSubjectViewFactory;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.IWebEntry;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.IWebJsonParmsProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.errorcode.IErrorCodeProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgCenter;
import com.gala.video.lib.share.ifmanager.bussnessIF.interaction.IActionManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.ILogRecordProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenapiReporterManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IGalaPlayerPageProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IGalaVideoPlayerGenerator;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IHCDNController;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerConfigProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerProfileCreator;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.playerIfFactory.IPlayerInterfaceFactory.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.ISkinManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.IThemeProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.IThemeZipHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.startup.IInit;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.IGalaAccountManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.IGalaVipManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.IHistoryCacheManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.ILoginProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.ISubscribeProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.web.IJSConfigDataProvider;

public class GetInterfaceTools {
    public static boolean isPlayerLoaded() {
        IInterfaceWrapper wrapper = InterfaceManager.get().get(InterfaceKey.PLAYER_IFF);
        if (wrapper == null || Wrapper.asInterface(wrapper.getInterface()) == null) {
            return false;
        }
        return true;
    }

    public static IFeedbackKeyProcess getIFeedbackKeyProcess() {
        IFeedbackKeyProcess ret = IFeedbackKeyProcess.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.EPG_FB));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized FeedbackKeyProcess before getting!");
        }
        return ret;
    }

    public static IScreenSaverOperate getIScreenSaver() {
        IScreenSaverOperate ret = IScreenSaverOperate.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper("screensaver"));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized ScreenSaverAdOperator before getting!");
        }
        return ret;
    }

    public static IActiveStateDispatcher getActiveStateDispatcher() {
        IActiveStateDispatcher ret = IActiveStateDispatcher.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_ACTIVE_STATE_DISPATCHER));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized ScreenSaverAdOperator before getting!");
        }
        return ret;
    }

    public static IThemeProvider getIThemeProvider() {
        return getISkinManager().getIThemeProvider();
    }

    public static IThemeZipHelper getIThemeZipHelper() {
        return getISkinManager().getIThemeZipHelper();
    }

    protected static ISkinManager getISkinManager() {
        ISkinManager ret = ISkinManager.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_SM));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized SkinResourceManager before getting!");
        }
        return ret;
    }

    public static IBackgroundManager getIBackgroundManager() {
        IBackgroundManager ret = IBackgroundManager.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_BM));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized BackgroundManager before getting!");
        }
        return ret;
    }

    public static ILogRecordProvider getILogRecordProvider() {
        ILogRecordProvider ret = ILogRecordProvider.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_LR));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized LogRecordProvider before getting!");
        }
        return ret;
    }

    public static IJSConfigDataProvider getIJSConfigDataProvider() {
        IJSConfigDataProvider ret = IJSConfigDataProvider.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_JS));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized JSConfigDataProviderCreator before getting!");
        }
        return ret;
    }

    public static IWebJsonParmsProvider getWebJsonParmsProvider() {
        IWebJsonParmsProvider ret = IWebJsonParmsProvider.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.EPG_WEB_DATA));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create IWebJsonParmsProvider fail!");
        }
        return ret;
    }

    public static IDynamicQDataProvider getIDynamicQDataProvider() {
        IDynamicQDataProvider ret = IDynamicQDataProvider.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_DP));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized DynamicQDataProvider before getting!");
        }
        return ret;
    }

    public static IAdApi getIAdApi() {
        IAdApi ret = IAdApi.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_AD));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized AdApi before getting!");
        }
        return ret;
    }

    public static IAdProcessingUtils getIAdProcessingUtils() {
        IAdProcessingUtils ret = IAdProcessingUtils.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_AD_PROCESS));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized AdApi before getting!");
        }
        return ret;
    }

    public static IInit getIInit() {
        IInit ret = IInit.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_IT));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized Init before getting!");
        }
        return ret;
    }

    public static IMsgCenter getMsgCenter() {
        IMsgCenter ret = IMsgCenter.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper("imsg"));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized MsgCenter before getting!");
        }
        return ret;
    }

    public static IGalaAccountManager getIGalaAccountManager() {
        IGalaAccountManager ret = IGalaAccountManager.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_QAM));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized GalaAccountManager before getting!");
        }
        return ret;
    }

    public static IGalaVipManager getIGalaVipManager() {
        IGalaVipManager ret = IGalaVipManager.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_QVM));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized GalaVipManager before getting!");
        }
        return ret;
    }

    public static IHistoryCacheManager getIHistoryCacheManager() {
        IHistoryCacheManager ret = IHistoryCacheManager.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_HCM));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized HistoryCacheManager before getting!");
        }
        return ret;
    }

    public static ISubscribeProvider getISubscribeProvider() {
        ISubscribeProvider ret = ISubscribeProvider.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_SBP));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized ISubscribeProvider before getting!");
        }
        return ret;
    }

    public static IActionManager getIActionManager() {
        IActionManager ret = IActionManager.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_ATM));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized SubscribeProvider before getting!");
        }
        return ret;
    }

    public static IActionJump getIActionJump() {
        IActionJump ret = IActionJump.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.EPG_AJ));
        if (ret == null) {
            InterfaceException.throwRuntimeException("IActionJump must be initialized SubscribeProvider before getting!");
        }
        return ret;
    }

    public static IHomePingback getIHomePingback() {
        IHomePingback ret = IHomePingback.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.EPG_HPB));
        if (ret == null) {
            InterfaceException.throwRuntimeException("IHomePingback must be initialized SubscribeProvider before getting!");
        }
        return ret;
    }

    public static IHomeFocusImageAdTask getIHomeFocusImageTask() {
        IHomeFocusImageAdTask ret = IHomeFocusImageAdTask.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.EPG_HFIADT));
        if (ret == null) {
            InterfaceException.throwRuntimeException("IHomeFocusImageAdTask must be initialized HomeFocusImageAdRequestTask before getting!");
        }
        return ret;
    }

    public static IHomeFocusImageAdProvider getIHomeFocusImageAdProvider() {
        IHomeFocusImageAdProvider ret = IHomeFocusImageAdProvider.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.EPG_HFIADP));
        if (ret == null) {
            InterfaceException.throwRuntimeException("getIHomeFocusImageAdProvider must be initialized HomeFocusImageAdProvider before getting!");
        }
        return ret;
    }

    public static IHomeConstants getIHomeConstants() {
        IHomeConstants ret = IHomeConstants.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.EPG_HC));
        if (ret == null) {
            InterfaceException.throwRuntimeException("getIHomeConstants must be initialized HomeConstants before getting!");
        }
        return ret;
    }

    public static ICarouselHistoryCacheManager getICarouselHistoryCacheManager() {
        ICarouselHistoryCacheManager ret = ICarouselHistoryCacheManager.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.EPG_CHCM));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized ICarouselHistoryCacheManager before getting!");
        }
        return ret;
    }

    public static IErrorCodeProvider getErrorCodeProvider() {
        IErrorCodeProvider ret = IErrorCodeProvider.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_EC));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized ErrorCodeProvider before getting!");
        }
        return ret;
    }

    public static IStartupDataLoader getStartupDataLoader() {
        IStartupDataLoader ret = IStartupDataLoader.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.EPG_SUDL));
        if (ret == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized StartupDataLoader before getting!");
        }
        return ret;
    }

    public static IDataBus getDataBus() {
        IDataBus ret = IDataBus.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_DBUS));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create DataBus fail!");
        }
        return ret;
    }

    public static IAlbumInfoHelper getAlbumInfoHelper() {
        IAlbumInfoHelper ret = IAlbumInfoHelper.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.EPG_AIH));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create albumInfoHelper fail!");
        }
        return ret;
    }

    public static ICornerProvider getCornerProvider() {
        ICornerProvider ret = ICornerProvider.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.EPG_CP));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create CornerProvider fail!");
        }
        return ret;
    }

    public static IWebEntry getWebEntry() {
        IWebEntry ret = IWebEntry.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.EPG_WE));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create WebEntry fail!");
        }
        return ret;
    }

    public static IMultiSubjectViewFactory getMultiSubjectViewFactory() {
        IMultiSubjectViewFactory ret = IMultiSubjectViewFactory.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.EPG_MSF));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create MultiSubjectViewFactory fail!");
        }
        return ret;
    }

    public static IUICreator getUICreator() {
        IUICreator ret = IUICreator.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.EPG_UIC));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create UICreator fail!");
        }
        return ret;
    }

    public static ILoginProvider getLoginProvider() {
        ILoginProvider ret = ILoginProvider.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_LP));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create LoginProvider fail!");
        }
        return ret;
    }

    public static IGalaPlayerPageProvider getPlayerPageProvider() {
        IGalaPlayerPageProvider ret = IGalaPlayerPageProvider.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.PLAYER_PP));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create PlayerPageProvider fail!");
        }
        return ret;
    }

    public static IPlayerConfigProvider getPlayerConfigProvider() {
        IPlayerConfigProvider ret = IPlayerConfigProvider.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.PLAYER_CP));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create PlayerProvider fail!");
        }
        return ret;
    }

    public static IPlayerFeatureProxy getPlayerFeatureProxy() {
        IPlayerFeatureProxy ret = IPlayerFeatureProxy.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.PLAYER_FP));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create PlayerFeatureProxy fail!");
        }
        return ret;
    }

    public static IGalaVideoPlayerGenerator getGalaVideoPlayerGenerator() {
        IGalaVideoPlayerGenerator ret = IGalaVideoPlayerGenerator.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.PLAYER_PG));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create GalaVideoPlayerGenerator fail!");
        }
        return ret;
    }

    public static IHCDNController getHCDNController() {
        IHCDNController ret = IHCDNController.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.PLAYER_HCDN));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create HCDNController fail!");
        }
        return ret;
    }

    public static IPlayerProfileCreator getPlayerProfileCreator() {
        IPlayerProfileCreator ret = IPlayerProfileCreator.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.PLAYER_PPC));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create PlayerProfileCreator fail!");
        }
        return ret;
    }

    public static IOpenapiReporterManager getOpenapiReporterManager() {
        IOpenapiReporterManager ret = IOpenapiReporterManager.Wrapper.asInterface(InterfaceFactory.getIInterfaceWrapper(InterfaceKey.SHARE_OPENAPI_REPORTER));
        if (ret == null) {
            InterfaceException.throwRuntimeException("create OpenapiReporterManager fail!");
        }
        return ret;
    }
}
