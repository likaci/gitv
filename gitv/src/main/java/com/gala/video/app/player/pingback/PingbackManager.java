package com.gala.video.app.player.pingback;

import com.gala.pingback.PingbackFactory;

public class PingbackManager {
    private final PingbackReceiver mReceiver = new PingbackReceiver();

    public void start() {
        this.mReceiver.start();
        PingbackFactory.initliaze(new PingbackFactoryImpl());
    }

    public void stop() {
        this.mReceiver.stop();
        PingbackFactory.release();
    }
}
