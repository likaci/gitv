package com.gala.video.lib.share.ifmanager.bussnessIF.skin;

public interface IThemeZipHelper {

    public enum BackgroundType {
        DAY_THUMB,
        DAY_BACKGROUND,
        NIGHT_THUMB,
        NIGHT_BACKGROUND
    }

    String getBackground(BackgroundType backgroundType);

    String getSkinZip();

    boolean hasThemeZip();

    void init();

    boolean isVersionChanged();

    void reset();

    void unZipFile(String str);
}
