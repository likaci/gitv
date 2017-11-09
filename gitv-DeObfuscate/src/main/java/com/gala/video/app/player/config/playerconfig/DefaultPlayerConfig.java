package com.gala.video.app.player.config.playerconfig;

public class DefaultPlayerConfig extends AbsPlayerConfig {

    private static class DefaultPlayerConfigInstanceHolder {
        public static DefaultPlayerConfig sDefaultPlayerConfig = new DefaultPlayerConfig();

        private DefaultPlayerConfigInstanceHolder() {
        }
    }

    private DefaultPlayerConfig() {
        this.TAG = "PlayerConfig/DefaultPlayerConfig@" + Integer.toHexString(hashCode());
    }

    public static DefaultPlayerConfig instance() {
        return DefaultPlayerConfigInstanceHolder.sDefaultPlayerConfig;
    }

    public void load() {
    }

    public boolean ready() {
        return true;
    }
}
