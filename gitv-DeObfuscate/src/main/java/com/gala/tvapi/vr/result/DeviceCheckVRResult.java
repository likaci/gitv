package com.gala.tvapi.vr.result;

import com.gala.tvapi.vr.result.model.DeviceCheckVR;
import com.gala.video.api.ApiResult;

public class DeviceCheckVRResult extends ApiResult {
    public DeviceCheckVR data;

    public boolean isUpgrade() {
        if (this.data == null || this.data.upgrade == null) {
            return false;
        }
        return true;
    }
}
