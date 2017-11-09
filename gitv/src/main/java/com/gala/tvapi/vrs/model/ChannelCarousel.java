package com.gala.tvapi.vrs.model;

public class ChannelCarousel extends Model {
    private static final long serialVersionUID = 1;
    public int boss;
    public String code;
    public long id;
    public String logo;
    public String name;
    public String shortName;
    public long tableNo;

    public boolean isVipChannel() {
        return this.boss == 1;
    }

    public String toString() {
        return "id=" + this.id + ", name=" + this.name + ",tableNo=" + this.tableNo + ",shortName=" + this.shortName + ",boss=" + this.boss;
    }
}
