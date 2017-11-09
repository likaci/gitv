package com.qiyi.tv.client.impl.p035a;

import android.content.Context;
import android.os.Bundle;
import com.qiyi.tv.client.Result;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.feature.p033a.C1989a;
import com.qiyi.tv.client.impl.Command;
import com.qiyi.tv.client.impl.Params.OperationType;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.ParamsHelper;

public final class C1994b {
    private final Context f2084a;

    public C1994b(Context context) {
        this.f2084a = context;
    }

    public static Command m1627a(Context context, int i) {
        return new C2004g(context, i);
    }

    public final int m1632a(Media media) {
        Bundle bundle = new Bundle();
        ParamsHelper.setMedia(bundle, media);
        return ParamsHelper.parseResultCode(C1989a.m1618a(bundle, C1994b.m1629a(this.f2084a, TargetType.TARGET_MULTISCREEN, OperationType.OP_PULL, 30000)));
    }

    public static Command m1626a(Context context) {
        return new C2008k(context);
    }

    public final int m1633a(boolean z) {
        Bundle bundle = new Bundle();
        ParamsHelper.setIsFullScreen(bundle, z);
        return ParamsHelper.parseResultCode(C1989a.m1618a(bundle, C1994b.m1629a(this.f2084a, TargetType.TARGET_SCREENSCALE, 20002, 30000)));
    }

    public static Command m1628a(Context context, int i, int i2) {
        return new C2007j(context, i, i2, 30000);
    }

    public final Result<Boolean> m1634a() {
        Bundle a = C1989a.m1618a(new Bundle(), C1994b.m1629a(this.f2084a, TargetType.TARGET_SCREENSCALE, 20003, 30000));
        return new Result(ParamsHelper.parseResultCode(a), Boolean.valueOf(ParamsHelper.parseIsFullScreen(a)));
    }

    public static Command m1629a(Context context, int i, int i2, int i3) {
        return new C2007j(context, i, i2, i3);
    }

    public static Command m1630b(Context context, int i, int i2) {
        return new C2002e(context, i, i2);
    }

    public final int m1631a(int i) {
        Bundle bundle = new Bundle();
        ParamsHelper.setStreamType(bundle, i);
        return ParamsHelper.parseResultCode(C1989a.m1618a(bundle, C1994b.m1629a(this.f2084a, TargetType.TARGET_STREAM_TYPE, 20002, 30000)));
    }

    public final Result<Integer> m1636b() {
        Bundle a = C1989a.m1618a(null, C1994b.m1629a(this.f2084a, TargetType.TARGET_STREAM_TYPE, 20003, 30000));
        return new Result(ParamsHelper.parseResultCode(a), Integer.valueOf(ParamsHelper.parseStreamType(a)));
    }

    public final int m1635b(boolean z) {
        Bundle bundle = new Bundle();
        ParamsHelper.setSkipHeaderTailer(bundle, z);
        return ParamsHelper.parseResultCode(C1989a.m1618a(bundle, C1994b.m1629a(this.f2084a, TargetType.TARGET_SKIP_HEADER_TAILER, 20002, 30000)));
    }

    public final Result<Boolean> m1637c() {
        Bundle a = C1989a.m1618a(null, C1994b.m1629a(this.f2084a, TargetType.TARGET_SKIP_HEADER_TAILER, 20003, 30000));
        return new Result(ParamsHelper.parseResultCode(a), Boolean.valueOf(ParamsHelper.parseIsSkipHeaderTailer(a)));
    }
}
