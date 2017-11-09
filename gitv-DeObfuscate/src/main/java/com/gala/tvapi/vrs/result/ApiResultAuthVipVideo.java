package com.gala.tvapi.vrs.result;

import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.type.WatchType;
import com.gala.tvapi.vrs.model.AuthVipVideo;
import com.gala.video.api.ApiResult;

public class ApiResultAuthVipVideo extends ApiResult {
    public AuthVipVideo data = null;
    public String previewEpisodes = "";
    public String previewTime = "";
    public String previewType = "";

    public void setData(AuthVipVideo d) {
        this.data = d;
    }

    public AuthVipVideo getData() {
        return this.data;
    }

    public boolean canPreview() {
        if (this.data == null || this.data.prv == null || !this.data.prv.equals("1")) {
            return false;
        }
        return true;
    }

    public WatchType getWatchType() {
        if (C0214a.m592a(this.previewType)) {
            return WatchType.MINUTE_TYPE;
        }
        return this.previewType.equals("1") ? WatchType.MINUTE_TYPE : WatchType.WHOLE_TYPE;
    }

    public String[] getPreviewEpisodes() {
        if (!C0214a.m592a(this.previewType) && this.previewType.equals("2")) {
            if (!C0214a.m592a(this.previewEpisodes)) {
                return this.previewEpisodes.split(",");
            }
        }
        return null;
    }
}
