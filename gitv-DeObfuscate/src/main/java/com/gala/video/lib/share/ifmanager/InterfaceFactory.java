package com.gala.video.lib.share.ifmanager;

import com.gala.video.lib.share.ifimpl.activestatepolicy.ActiveStateCreator;
import com.gala.video.lib.share.ifimpl.ads.AdApiCreator;
import com.gala.video.lib.share.ifimpl.ads.AdProcessingUtilsCreator;
import com.gala.video.lib.share.ifimpl.background.BackgroundManagerCreator;
import com.gala.video.lib.share.ifimpl.databus.DataBusCreater;
import com.gala.video.lib.share.ifimpl.dynamic.DynamicQDataProviderCreator;
import com.gala.video.lib.share.ifimpl.errorcode.ErrorCodeProviderCreater;
import com.gala.video.lib.share.ifimpl.imsg.MsgCenterCreator;
import com.gala.video.lib.share.ifimpl.interaction.ActionManagerCreator;
import com.gala.video.lib.share.ifimpl.logrecord.LogRecordProviderCreator;
import com.gala.video.lib.share.ifimpl.skin.SkinManagerCreator;
import com.gala.video.lib.share.ifimpl.startup.InitCreator;
import com.gala.video.lib.share.ifimpl.ucenter.LoginProviderCreater;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.GalaAccountCreator;
import com.gala.video.lib.share.ifimpl.ucenter.account.vipRight.GalaVipCreator;
import com.gala.video.lib.share.ifimpl.ucenter.history.impl.HistoryCacheCreator;
import com.gala.video.lib.share.ifimpl.ucenter.subscribe.SubscribeProviderCreator;
import com.gala.video.lib.share.ifimpl.voice.VoiceCommonCreater;
import com.gala.video.lib.share.ifimpl.web.config.JSConfigDataProviderCreator;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.epgIfFactory.IEpgInterfaceFactory;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.epgIfFactory.IEpgInterfaceFactory.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.OpenapiReporterManagerCreator;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.playerIfFactory.IPlayerInterfaceFactory;

class InterfaceFactory {
    InterfaceFactory() {
    }

    private static IEpgInterfaceFactory getEpgInterfaceFactory() {
        IInterfaceWrapper wrapper = InterfaceManager.get().get(InterfaceKey.EPG_IFF);
        if (wrapper == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized epgInterfaceFactory before getting!");
        }
        IEpgInterfaceFactory factory = Wrapper.asInterface(wrapper.getInterface());
        if (factory == null) {
            InterfaceException.throwRuntimeException("IEpgInterfaceFactory is null!");
        }
        return factory;
    }

    private static IPlayerInterfaceFactory getPlayerInterfaceFactory() {
        IInterfaceWrapper wrapper = InterfaceManager.get().get(InterfaceKey.PLAYER_IFF);
        if (wrapper == null) {
            InterfaceException.throwRuntimeException("fetcher must be initialized playerInterfaceFactory before getting!");
        }
        IPlayerInterfaceFactory factory = IPlayerInterfaceFactory.Wrapper.asInterface(wrapper.getInterface());
        if (factory == null) {
            InterfaceException.throwRuntimeException("IPlayerInterfaceFactory is null!");
        }
        return factory;
    }

