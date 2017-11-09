package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.FailData;
import com.gala.tvapi.vrs.model.SendModel;
import com.gala.video.api.ApiResult;

public class ApiResultCloudMessage extends ApiResult {
    public SendModel data;

    public boolean isCheckSendSuccess(String id) {
        if (this.data != null) {
            if (this.data.success != null && this.data.success.size() > 0) {
                for (String equals : this.data.success) {
                    if (equals.equals(id)) {
                        return true;
                    }
                }
            }
            if (this.data.failed != null && this.data.failed.size() > 0) {
                for (FailData failData : this.data.failed) {
                    if (failData.id.equals(id)) {
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
