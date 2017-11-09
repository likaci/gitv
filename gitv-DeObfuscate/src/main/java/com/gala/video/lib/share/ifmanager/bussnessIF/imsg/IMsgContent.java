package com.gala.video.lib.share.ifmanager.bussnessIF.imsg;

import java.io.Serializable;

public class IMsgContent implements Serializable {
    private static final long serialVersionUID = 1;
    public String album;
    public short app_id;
    public String button_name;
    public int channelId;
    public String content = "";
    public String count = "";
    public String coupon_key;
    public String coupon_sign;
    public String description;
    public String extraInfo = "";
    public boolean isRead = false;
    public boolean isSeries;
    public boolean isShowDialog = false;
    public int is_detailpage;
    public long localTime;
    public String min_support_version;
    public int msg_id = -10000;
    public int msg_level = 4;
    public int msg_template_id;
    public String msg_title;
    public int msg_type;
    public int page_jumping;
    public String pic_url;
    public String related_aids;
    public String related_plids;
    public String related_vids;
    public String scene = "";
    public String showTime;
    public String sourceCode;
    public int style = 0;
    public int tv_type;
    public String type = "";
    public String url;
    public String url_window;
    public long valid_till;

    public boolean isHasDetail() {
        return this.msg_template_id == 1 && this.is_detailpage == 1 && this.page_jumping >= 1 && this.page_jumping <= 5;
    }

    public String toString() {
        return "IMsgContent{app_id=" + this.app_id + ", msg_id=" + this.msg_id + ", msg_template_id=" + this.msg_template_id + ", msg_level=" + this.msg_level + ", msg_type=" + this.msg_type + ", is_detailpage=" + this.is_detailpage + ", page_jumping=" + this.page_jumping + ", min_support_version='" + this.min_support_version + '\'' + ", pic_url='" + this.pic_url + '\'' + ", description='" + this.description + '\'' + ", button_name='" + this.button_name + '\'' + ", msg_title='" + this.msg_title + '\'' + ", url='" + this.url + '\'' + ", related_plids='" + this.related_plids + '\'' + ", related_aids='" + this.related_aids + '\'' + ", related_vids='" + this.related_vids + '\'' + ", isRead=" + this.isRead + ", tv_type=" + this.tv_type + ", isSeries=" + this.isSeries + ", sourceCode='" + this.sourceCode + '\'' + ", channelId=" + this.channelId + ", localTime=" + this.localTime + ", album='" + this.album + '\'' + ", content='" + this.content + '\'' + ", showTime='" + this.showTime + '\'' + ", isShowDialog=" + this.isShowDialog + ", style=" + this.style + ", url_window='" + this.url_window + '\'' + ", coupon_key='" + this.coupon_key + '\'' + ", coupon_sign='" + this.coupon_sign + '\'' + ", valid_till=" + this.valid_till + ", type='" + this.type + '\'' + ", scene='" + this.scene + '\'' + ", count='" + this.count + '\'' + ", extraInfo='" + this.extraInfo + '\'' + '}';
    }
}