    static Object getIInterfaceWrapper(String key) {
        IInterfaceWrapper interfaceWrapper = InterfaceManager.get().get(key);
        if (interfaceWrapper == null) {
            if (InterfaceKey.EPG_FB.equals(key) || InterfaceKey.EPG_CHCM.equals(key) || InterfaceKey.EPG_SUDL.equals(key) || InterfaceKey.EPG_AIH.equals(key) || InterfaceKey.EPG_CP.equals(key) || InterfaceKey.EPG_WEB_DATA.equals(key) || InterfaceKey.EPG_MSF.equals(key) || InterfaceKey.EPG_UIC.equals(key) || InterfaceKey.EPG_WE.equals(key) || InterfaceKey.EPG_OPC.equals(key) || "screensaver".equals(key) || InterfaceKey.EPG_AJ.equals(key) || InterfaceKey.EPG_HPB.equals(key) || InterfaceKey.EPG_HFIADT.equals(key) || InterfaceKey.EPG_HFIADP.equals(key) || InterfaceKey.EPG_HC.equals(key)) {
                interfaceWrapper = getEpgInterfaceFactory().getEpgInterface(key);
                if (interfaceWrapper == null) {
                    InterfaceException.throwRuntimeException("epg interface factory not match the interface. key=" + key);
                }
            } else if (InterfaceKey.PLAYER_CP.equals(key) || InterfaceKey.PLAYER_IP.equals(key) || InterfaceKey.PLAYER_PP.equals(key) || InterfaceKey.PLAYER_FP.equals(key) || InterfaceKey.PLAYER_OPC.equals(key) || InterfaceKey.PLAYER_PG.equals(key) || InterfaceKey.PLAYER_HCDN.equals(key) || InterfaceKey.PLAYER_PPC.equals(key)) {
                interfaceWrapper = getPlayerInterfaceFactory().getPlayerInterface(key);
                if (interfaceWrapper == null) {
                    InterfaceException.throwRuntimeException("player interface factory not match the interface. key=" + key);
                }
            } else if (InterfaceKey.SHARE_JS.equals(key)) {
                interfaceWrapper = JSConfigDataProviderCreator.create();
            } else if (InterfaceKey.SHARE_LR.equals(key)) {
                interfaceWrapper = LogRecordProviderCreator.create();
            } else if (InterfaceKey.SHARE_SM.equals(key)) {
                interfaceWrapper = SkinManagerCreator.create();
            } else if ("imsg".equals(key)) {
                interfaceWrapper = MsgCenterCreator.create();
            } else if (InterfaceKey.SHARE_DP.equals(key)) {
                interfaceWrapper = DynamicQDataProviderCreator.create();
            } else if (InterfaceKey.SHARE_BM.equals(key)) {
                interfaceWrapper = BackgroundManagerCreator.create();
            } else if (InterfaceKey.SHARE_AD.equals(key)) {
                interfaceWrapper = AdApiCreator.create();
            } else if (InterfaceKey.SHARE_AD_PROCESS.equals(key)) {
                interfaceWrapper = AdProcessingUtilsCreator.create();
            } else if (InterfaceKey.SHARE_QAM.equals(key)) {
                interfaceWrapper = GalaAccountCreator.create();
            } else if (InterfaceKey.SHARE_QVM.equals(key)) {
                interfaceWrapper = GalaVipCreator.create();
            } else if (InterfaceKey.SHARE_HCM.equals(key)) {
                interfaceWrapper = HistoryCacheCreator.create();
            } else if (InterfaceKey.SHARE_SBP.equals(key)) {
                interfaceWrapper = SubscribeProviderCreator.create();
            } else if (InterfaceKey.SHARE_ATM.equals(key)) {
                interfaceWrapper = ActionManagerCreator.create();
            } else if (InterfaceKey.SHARE_EC.equals(key)) {
                interfaceWrapper = ErrorCodeProviderCreater.create();
            } else if (InterfaceKey.SHARE_IT.equals(key)) {
                interfaceWrapper = InitCreator.create();
            } else if (InterfaceKey.SHARE_DBUS.equals(key)) {
                interfaceWrapper = DataBusCreater.create();
            } else if (InterfaceKey.SHARE_ACTIVE_STATE_DISPATCHER.equals(key)) {
                interfaceWrapper = ActiveStateCreator.create();
            } else if (InterfaceKey.SHARE_LP.equals(key)) {
                interfaceWrapper = LoginProviderCreater.create();
            } else if (InterfaceKey.SHARE_OPENAPI_REPORTER.equals(key)) {
                interfaceWrapper = OpenapiReporterManagerCreator.create();
            } else {
                InterfaceException.throwRuntimeException("fetcher must be initialized interfaceWrapper before getting!");
            }
            InterfaceManager.get().register(key, interfaceWrapper);
        }
        return interfaceWrapper.getInterface();
    }

    static Object createWithoutRegister(String key) {
        IInterfaceWrapper interfaceWrapper = InterfaceManager.get().get(key);
        if (interfaceWrapper == null) {
            if (InterfaceKey.EPG_GAP.equals(key) || InterfaceKey.EPG_DCP.equals(key) || InterfaceKey.EPG_FBDC.equals(key) || InterfaceKey.EPG_NP.equals(key) || InterfaceKey.EPG_PB.equals(key) || InterfaceKey.EPG_E.equals(key) || InterfaceKey.EPG_LCF.equals(key) || InterfaceKey.EPG_MSM.equals(key) || InterfaceKey.EPG_FRC.equals(key) || InterfaceKey.EPG_WEB_ROLE.equals(key) || InterfaceKey.EPG_FF.equals(key) || InterfaceKey.EPG_MH.equals(key) || InterfaceKey.EPG_GDH.equals(key) || InterfaceKey.EPG_CHNPP.equals(key) || InterfaceKey.EPG_MSU.equals(key) || InterfaceKey.EPG_BAP.equals(key) || InterfaceKey.EPG_OPC.equals(key) || InterfaceKey.EPG_OPA.equals(key) || InterfaceKey.EPG_BUILD_IF.equals(key) || InterfaceKey.EPG_CFG_IF.equals(key) || InterfaceKey.EPG_CTRL_IF.equals(key) || InterfaceKey.EPG_UM.equals(key) || InterfaceKey.EPG_ADM.equals(key) || InterfaceKey.EPG_PC.equals(key)) {
                interfaceWrapper = getEpgInterfaceFactory().getEpgInterface(key);
                if (interfaceWrapper == null) {
                    InterfaceException.throwRuntimeException("epg interface factory not match the interface. key=" + key);
                }
            } else if (InterfaceKey.SHARE_VC.equals(key)) {
                interfaceWrapper = VoiceCommonCreater.create();
            } else if (InterfaceKey.PLAYER_OPC.equals(key) || InterfaceKey.PLAYER_OPA.equals(key) || InterfaceKey.PLAYER_MEH.equals(key) || InterfaceKey.PLAYER_EH.equals(key)) {
                interfaceWrapper = getPlayerInterfaceFactory().getPlayerInterface(key);
                if (interfaceWrapper == null) {
                    InterfaceException.throwRuntimeException("player interface factory not match the interface. key=" + key);
                }
            } else {
                InterfaceException.throwRuntimeException("fetcher must be initialized interfaceWrapper before getting!");
            }
        }
        return interfaceWrapper.getInterface();
    }
}
