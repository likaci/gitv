package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.DailyLabel;
import com.gala.video.api.ApiResult;
import java.util.List;

public class ApiResultDailyLabels extends ApiResult {
    public List<DailyLabel> labels = null;

    public void setLables(List<?> list) {
        this.labels = list;
    }

    public List<DailyLabel> getLabels() {
        return this.labels;
    }
}
