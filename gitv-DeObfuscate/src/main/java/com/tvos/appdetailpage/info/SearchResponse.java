package com.tvos.appdetailpage.info;

import java.util.ArrayList;
import java.util.Iterator;

public class SearchResponse {
    public String all_channels;
    public String bkt;
    public String code;
    public ArrayList<SearchDocInfo> docinfos;
    public String event_id;
    public boolean isReplaced;
    public int max_result_num;
    public boolean need_qc;
    public int page_num;
    public int page_size;
    public String qc;
    public String real_query;
    public int result_num;
    public String search_time;
    public String server_time;
    public ArrayList<String> terms;

    public static class SearchDocInfo {
        public AlbumDocInfo albumDocInfo;
        public String doc_id;
        public String score;

        public static class AlbumDocInfo {
            public AppInfo app;
            public String videoDocType;

            public static class AppInfo {
                public String app_id;
                public String app_type;
                public String bbs_url;
                public String content_rank;
                public long create_time;
                public String demo_url;
                public String description_brief;
                public String description_details;
                public String developer_id;
                public String developer_name;
                public String distribute_channels;
                public long download_count;
                public String download_url;
                public String game_background;
                public String game_style;
                public String game_type;
                public String gift_big_receive_url;
                public String history_version_total_rating;
                public String home_page_url;
                public String id;
                public boolean is_hot;
                public boolean is_new_service;
                public String language;
                public String latest_version;
                public String logo_url;
                public ArrayList<MediaURL> media_list;
                public String painting_style;
                public String pay_url;
                public ArrayList<Price> prices;
                public String program_package_name;
                public long program_package_size;
                public long publish_time;
                public String rating;
                public String similar_famous_game;
                public String sub_title;
                public String tag;
                public String title;
                public String top_category_id;

                public static class Price {
                    public String currency;
                    public String price;
                }
            }
        }
    }

    public SearchResponse postProcessing() {
        Iterator it = this.docinfos.iterator();
        while (it.hasNext()) {
            SearchDocInfo doc = (SearchDocInfo) it.next();
            if (doc.albumDocInfo.app != null) {
                AppInfo appInfo = doc.albumDocInfo.app;
                appInfo.publish_time *= 1000;
                appInfo = doc.albumDocInfo.app;
                appInfo.create_time *= 1000;
            }
        }
        return this;
    }
}
