package com.gala.video.app.player.config.configWriter;

public class ConfigWriterManager {

    private static class ConfigWriterManagerInstanceHolder {
        public static ConfigWriterManager sConfigWriterManager = new ConfigWriterManager();

        private ConfigWriterManagerInstanceHolder() {
        }
    }

    public static ConfigWriterManager instance() {
        return ConfigWriterManagerInstanceHolder.sConfigWriterManager;
    }

    private ConfigWriterManager() {
    }

    public IConfigWriter getConfigWriter() {
        return CachedConfigWriter.instance();
    }
}
