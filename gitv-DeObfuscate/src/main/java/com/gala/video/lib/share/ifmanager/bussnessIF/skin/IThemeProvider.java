package com.gala.video.lib.share.ifmanager.bussnessIF.skin;

import java.io.Serializable;

public interface IThemeProvider {

    public static class Status {
        public static final int DOWNLOADING = 0;
        public static final int FINISHED = 1;
    }

    public static class ThemeModel implements Serializable {
        private static final long serialVersionUID = 1;
        public String mChannelIconUrls;
        public String mThemeSourceName;
        public String mThemeSourcePath;
    }

    String getDayChannelIconUrls();

    String getDayThemeSourceName();

    String getDayThemeSourcePath();

    int getStatus();

    ThemeModel getThemeModel(String str);

    void resetDayTheme();

    void saveDayThemeJson(ThemeModel themeModel);

    void setStatus(int i);
}
