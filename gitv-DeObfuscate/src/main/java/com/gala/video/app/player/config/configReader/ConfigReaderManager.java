package com.gala.video.app.player.config.configReader;

public class ConfigReaderManager {
    public static final int ReaderType_Cached = 1;
    public static final int ReaderType_Remote = 2;

    private static class ConfigReaderManagerInstanceHolder {
        public static ConfigReaderManager sConfigReaderManager = new ConfigReaderManager();

        private ConfigReaderManagerInstanceHolder() {
        }
    }

    public static ConfigReaderManager instance() {
        return ConfigReaderManagerInstanceHolder.sConfigReaderManager;
    }

    private ConfigReaderManager() {
    }

    public IConfigReader getConfigReader(int readerType) {
        switch (readerType) {
            case 1:
                return CachedConfigReader.instance();
            case 2:
                return RemoteConfigReader.instance();
            default:
                return null;
        }
    }
}
