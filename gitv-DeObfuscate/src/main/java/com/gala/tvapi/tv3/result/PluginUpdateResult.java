package com.gala.tvapi.tv3.result;

import com.gala.tvapi.tv3.ApiResult;
import com.gala.tvapi.tv3.result.model.PluginBundle;
import java.util.List;

public class PluginUpdateResult extends ApiResult {
    public List<PluginBundle> bundles;
    public String response;
    public String upTip;
    public String uuid;
    public String version;
}
