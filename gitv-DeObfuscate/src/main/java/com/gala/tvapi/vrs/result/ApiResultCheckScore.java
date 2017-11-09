package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.CheckScore;
import com.gala.video.api.ApiResult;
import java.util.List;

public class ApiResultCheckScore extends ApiResult {
    public List<CheckScore> data;

    public boolean isUploaded() {
        if (this.data == null || this.data.size() <= 0 || ((CheckScore) this.data.get(0)).score == null || ((CheckScore) this.data.get(0)).score.size() <= 0) {
            return false;
        }
        if (((CheckScore) this.data.get(0)).score.get(0) == "-1.0") {
            return false;
        }
        return true;
    }
}
