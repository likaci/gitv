package com.gala.albumprovider;

import com.gala.albumprovider.base.IAlbumProvider;
import com.gala.albumprovider.logic.a;
import com.gala.albumprovider.model.ILanguage;
import com.gala.albumprovider.model.Language;
import com.gala.albumprovider.util.DefaultMenus;

public class AlbumProviderApi {
    public static int TagMaxCount = 24;
    private static a a = new a();
    private static ILanguage f48a;

    public static IAlbumProvider getAlbumProvider() {
        return a;
    }

    public static void registerLanguages(ILanguage language) {
        f48a = language;
        DefaultMenus.initData();
    }

    public static ILanguage getLanguages() {
        if (f48a == null) {
            f48a = new Language();
        }
        return f48a;
    }

    public static void unregisterLanguages() {
        if (!(f48a instanceof Language)) {
            f48a = null;
        }
    }

    public static void setTagMaxCount(int maxCount) {
        TagMaxCount = maxCount;
    }
}
