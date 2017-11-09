package com.gala.albumprovider.util;

import com.gala.albumprovider.AlbumProviderApi;

public class DefaultMenus {
    public static final int TAGALL = 0;
    public static final int TAGHOT = -101;
    public static final int TAGPOLYMERIC = -201;
    public static final int TAGPRAISE = -102;
    public static MenuTag TagAll = new MenuTag();
    public static MenuTag TagHot = new MenuTag();
    public static MenuTag TagPolymeric = new MenuTag();
    public static MenuTag TagPraise = new MenuTag();

    public static class MenuTag {
        public String id;
        public String name;

        MenuTag() {
        }

        void a(String str, String str2) {
            this.id = str;
            this.name = str2;
        }
    }

    public static void initData() {
        TagAll.a(toString(0), AlbumProviderApi.getLanguages().getTagAllName());
        TagHot.a(toString(-101), AlbumProviderApi.getLanguages().getTagHotName());
        TagPraise.a(toString(-102), AlbumProviderApi.getLanguages().getTagPraiseName());
        TagPolymeric.a(toString(TAGPOLYMERIC), AlbumProviderApi.getLanguages().getTagAllName());
    }

    public static String toString(int value) {
        return String.valueOf(value);
    }
}
