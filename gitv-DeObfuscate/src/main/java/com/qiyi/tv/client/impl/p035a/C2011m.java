package com.qiyi.tv.client.impl.p035a;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import com.qiyi.tv.client.OperationInMainThreadException;
import com.qiyi.tv.client.Result;
import com.qiyi.tv.client.data.AppInfo;
import com.qiyi.tv.client.data.Channel;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.feature.p033a.C1989a;
import com.qiyi.tv.client.impl.Command;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.ParamsHelper;
import com.qiyi.tv.client.impl.Utils;
import java.util.ArrayList;
import java.util.List;

public final class C2011m {
    private final Context f2112a;

    public C2011m(Context context) {
        this.f2112a = context;
    }

    private static <T> Result<List<T>> m1676a(Bundle bundle, Command... commandArr) {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            throw new OperationInMainThreadException("This function cannot be called in main thread!", null);
        }
        try {
            Bundle a = C1989a.m1618a(bundle, commandArr);
            return new Result(ParamsHelper.parseResultCode(a), (List) ParamsHelper.parseResultData(a));
        } catch (Exception e) {
            return new Result(Utils.parseErrorCode(e), null);
        }
    }

    private Command m1678a(int i, int i2) {
        return C1994b.m1630b(this.f2112a, i, i2);
    }

    private Command m1677a(int i) {
        return C1994b.m1630b(this.f2112a, 10008, i);
    }

    public final Result<List<Channel>> m1679a() {
        return C2011m.m1676a(null, m1678a((int) TargetType.TARGET_CHANNEL, (int) DataType.DATA_CHANNEL_LIST));
    }

    public final Result<List<Media>> m1683a(Channel channel, int i) {
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
        return C2011m.m1676a(bundle, m1678a((int) TargetType.TARGET_CHANNEL, (int) DataType.DATA_MEDIA_LIST));
    }

    public final Result<List<Media>> m1684a(Channel channel, String str, int i) {
        Utils.assertTrue(channel != null, "Channel should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setChannel(bundle, channel);
        ParamsHelper.setClassTag(bundle, str);
        ParamsHelper.setMaxCount(bundle, i);
        return C2011m.m1676a(bundle, m1678a((int) TargetType.TARGET_CHANNEL, (int) DataType.DATA_MEDIA_LIST));
    }

    public final Result<List<Media>> m1685a(Channel channel, List<String> list, String str, int i) {
        Utils.assertTrue(channel != null, "Channel should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setChannel(bundle, channel);
        ParamsHelper.setFilterTags(bundle, list);
        ParamsHelper.setSort(bundle, str);
        ParamsHelper.setMaxCount(bundle, i);
        return C2011m.m1676a(bundle, m1678a((int) TargetType.TARGET_CHANNEL, (int) DataType.DATA_MEDIA_LIST));
    }

    public final Result<List<Media>> m1692b(Channel channel, int i) {
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
        return C2011m.m1676a(bundle, m1678a((int) TargetType.TARGET_CHANNEL, (int) DataType.DATA_RECOMMENDATION));
    }

    public final Result<List<Media>> m1695c(Channel channel, int i) {
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
        return C2011m.m1676a(bundle, m1678a((int) TargetType.TARGET_CHANNEL, (int) DataType.DATA_RECOMMENDATION_FOR_TAB));
    }

    public final Result<List<Media>> m1680a(int i) {
        Bundle bundle = new Bundle();
        ParamsHelper.setPosition(bundle, i);
        return C2011m.m1676a(bundle, m1678a((int) TargetType.TARGET_RECOMMEND, (int) DataType.DATA_MEDIA_LIST));
    }

    public final Result<List<Media>> m1689a(String str, int i) {
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
        return C2011m.m1676a(bundle, m1678a((int) TargetType.TARGET_RESOURCE_MEDIA, (int) DataType.DATA_MEDIA_LIST));
    }

    public final Result<List<String>> m1691b() {
        return C2011m.m1676a(null, m1677a((int) DataType.DATA_SEARCH_HOT));
    }

    public final Result<List<String>> m1688a(String str) {
        Bundle bundle = new Bundle();
        ParamsHelper.setKeyword(bundle, str);
        return C2011m.m1676a(bundle, m1677a((int) DataType.DATA_SEARCH_SUGGESTION));
    }

    public final Result<List<String>> m1682a(int i, ArrayList<String> arrayList) {
        Bundle bundle = new Bundle();
        ParamsHelper.setPictureSize(bundle, i);
        ParamsHelper.setPictureUrl(bundle, arrayList);
        return C2011m.m1676a(bundle, new C2003f(this.f2112a));
    }

    public final Result<String> m1687a(Media media, int i, int i2) {
        Bundle bundle = new Bundle();
        ParamsHelper.setMedia(bundle, media);
        ParamsHelper.setPictureType(bundle, i);
        ParamsHelper.setPictureSize(bundle, i2);
        bundle = C1989a.m1618a(bundle, C1994b.m1629a(this.f2112a, TargetType.TARGET_MEDIA, 20003, DataType.DATA_URL));
        return new Result(ParamsHelper.parseResultCode(bundle), ParamsHelper.parseResourcePictureUrl(bundle));
    }

    public final Result<Media> m1686a(Media media) {
        Utils.assertTrue(media != null, "Media should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setMedia(bundle, media);
        bundle = C1989a.m1618a(bundle, C1994b.m1629a(this.f2112a, TargetType.TARGET_MEDIA, 20003, DataType.DATA_MEDIA));
        return new Result(ParamsHelper.parseResultCode(bundle), (Media) ParamsHelper.parseResultData(bundle));
    }

    public final Result<Media> m1693b(Media media) {
        Utils.assertTrue(media != null, "Media should not be null!");
        Bundle bundle = new Bundle();
        ParamsHelper.setMedia(bundle, media);
        bundle = C1989a.m1618a(bundle, C1994b.m1629a(this.f2112a, TargetType.TARGET_MEDIA_DETAIL, 20003, DataType.DATA_MEDIA));
        return new Result(ParamsHelper.parseResultCode(bundle), (Media) ParamsHelper.parseResultData(bundle));
    }

    public final Result<String> m1694c() {
        Bundle a = C1989a.m1618a(null, C1994b.m1629a(this.f2112a, TargetType.TARGET_QR_CODE, 20003, DataType.DATA_URL));
        return new Result(ParamsHelper.parseResultCode(a), ParamsHelper.parseQrCodeUrl(a));
    }

    public final Result<List<Media>> m1690a(String str, int i, int i2) {
        Bundle bundle = new Bundle();
        ParamsHelper.setKeyword(bundle, str);
        ParamsHelper.setChannelId(bundle, i);
        ParamsHelper.setMaxCount(bundle, i2);
        return C2011m.m1676a(bundle, m1678a(10008, (int) DataType.DATA_MEDIA_LIST));
    }

    public final Result<String> m1696d() {
        Bundle a = C1989a.m1618a(null, C1994b.m1629a(this.f2112a, TargetType.TARGET_TV_QR_CODE, 20003, DataType.DATA_URL));
        return new Result(ParamsHelper.parseResultCode(a), ParamsHelper.parseQrCodeUrl(a));
    }

    public final Result<List<AppInfo>> m1681a(int i, int i2) {
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
        return C2011m.m1676a(bundle, new C2006i(this.f2112a));
    }
}
