package com.gala.video.lib.share.ifimpl.skin;

import com.gala.video.lib.share.ifmanager.bussnessIF.skin.ISkinManager.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.IThemeProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.IThemeZipHelper;

class SkinManager extends Wrapper {
    private IThemeProvider mThemeProvider = new ThemeProvider();
    private IThemeZipHelper mThemeZipHelper = new ThemeZipHelper();

    public IThemeProvider getIThemeProvider() {
        return this.mThemeProvider;
    }

    public IThemeZipHelper getIThemeZipHelper() {
        return this.mThemeZipHelper;
    }
}
