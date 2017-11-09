package com.gala.video.app.player.init;

import com.gala.video.app.player.SOpenApiPlayerCommandHolder;
import com.gala.video.app.player.controller.GalaVideoPlayerGenerator;
import com.gala.video.app.player.controller.HCDNController;
import com.gala.video.app.player.feature.PlayerFeatureProxy;
import com.gala.video.app.player.multiscreen.MultiEventHelper;
import com.gala.video.app.player.openBroadcast.OpenBroadcastPlayerActionHolder;
import com.gala.video.app.player.provider.GalaPlayerPageProviderCreater;
import com.gala.video.app.player.provider.PlayerConfigProviderCreater;
import com.gala.video.app.player.utils.PlayerExitHelper;
import com.gala.video.app.player.utils.PlayerProfileCreator;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.playerIfFactory.IPlayerInterfaceFactory.Wrapper;

public class PlayerInterfaceFactory extends Wrapper {
    public IInterfaceWrapper getPlayerInterface(String key) {
        if (InterfaceKey.PLAYER_CP.equals(key)) {
            return PlayerConfigProviderCreater.create();
        }
        if (InterfaceKey.PLAYER_FP.equals(key)) {
            return PlayerFeatureProxy.getInstance();
        }
        if (InterfaceKey.PLAYER_IP.equals(key)) {
            return null;
        }
        if (InterfaceKey.PLAYER_PP.equals(key)) {
            return GalaPlayerPageProviderCreater.create();
        }
        if (InterfaceKey.PLAYER_OPC.equals(key)) {
            return new SOpenApiPlayerCommandHolder();
        }
        if (InterfaceKey.PLAYER_OPA.equals(key)) {
            return new OpenBroadcastPlayerActionHolder();
        }
        if (InterfaceKey.PLAYER_PG.equals(key)) {
            return GalaVideoPlayerGenerator.getInstance();
        }
        if (InterfaceKey.PLAYER_HCDN.equals(key)) {
            return new HCDNController();
        }
        if (InterfaceKey.PLAYER_PPC.equals(key)) {
            return new PlayerProfileCreator();
        }
        if (InterfaceKey.PLAYER_MEH.equals(key)) {
            return new MultiEventHelper();
        }
        if (InterfaceKey.PLAYER_EH.equals(key)) {
            return new PlayerExitHelper();
        }
        return null;
    }
}
