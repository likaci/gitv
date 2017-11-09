package com.qiyi.tv.client.impl.a;

import android.content.Context;
import android.os.Bundle;
import com.qiyi.tv.client.Result;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.feature.a.a;
import com.qiyi.tv.client.impl.Command;
import com.qiyi.tv.client.impl.Params.OperationType;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.ParamsHelper;

public final class b {
    private final Context a;

    public b(Context context) {
        this.a = context;
    }

    public static Command a(Context context, int i) {
        return new g(context, i);
    }

    public final int a(Media media) {
        Bundle bundle = new Bundle();
        ParamsHelper.setMedia(bundle, media);
        return ParamsHelper.parseResultCode(a.a(bundle, a(this.a, TargetType.TARGET_MULTISCREEN, OperationType.OP_PULL, 30000)));
    }

    public static Command a(Context context) {
        return new k(context);
    }

    public final int a(boolean z) {
        Bundle bundle = new Bundle();
        ParamsHelper.setIsFullScreen(bundle, z);
        return ParamsHelper.parseResultCode(a.a(bundle, a(this.a, TargetType.TARGET_SCREENSCALE, 20002, 30000)));
    }

    public static Command a(Context context, int i, int i2) {
        return new j(context, i, i2, 30000);
    }

    public final Result<Boolean> a() {
        Bundle a = a.a(new Bundle(), a(this.a, TargetType.TARGET_SCREENSCALE, 20003, 30000));
        return new Result(ParamsHelper.parseResultCode(a), Boolean.valueOf(ParamsHelper.parseIsFullScreen(a)));
    }

    public static Command a(Context context, int i, int i2, int i3) {
        return new j(context, i, i2, i3);
    }

    public static Command b(Context context, int i, int i2) {
        return new e(context, i, i2);
    }

    public final int a(int i) {
        Bundle bundle = new Bundle();
        ParamsHelper.setStreamType(bundle, i);
        return ParamsHelper.parseResultCode(a.a(bundle, a(this.a, TargetType.TARGET_STREAM_TYPE, 20002, 30000)));
    }

    public final Result<Integer> b() {
        Bundle a = a.a(null, a(this.a, TargetType.TARGET_STREAM_TYPE, 20003, 30000));
        return new Result(ParamsHelper.parseResultCode(a), Integer.valueOf(ParamsHelper.parseStreamType(a)));
    }

    public final int b(boolean z) {
        Bundle bundle = new Bundle();
        ParamsHelper.setSkipHeaderTailer(bundle, z);
        return ParamsHelper.parseResultCode(a.a(bundle, a(this.a, TargetType.TARGET_SKIP_HEADER_TAILER, 20002, 30000)));
    }

    public final Result<Boolean> c() {
        Bundle a = a.a(null, a(this.a, TargetType.TARGET_SKIP_HEADER_TAILER, 20003, 30000));
        return new Result(ParamsHelper.parseResultCode(a), Boolean.valueOf(ParamsHelper.parseIsSkipHeaderTailer(a)));
    }
}
