package com.gala.tvapi.vrs.model;

import com.gala.tvapi.b.a;

public class GroupKvs extends Model {
    private static final String TRUE = "1";
    private static final long serialVersionUID = 1;
    public String area;
    public String canshort;
    public String card_name;
    public String enterall_layout_id;
    public String isTop;
    public String issort;
    public String loadplaylist;
    public String source;
    public int sub_template_chid;
    public int sub_template_cid;
    public int sub_template_lid;
    public int sub_template_tid;
    public String template_id;
    public String tvBackgroundUrl;
    public String vrs_template_code;

    public boolean isTop() {
        return isTrue(this.isTop);
    }

    public boolean isSort() {
        return isTrue(this.issort);
    }

    public boolean canShort() {
        return isTrue(this.canshort);
    }

    public boolean loadPlayList() {
        return isTrue(this.loadplaylist);
    }

    private boolean isTrue(String key) {
        if (a.a(key) || !key.equals("1")) {
            return false;
        }
        return true;
    }
}
