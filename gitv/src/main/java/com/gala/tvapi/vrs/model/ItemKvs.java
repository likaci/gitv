package com.gala.tvapi.vrs.model;

import com.alibaba.fastjson.JSON;

public class ItemKvs extends Model {
    private static final long serialVersionUID = 1;
    public String LiveEpisode_EndTime = null;
    public String LiveEpisode_StartTime = null;
    public String androidTVRec = "";
    public String androidTVRec_size = "";
    public String androidTV_bg = "";
    public String appkey;
    public String available;
    public String dataid;
    public String defImg_size;
    public String defaultpic;
    public String extraImage;
    public String extraImage_size;
    public int goto_resource = 0;
    public String homepageTitle = "";
    public String id_Channel;
    public String imageGif;
    public String imageGif_size;
    public String isDisableInNoLogin = "1";
    public int isFirst;
    public String is_Channel = "0";
    public int jump;
    public String pageUrl;
    public String platform = "";
    public String rCornerImg = "";
    public String showTime;
    public String tvIcon;
    public String tvPic;
    public String tvPic_size;
    public String tvShowName;
    public String tv_img_1140_1140_4K = "";
    public String tv_img_495_495 = "";
    public String tv_img_570_570 = "";
    public String tv_img_950_470 = "";
    public String tv_livebackground = "";
    public String tv_livecollection = "";
    public String tv_livedesc = "";
    public String tv_still_tags = "";
    public String tv_suggest_pid = "";
    public String tv_topic_pic_pid = "";
    public String tv_ver_type = "";
    public String tvfunction;
    public String tvtag;
    public String vip_chnId = "";
    public String vip_dataId = "";
    public String vip_dataType = "";
    public String vip_listStyle = "";
    public String vip_tagClass = "";
    public String vip_tagIcon = "";
    public String vip_tagIconFocus = "";

    public TVTags getTVTag() {
        if (this.tvtag != null) {
            try {
                return (TVTags) JSON.parseObject(this.tvtag, TVTags.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
