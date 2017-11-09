package com.gala.tvapi.vrs.p031a;

import com.gala.tvapi.vrs.model.LiveStream;
import com.gala.tvapi.vrs.result.ApiResultLiveM3u8;
import com.gala.video.api.ApiResult;

public final class C0340c<T extends ApiResult> extends C0339d<T> {
    public final T mo850a(String str, Class<T> cls) {
        T a = super.mo850a(str, (Class) cls);
        if (a instanceof ApiResultLiveM3u8) {
            a = (ApiResultLiveM3u8) a;
            if (!(a == null || a.data == null || a.data.streams == null || a.data.streams.size() <= 0)) {
                for (LiveStream liveStream : a.data.streams) {
                    liveStream.url = m767a(this.a, liveStream.url);
                }
            }
        }
        return a;
    }
}
