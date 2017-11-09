package com.gala.tvapi.tv2.model;

import java.util.List;

public class VipGuideInfo extends Model {
    private static final long serialVersionUID = 1;
    public int count;
    public List<Integer> role_type;

    public String toString() {
        return "count = " + this.count + ", role_type = " + (this.role_type == null ? "" : this.role_type.toString());
    }
}
