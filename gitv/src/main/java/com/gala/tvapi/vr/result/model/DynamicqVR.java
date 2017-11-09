package com.gala.tvapi.vr.result.model;

import java.io.Serializable;
import java.util.Map;

public class DynamicqVR implements Serializable {
    public boolean isOpenHcdn;
    public String playerConfig;
    public Map<String, Integer> pvStartBitstream;
    public int retry_times_after_started;
    public int retry_times_before_started;
}
