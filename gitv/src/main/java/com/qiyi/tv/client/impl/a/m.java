package com.qiyi.tv.client.impl.a;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import com.qiyi.tv.client.OperationInMainThreadException;
import com.qiyi.tv.client.Result;
import com.qiyi.tv.client.data.AppInfo;
import com.qiyi.tv.client.data.Channel;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.feature.a.a;
import com.qiyi.tv.client.impl.Command;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.ParamsHelper;
import com.qiyi.tv.client.impl.Utils;
import java.util.ArrayList;
import java.util.List;

public final class m {
    private final Context a;

    public m(Context context) {
        this.a = context;
    }

    private static <T> Result<List<T>> a(Bundle bundle, Command... commandArr) {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            throw new OperationInMainThreadException("This function cannot be called in main thread!", null);
        }
        try {
            Bundle a = a.a(bundle, commandArr);
            return new Result(ParamsHelper.parseResultCode(a), (List) ParamsHelper.parseResultData(a));
        } catch (Exception e) {
            return new Result(Utils.parseErrorCode(e), null);
        }
    }

    private Command a(int i, int i2) {
        return b.b(this.a, i, i2);
    }

    private Command a(int i) {
        return b.b(this.a, 10008, i);
    }

    public final Result<List<Channel>> a() {
        return a(null, a((int) TargetType.TARGET_CHANNEL, (int) DataType.DATA_CHANNEL_LIST));
    }

    public final Result<List<Media>> a(Channel channel, int i) {
        boolean z;
        if (channel != null) {
            z = true;
        } else {
            z = false;
        }
        Utils.assertTrue(z, "Channel should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setChannel(bundle, channel);
        ParamsHelper.setMaxCount(bundle, i);
        return a(bundle, a((int) TargetType.TARGET_CHANNEL, (int) DataType.DATA_MEDIA_LIST));
    }

    public final Result<List<Media>> a(Channel channel, String str, int i) {
        Utils.assertTrue(channel != null, "Channel should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setChannel(bundle, channel);
        ParamsHelper.setClassTag(bundle, str);
        ParamsHelper.setMaxCount(bundle, i);
        return a(bundle, a((int) TargetType.TARGET_CHANNEL, (int) DataType.DATA_MEDIA_LIST));
    }

    public final Result<List<Media>> a(Channel channel, List<String> list, String str, int i) {
        Utils.assertTrue(channel != null, "Channel should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setChannel(bundle, channel);
        ParamsHelper.setFilterTags(bundle, list);
        ParamsHelper.setSort(bundle, str);
        ParamsHelper.setMaxCount(bundle, i);
        return a(bundle, a((int) TargetType.TARGET_CHANNEL, (int) DataType.DATA_MEDIA_LIST));
    }

    public final Result<List<Media>> b(Channel channel, int i) {
        boolean z;
        if (channel != null) {
            z = true;
        } else {
            z = false;
        }
        Utils.assertTrue(z, "Channel should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setChannel(bundle, channel);
        ParamsHelper.setMaxCount(bundle, i);
        return a(bundle, a((int) TargetType.TARGET_CHANNEL, (int) DataType.DATA_RECOMMENDATION));
    }

    public final Result<List<Media>> c(Channel channel, int i) {
        boolean z;
        if (channel != null) {
            z = true;
        } else {
            z = false;
        }
        Utils.assertTrue(z, "Channel should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setChannel(bundle, channel);
        ParamsHelper.setMaxCount(bundle, i);
        return a(bundle, a((int) TargetType.TARGET_CHANNEL, (int) DataType.DATA_RECOMMENDATION_FOR_TAB));
    }

    public final Result<List<Media>> m221a(int i) {
        Bundle bundle = new Bundle();
        ParamsHelper.setPosition(bundle, i);
        return a(bundle, a((int) TargetType.TARGET_RECOMMEND, (int) DataType.DATA_MEDIA_LIST));
    }

    public final Result<List<Media>> a(String str, int i) {
        boolean z;
        if (TextUtils.isEmpty(str)) {
            z = false;
        } else {
            z = true;
        }
        Utils.assertTrue(z, "resourceId should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setMaxCount(bundle, i);
        ParamsHelper.setResourceId(bundle, str);
        return a(bundle, a((int) TargetType.TARGET_RESOURCE_MEDIA, (int) DataType.DATA_MEDIA_LIST));
    }

    public final Result<List<String>> b() {
        return a(null, a((int) DataType.DATA_SEARCH_HOT));
    }

    public final Result<List<String>> a(String str) {
        Bundle bundle = new Bundle();
        ParamsHelper.setKeyword(bundle, str);
        return a(bundle, a((int) DataType.DATA_SEARCH_SUGGESTION));
    }

    public final Result<List<String>> a(int i, ArrayList<String> arrayList) {
        Bundle bundle = new Bundle();
        ParamsHelper.setPictureSize(bundle, i);
        ParamsHelper.setPictureUrl(bundle, arrayList);
        return a(bundle, new f(this.a));
    }

    public final Result<String> a(Media media, int i, int i2) {
        Bundle bundle = new Bundle();
        ParamsHelper.setMedia(bundle, media);
        ParamsHelper.setPictureType(bundle, i);
        ParamsHelper.setPictureSize(bundle, i2);
        bundle = a.a(bundle, b.a(this.a, TargetType.TARGET_MEDIA, 20003, DataType.DATA_URL));
        return new Result(ParamsHelper.parseResultCode(bundle), ParamsHelper.parseResourcePictureUrl(bundle));
    }

    public final Result<Media> a(Media media) {
        Utils.assertTrue(media != null, "Media should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setMedia(bundle, media);
        bundle = a.a(bundle, b.a(this.a, TargetType.TARGET_MEDIA, 20003, DataType.DATA_MEDIA));
        return new Result(ParamsHelper.parseResultCode(bundle), (Media) ParamsHelper.parseResultData(bundle));
    }

    public final Result<Media> b(Media media) {
        Utils.assertTrue(media != null, "Media should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setMedia(bundle, media);
        bundle = a.a(bundle, b.a(this.a, TargetType.TARGET_MEDIA_DETAIL, 20003, DataType.DATA_MEDIA));
        return new Result(ParamsHelper.parseResultCode(bundle), (Media) ParamsHelper.parseResultData(bundle));
    }

    public final Result<String> c() {
        Bundle a = a.a(null, b.a(this.a, TargetType.TARGET_QR_CODE, 20003, DataType.DATA_URL));
        return new Result(ParamsHelper.parseResultCode(a), ParamsHelper.parseQrCodeUrl(a));
    }

    public final Result<List<Media>> a(String str, int i, int i2) {
        Bundle bundle = new Bundle();
        ParamsHelper.setKeyword(bundle, str);
        ParamsHelper.setChannelId(bundle, i);
        ParamsHelper.setMaxCount(bundle, i2);
        return a(bundle, a(10008, (int) DataType.DATA_MEDIA_LIST));
    }

    public final Result<String> d() {
        Bundle a = a.a(null, b.a(this.a, TargetType.TARGET_TV_QR_CODE, 20003, DataType.DATA_URL));
        return new Result(ParamsHelper.parseResultCode(a), ParamsHelper.parseQrCodeUrl(a));
    }

    public final Result<List<AppInfo>> m222a(int i, int i2) {
        boolean z;
        if (i <= 3) {
            z = true;
        } else {
            z = false;
        }
        Utils.assertTrue(z, "category argument invalid !");
        Bundle bundle = new Bundle();
        ParamsHelper.setAppCategory(bundle, i);
        ParamsHelper.setMaxCount(bundle, i2);
        return a(bundle, new i(this.a));
    }
}
