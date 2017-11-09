package com.gala.albumprovider;

import com.gala.albumprovider.base.IAlbumProvider;
import com.gala.albumprovider.logic.C0041a;
import com.gala.albumprovider.model.ILanguage;
import com.gala.albumprovider.model.Language;
import com.gala.albumprovider.util.DefaultMenus;

public class AlbumProviderApi {
    public static int TagMaxCount = 24;
    private static C0041a f58a = new C0041a();
    private static ILanguage f59a;

    public static IAlbumProvider getAlbumProvider() {
        return f58a;
    }

    public static void registerLanguages(ILanguage language) {
        f59a = language;
        DefaultMenus.initData();
    }

    public static ILanguage getLanguages() {
        if (f59a == null) {
            f59a = new Language();
        }
        return f59a;
    }

    public static void unregisterLanguages() {
        if (!(f59a instanceof Language)) {
            f59a = null;
        }
    }

    public static void setTagMaxCount(int maxCount) {
        TagMaxCount = maxCount;
    }
}
