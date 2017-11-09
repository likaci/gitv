package com.gala.tvapi.tv2.result;

import com.gala.tvapi.tv2.model.ModuleUpdate;
import com.gala.video.api.ApiResult;
import java.util.List;

public class ApiResultModuleUpdate extends ApiResult {
    public String code;
    public List<ModuleUpdate> data;

    public List<ModuleUpdate> getData() {
        return this.data;
    }
}
