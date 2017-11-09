package com.qiyi.tv.client.impl.a;

import android.content.Context;
import android.os.Bundle;
import com.qiyi.tv.client.data.AppInfo;
import com.qiyi.tv.client.data.Channel;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.feature.a.a;
import com.qiyi.tv.client.feature.common.PlayParams;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.ParamsHelper;
import com.qiyi.tv.client.impl.Utils;
import java.util.List;

public final class o {
    private final Context a;

    public o(Context context) {
        this.a = context;
    }

    public final int a(int i, String str, int i2) {
        Bundle bundle = new Bundle();
        ParamsHelper.setTitle(bundle, str);
        ParamsHelper.setIntentFlag(bundle, i2);
        return ParamsHelper.parseResultCode(a.a(bundle, b.a(this.a, i), b.a(this.a)));
    }

    public final int a(int i, int i2) {
        return a(i, null, i2);
    }

    public final int a(Channel channel, String str) {
        Utils.assertTrue(channel != null, "Channel should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setChannel(bundle, channel);
        ParamsHelper.setIntentFlag(bundle, Utils.INTENT_FLAG_DEFAULT);
        ParamsHelper.setTitle(bundle, str);
        return ParamsHelper.parseResultCode(a.a(bundle, b.a(this.a, TargetType.TARGET_CHANNEL), b.a(this.a)));
    }

    public final int a(String str) {
        Bundle bundle = new Bundle();
        ParamsHelper.setActivateCode(bundle, str);
        ParamsHelper.setIntentFlag(bundle, Utils.INTENT_FLAG_DEFAULT);
        return ParamsHelper.parseResultCode(a.a(bundle, b.a(this.a, TargetType.TARGET_ACTIVATE_PAGE), b.a(this.a)));
    }

    public final int a(Channel channel, List<String> list, String str, String str2) {
        Utils.assertTrue(channel != null, "Channel should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setChannel(bundle, channel);
        ParamsHelper.setIntentFlag(bundle, Utils.INTENT_FLAG_DEFAULT);
        ParamsHelper.setFilterTags(bundle, list);
        ParamsHelper.setSort(bundle, str);
        ParamsHelper.setTitle(bundle, str2);
        return ParamsHelper.parseResultCode(a.a(bundle, b.a(this.a, TargetType.TARGET_CHANNEL), b.a(this.a)));
    }

    public final int a(Channel channel, List<String> list, String str, String str2, int i) {
        Utils.assertTrue(channel != null, "Channel should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setChannel(bundle, channel);
        ParamsHelper.setIntentFlag(bundle, Utils.INTENT_FLAG_DEFAULT);
        ParamsHelper.setFilterTags(bundle, list);
        ParamsHelper.setSort(bundle, str);
        ParamsHelper.setTitle(bundle, str2);
        ParamsHelper.setCount(bundle, i);
        return ParamsHelper.parseResultCode(a.a(bundle, b.a(this.a, TargetType.TARGET_CHANNEL), b.a(this.a)));
    }

    public final int a(Channel channel, String str, String str2) {
        Utils.assertTrue(channel != null, "Channel should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setChannel(bundle, channel);
        ParamsHelper.setIntentFlag(bundle, Utils.INTENT_FLAG_DEFAULT);
        ParamsHelper.setClassTag(bundle, str);
        ParamsHelper.setTitle(bundle, str2);
        return ParamsHelper.parseResultCode(a.a(bundle, b.a(this.a, TargetType.TARGET_CHANNEL), b.a(this.a)));
    }

    public final int a(Channel channel, String str, String str2, int i) {
        Utils.assertTrue(channel != null, "Channel should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setChannel(bundle, channel);
        ParamsHelper.setIntentFlag(bundle, Utils.INTENT_FLAG_DEFAULT);
        ParamsHelper.setClassTag(bundle, str);
        ParamsHelper.setTitle(bundle, str2);
        ParamsHelper.setCount(bundle, i);
        return ParamsHelper.parseResultCode(a.a(bundle, b.a(this.a, TargetType.TARGET_CHANNEL), b.a(this.a)));
    }

    public final int a(Media media, PlayParams playParams) {
        Utils.assertTrue(media != null, "Media should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setMedia(bundle, media);
        ParamsHelper.setPlayParams(bundle, playParams);
        ParamsHelper.setIntentFlag(bundle, Utils.INTENT_FLAG_DEFAULT);
        return ParamsHelper.parseResultCode(a.a(bundle, b.a(this.a, TargetType.TARGET_MEDIA), b.a(this.a)));
    }

    public final int a(String str, int i) {
        Bundle bundle = new Bundle();
        ParamsHelper.setKeyword(bundle, str);
        ParamsHelper.setIntentFlag(bundle, i);
        return ParamsHelper.parseResultCode(a.a(bundle, b.a(this.a, TargetType.TARGET_SEARCH_RESULT), b.a(this.a)));
    }

    private int a(Media media, PlayParams playParams, String str, String str2, int i, int i2) {
        Bundle bundle = new Bundle();
        ParamsHelper.setMedia(bundle, media);
        ParamsHelper.setIntentFlag(bundle, i2);
        ParamsHelper.setPackageName(bundle, str);
        ParamsHelper.setPluginProvider(bundle, str2);
        ParamsHelper.setPlayParams(bundle, playParams);
        return ParamsHelper.parseResultCode(a.a(bundle, b.a(this.a, i, 20004)));
    }

    public final int a(Media media, PlayParams playParams, int i) {
        Utils.assertTrue(media != null, "Media should not be null!");
        return a(media, playParams, null, null, TargetType.TARGET_MEDIA, i);
    }

    public final int a(Media media, String str, String str2) {
        Utils.assertTrue(media != null, "Media should not be null!");
        return a(media, null, str, str2, TargetType.TARGET_MEDIA, Utils.INTENT_FLAG_DEFAULT);
    }

    public final int a(Media media, String str) {
        boolean z;
        boolean z2 = true;
        if (media != null) {
            z = true;
        } else {
            z = false;
        }
        Utils.assertTrue(z, "Media should not be null!");
        if (1 != media.getType()) {
            z2 = false;
        }
        Utils.assertTrue(z2, "Media must be video type!");
        com.qiyi.tv.client.Utils.setVid(media, str);
        return a(media, null, null, null, TargetType.TARGET_VRS_MEDIA, Utils.INTENT_FLAG_DEFAULT);
    }

    public final int a(AppInfo appInfo) {
        Bundle bundle = new Bundle();
        ParamsHelper.setAppInfo(bundle, appInfo);
        ParamsHelper.setIntentFlag(bundle, Utils.INTENT_FLAG_DEFAULT);
        return ParamsHelper.parseResultCode(a.a(bundle, b.a(this.a, TargetType.TARGET_APP_STORE_APPDETAIL), b.a(this.a)));
    }

    public final int a(int i) {
        Bundle bundle = new Bundle();
        ParamsHelper.setHomeTabType(bundle, i);
        ParamsHelper.setIntentFlag(bundle, Utils.INTENT_FLAG_DEFAULT);
        return ParamsHelper.parseResultCode(a.a(bundle, b.a(this.a, TargetType.TARGET_HOME_TAB), b.a(this.a)));
    }
}
