package com.tvos.appdetailpage.info;

import java.util.ArrayList;

public class AppDetail {
    public BasicInfo basic;
    public GameInfo game;
    public ArrayList<ResInfo> res;
    public ArrayList<StatInfo> stat;

    public static class BasicInfo {
        public String age_restriction;
        public ArrayList<String> appPlatforms;
        public String app_category_path;
        public String app_currency;
        public String app_desc;
        public String app_desc_detail;
        public String app_download_url;
        public String app_homepage;
        public String app_id;
        public String app_logo;
        public String app_name;
        public String app_new_feature;
        public String app_package_name;
        public long app_package_size;
        public String app_permissions;
        public Double app_price;
        public String app_price_unit;
        public String app_tags;
        public String app_top_category;
        public String app_type;
        public String app_ver_code;
        public String app_ver_name;
        public String audit_status;
        public String create_time;
        public String developer_name;
        public String developer_uid;
        public ArrayList<String> languages;
        public String latest_version;
        public String online_status;
        public ArrayList<String> permissions;
        public String platform_restriction;
        public String publish_areas;
        public String publish_time;
        public String qipu_id;
        public String total_download;
        public String total_rate;
        public String update_time;
    }

    public static class GameInfo {
        public String app_id;
        public String app_ver_code;
        public String bbs_url;
        public String create_time;
        public String game_background;
        public String game_painting_style;
        public String game_style;
        public String game_sub_title;
        public String game_type;
        public String gift_url;
        public String guest_url;
        public String id;
        public int is_hot;
        public int is_newserver;
        public String pay_url;
        public ArrayList<String> similarGame;
        public String status;
        public String update_time;
    }

    public static class ResInfo {
        public String media_type;
        public String media_url;
        public String qipu_id;
    }

    public static class StatInfo {
        public String stat_key;
        public String stat_value;
    }
}
