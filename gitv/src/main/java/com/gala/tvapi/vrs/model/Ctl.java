package com.gala.tvapi.vrs.model;

import java.util.Map;

public class Ctl extends Model {
    private static final long serialVersionUID = 1;
    public Map<String, VipBidInfo> configs;
    public Map<String, VipBidInfo> dolby_vision;
    public Map<String, VipBidInfo> hdr;
    public long timestamp;
    public VipBids vip;
}
