package com.qiyi.tv.client;

import com.qiyi.tv.client.data.Media;

public class Utils {
    public static void setVid(Media media, String vid) {
        com.qiyi.tv.client.impl.Utils.assertTrue(media != null, "Media should not be null!");
        media.getUserTags().putString("com.qiyi.tv.sdk.extra.MEDIA_VID", vid);
    }

    public static String getVid(Media media) {
        com.qiyi.tv.client.impl.Utils.assertTrue(media != null, "Media should not be null!");
        return media.getUserTags().getString("com.qiyi.tv.sdk.extra.MEDIA_VID");
    }
}
