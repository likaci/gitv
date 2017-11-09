package com.gala.tvapi.vrs.a;

import com.gala.tvapi.vrs.model.LiveStream;
import com.gala.tvapi.vrs.result.ApiResultLiveM3u8;
import com.gala.video.api.ApiResult;

public final class c<T extends ApiResult> extends d<T> {
    public final T a(String str, Class<T> cls) {
        T a = super.a(str, (Class) cls);
        if (a instanceof ApiResultLiveM3u8) {
            a = (ApiResultLiveM3u8) a;
            if (!(a == null || a.data == null || a.data.streams == null || a.data.streams.size() <= 0)) {
                for (LiveStream liveStream : a.data.streams) {
                    liveStream.url = a(this.a, liveStream.url);
                }
            }
        }
        return a;
    }
}
