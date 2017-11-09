package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.Province;
import com.gala.video.api.ApiResult;
import java.util.List;

public class ApiResultAreaList extends ApiResult {
    public List<Province> list;

    public void setProvinceList(List<?> provinces) {
        this.list = provinces;
    }

    public List<Province> getProvinceList() {
        return this.list;
    }
}
