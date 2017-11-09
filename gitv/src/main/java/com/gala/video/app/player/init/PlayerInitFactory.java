package com.gala.video.app.player.init;

import com.gala.video.app.player.init.task.IntertrustDrmPluginLoadTask;
import com.gala.video.app.player.init.task.PlayerConfigJsInitTask;
import com.gala.video.app.player.init.task.PlayerPingbackManagerInitTask;
import com.gala.video.app.player.init.task.PlayerPluginInitTask;
import com.gala.video.lib.share.ifmanager.bussnessIF.startup.InitTaskInput;
import java.util.ArrayList;
import java.util.List;

public class PlayerInitFactory {
    public static InitTaskInput makeUpPlayerPluginInitTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        process.add(Integer.valueOf(1));
        return new InitTaskInput(new PlayerPluginInitTask(), process, 100);
    }

    public static InitTaskInput makeUpPlayerPingbackManagerInitTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        process.add(Integer.valueOf(1));
        return new InitTaskInput(new PlayerPingbackManagerInitTask(), process, 100);
    }

    public static InitTaskInput makeUpPlayerConfigJsInitTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        process.add(Integer.valueOf(1));
        return new InitTaskInput(new PlayerConfigJsInitTask(), process, 100);
    }

    public static InitTaskInput makeUpIntertrustDrmPluginInitTask() {
        List<Integer> process = new ArrayList();
        process.add(Integer.valueOf(0));
        process.add(Integer.valueOf(1));
        return new InitTaskInput(new IntertrustDrmPluginLoadTask(), process, 101, 20000);
    }
}
