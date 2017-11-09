package com.gala.video.lib.share.uikit.data.data.Model.cardlayout;

import com.alibaba.fastjson.JSON;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.util.List;

public class CardStyle {
    public short backId;
    public short body_h;
    public short body_mg_b;
    public short body_mg_l;
    public short body_mg_r;
    public short body_mg_t;
    public short body_pd_b;
    public short body_pd_l;
    public short body_pd_r;
    public short body_pd_t;
    public short default_focus;
    public short header_h;
    public short header_pd_b;
    public short header_pd_l;
    public short header_pd_r;
    public short header_pd_t;
    public short row_nolimit;
    public List<Row> rows;
    public float scale;
    public short show_position;
    public short space_h;
    public short space_v;
    public short type;
    public short f2039w;

    public CardStyle copy() {
        try {
            return (CardStyle) JSON.parseObject(JSON.toJSONString(this), CardStyle.class);
        } catch (Exception e) {
            return this;
        }
    }

    public String toString() {
        return "CardStyle [type=" + this.type + ", w=" + this.f2039w + ", body_h=" + this.body_h + ", body_pd_l=" + this.body_pd_l + ", body_pd_r=" + this.body_pd_r + ", body_pd_t=" + this.body_pd_t + ", body_pd_b=" + this.body_pd_b + ", body_mg_l=" + this.body_mg_l + ", body_mg_r=" + this.body_mg_r + ", body_mg_t=" + this.body_mg_t + ", body_mg_b=" + this.body_mg_b + ", header_h=" + this.header_h + ", header_pd_l=" + this.header_pd_l + ", header_pd_r=" + this.header_pd_r + ", header_pd_t=" + this.header_pd_t + ", header_pd_b=" + this.header_pd_b + ", space_v=" + this.space_v + ", space_h=" + this.space_h + ", row_nolimit=" + this.row_nolimit + ", default_focus=" + this.default_focus + ", show_position=" + this.show_position + ", scale=" + this.scale + ", rows=" + this.rows + AlbumEnterFactory.SIGN_STR;
    }
}
