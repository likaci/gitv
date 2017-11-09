package com.gala.tvapi.tv3.result.model;

import java.io.Serializable;
import java.util.List;

public class PluginBundle implements Serializable {
    public PluginPlatmodel chipVer;
    public List<PluginDependency> dependencies;
    public String icon;
    public PluginPlatmodel mac;
    public String md5;
    public String option;
    public PluginPlatmodel osVer;
    public String pkg;
    public PluginPlatmodel platModel;
    public String uri;
    public String url;
    public String version;
}
